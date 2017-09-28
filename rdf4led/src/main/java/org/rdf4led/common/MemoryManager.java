package org.rdf4led.common;

/**
 * org.rdf4led.system
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 16/08/17.
 */
public class MemoryManager {
    static Runtime runtime;

    private static final long MEGABYTE = 1024L * 1024L;

    public static int bytesToMegabytes(long bytes) {
        return (int) (bytes / MEGABYTE);
    }

    public static int usedMemory(){
        runtime = Runtime.getRuntime();
        int used_ = bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
        return used_;
    }

    public static void log1() {
        Runtime runtime = Runtime.getRuntime();

        //Run the garbage collector
        //runtime.gc();

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));
        System.out.println("Total memory: " + bytesToMegabytes(runtime.totalMemory()));
        System.out.println("Free memory: " + bytesToMegabytes(runtime.freeMemory()));
    }


}