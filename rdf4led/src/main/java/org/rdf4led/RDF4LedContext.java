package org.rdf4led;

import org.rdf4led.graph.Graph;
import org.rdf4led.query.QueryContextInt;
import org.rdf4led.rdf.dictionary.*;

import java.util.HashMap;

/**
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 */
public class RDF4LedContext extends QueryContextInt implements Context<Integer>{
  private String location;

  HashMap<Graph<Integer>, String> storage_name_map;
  HashMap<String, Graph<Integer>> name_storage_map;

  public RDF4LedContext(String location) {
    super(null);

    HashNodeIdTable<Integer> hashNodeIdTable = new HashNodeIdTableInt(location);

    NodeIdLexTable<String, Integer> nodeIdLexTable = new NodeIdLexTableInt(location);

    super.dictionary = new RDFDictionaryInt(hashNodeIdTable, nodeIdLexTable);

    this.location = location;

    storage_name_map = new HashMap<>();

    name_storage_map = new HashMap<>();
  }

  @Override
  public Graph<Integer> getGraphByName(String nameGraph) {

    Graph<Integer> graph = null;

    graph = name_storage_map.get(nameGraph);

    if (graph != null) return graph;

    Integer nodeGraph = dictionary.createURI(nameGraph);

    System.out.println(nameGraph + " " + nodeGraph);

    graph = new GraphStore(location + "/" + nodeGraph + "/");

    storage_name_map.put(graph, nameGraph);

    name_storage_map.put(nameGraph, graph);

    return graph;
  }

  @Override
  public RDFDictionary<Integer, String, Byte, Byte> getDictionary() {
    return dictionary;
  }

  @Override
  public String getNameOfGraph(Graph<Integer> graph) {
    return storage_name_map.get(graph);
  }

  @Override
  public void sync() {
    ((RDFDictionaryInt) dictionary).sync();
  }
}
