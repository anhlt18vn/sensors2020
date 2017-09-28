package org.rdf4led.benchmark.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * org.benchmark.tool
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  05/10/17.
 */
public class RESULT_PROCESS {
    public static final String pathToResult = System.getProperty("user.home") +  "/exp/result/";

    public static final String pathToPlotData = System.getProperty("user.home")  +  "/exp/result/";

    //public static NumberFormat numberFormatter = new DecimalFormat("##.000");

    public static void main(String[] args) {
//==================VIRTUOSO=========================================================================================
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv300", "PII", 100000, 1).processQueryResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv200", "PII", 100000, 1).processQueryResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv100", "PII", 100000, 1).processQueryResult();
//
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv200", "PI0", 100000, 1).processQueryResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv100", "PI0", 100000, 1).processQueryResult();
//
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv200", "BBB", 100000, 1).processQueryResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv100", "BBB", 100000, 1).processQueryResult();
//
//
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv20", "GII", 10000000, 1).processQueryResult();
//
//
//        EX_RESULT_PROCESS.set("watdiv300","GII", 100000,"RDF_XS", 1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300","GII",100000,"JENATDB",1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300", "PII", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv200", "PII", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv100", "PII", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv300", "PI0", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv200", "PI0", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv100", "PI0", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv300", "BBB", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv200", "BBB", 100000, "RDF_XS", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv100", "BBB", 100000, "RDF_XS", 1).processQueryResult();
//
//        EX_RESULT_PROCESS.set("watdiv200", "PII", 100000, "JENATDB", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv100", "PII", 100000, "JENATDB", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv300", "PI0", 10000, "JENATDB", 1).processQueryResult();
        EX_RESULT_PROCESS.set("watdiv100", "PI0", 100000, "JENATDB", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv200", "BBB", 100000, "JENATDB", 1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv100", "BBB", 100000, "JENATDB", 1).processQueryResult();

//        VIRTUOSO_RESULTS_PROCESS.set("watdiv300", "PI0", 100000, 1).processInputResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv300", "BBB", 100000, 1).processInputResult();
//        VIRTUOSO_RESULTS_PROCESS.set("watdiv300", "PII", 100000, 1).processInputResult();
//          VIRTUOSO_RESULTS_PROCESS.set("watdiv300","GII", 100000,1).processInputResult();

//        EX_RESULT_PROCESS.set("watdiv300", "PI0", 100000, "RDF_XS",1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300", "BBB", 100000, "RDF_XS",1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300", "PII", 100000, "RDF_XS",1).processInputResult();
//
//        EX_RESULT_PROCESS.set("watdiv300", "PI0", 100000, "JENATDB",1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300", "BBB", 100000, "JENATDB",1).processInputResult();
//        EX_RESULT_PROCESS.set("watdiv300", "PII", 100000, "JENATDB",1).processInputResult();

//        EX_RESULT_PROCESS.set("watdiv20", "GII", 100000, "JENATDB",1).processQueryResult();
//        EX_RESULT_PROCESS.set("watdiv20", "GII", 100000, "RDF_XS",1).processQueryResult();


    }
}



















































//    public  static HashMap<Integer, List<Integer>>  processRDF3XFile(String folderIn)
//    {
//        HashMap<Integer, List<Integer>> queryId_results = new HashMap<>();
//
//        try {
//
//            File folderOfResult = new File(folderIn);
//
//            File[] listFiles = folderOfResult.listFiles();
//
//            Arrays.sort(listFiles);
//
//            int i = 0;
//
//            for (File file : listFiles)
//            {
//                if (file.getName().contains(".txt"))
//                {
//                    queryId_results.put(i++, readTextFromRDF3XFile(file));
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println(folderIn);
//
//            e.printStackTrace();
//        }
//        return queryId_results;
//    }
//
//    public static int processRDF3XString(String s)
//    {
//        System.out.println(s);
//
//        int first_space = s.indexOf(' ');
//
//        int first_tab  =  s.indexOf('\t');
//
//        int second_tab = s.indexOf('\t',first_tab + 1);
//
//        //System.out.println(" fs " + first_space + " ft " + first_tab + " st " + second_tab);
//
//        String start_time = s.substring(first_space + 1, first_tab);
//
//        String end_time = s.substring(first_tab + 1, second_tab);
//
//        long startTime = Long.parseLong(start_time);
//
//        long endTime   = Long.parseLong(end_time);
//
//        int duration =  (int) ((endTime - startTime));
//
//        System.out.println(" st " + start_time + " et " + end_time + " du " + Integer.toString(duration));
//
//        return duration;
//    }
//
//    public static List<Integer> readTextFromRDF3XFile(File file)
//    {
//        ArrayList<Integer> arrayList = new ArrayList<>();
//
//        try
//        {
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//
//            String s = reader.readLine();
//
//            while (s != null)
//            {
//                arrayList.add(processRDF3XString(s));
//
//                s = reader.readLine();
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return arrayList;
//    }
//
//    public static void writeResult(HashMap<Integer, List<Integer>> result, String folder, String file)
//    {
//        List<String> stringList = new ArrayList<>();
//
//        for (int i=0; i < result.size(); i++)
//        {
//            List<Integer> integerList = result.get(i);
//
//            String s = "";
//
//            for (int intElement:integerList)
//            {
//                s = s + numberFormatter.format(intElement) + "\t";
//            }
//
//            s = s + "\n";
//
//            stringList.add(s);
//        }
//
//        FileUtil.write(folder, file, stringList);
//    }
