package org.rdf4led.benchmark.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * org.benchmark.tool
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  30/09/17.
 */
public class RDF_3X_UTIL
{
    private static String PATH_TO_EXP;
    private static String PATH_TO_DATA;
    private static String PATH_TO_QUERY;
    private static String PATH_TO_CONSOLE;
    private static String PATH_TO_RESULT;
    private static String DATA_SET;
    private static String DEVICE_NAME;

    private static File FOLDER_QUERY;
    private static File FOLDER_DATA;
    private static File FOLDER_RESULT;

    public static void main(String[] args)
    {
        PATH_TO_EXP = args[0];
        DATA_SET    = args[1];
        DEVICE_NAME = args[2];

        PATH_TO_DATA      = PATH_TO_EXP + "/data/";
        PATH_TO_QUERY     = PATH_TO_EXP + "/queries/";
        PATH_TO_CONSOLE   = PATH_TO_EXP + "/console/";
        PATH_TO_RESULT    = PATH_TO_EXP + "/result/" + DATA_SET + "/" + DEVICE_NAME + "/RDF-3X/";

        FOLDER_QUERY = new File(PATH_TO_QUERY + DATA_SET + "/");
        FOLDER_DATA  = new File(PATH_TO_DATA + DATA_SET + "/");
        FOLDER_RESULT= new File(PATH_TO_RESULT + DATA_SET + "/" + DEVICE_NAME + "/RDF-3X/");

        if (!FOLDER_RESULT.exists()) FOLDER_RESULT.mkdirs();

        generateRDF3XUpdate();
        generateRDF3XQuery();
    }

    public static void generateRDF3XUpdate()
    {
        try
        {
            String Command = "sudo ../gh-rdf3x/bin/rdf3xupdate " + DATA_SET ;

            String echoTime = "echo $(date +%s%3N) \n";

            String echoTimeToFile = "echo $(date +%s%3N) >> " + PATH_TO_RESULT
                    + DATA_SET + "_"
                    + DEVICE_NAME + "_"
                    + "RDF-3X_UPDATE.result \n";


            File consoleUpdate = new File(PATH_TO_CONSOLE + DATA_SET + "_UPDATE_RDF-3X.sh");

            FileWriter writer  = new FileWriter(consoleUpdate);

            BufferedWriter bw  = new BufferedWriter(writer);

            File[] dataFiles = FOLDER_DATA.listFiles();

            Arrays.sort(dataFiles);

            System.out.print(echoTime);
            System.out.print(echoTimeToFile);
            System.out.println("../gh-rdf3x/bin/rdf3xload " + DATA_SET  + " " +  dataFiles[0].getCanonicalFile() + "\n");
            System.out.print(echoTime);
            System.out.print(echoTimeToFile);
            bw.write(echoTime);
            bw.write(echoTimeToFile);
            bw.write("../gh-rdf3x/bin/rdf3xload " + DATA_SET  + " " +  dataFiles[0].getCanonicalFile() + "\n");
            bw.write(echoTime);
            bw.write(echoTimeToFile);

            for (int i=1; i< dataFiles.length; i++)
            {
                String commandLine = Command + " " + dataFiles[i].getCanonicalFile() + "\n";

                bw.write(commandLine);
                bw.write(echoTime);
                bw.write(echoTimeToFile);

                System.out.print(commandLine);
                System.out.print(echoTime);
                System.out.print(echoTimeToFile);
            }

            bw.flush();

            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void generateRDF3XQuery()
    {
        try
        {
            String Command          = "sudo ../gh-rdf3x/bin/rdf3xquery";

            String echoTime         = "echo $(date +%s%3N) \n";

            String echoTimeToFile   = "echo $(date +%s%3N) >> result" + "/" + DATA_SET
                    + "/" + DEVICE_NAME
                    + "/" + "RDF-3X" + "/"
                    + DATA_SET + "_"
                    + DEVICE_NAME + "_"
                    + "RDF-3X_QUERY.tst \n";


            File consoleQuery       = new File(PATH_TO_CONSOLE + DATA_SET + "_" + "QUERY_RDF-3X.sh");

            FileWriter fileWriter   = new FileWriter(consoleQuery);

            BufferedWriter bw       = new BufferedWriter(fileWriter);

            File[] query_files      = FOLDER_QUERY.listFiles();

            Arrays.sort(query_files);

            for (File file:query_files)
            {
                if (!file.isFile()) continue;
                if (file.getName().contains("~")) continue;

                String echo = "\n\necho " + "\"" + file.getCanonicalFile() + "\"" + "\n";
                String commandLine = Command + " " + DATA_SET + " " + file.getCanonicalFile() + "\n";

                System.out.print(echo);
                bw.write(echo);

                for (int i=0; i<100; i++)
                {

                    System.out.print(echoTime);
                    bw.write(echoTime);

                    System.out.print(echoTimeToFile);
                    bw.write(echoTimeToFile);

                    System.out.print(commandLine);
                    bw.write(commandLine);
                }

                System.out.print(echoTime);
                bw.write(echoTime);

                System.out.print(echoTimeToFile);
                bw.write(echoTimeToFile);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
