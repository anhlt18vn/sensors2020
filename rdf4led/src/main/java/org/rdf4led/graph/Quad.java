package org.rdf4led.graph;

/**
 * org.rdf4led.graph
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 02/05/17.
 */
public class Quad<Node> extends Triple<Node> {
  protected Node graphNode;

  public Quad(Node graphNode, Node s, Node p, Node o) {
    super(s, p, o);

    this.graphNode = graphNode;
  }

  public Node getGraphNode() {
    return graphNode;
  }
}
