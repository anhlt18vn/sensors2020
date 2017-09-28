package org.rdf4led;

import org.rdf4led.graph.Graph;
import org.rdf4led.rdf.dictionary.RDFDictionary;

/**
 * org.rdf4led
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 30/04/17.
 */
public interface Context<Node>  {

  public Graph<Node> getGraphByName(String nameGraph);

  public RDFDictionary<Node, String, Byte, Byte> getDictionary();

  public String getNameOfGraph(Graph<Node> graph);

  public void sync();
}
