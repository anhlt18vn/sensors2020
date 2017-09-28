package org.rdf4led.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public class ExperimentContext {
    private String path2Exp;
    private String expType;
    private String dataset;
    private String engine;
    private String dataSize;
    private String device;
    private int batchSize;

    public ExperimentContext() {

    }


    public String getExpType() {
        return expType;
    }


    public void setExpType(String expType) {
        this.expType = expType;
    }


    public String getDataset() {
        return dataset;
    }


    public void setDataset(String dataset) {
        this.dataset = dataset;
    }


    public String getEngine() {
        return engine;
    }


    public void setEngine(String engine) {
        this.engine = engine;
    }


    public String getDataSize() {
        return dataSize;
    }


    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }


    public String getDevice() {
        return device;
    }


    public void setDevice(String device) {
        this.device = device;
    }


    public String getPath2Exp() {
        return path2Exp;
    }


    public void setPath2Exp(String path2Exp) {
        this.path2Exp = path2Exp;
    }


    public static void main(String[] args) {
        ExperimentContext experimentContext = new ExperimentContext();
        experimentContext.setDataset("noaa");
        experimentContext.setDataSize("50000000");
        experimentContext.setDevice("X28");
        experimentContext.setPath2Exp("/home/anh/exp/");
        experimentContext.setEngine("BlazeGraph");
        experimentContext.setExpType("Input");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(experimentContext);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public int getBatchSize() {
        return batchSize;
    }


    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
