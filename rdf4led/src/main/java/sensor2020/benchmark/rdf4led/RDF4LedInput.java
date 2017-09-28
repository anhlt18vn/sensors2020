package sensor2020.benchmark.rdf4led;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rdf4led.GraphStore;
import org.rdf4led.RDF4LedContext;
import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentInput;
import org.rdf4led.graph.parser.ParserNTriple;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 */
public class RDF4LedInput extends ExperimentInput<GraphStore> {

    private RDF4LedContext context;

    protected RDF4LedInput(ExperimentContext experimentContext) {
        super(experimentContext);
    }

    @Override
    protected GraphStore initializeEngine() {
        context = new RDF4LedContext(this.getPath2StoreFolder());
        GraphStore graphStore = (GraphStore) context.getGraphByName("http://rdf4led.org");
        return graphStore;
    }

    @Override
    protected void doInput(File file, GraphStore graphStore) {
        ParserNTriple<Integer> parser = new ParserNTriple<>(this.context);
        graphStore = (GraphStore) parser.parseToGraphFromFile(file, graphStore);
    }

    @Override
    protected long getSize(GraphStore graphStore) {
        return 0;
    }

    @Override
    protected void close(GraphStore graphStore) {
        context.sync();
        graphStore.sync();
    }

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
}