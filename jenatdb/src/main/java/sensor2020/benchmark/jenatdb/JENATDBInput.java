package sensor2020.benchmark.jenatdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 */
public class JENATDBInput extends ExperimentInput<Model> {

    protected JENATDBInput(ExperimentContext experimentContext) {
        super(experimentContext);
    }

    @Override
    protected Model initializeEngine() {
        Location location = Location.create(this.path2StoreFolder);

        Model model = ModelFactory.createModelForGraph(TDBFactory.createDataset(location).getDefaultModel().getGraph());

        return model;
    }

    @Override
    protected void doInput(File fileInput, Model model) {
        try {
            model.read(new FileInputStream(fileInput), null, "N-TRIPLES");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected long getSize(Model model) {
        return model.size();
    }

    @Override
    protected void close(Model model) {
        TDB.sync(model.getGraph());
    }

    public static void main(String[] args) {
        String path2File = args[0];
        File file = new File(path2File);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExperimentContext experimentContext = objectMapper.readValue(file, ExperimentContext.class);
            JENATDBInput jenatdbInput = new JENATDBInput(experimentContext);
            jenatdbInput.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
