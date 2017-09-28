package org.rdf4led.benchmark;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public class StopWatch {
    public static long getTimeStampMillis() {
        return System.currentTimeMillis();
    }


    public static long getTimeSampleNanos() {
        return System.nanoTime();
    }
}
