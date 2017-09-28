package org.rdf4led.rdf.dictionary;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * RDFDictionary.java
 *
 * <p>TNode - The data type of encoded node (integer, long, or array of bytes)
 *
 * <p>TLexical - The data type of input lexical (String UTF-8, or other types )
 *
 * <p>TLangTag - The data type of input lexical (String UTF-8, or other types )
 *
 * <p>TNodeType - NodeType of a RDF Node is described in RDFNodeType.java
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public interface RDFDictionary<TNode, TLexical, TLangTag, TNodeType> {
  public TNode createBNode();

  public TNode createBNode(TLexical bNodeLabel);

  public TNode createBNode(TLexical currentGraph, TLexical bNodeLabel);

  public TNode createURI(TLexical uri);

  public TNode createTypedLiteral(TLexical lexical, TNodeType nodeType);

  public TNode createTypedLiteral2(TLexical lexical, TLexical nodeType);

  public TNode createLangLiteral(TLexical lexical, TLangTag langTag);

  public TNode createPlainLiteral(TLexical lexical);

  public TNode createIntegerNode(BigInteger integer);

  public TNode createFloatNode(float _float);

  public TNode createDoubleNode(double _double);

  public TNode createDecimalNode(BigDecimal decimal);

  public TNode createBooleanNode(Boolean bool);

  public TNode createStringNode(String string);

  public TNode createDateNode(String date);

  public TNode getXSDTrue();

  public TNode getXSDFalse();

  public TNode getRDFtype();

  public TNode getRDFnil();

  public TNode getRDFfirst();

  public TNode getRDFrest();

  public TNode getRDFsubject();

  public TNode getRDFpredicate();

  public TNode getRDFobject();

  public boolean isBooleanNode(TNode node);

  public boolean isURI(TNode node);

  public boolean isLiteral(TNode node);

  public boolean isBNode(TNode node);

  public boolean isNumber(TNode node);

  public boolean isPlainLiteral(TNode node);

  public boolean isStringNode(TNode node);

  public TLexical getURI(TNode node);

  public TLexical getLexicalValue(TNode node);

  public TLexical getLexicalForm(TNode node);

  public TLexical toLangTag(TLangTag langTag);

  public TLangTag getLangTag(TNode node);

  public TNodeType getDataType(TNode node);

  public TNodeType getXSDType(TLexical XSD_URI);

  public TNodeType getNodeType(TNode node);

  // varaible Node for Query
  public TNode getNodeAny();

  public TNode encodeVar(TNode varNode);

  public TNode decodeVar(TNode varNode);
}
