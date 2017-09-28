package org.rdf4led.benchmark;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public abstract class Experiment {

    protected String path2ExpFolder;
    protected String path2DataFolder;
    protected String path2QueryFolder;
    protected String path2StoreFolder;
    protected ResultWriter resultWriter;
    private ExperimentContext experimentContext;
    private String engine;
    private String path2Result;
    private String resultFileName;
    private String mem_resultFileName;

    public Experiment(ExperimentContext experimentContext) {
        this.experimentContext = experimentContext;

        this.path2ExpFolder = experimentContext.getPath2Exp();
        String dataSet = experimentContext.getDataset();
        String dataSize = experimentContext.getDataSize();
        this.engine = experimentContext.getEngine();
        String device = experimentContext.getDevice();

        this.path2DataFolder = path2ExpFolder + "data/" + experimentContext.getDataset();
        this.path2Result = path2ExpFolder + "result/";
        String experimentType = experimentContext.getExpType().toUpperCase();
        switch (experimentType) {
            case "INPUT":
                this.path2QueryFolder = null;
                this.path2StoreFolder = path2ExpFolder + "store/" + experimentContext.getEngine() + "/";
                this.resultFileName = "INPUT" + "_" + this.engine + "_" + dataSet + "_" + device;
                break;
            case "QUERY":
                this.path2StoreFolder = path2ExpFolder + "store/" + this.engine + "/" + dataSet + "/" + dataSize + "/";
                this.path2QueryFolder = path2ExpFolder + "query/" + dataSet + "/";
                this.resultFileName = "QUERY" + "_" + this.engine + "_" + device + "_" + dataSet + "_" + dataSize + ".txt";
        }

        this.mem_resultFileName = "Mem_" + this.resultFileName;

        this.resultWriter = new ResultWriter();
        try {
            PrintStream printStream = new PrintStream(new File(this.path2Result + "/" + this.resultFileName));
            this.resultWriter.setPrintWriter(printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        viewExpInfo();
    }

    public String getPath2ExpFolder() {
        return path2ExpFolder;
    }

    public void setPath2ExpFolder(String path2ExpFolder) {
        this.path2ExpFolder = path2ExpFolder;
    }

    public String getPath2DataFolder() {
        return path2DataFolder;
    }

    public void setPath2DataFolder(String path2DataFolder) {
        this.path2DataFolder = path2DataFolder;
    }

    public String getPath2QueryFolder() {
        return path2QueryFolder;
    }

    public void setPath2QueryFolder(String path2QueryFolder) {
        this.path2QueryFolder = path2QueryFolder;
    }

    public String getPath2StoreFolder() {
        return path2StoreFolder;
    }

    public void setPath2StoreFolder(String path2StoreFolder) {
        this.path2StoreFolder = path2StoreFolder;
    }

    protected void viewExpInfo() {
        System.out.println("Running " + this.experimentContext.getExpType() + " with " + this.engine);
        System.out.println("Data set: " + this.experimentContext.getDataset());
        System.out.println("Data path: " + this.path2DataFolder);

        if (path2StoreFolder != null) {
            System.out.println("Path to Storage: " + path2StoreFolder);
        }
        if (path2QueryFolder != null) {
            System.out.println("Path to Queries: " + path2QueryFolder);
        }

        System.out.println("Path to Result: " + path2Result);
    }


    public void execute() {
        MemMonitor memMonitor = new MemMonitor();
        try {
            PrintStream printStream = new PrintStream(new File(this.path2Result + this.mem_resultFileName));
            memMonitor.setStreamOut(printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(memMonitor);
        thread.start();

        try {
            run();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            thread.stop();
        }
        thread.stop();
    }

    protected abstract void run();
}
