package org.rdf4led.rdf.dictionary.codec;

/**
 * HashNode.java
 *
 * <p>This is the interface of Hash function for RDF Node. The implementation of NodeHashing can be
 * implemented by different hash algorithm (mummurHash example).
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public interface HashNode<TNode, TLexical, TNodeType, TLangTag> {
  public TNode getHash(TLexical lexical, TLangTag langTag, TNodeType nodeType);
}
