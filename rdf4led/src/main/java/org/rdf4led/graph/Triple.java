package org.rdf4led.graph;

/**
 * org.rdf4led
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 30/04/17.
 */
public class Triple<Node> {
  protected Node s, p, o;

  public Triple(Node s, Node p, Node o) {
    this.s = s;

    this.p = p;

    this.o = o;
  }

  public Node getSubject() {
    return s;
  }

  public Node getPredicate() {
    return p;
  }

  public Node getObject() {
    return o;
  }
}
