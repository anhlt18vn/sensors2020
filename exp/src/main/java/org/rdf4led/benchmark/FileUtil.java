package org.rdf4led.benchmark;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * org.util
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  17/08/17.
 */
public class FileUtil
{
    public static String USER_HOME = System.getProperty("user.home") + "/";


    public static FileChannel open(String filePath)
    {
        File file = new File(filePath);

        String location = file.getParent();

        return open(location, file.getName(),"rw");
    }

    //Random Access File Channel
    public static FileChannel open(String locator, String name, String mode)
    {

        //Create File
        File f = new File(locator);

        if (f.mkdirs())
        {
            f.mkdirs();
        }

        try
        {
            RandomAccessFile out = new RandomAccessFile(locator + "/" +name, mode);

            return out.getChannel();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e.toString());
        }
     }

    public static String readStringFromFile(String fileName)
    {
        File file = new File(fileName);

        return readStringFromFile(file);
    }

    public static String readStringFromFile(File file)
    {
        try
        {
            FileReader reader = new FileReader(file);

            String s = readStringFromReader(reader);

            return s;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.toString());
        }

    }

    public static File initFile(String filePath){
        //TODO:
        File file = new File(filePath);
        return file;
//        if (file.exists()){
//            return file;
//        } else {
//            file.mkdirs();
//            return file;
//        }
    }

    public static String readStringFromReader(Reader reader)
    {
        BufferedReader buff =  new BufferedReader(reader);

        try
        {

            StringBuilder stringBuilder = new StringBuilder();

            try
            {
                for (String line = buff.readLine(); line != null; line = buff.readLine())
                {
                    stringBuilder.append(line);

                    stringBuilder.append("\n");
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }
        finally
        {
            try
            {
                buff.close();

                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteDirectory(File dir)
    {
        if (dir.isDirectory())
        {
            File[] children = dir.listFiles();

            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDirectory(children[i]);

                if (!success)
                {
                    return false;
                }
            }
        }
        // either file or an empty directory
        System.out.println("removing file or directory : " + dir.getName());

        return dir.delete();
    }

    /*write data from list String into file*/
    public static void write(String folderOut, String fileNameOut, List<String> stringList)
    {
        try
        {
            FileWriter fw = new FileWriter(folderOut + "/" + fileNameOut);

            BufferedWriter bw = new BufferedWriter(fw);

            for (String s : stringList)
            {
                bw.write(s + "\n");

            }

            bw.flush();
            bw.close();
            fw.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.toString());
        }
    }

    public static void extract(String fileInput, String folderOut, String prefix, int numberOfLine, int form, int to)
    {
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInput));

            int start = form;

            List<String> sList = new ArrayList<>();

            while (start<=to)
            {
                String s = bufferedReader.readLine();

                if ((sList.size() == 40000) || (s == null))
                {
                    System.out.println(start);

                    String filename="";

                    if (start < 10) filename = prefix + "00" + start + ".nt";
                    else
                    if (start <100) filename = prefix + "0" + start + ".nt";
                    else filename = prefix + start + ".nt";

                    FileUtil.write(folderOut, filename, sList);

                    start++;

                    sList.clear();
                }

                if (s == null) break;

                if (s.contains("http://www.w3.org/2001/XMLSchema#gYearMonth")
                        ||s.contains("http://www.w3.org/2001/XMLSchema#gYear")
                        ||s.contains("\"Infinity\"^^<http://www.w3.org/2001/XMLSchema#double>")
                )
                {
                    continue;
                }


                sList.add(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static int countLine(String fileIn)
    {
        int i=0;

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIn));;

            String s = bufferedReader.readLine();

            while (s != null)
            {
                s = bufferedReader.readLine();

                i++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return i;
    }



    /**
     * Created by Anh Le-Tuan
     * Email: anh.letuan@tu-berlin.de
     * <p>
     * Date: 27.03.19
     * TODO Description:
     */
    public static class FileChannelUtil {

        public static FileChannel openFileChannel(String path) throws FileNotFoundException {
            RandomAccessFile file = new RandomAccessFile(path, "rw");
            return file.getChannel();
        }

        public static ByteBuffer readAll(FileChannel fileChannel) throws IOException {
            long len = fileChannel.size();

            ByteBuffer buffer = ByteBuffer.allocate((int) len);

            fileChannel.read(buffer);

            return buffer;
        }
    }

}