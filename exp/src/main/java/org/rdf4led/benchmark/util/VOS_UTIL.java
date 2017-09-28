package org.rdf4led.benchmark.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * tools
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 25/09/17.
 */
public class VOS_UTIL {
    public static void main(String[] args) {
        create_file_script(args);
    }


    public static void create_file_script(String[] args) {
        String DEVICE_NAME = args[0];

        String PATH_TO_CONSOLE = args[1];

        String PATH_TO_DATA = args[2];

        String PATH_TO_QUERY = args[3];

        String PATH_TO_RESULT = args[4];

        String PATH_TO_VIRTUOSO = args[5];

        String DATA_SET = args[6];

        String PASSWORD = args[7];

        File resultFolder = new File(PATH_TO_RESULT + DATA_SET + "/" + DEVICE_NAME + "/VIRTUOSO/");

        if (!resultFolder.exists()) resultFolder.mkdirs();

        // Input script
        try {
            BufferedWriter bufferedWriter =
                    new BufferedWriter(
                            new FileWriter(new File(PATH_TO_CONSOLE + DATA_SET + "_" + "INPUT_VIRTUOSO.sh")));

            File dataFolder = new File(PATH_TO_DATA);

            File[] dataFiles = dataFolder.listFiles();

            if (dataFiles == null) {
                if (dataFiles.length == 0) {
                    System.err.println("empty data folder");
                }
            }

            Arrays.sort(dataFiles);

            String pid = "pid=$(pidof virtuoso-t);\n";

            bufferedWriter.write(pid);

            for (File file : dataFiles) {
                String command = "sh ./console/virtuoso/virtuoso_input_one_file.sh";

                if (!file.isFile()) continue;

                String echo = "echo \"input " + file.getName() + "\"; \n";

                bufferedWriter.write(echo);

                command =
                        command
                                + " "
                                + PASSWORD
                                + " "
                                + PATH_TO_VIRTUOSO
                                + " "
                                + dataFolder.getCanonicalPath()
                                + "/"
                                + " "
                                + file.getName()
                                + " ";

                command = command + "\n";

                bufferedWriter.write(command);

                String result_to_console =
                        "echo $(date +%s%3N) \t $(cat /proc/${pid}/status |grep VmSize) \t $(cat /proc/${pid}/status |grep VmRSS); \n";

                bufferedWriter.write(result_to_console);

                String result_to_file =
                        "echo $(date +%s%3N) \t $(cat /proc/${pid}/status |grep VmSize) \t $(cat /proc/${pid}/status |grep VmRSS)";

                result_to_file =
                        result_to_file
                                + ">> "
                                + PATH_TO_RESULT
                                + DATA_SET
                                + "/"
                                + DEVICE_NAME
                                + "/VIRTUOSO/"
                                + DATA_SET
                                + "_"
                                + DEVICE_NAME
                                + "_VIRTUOSO_INPUT.txt 2>&1; \n";
                bufferedWriter.write(result_to_file);
            }

            bufferedWriter.flush();

            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Query script
        try {
            BufferedWriter bufferedWriter =
                    new BufferedWriter(
                            new FileWriter(new File(PATH_TO_CONSOLE + DATA_SET + "_" + "QUERY_VIRTUOSO.sh")));

            File queryFolder = new File(PATH_TO_QUERY);

            File[] queryFiles = queryFolder.listFiles();

            if (queryFiles == null) {
                if (queryFiles.length == 0) {
                    System.err.println("empty data folder");
                }
            }

            String pid = "pid=$(pidof virtuoso-t);\n";

            bufferedWriter.write(pid);

            Arrays.sort(queryFiles);

            for (File file : queryFiles) {
                if (!file.isFile()) continue;
                if (file.getName().contains("~")) continue;

                String echo = "echo \"query " + file.getName() + "\"; \n";

                bufferedWriter.write(echo);

                for (int i = 0; i < 1; i++) {
                    String command = "bash ./console/virtuoso/virtuoso_single_query.sh";

                    command =
                            command
                                    + " "
                                    + PASSWORD
                                    + " "
                                    + queryFolder.getCanonicalPath()
                                    + "/"
                                    + " "
                                    + file.getName()
                                    + " "
                                    + PATH_TO_VIRTUOSO
                                    + " ";

                    command =
                            command
                                    + resultFolder.getCanonicalPath()
//                  + DATA_SET
                                    + "/"
//                  + DEVICE_NAME
//                  + "/VIRTUOSO/"
                                    + DATA_SET
                                    + "_"
                                    + DEVICE_NAME
                                    + "_VIRTUOSO_QUERY_"
                                    + file.getName()
                                    + "; \n";
//                  + " > /dev/null; \n";
                    bufferedWriter.write(command);

                    String result_to_console =
                            "echo $(date +%s%3N) \t $(cat /proc/${pid}/status |grep VmSize) \t $(cat /proc/${pid}/status |grep VmRSS); \n";

                    //bufferedWriter.write(result_to_console);

                    String result_to_file =
                            "echo $(date +%s%3N) \t $(cat /proc/${pid}/status |grep VmSize) \t $(cat /proc/${pid}/status |grep VmRSS)";

                    result_to_file =
                            result_to_file
                                    + "  "
                                    + PATH_TO_RESULT
                                    + DATA_SET
                                    + "/"
                                    + DEVICE_NAME
                                    + "/VIRTUOSO/"
                                    + DATA_SET
                                    + "_"
                                    + DEVICE_NAME
                                    + "_VIRTUOSO_QUERY_MEM_"
                                    + file.getName()
                                    + " > /dev/null; \n";

                    //bufferedWriter.write(result_to_file);
                }
            }

            bufferedWriter.flush();

            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
