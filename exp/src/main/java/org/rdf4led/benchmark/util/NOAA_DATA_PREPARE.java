package org.rdf4led.benchmark.util;

import org.rdf4led.benchmark.FileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * tools
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  27/09/17.
 */
public class NOAA_DATA_PREPARE
{
    static String folderInput = "/home/anhlt185/Desktop/data/noaa_/";

    static String folderOut   = "/home/anhlt185/Desktop/data/noaa/";

    public static void main(String[] args)
    {
        concatFile("noaaX_clean.nt", folderOut, 1, 250);
    }

    public static void concatFile(String fileInput, String folderOut, int form, int to)
    {
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(folderInput + fileInput));
            int start = form;

            List<String> sList = new ArrayList<>();

            while (start<=to)
            {
                String s = bufferedReader.readLine();

                if ((sList.size() == 40000) || (s == null))
                {
                    System.out.println(start);

                    String filename="";

                    if (start < 10) filename = "noaa00" + start + ".nt";
                    else
                    if (start <100) filename = "noaa0" + start + ".nt";
                    else filename = "noaa" + start + ".nt";

                    FileUtil.write(folderOut, filename, sList);

                    start++;

                    sList.clear();
                }

                if (s == null) break;

                sList.add(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
