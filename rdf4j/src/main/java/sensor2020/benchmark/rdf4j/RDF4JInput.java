package sensor2020.benchmark.rdf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import org.rdf4led.benchmark.ExperimentContext;
import org.rdf4led.benchmark.ExperimentInput;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 */
public class RDF4JInput extends ExperimentInput<RepositoryConnection> {

    private Repository repository;

    protected RDF4JInput(ExperimentContext experimentContext) {
        super(experimentContext);
    }

    @Override
    protected RepositoryConnection initializeEngine() {
        File dataDir = new File(this.path2StoreFolder);
        String indexes = "spoc,posc,cosp";
        this.repository = new SailRepository(new NativeStore(dataDir, indexes));
        repository.isInitialized();
        return repository.getConnection();
    }

    @Override
    protected void doInput(File fileInput, RepositoryConnection connection) {
        try {
            if (!connection.isOpen()) connection.begin();

            connection.setAutoCommit(true);

            FileReader fileReader = new FileReader(fileInput);

            try {
                connection.add(fileReader, "", RDFFormat.NTRIPLES);
            } finally {
                fileReader.close();
            }

            connection.commit();

        } catch (RepositoryException | RDFParseException | IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    protected long getSize(RepositoryConnection connection) {
        try {
            if (!connection.isOpen()) connection.begin();

            return connection.size();

        } catch (RepositoryException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    protected void close(RepositoryConnection connection) {
        try {
            connection.commit();
            connection.close();
            repository.shutDown();
        } catch (RepositoryException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static void main(String[] args) {
        String path2File = args[0];
        File file = new File(path2File);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExperimentContext experimentContext = objectMapper.readValue(file, ExperimentContext.class);
            RDF4JInput rdf4JInput = new RDF4JInput(experimentContext);
            rdf4JInput.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
