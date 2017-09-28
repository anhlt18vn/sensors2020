package org.rdf4led.rdf.dictionary;


import org.rdf4led.rdf.dictionary.codec.Encoder;
import org.rdf4led.rdf.dictionary.codec.HashNodeImpl;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;
import org.rdf4led.rdf.dictionary.vocab.LangTag;
import org.rdf4led.rdf.dictionary.vocab.RDF;
import org.rdf4led.rdf.dictionary.vocab.VocabHandler;
import org.rdf4led.rdf.dictionary.vocab.XSD;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * RDFDictionaryJ2SE.java
 *
 * <p>* == Hash Table ======= ===== Lexicons Table === | hash || EncodedNode | | strID || Lexical
 * form | | . || . | | . || . | | . || . | | . || . | | . || . | | . || . | | hash || EncodedNode |
 * | strID || Lexical form |
 *
 * <p>Author : Le Tuan Anh
 *
 * <p>Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public abstract class RDFDictionaryAbstract<TNode>
    implements RDFDictionary<TNode, String, Byte, Byte> {
  protected HashNodeIdTable<TNode> hashTable;

  protected NodeIdLexTable<String, TNode> lexTable;

  protected Encoder<TNode, Byte, Byte, TNode> encoder;

  protected abstract TNode doEncode(Byte nodeType, Byte prefix, TNode suffix);

  protected abstract TNode from_byte_to_TNode(byte suffix);

  protected abstract byte from_TNode_to_byte(TNode suffix);

  protected HashNodeImpl<TNode> hashNode;

  protected RDFDictionaryAbstract(
      HashNodeImpl<TNode> hashNode,
      HashNodeIdTable<TNode> hashTable,
      NodeIdLexTable<String, TNode> lexTable,
      Encoder<TNode, Byte, Byte, TNode> encoder) {
    this.hashNode = hashNode;
    this.hashTable = hashTable;
    this.lexTable = lexTable;
    this.encoder = encoder;
  }

  @Override
  public TNode createBNode() {
    throw new RuntimeException("Need implement");
  }

  @Override
  public TNode createBNode(String bNodeLabel) {
    return encodeBNode(bNodeLabel);
  }

  @Override
  public TNode createBNode(String currentGraph, String bNodeLabel) {
    TNode hash = hashNode.getHash(currentGraph, (byte) 0, (byte) 0);

    return createBNode(bNodeLabel + hash);
  }

  @Override
  public TNode createURI(String uri) {
    return encodeURI(uri);
  }

  @Override
  public TNode createTypedLiteral(String lex, Byte nodeType) {
    return encodeLiteral(lex, RDFNodeType.NULL, nodeType);
  }

  @Override
  public TNode createTypedLiteral2(String lexical, String nodeType) {
    if (nodeType != null) {
      if (!(nodeType.contains(XSD.xsd.prefix))) {
        throw new IllegalArgumentException("Unknown nodeType !!! " + nodeType);
      }
      byte nodeTypeB = XSD.xsd.getSuffix(nodeType);

      return createTypedLiteral(lexical, nodeTypeB);
    }

    throw new IllegalArgumentException("createTypedLiteral nodeType can not be null " + nodeType);
  }

  @Override
  public TNode createLangLiteral(String lexical, Byte langTag) {
    if ((langTag == null) || (langTag == RDFNodeType.NULL)) {
      return createPlainLiteral(lexical);
    } else {
      return encodeLiteral(lexical, langTag, RDFNodeType.LITERALHASLANG);
    }
  }

  @Override
  public TNode createPlainLiteral(String lexical) {
    return encodeLiteral(lexical, RDFNodeType.NULL, RDFNodeType.LITERAL);
  }

  @Override
  public TNode createIntegerNode(BigInteger integer) {
    return createTypedLiteral(integer.toString(), XSD.XSDint);
  }

  @Override
  public TNode createFloatNode(float _float) {
    return createTypedLiteral(Float.toString(_float), XSD.XSDfloat);
  }

  @Override
  public TNode createDoubleNode(double _double) {
    return createTypedLiteral(Double.toString(_double), XSD.XSDdouble);
  }

  @Override
  public TNode createDecimalNode(BigDecimal decimal) {
    return createTypedLiteral(decimal.toString(), XSD.XSDdecimal);
  }

  @Override
  public TNode createBooleanNode(Boolean bool) {
    return bool ? getXSDTrue() : getXSDFalse();
  }

  @Override
  public TNode createStringNode(String string) {
    return createTypedLiteral(string, XSD.XSDstring);
  }

  @Override
  public TNode createDateNode(String date) {
    return createTypedLiteral(date, XSD.XSDdate);
  }

  TNode xsd_true = null;
  TNode xsd_false = null;

  @Override
  public TNode getXSDTrue() {
    if (xsd_true == null) {
      xsd_true = createTypedLiteral("true", XSD.xsd.XSDboolean);
    }

    return xsd_true;
  }

  @Override
  public TNode getXSDFalse() {
    if (xsd_false == null) {
      xsd_false = createTypedLiteral("false", XSD.xsd.XSDboolean);
    }

    return xsd_false;
  }

  @Override
  public TNode getRDFtype() {
    return encodeVocab(RDF.c_prefix, RDF.type);
  }

  @Override
  public TNode getRDFnil() {
    return encodeVocab(RDF.c_prefix, RDF.nil);
  }

  @Override
  public TNode getRDFfirst() {
    return encodeVocab(RDF.c_prefix, RDF.first);
  }

  @Override
  public TNode getRDFrest() {
    return encodeVocab(RDF.c_prefix, RDF.rest);
  }

  @Override
  public TNode getRDFsubject() {
    return encodeVocab(RDF.c_prefix, RDF.subject);
  }

  @Override
  public TNode getRDFpredicate() {
    return encodeVocab(RDF.c_prefix, RDF.predicate);
  }

  @Override
  public TNode getRDFobject() {
    return encodeVocab(RDF.c_prefix, RDF.object);
  }

  @Override
  public boolean isBooleanNode(TNode tNode) {
    byte nodeType = getNodeType(tNode);

    return (nodeType == XSD.XSDboolean);
  }

  @Override
  public synchronized boolean isURI(TNode node) {
    byte nodeType = encoder.getNodeType(node);

    return (nodeType == RDFNodeType.URI) || (nodeType == RDFNodeType.VOCABULARY);
  }

  @Override
  public synchronized boolean isLiteral(TNode node) {
    byte nodeType = encoder.getNodeType(node);

    return nodeType <= RDFNodeType.LITERALHASLANG;
  }

  @Override
  public synchronized boolean isBNode(TNode node) {
    byte nodeType = encoder.getNodeType(node);

    return nodeType == RDFNodeType.BLANK;
  }

  @Override
  public boolean isNumber(TNode tNode) {
    return false;
  }

  @Override
  public boolean isPlainLiteral(TNode tNode) {
    byte nodeType = getNodeType(tNode);

    return (nodeType == RDFNodeType.LITERAL);
  }

  @Override
  public boolean isStringNode(TNode tNode) {
    byte nodeType = getNodeType(tNode);

    return (nodeType == XSD.XSDstring);
  }

  @Override
  public String getURI(TNode tNode) {
    return getLexicalValue(tNode);
  }

  @Override
  public synchronized String getLexicalValue(TNode node) {
    if (node == null) {
      return "NODE_NULL";
    }

    byte nodetype = encoder.getNodeType(node);

    byte prefix = encoder.getPrefix(node);

    TNode suffix = encoder.getSuffix(node);

    if (nodetype == RDFNodeType.VOCABULARY) {
      return VocabHandler.vocabHandler.getLexical(prefix, from_TNode_to_byte(suffix));
    }

    if (nodetype < RDFNodeType.LITERAL) {
      String lex = lexTable.get(suffix);

      return lex;
    }

    if (nodetype == RDFNodeType.LITERALHASLANG) {
      String lex = lexTable.get(suffix);

      return lex;
    }

    if (nodetype == RDFNodeType.BLANK) {
      return "_:" + node;
    }

    if (nodetype == RDFNodeType.VARIABLE) {
      return "var with id " + encoder.getSuffix(node);
    }

    TNode lex_id = encoder.getSuffix(node);

    String lex = lexTable.get(lex_id);

    return lex;
  }

  @Override
  public synchronized String getLexicalForm(TNode node) {
    if (node == null) {
      return "NODE_NULL";
    }

    byte nodeType = encoder.getNodeType(node);

    byte prefix = encoder.getPrefix(node);

    TNode suffix = encoder.getSuffix(node);

    if (nodeType == RDFNodeType.VOCABULARY) {
      return "<" + VocabHandler.vocabHandler.getLexical(prefix, from_TNode_to_byte(suffix)) + ">";
    }

    if (nodeType < RDFNodeType.LITERAL) {
      String lex = lexTable.get(suffix);

      String xsd = VocabHandler.vocabHandler.getLexical(XSD.c_prefix, nodeType);

      return "\"" + lex + "\"" + "^^" + "<" + xsd + ">";
    }

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      String lex = lexTable.get(suffix);

      String tag = LangTag.langtag.toLang(prefix);

      return "\"" + lex + "\"" + "@" + tag;
    }

    if (nodeType == RDFNodeType.BLANK) {
      return "_:b" + node;
    }

    if (nodeType == RDFNodeType.VARIABLE) {
      return "?id_" + encoder.getSuffix(node);
    }

    TNode lex_id = encoder.getSuffix(node);

    String lex = lexTable.get(lex_id);

    if (nodeType == RDFNodeType.URI) {
      lex = "<" + lex + ">";
    }

    return lex;
  }

  @Override
  public String toLangTag(Byte langtag) {
    return LangTag.langtag.toLang(langtag);
  }

  @Override
  public synchronized Byte getLangTag(TNode node) {
    byte nodeType = encoder.getNodeType(node);

    if (nodeType != RDFNodeType.LITERALHASLANG) {
      throw new IllegalArgumentException(nodeType + " is not LiteralHashLang");
    }

    return encoder.getPrefix(node);
  }

  @Override
  public synchronized Byte getXSDType(String XSD_URI) {
    return XSD.xsd.getSuffix(XSD_URI);
  }

  @Override
  public synchronized Byte getDataType(TNode node) {
    byte nodeType = encoder.getNodeType(node);

    if (nodeType < 43) {
      throw new IllegalArgumentException(nodeType + " is not Typed Literal");
    }

    return nodeType;
  }

  @Override
  public synchronized Byte getNodeType(TNode node) {
    return encoder.getNodeType(node);
  }

  @Override
  public synchronized TNode encodeVar(TNode varIndex) {
    return encoder.encode(RDFNodeType.VARIABLE, RDFNodeType.NULL, varIndex);
  }

  @Override
  public synchronized TNode decodeVar(TNode varNode) {
    if (getNodeType(varNode) != RDFNodeType.VARIABLE) {
      throw new IllegalArgumentException(varNode + " is not a var node");
    }

    return encoder.getSuffix(varNode);
  }

  // =================================================================================================================//
  private synchronized TNode encodeVocab(Byte prefix, Byte suffix) {
    return doEncode(RDFNodeType.VOCABULARY, prefix, from_byte_to_TNode(suffix));
  }

  private synchronized TNode encodeURI(String uri) {
    byte prefix = VocabHandler.vocabHandler.getPrefix(uri);

    if (prefix != RDFNodeType.NULL) {
      TNode suffix = from_byte_to_TNode(VocabHandler.vocabHandler.getSuffix(uri));

      return doEncode(RDFNodeType.VOCABULARY, prefix, suffix);
    }

    TNode hash = hashNode.getHash(uri, (byte) 0, RDFNodeType.URI);

    TNode node = hashTable.get(hash);

    if (node != null) return node;

    TNode lexId = lexTable.put(uri);

    node = doEncode(RDFNodeType.URI, RDFNodeType.NULL, lexId);

    hashTable.put(hash, node);

    return node;
  }

  private synchronized TNode encodeLiteral(String lexical, Byte langTag, Byte nodeType) {
    TNode node;

    TNode hash = hashNode.getHash(lexical, langTag, nodeType);

    node = hashTable.get(hash);

    if (node != null) return node;

    TNode lexId = lexTable.put(lexical);

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      node = doEncode(nodeType, langTag, lexId);

      hashTable.put(hash, node);

      return node;
    }

    node = doEncode(nodeType, RDFNodeType.NULL, lexId);

    hashTable.put(hash, node);

    return node;
  }

  private synchronized TNode encodeBNode(String BNodeLabel) {
    TNode node;

    TNode hash = hashNode.getHash(BNodeLabel, (byte) 0, RDFNodeType.BLANK);

    node = hashTable.get(hash);

    if (node != null) return node;

    TNode lexId = lexTable.put(":_");

    node = doEncode(RDFNodeType.BLANK, RDFNodeType.NULL, lexId);

    hashTable.put(hash, node);

    return node;
  }
}
