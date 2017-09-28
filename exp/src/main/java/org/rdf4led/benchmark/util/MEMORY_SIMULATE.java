package org.rdf4led.benchmark.util;

import java.util.Arrays;

/**
 * org.benchmark.tool
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  08/10/17.
 */
public class MEMORY_SIMULATE
{
    public static void main(String[] args)
    {
        int MB = 1024*1024;

        byte[] buffer;

        while(true)
        {
            System.out.println("Input amount: ");

            String in = System.console().readLine();

            int amount = Integer.parseInt(in);

            if (amount == 0) break;

            buffer = new byte[amount * MB];

            Arrays.fill(buffer, (byte)1);
        }
    }
}
