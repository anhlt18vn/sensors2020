package org.rdf4led.benchmark;

import java.io.File;
import java.util.Arrays;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */
public abstract class ExperimentInput<Engine> extends Experiment {

    private int batchSize;

    protected ExperimentInput(ExperimentContext experimentContext) {
        super(experimentContext);
        this.batchSize = experimentContext.getBatchSize();
    }


    @Override
    public void run() {
        Engine engine = initializeEngine();
        File file = new File(path2DataFolder);

        int count = 1;
        File[] files = file.listFiles();
        Arrays.sort(files);

        for (File input : files) {
            if (input.isHidden()) continue;
            if (input.isDirectory()) continue;
            if (input.getName().contains(".txt")) continue;

            long start = StopWatch.getTimeStampMillis();

            long size = count* batchSize;

            doInput(input, engine);

            long duration = StopWatch.getTimeStampMillis() - start;

            System.out.println("Inserting file: " + input.getName() + " storageSize " + size +  " speed " +  (batchSize*1000f)/duration + " tps" + " in " + duration + "ms");
            String toWrite = StopWatch.getTimeStampMillis() + "\t" + count;
            resultWriter.write(toWrite);
            count++;
        }

        close(engine);
    }


    protected abstract Engine initializeEngine();

    protected abstract void doInput(File fileInput, Engine engine);

    protected abstract long getSize(Engine engine);

    protected abstract void close(Engine engine);
}