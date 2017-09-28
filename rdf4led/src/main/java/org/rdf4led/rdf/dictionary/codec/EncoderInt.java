package org.rdf4led.rdf.dictionary.codec;

/**
 * EncoderJ2SEInt.java
 *
 * <p>Author : Le Tuan Anh
 *
 * <p>Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>28 May 2015
 */
public final class EncoderInt implements Encoder<Integer, Byte, Byte, Integer> {
  /**
   * //=======================================================================================================================================//
   * // 1 2 3 4 5 6 | 7 8 9 10 11 12 | 13 14 15 16 17 | 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32
   * //
   * //=======================================================================================================================================//
   * // Normal Node | // // <--------Node Type-------> | <-------------------------------Lexical Id
   * ------------------------------------------------------------->// // <--------Data Type------->
   * |
   * <------------------------------------------------------------------------------------------------------->//
   * // Node Constant | // // <---Lit has LangTag -----> | <-------LangTag-------> |
   * <-------------------------Lexical Id------------------------------------------>// // <------
   * Vocabulary ------> | <--------------------Prefix----------------> |
   * <---------------------Suffix----------------------------->//
   * //=======================================================================================================================================//
   */
  public static final EncoderInt encodeInt = new EncoderInt();

  private EncoderInt() {}

  @Override
  public Integer encode(Byte nodeType, Byte prefix, Integer suffix) {
    int node = nodeType;

    // If input is a vocabulary
    // prefix is the vocabulary's prefix
    // suffix is the vocabulary's suffix
    if (nodeType == RDFNodeType.VOCABULARY) {
      node = BitsInt.pack(node, prefix, 13, 18);

      node = BitsInt.pack(node, suffix, 18, 32);

      return node;
    }

    // If input is a literal with langTag
    //   prefix is the langTag
    //   suffix is the lexicalId
    if (nodeType == RDFNodeType.LITERALHASLANG) {
      node = BitsInt.pack(node, prefix, 7, 13);

      node = BitsInt.pack(node, suffix, 13, 32);

      return node;
    }

    if (prefix != RDFNodeType.NULL) {
      throw new IllegalArgumentException("Can not regconise prefix: " + prefix);
    }

    // If input is a plain literal, an uri, or a blank node
    //   prefix is null
    //   suffix is the lexicalId
    node = BitsInt.pack(node, suffix, 7, 32);

    return node;
  }

  @Override
  public Byte getPrefix(Integer node) {
    byte nodeType = getNodeType(node);

    if (nodeType == RDFNodeType.VOCABULARY) {
      byte prefix = (byte) BitsInt.unpack(node, 13, 18);

      return prefix;
    }

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      byte langTag = (byte) BitsInt.unpack(node, 7, 13);

      return langTag;
    }

    return RDFNodeType.NULL;
  }

  @Override
  public Integer getSuffix(Integer node) {
    byte nodeType = getNodeType(node);

    if (nodeType == RDFNodeType.VOCABULARY) {
      int suffix = BitsInt.unpack(node, 18, 32);

      return suffix;
    }

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      int lexId = BitsInt.unpack(node, 18, 32);

      return lexId;
    }

    int lexId = BitsInt.unpack(node, 7, 32);

    return lexId;
  }

  @Override
  public Byte getNodeType(Integer node) {
    byte nodeType = (byte) BitsInt.unpack(node, 0, 7);

    return nodeType;
  }
}
