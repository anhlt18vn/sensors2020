package org.rdf4led.graph.parser;


import org.rdf4led.Context;
import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

/**
 * Created by anhlt185 on 30/04/17.
 */
public class ParserNTriple<Node> implements RDFParser<Node> {
    private Context<Node> context;

    public ParserNTriple(Context<Node> context) {
        this.context = context;
    }

    @Override
    public Graph<Node> parseToGraphFromString(String data, String graphName) {
        PeekReader reader = PeekReader.readString(data);

        return parse(reader, graphName);
    }

    @Override
    public Graph<Node> parseToGraphFromFileName(String fileName, String graphName) {
        PeekReader reader = PeekReader.open(fileName);

        return parse(reader, graphName);
    }

    @Override
    public Graph<Node> parseToGraphFromFile(File file, String graphName) {
        try {

            PeekReader reader = PeekReader.make(new FileReader(file));

            return parse(reader, graphName);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public Graph<Node> parseToGraphFromFile(File file, Graph<Node> graph) {
        try {

            PeekReader reader = PeekReader.make(new FileReader(file));

            return parse(reader, graph);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public Graph<Node> parseToGraphFromInputStream(InputStream data, String graphName) {
        PeekReader reader = PeekReader.makeUTF8(data);

        return parse(reader, graphName);
    }

    private Graph<Node> parse(PeekReader reader, String graphName) {
        Tokenizer tokenizer = new Tokenizer(reader);

        LangNTriples<Node> lang = new LangNTriples<>(tokenizer, context.getDictionary());

        return parseToGraph(lang, graphName);
    }

    private Graph<Node> parse(PeekReader reader, Graph<Node> graph) {
        Tokenizer tokenizer = new Tokenizer(reader);

        LangNTriples<Node> lang = new LangNTriples<>(tokenizer, context.getDictionary());

        return parseToGraph(lang, graph);
    }

    private Graph<Node> parseToGraph(LangNTriples<Node> lang, String graphName) {
        Graph<Node> graph = context.getGraphByName(graphName);

        while (lang.hasNext()) {
            Triple<Node> triple = lang.parseOne(graphName);
            graph.add(triple);
        }
        return graph;
    }

    private Graph<Node> parseToGraph(LangNTriples<Node> lang, Graph<Node> graph) {
        String graphName = context.getNameOfGraph(graph);

        while (lang.hasNext()) {
            Triple<Node> triple = lang.parseOne(graphName);

            graph.add(triple);
        }

        return graph;
    }
}
