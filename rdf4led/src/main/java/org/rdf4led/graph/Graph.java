package org.rdf4led.graph;

import java.io.Closeable;
import java.util.Iterator;

/**
 * org.rdf4led.graph
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 30/04/17.
 */
public interface Graph<Node> extends Closeable {
  public void add(Triple<Node> triple);

  public void delete(Triple<Node> triple);

  public Iterator<Triple<Node>> find(Triple<Node> triple);

  public Iterator<Triple<Node>> find(Node s, Node p, Node o);

  public boolean contains(Triple<Node> triple);

  public void sync();
}
