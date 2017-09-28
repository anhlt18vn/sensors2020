package org.rdf4led.benchmark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rdf4led.benchmark.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import static org.rdf4led.benchmark.FileUtil.USER_HOME;

/**
 * org.benchmark.tool
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  09/01/18.
 */
public class EX_RESULT_PROCESS
{
    private String dataSetName;
    private String deviceName;
    private String engine;
    private int    numLines;
    private int    scale;

    private String path_to_file_result;

    String[] queryName = new String[]{"F1", "F2", "F3", "F4", "F5", "L1", "L2", "L3", "L4", "L5", "S1", "S2", "S3", "S4", "S5", "S6"};

    private EX_RESULT_PROCESS(String engine, String dataSetName, String deviceName, int numLines,  int scale)
    {
        this.dataSetName = dataSetName;
        this.deviceName = deviceName;
        this.engine      = engine;
        this.numLines    = numLines;
        this.path_to_file_result = RESULT_PROCESS.pathToResult;
        this.scale = scale;
    }

    public static EX_RESULT_PROCESS set(String dataSetName, String deviceName, int i, String engine, int i1) {
       return new EX_RESULT_PROCESS(engine, dataSetName, deviceName, i, i1);
    }

    public void processInputResult()
    {
        String fileInput = "INPUT_" + engine + "_" + dataSetName + "_" + deviceName;
        String fileInputPlot = fileInput + "_latex.txt";

        File file = new File(this.path_to_file_result + fileInput);

        ArrayList<Long> arrayLong = new ArrayList<>();
        ArrayList<String> arrayString = new ArrayList<>();

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s = bufferedReader.readLine();
            while (s != null)
            {
                arrayLong.add(parseLong(s));
                s = bufferedReader.readLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        for (int i = 1; i< arrayLong.size(); i=i+scale)
        {
            long duration = arrayLong.get(i) - arrayLong.get(i-1);
            System.out.println(duration);
            int speed     =   (int) (this.numLines*1000 / duration);
            float size      = (i * this.numLines)/1000000f;
            if ((i==1) || ( i%scale == 1))
            arrayString.add("(" + size + ", " + speed + ")");
        }

        FileUtil.write(RESULT_PROCESS.pathToPlotData, fileInputPlot, arrayString);
    }

    private long parseLong(String jenaLog)
    {
        int index       = jenaLog.indexOf("\t");

        String toParse  = jenaLog.substring(0, index);

        return Long.parseLong(toParse);
    }

    public String processQuery(String queryName, String result){
        ArrayList<Integer> intArray = new ArrayList<>();

        while (result.contains("\t")){
            int index = result.indexOf("\t");

            String time = result.substring(0, index);

            result = result.substring(index + 1);

            if (time.length()>1)
            intArray.add(Integer.parseInt(time));
        }

        intArray.add(Integer.parseInt(result));

        Collections.sort(intArray);

        int avg = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i <10; i ++){
            int c = intArray.get(i);
            avg = avg + c;
            min = Math.min(c, min);
            max = Math.max(c, max);
        }

        return queryName + " " + avg/10 + " " + min + " " + max;
    }


    public void processQueryResult(){
        String fileName = dataSetName + "_" + deviceName + "_" + engine + "_QUERY";

        String fileInput = fileName + ".result";

        String fileInputPlot = fileName + ".plot";

        File file = new File(this.path_to_file_result + fileInput);

        ArrayList<String> arrayString = new ArrayList<>();

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String s = bufferedReader.readLine();

            int queryNameIndex = 0;

            while (s != null)
            {
                if (s.length()>10)
                {
                    arrayString.add(processQuery(queryName[queryNameIndex], s));

                    queryNameIndex++;
                }

                s = bufferedReader.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        FileUtil.write(RESULT_PROCESS.pathToPlotData, fileInputPlot, arrayString);
    }

    public static void main(String[] args){
        File contextFile = new File(USER_HOME + "exp/sensor2020/01config/ex_result.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Context context = objectMapper.readValue(contextFile, Context.class);
            int numLines = context.getNumTriplePerFile();
            int scale    = context.getScale();
            String engine = context.getEngine();
            String dataset = context.getDataSet();
            String device  = context.getDeviceName();
            EX_RESULT_PROCESS ex_result_process = new EX_RESULT_PROCESS( engine, dataset, device, numLines, scale);
            ex_result_process.processQueryResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
