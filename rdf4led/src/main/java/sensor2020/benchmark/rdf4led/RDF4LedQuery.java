package sensor2020.benchmark.rdf4led;


/*
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 *
 * Date: 15.04.20
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import org.rdf4led.GraphStore;
import org.rdf4led.RDF4LedContext;
import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentQuery;
import org.rdf4led.common.Config;
import org.rdf4led.common.mapping.MappingHashMap;
import org.rdf4led.query.engine.Forwarder;
import org.rdf4led.query.engine.ForwarderPrint;
import org.rdf4led.query.engine.QueryExecutor;
import org.rdf4led.query.sparql.Query;
import org.rdf4led.query.sparql.algebra.AlgebraGenerator;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.parser.lang.ParserSPARQL11;

import java.io.File;
import java.io.IOException;

public class RDF4LedQuery extends ExperimentQuery<GraphStore> {

    private RDF4LedContext context;

    public static void main(String[] args) {
        String path2File = args[0];
        File file = new File(path2File);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExperimentContext experimentContext = objectMapper.readValue(file, ExperimentContext.class);
            RDF4LedInput rdf4LedInput = new RDF4LedInput(experimentContext);
            rdf4LedInput.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected RDF4LedQuery(ExperimentContext context) {
        super(context);
    }

    protected GraphStore intializeEngine() {
        context = new RDF4LedContext(this.getPath2StoreFolder());
        GraphStore graphStore = (GraphStore) context.getGraphByName("http://rdf4led.org");
        return graphStore;
    }

    protected void doQueries(String queryString, GraphStore graphStore) {
        Query<Integer> query = new Query<>(context);
        ParserSPARQL11<Integer> parser = new ParserSPARQL11<>(context);
        query = parser.parse(query, queryString);
        AlgebraGenerator<Integer> algebraGenerator = new AlgebraGenerator<>(context);
        Op<Integer> op = algebraGenerator.compile(query);
        Config.R = true;
        QueryExecutor queryExecutor = new QueryExecutor(context, graphStore);
        Forwarder<Integer> opForward = queryExecutor.compile(op, new ForwarderPrint<>(context));
        opForward.process(new MappingHashMap<Integer>());
    }

    protected void printResult(Object resultSet) {

    }
}
