package org.rdf4led.graph.parser;

import org.rdf4led.graph.Graph;

import java.io.File;
import java.io.InputStream;

/**
 * org.rdf4led.org.rdf4led.sparql.parser
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 30/04/17.
 */
public interface RDFParser<Node> {
  public Graph<Node> parseToGraphFromString(String data, String graphName);

  public Graph<Node> parseToGraphFromFileName(String fileName, String graphName);

  public Graph<Node> parseToGraphFromFile(File file, String graphName);

  public Graph<Node> parseToGraphFromInputStream(InputStream data, String graphName);
}
