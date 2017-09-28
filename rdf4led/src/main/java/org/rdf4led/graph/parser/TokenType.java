package org.rdf4led.graph.parser;

/**
 * TokenType.java
 *
 * <p>TODO
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public enum TokenType {
  NODE,
  IRI,
  PREFIXED_NAME,
  BNODE,

  STRING,
  STRING1,
  STRING2,
  LONG_STRING1,
  LONG_STRING2,

  LITERAL_LANG,
  LITERAL_DT,
  INTEGER,
  DECIMAL,
  DOUBLE,

  KEYWORD,
  VAR,
  HEX,
  CNTRL,

  DOT,
  COMMA,
  SEMICOLON,
  COLON,
  DIRECTIVE,

  LT,
  GT,
  LE,
  GE,
  LOGICAL_AND,
  LOGICAL_OR,
  VBAR,
  AMPHERSAND,

  UNDERSCORE,
  LBRACE,
  RBRACE,
  LPAREN,
  RPAREN,
  LBRACKET,
  RBRACKET,

  EQUALS,
  EQUIVALENT,
  PLUS,
  MINUS,
  STAR,
  SLASH,
  RSLASH,

  WS,
  COMMENT,
  COMMENT1,
  COMMENT2,
  EOF
}
