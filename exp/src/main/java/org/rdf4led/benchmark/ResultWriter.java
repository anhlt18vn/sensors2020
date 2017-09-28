package org.rdf4led.benchmark;

import java.io.*;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public class ResultWriter {
    private PrintStream printWriter;


    public ResultWriter() {
        this.printWriter = System.out;
    }


    public void setPrintWriter(PrintStream printWriter) {
        this.printWriter = printWriter;
    }


    public void write(String s) {
        this.printWriter.println(s);
        this.printWriter.flush();
    }


    public void close() {
        this.printWriter.close();
    }
}
