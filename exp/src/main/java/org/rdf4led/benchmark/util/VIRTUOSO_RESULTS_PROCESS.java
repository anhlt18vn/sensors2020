package org.rdf4led.benchmark.util;


import org.rdf4led.benchmark.FileUtil;

import java.io.*;
import java.util.*;

/**
 * org.benchmark.tool
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 08/01/18.
 */
public class VIRTUOSO_RESULTS_PROCESS {
    private static final String Engine = "VIRTUOSO";

    private String dataSetName;
    private String deviceName;
    private String path_to_file_result;

    private int numLines;
    private int scale;


    private VIRTUOSO_RESULTS_PROCESS(String dataSetName, String deviceName, int numLines, int scale) {
        this.dataSetName = dataSetName;

        this.deviceName = deviceName;

        this.path_to_file_result = RESULT_PROCESS.pathToResult + dataSetName + "/" + deviceName + "/" + Engine + "/";
        this.numLines = numLines;
        this.scale = scale;
    }


    public static VIRTUOSO_RESULTS_PROCESS set(String dataSetName, String deviceName, int numLines, int scale) {
        return new VIRTUOSO_RESULTS_PROCESS(dataSetName, deviceName, numLines, scale);
    }

    public static void main(String[] args) {
        VIRTUOSO_RESULTS_PROCESS rp2 = new VIRTUOSO_RESULTS_PROCESS("noaa", "rp2", 10000, 100);
        rp2.processInputResultLatex();
    }

    public void processInputResult() {
        String fileName = dataSetName + "_" + deviceName + "_" + Engine + "_INPUT";

        String fileInput = fileName + ".txt";

        String fileInputPlot = fileName + ".plot";

        File file = new File(this.path_to_file_result + fileInput);

        ArrayList<Long> arrayLong = new ArrayList<>();

        ArrayList<String> arrayString = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String s = bufferedReader.readLine();

            while (s != null) {
                arrayLong.add(parseLong(s));

                s = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < arrayLong.size(); i++) {
            long duration = arrayLong.get(i) - arrayLong.get(i - 1);

            int speed = (int) (this.numLines * 1000 / duration);

            int size = i * this.numLines;

            if ((i == 1) || (i % scale == 0)) arrayString.add(size + ", " + speed);
        }

        FileUtil.write(RESULT_PROCESS.pathToPlotData, fileInputPlot, arrayString);
    }

    public void processInputResultLatex() {
        String fileName = dataSetName + "_" + deviceName + "_" + Engine + "_INPUT";

        String fileInput = fileName + ".txt";

        String fileInputPlot = fileName + ".latex";

        File file = new File(this.path_to_file_result + fileInput);

        ArrayList<Long> arrayLong = new ArrayList<>();

        ArrayList<String> arrayString = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String s = bufferedReader.readLine();

            while (s != null) {
                arrayLong.add(parseLong(s));

                s = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < arrayLong.size(); i = i + scale) {
            long duration = arrayLong.get(i) - arrayLong.get(i - 1);
            System.out.println(duration);
            int speed = (int) (this.numLines * 1000 / duration);
            float size = (i * this.numLines) / 1000000f;
            if ((i == 1) || (i % scale == 1))
                arrayString.add("(" + size + ", " + speed + ")");
        }

        FileUtil.write(RESULT_PROCESS.pathToPlotData, fileInputPlot, arrayString);
    }

    private List<Integer> processQueryLog(File file) {
        ArrayList<Integer> arrayList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s = reader.readLine();

            while (s != null) {
                if (s.contains("Rows")) {
                    arrayList.add(processQueryString(s));
                }

                s = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public int processQueryString(String s) {
        int index = s.indexOf("--");

        int index_fs = s.indexOf(" ", index + 1);

        int index_ss = s.indexOf(" ", index_fs + 1);

        String result = s.substring(index_fs + 1, index_ss);

        double resultInt = Integer.parseInt(result);

        return (int) resultInt;
    }

    private long parseLong(String virtuosoLog) {
        int index = virtuosoLog.indexOf(" ");

        String toParse = virtuosoLog.substring(0, index);

        return Long.parseLong(toParse);
    }

    //===================================================================================================================
    private String processQueryFile(File file) {

        ArrayList<Long> arrayLong = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s = reader.readLine();

            while (s != null) {
                int index = s.indexOf(" ");

                String timeStamp = s.substring(0, index);

                arrayLong.add(Long.parseLong(timeStamp));

                s = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Integer> arrayInteger = new ArrayList<>();

        long current = arrayLong.get(0);

        for (int i = 1; i < arrayLong.size(); i++) {
            arrayInteger.add((int) (arrayLong.get(i) - current));
            current = arrayLong.get(i);
        }

        Collections.sort(arrayInteger);

        int avg = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < 10; i++) {
            int c = arrayInteger.get(i);
            avg = avg + c;
            min = c < min ? c : min;
            max = c > max ? c : max;
        }

        String fileName = file.getName();

        int index = fileName.indexOf(".");
        fileName = fileName.substring(index - 2, index);

        System.out.println(file.getName() + " " + avg / 10 + " " + min + " " + max);
        return fileName + " " + avg / 10 + " " + min + " " + max;
    }

    public void processQueryResult() {
        String fileName = dataSetName + "_" + deviceName + "_" + Engine + "_QUERY.plot";

        List<String> listString = new ArrayList<>();

        File folder = new File(this.path_to_file_result);

        System.out.println(this.path_to_file_result);

        File[] listFiles = folder.listFiles();

        Arrays.sort(listFiles);

        int i = 0;

        for (File file : listFiles) {
            if (file.getName().contains("MEM")) {
                continue;
            }
            if (file.getName().contains("INPUT")) {
                continue;
            }

            listString.add(processQueryFile(file));
        }


        FileUtil.write(RESULT_PROCESS.pathToPlotData, fileName, listString);
    }
}
