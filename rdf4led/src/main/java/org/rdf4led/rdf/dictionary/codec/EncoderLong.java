package org.rdf4led.rdf.dictionary.codec;

/**
 * EncoderLong.java
 *
 * <p>Author : Le Tuan Anh
 *
 * <p>Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>28 May 2015
 */
public final class EncoderLong implements Encoder<Long, Byte, Byte, Long> {
  /**
   * //=======================================================================================================================================//
   * // 1 2 3 4 5 6 | 7 8 9 10 11 12 | 13 14 15 16 17 | 18 19 20 21 22 23 24 25 26 27 28 29 ... 64
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
  public static final EncoderLong encodeJ2SELong = new EncoderLong();

  private EncoderLong() {}

  @Override
  public Long encode(Byte nodeType, Byte prefix, Long suffix) {
    long node = nodeType;

    // If input is a vocabulary
    // prefix is the vocabulary's prefix
    // suffix is the vocabulary's suffix
    if (nodeType == RDFNodeType.VOCABULARY) {
      node = BitsLong.pack(node, prefix, 13, 18);

      node = BitsLong.pack(node, suffix, 18, BitsLong.IntLen);

      return node;
    }

    // If input is a literal with langTag
    //   prefix is the langTag
    //   suffix is the lexicalId
    if (nodeType == RDFNodeType.LITERALHASLANG) {
      node = BitsLong.pack(node, prefix, 7, 13);

      node = BitsLong.pack(node, suffix, 13, BitsLong.IntLen);

      return node;
    }

    if (prefix != RDFNodeType.NULL) {
      throw new IllegalArgumentException("Can not regconise prefix: " + prefix);
    }

    // If input is a plain literal, an uri, or a blank node
    //   prefix is null
    //   suffix is the lexicalId
    node = BitsLong.pack(node, suffix, 7, BitsLong.IntLen);

    return node;
  }

  @Override
  public Byte getPrefix(Long node) {
    byte nodeType = getNodeType(node);

    if (nodeType == RDFNodeType.VOCABULARY) {
      byte prefix = (byte) BitsLong.unpack(node, 13, 18);

      return prefix;
    }

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      byte langTag = (byte) BitsLong.unpack(node, 7, 13);

      return langTag;
    }

    return RDFNodeType.NULL;
  }

  @Override
  public Long getSuffix(Long node) {
    byte nodeType = getNodeType(node);

    if (nodeType == RDFNodeType.VOCABULARY) {
      long suffix = BitsLong.unpack(node, 18, 32);

      return suffix;
    }

    if (nodeType == RDFNodeType.LITERALHASLANG) {
      long lexId = BitsLong.unpack(node, 18, 32);

      return lexId;
    }

    long lexId = BitsLong.unpack(node, 7, 32);

    return lexId;
  }

  @Override
  public Byte getNodeType(Long node) {
    byte nodeType = (byte) BitsLong.unpack(node, 0, 7);

    return nodeType;
  }
}
