package org.rdf4led.rdf.dictionary.codec;

/**
 * Encoder.java
 *
 * <p>This encoder is the interface for encoding and decoding module. That convert an RDF node into
 * an encoded form or revert an RDF node from its encoded form.
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public interface Encoder<Node, NodeType, Prefix, Suffix> {
  public Node encode(NodeType NodeType, Prefix prefix, Suffix suffix);

  public Prefix getPrefix(Node node);

  public Suffix getSuffix(Node node);

  public NodeType getNodeType(Node node);
}
