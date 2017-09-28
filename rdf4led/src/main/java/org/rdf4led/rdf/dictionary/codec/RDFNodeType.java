package org.rdf4led.rdf.dictionary.codec;

/**
 * RDFNodeType.java
 *
 * <p>TODO
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public interface RDFNodeType {

  public static final byte NULL = 0;

  public static final byte LITERAL = 43;

  public static final byte LITERALHASLANG = 44;

  public static final byte URI = 45;

  public static final byte BLANK = 46;

  public static final byte VARIABLE = 47;

  public static final byte DTT_CUSTOM = 48;

  public static final int ANY = -9;

  public static final byte FUNCTOR = 49;

  public static final byte RULE_VARAIBLE = 51;

  public static final byte VOCABULARY = 60;
}
