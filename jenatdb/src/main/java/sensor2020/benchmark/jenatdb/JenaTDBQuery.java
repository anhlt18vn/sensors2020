package sensor2020.benchmark.jenatdb;


/*
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentQuery;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JenaTDBQuery extends ExperimentQuery<Model> {
    public static void main(String args[]) {
        String path2File = args[0];
        File file = new File(path2File);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExperimentContext experimentContext = objectMapper.readValue(file, ExperimentContext.class);
            JenaTDBQuery rdf4JInput = new JenaTDBQuery(experimentContext);
            rdf4JInput.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected JenaTDBQuery(ExperimentContext experimentContext) {
        super(experimentContext);
    }

    protected Model intializeEngine() {
        Location location = Location.create(this.path2StoreFolder);
        Model model = ModelFactory.createModelForGraph(TDBFactory.createDataset(location).getDefaultModel().getGraph());
        return model;
    }

    protected void doQueries(String queryString, Model model) {
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        ResultSet resultSet = queryExecution.execSelect();
        printResult(resultSet);
    }

    protected void printResult(Object object) {
        if (object instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) object;
            int i = 0;
            for (; resultSet.hasNext(); ) {
                QuerySolution querySolution = resultSet.nextSolution();
                i++;
                Iterator<String> vars = querySolution.varNames();
                for (String var = ""; vars.hasNext(); var = vars.next()) {
                    System.out.println(querySolution.get(var).toString());
                }
                System.out.println(i);
            }
            System.out.println(i);
        }

    }
}
