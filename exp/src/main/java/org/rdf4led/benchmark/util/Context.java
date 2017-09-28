package org.rdf4led.benchmark.util;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 1/30/20
 * PROJECT: benchmark
 */
public class Context{
    private String engine;
    private String dataSet;
    private String deviceName;
    private int numTriplePerFile;
    private int scale;

    public Context(){}

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getNumTriplePerFile() {
        return numTriplePerFile;
    }

    public void setNumTriplePerFile(int numTriplePerFile) {
        this.numTriplePerFile = numTriplePerFile;
    }
}
