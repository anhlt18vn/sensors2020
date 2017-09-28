package org.rdf4led.benchmark;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 05.01.20
 */

public abstract  class ExperimentQuery<Engine> extends Experiment
{
    protected ExperimentQuery(ExperimentContext context)
    {
        super(context);
    }

    @Override
    public void run()
    {
        Engine engine = intializeEngine();

        File queries  = new File(path2QueryFolder);

        doQueryFolder(queries, engine);

        resultWriter.close();
    }

    private void doQueryFolder(File queries, Engine engine){

        File[] listFiles= queries.listFiles();

        Arrays.sort(listFiles);

        for (File file:listFiles) {

            if (file.isHidden()) continue;

            if (file.getName().contains("~")) continue;

            if (file.isDirectory()) {
                doQueryFolder(file, engine);
            }

            System.out.println("Query " + file.getName() + " is running");

            String queryString = FileUtil.readStringFromFile(file);

            String[] listQueries = readListQueries(queryString);

            if (listQueries.length <= 2) {
                queryOne(listQueries[0], engine);
            } else {
                queryMany(listQueries, engine);
            }
        }

    }

    private void queryMany(String[] queries, Engine engine){

        String toWrite = "";

        System.out.println(queries[0]);

        for (int index=0; index<queries.length-1; index ++)
        {
            long start = StopWatch.getTimeStampMillis();

            doQueries(queries[index], engine);

            long duration = StopWatch.getTimeStampMillis() - start;

            toWrite = toWrite + "\t" + duration;
        }

        System.out.println(toWrite);

        resultWriter.write(toWrite + "\n\n");
    }

    private void queryOne(String queryString, Engine engine){

        String toWrite = "";

        System.out.println(queryString);


        for (int i = 0; i < 1; i++)
        {
            long start = StopWatch.getTimeStampMillis();

            doQueries(queryString, engine);

            long duration = StopWatch.getTimeStampMillis() - start;

            toWrite = toWrite + "\t" + duration;
        }


        System.out.println(toWrite);

        resultWriter.write(toWrite + "\n\n");
    }


    protected String[] readListQueries(String listQuery){
        String[] part = listQuery.split("\n\n");
        return part;
    }

    protected abstract Engine intializeEngine();

    protected abstract void doQueries(String queryString, Engine engine);

    protected long getSize(Engine engine){ return  0;}

    protected abstract void printResult(Object resultSet);
}
