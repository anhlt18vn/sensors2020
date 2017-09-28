package org.rdf4led.benchmark;
import com.sun.management.OperatingSystemMXBean;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;

import static org.rdf4led.benchmark.StopWatch.getTimeStampMillis;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public class MemMonitor implements Runnable {

    private OperatingSystemMXBean bean;

    private boolean isStopped;

    private PrintStream printStream;

    public MemMonitor() {
        this.isStopped = false;
        bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        printStream = System.out;
    }

    public void setStreamOut(PrintStream printStream){
        this.printStream = printStream;
    }

    public void run() {
        while (!isStopped) {
            try {
                long free = Runtime.getRuntime().freeMemory();
                long total = Runtime.getRuntime().totalMemory();
                long consume = free - total;

                String s = getTimeStampMillis() + "\t" + free + "\t" + total + "\t" + consume + "\n";
                printStream.print(s);
                printStream.flush();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }


    public void stop() {
        isStopped = true;
    }
}
