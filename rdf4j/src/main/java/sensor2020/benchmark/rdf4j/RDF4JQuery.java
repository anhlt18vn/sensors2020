package sensor2020.benchmark.rdf4j;


/*
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentQuery;

import java.io.File;
import java.io.IOException;

public class RDF4JQuery extends ExperimentQuery<RepositoryConnection> {

    public static void main(String[] args) {
        String path2File = args[0];
        File file = new File(path2File);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExperimentContext experimentContext = objectMapper.readValue(file, ExperimentContext.class);
            RDF4JQuery rdf4JQuery = new RDF4JQuery(experimentContext);
            rdf4JQuery.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Repository repository;

    protected RDF4JQuery(ExperimentContext context) {
        super(context);
    }

    @Override
    protected RepositoryConnection intializeEngine() {
        File dataDir = new File(this.path2StoreFolder);
        String indexes = "spoc,posc,cosp";
        this.repository = new SailRepository(new NativeStore(dataDir, indexes));
        repository.isInitialized();
        return repository.getConnection();
    }

    @Override
    protected void doQueries(String queryString, RepositoryConnection connection) {
        try {
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult results = tupleQuery.evaluate();

            printResult(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printResult(Object resultSet) {
        try {

            if (resultSet instanceof TupleQueryResult) {
                TupleQueryResult tupleQueryResult = (TupleQueryResult) resultSet;

                for (; tupleQueryResult.hasNext(); ) {
                    System.out.println(((TupleQueryResult) resultSet).next());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
