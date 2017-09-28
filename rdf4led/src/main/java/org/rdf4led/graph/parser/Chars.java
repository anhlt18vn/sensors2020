/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rdf4led.graph.parser;

import java.nio.charset.Charset;

/**
 * TODO: Chars
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 6 Jan 2015
 */
public class Chars {

  public final char[] digits10 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  /** Hex digits : upper case * */
  public final char[] hexDigitsUC = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  /** Hex digits : lower case * */
  public static final char[] hexDigitsLC = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  // 'g' , 'h' ,
  // 'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
  // 'o' , 'p' , 'q' , 'r' , 's' , 't' ,
  // 'u' , 'v' , 'w' , 'x' , 'y' , 'z'

  /** Java name for UTF-8 encoding */
  private final String encodingUTF8 = "utf-8";

  /** Java name for ASCII encoding */
  private final String encodingASCII = "ascii";

  public final Charset charsetUTF8 = Charset.forName(encodingUTF8);
  public final Charset charsetASCII = Charset.forName(encodingASCII);

  public void encodeAsHex(StringBuilder buff, char marker, char ch) {
    if (ch < 256) {
      buff.append(marker);
      int lo = ch & 0xF;
      int hi = (ch >> 4) & 0xF;
      buff.append(hexDigitsUC[hi]);
      buff.append(hexDigitsUC[lo]);
      return;
    }
    int n4 = ch & 0xF;
    int n3 = (ch >> 4) & 0xF;
    int n2 = (ch >> 8) & 0xF;
    int n1 = (ch >> 12) & 0xF;
    buff.append(marker);
    buff.append(hexDigitsUC[n1]);
    buff.append(hexDigitsUC[n2]);
    buff.append(marker);
    buff.append(hexDigitsUC[n3]);
    buff.append(hexDigitsUC[n4]);
  }

  /** End of file - not a Unicode codepoint */
  public static final int EOF = -1;
  // BOM : U+FEFF encoded in bytes as xEF,0xBB,0xBF
  public static final char BOM = 0xFEFF;

  /** undefined character (exact meaning depends on use) - not a Unicode codepoint */
  public final int UNSET = -2;

  public static final char NL = '\n';
  public static final char CR = '\r';

  public static final char CH_ZERO = (char) 0;

  public static final char CH_LBRACKET = '[';
  public static final char CH_RBRACKET = ']';

  public static final char CH_LBRACE = '{';
  public static final char CH_RBRACE = '}';

  public static final char CH_LPAREN = '(';
  public static final char CH_RPAREN = ')';

  public static final char CH_LT = '<';
  public static final char CH_GT = '>';
  public static final char CH_UNDERSCORE = '_';

  public static final char CH_QUOTE1 = '\'';
  public static final char CH_QUOTE2 = '"';

  public static final char CH_EQUALS = '=';
  public static final char CH_STAR = '*';
  public static final char CH_DOT = '.';
  public static final char CH_COMMA = ',';
  public static final char CH_SEMICOLON = ';';
  public static final char CH_COLON = ':';
  public static final char CH_AMPHERSAND = '&';
  public static final char CH_AT = '@';
  public static final char CH_QMARK = '?';
  public static final char CH_HASH = '#';
  public static final char CH_PLUS = '+';
  public static final char CH_MINUS = '-';
  public static final char CH_SLASH = '/';
  public static final char CH_RSLASH = '\\';

  public final byte B_NL = NL;
  public final byte B_CR = CR;

  public final byte B_LBRACKET = '[';
  public final byte B_RBRACKET = ']';
  public final byte B_LBRACE = '{';
  public final byte B_RBRACE = '}';

  public final byte B_LPAREN = '(';
  public final byte B_RPAREN = ')';

  public final byte B_LT = '<';
  public final byte B_GT = '>';
  public final byte B_UNDERSCORE = '_';

  public final byte B_QUOTE1 = '\'';
  public final byte B_QUOTE2 = '"';

  public final byte B_EQUALS = '=';
  public final byte B_STAR = '*';
  public final byte B_DOT = '.';
  public final byte B_COMMA = ',';
  public final byte B_SEMICOLON = ';';
  public final byte B_COLON = ':';
  public final byte B_AT = '@';
  public final byte B_AMPHERSAND = '&';
  public final byte B_QMARK = '?';
  public final byte B_HASH = '#';
  public final byte B_PLUS = '+';
  public final byte B_MINUS = '-';
  public final byte B_SLASH = '/';
  public final byte B_RSLASH = '\\';

  public boolean isAlpha(int codepoint) {
    return Character.isLetter(codepoint);
  }

  public boolean isAlphaNumeric(int codepoint) {
    return Character.isLetterOrDigit(codepoint);
  }

  /** ASCII A-Z */
  public boolean isA2Z(int ch) {
    return range(ch, 'a', 'z') || range(ch, 'A', 'Z');
  }

  /** ASCII A-Z or 0-9 */
  public boolean isA2ZN(int ch) {
    return range(ch, 'a', 'z') || range(ch, 'A', 'Z') || range(ch, '0', '9');
  }

  /** ASCII 0-9 */
  public boolean isDigit(int ch) {
    return range(ch, '0', '9');
  }

  public boolean isWhitespace(int ch) {
    // ch = ch | 0xFF ;
    return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '\f';
  }

  public boolean isNewlineChar(int ch) {
    return ch == '\r' || ch == '\n';
  }

  /*
   * The token rules from SPARQL and Turtle. PNAME_NS ::= PN_PREFIX? ':'
   * PNAME_LN ::= PNAME_NS PN_LOCAL[131] BLANK_NODE_LABEL ::= '_:' PN_LOCAL
   * PN_CHARS_BASE ::= [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] |
   * [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] |
   * [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] |
   * [#xFDF0-#xFFFD] | [#x10000-#xEFFFF] PN_CHARS_U ::= PN_CHARS_BASE | '_'
   * VARNAME ::= ( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 |
   * [#x0300-#x036F] | [#x203F-#x2040] )* PN_CHARS ::= PN_CHARS_U | '-' |
   * [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] PN_PREFIX ::=
   * PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)? PN_LOCAL ::= ( PN_CHARS_U |
   * [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?
   */

  public boolean isPNCharsBase(int ch) {
    // PN_CHARS_BASE ::= [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] |
    // [#x00F8-#x02FF] |
    // [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F]
    // |
    // [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD]
    // |
    // [#x10000-#xEFFFF]
    return r(ch, 'a', 'z')
        || r(ch, 'A', 'Z')
        || r(ch, 0x00C0, 0x00D6)
        || r(ch, 0x00D8, 0x00F6)
        || r(ch, 0x00F8, 0x02FF)
        || r(ch, 0x0370, 0x037D)
        || r(ch, 0x037F, 0x1FFF)
        || r(ch, 0x200C, 0x200D)
        || r(ch, 0x2070, 0x218F)
        || r(ch, 0x2C00, 0x2FEF)
        || r(ch, 0x3001, 0xD7FF)
        || r(ch, 0xF900, 0xFDCF)
        || r(ch, 0xFDF0, 0xFFFD)
        || r(ch, 0x10000, 0xEFFFF); // Outside the basic plain.
  }

  public boolean isPNChars_U(int ch) {
    // PN_CHARS_BASE | '_'
    return isPNCharsBase(ch) || (ch == '_');
  }

  public boolean isPNChars_U_N(int ch) {
    // PN_CHARS_U | [0-9]
    return isPNCharsBase(ch) || (ch == '_') || isDigit(ch);
  }

  public boolean isPNChars(int ch) {
    // PN_CHARS ::= PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] |
    // [#x203F-#x2040]
    return isPNChars_U(ch)
        || isDigit(ch)
        || (ch == '-')
        || ch == 0x00B7
        || r(ch, 0x300, 0x036F)
        || r(ch, 0x203F, 0x2040);
  }

  public int valHexChar(int ch) {
    if (range(ch, '0', '9')) return ch - '0';
    if (range(ch, 'a', 'f')) return ch - 'a' + 10;
    if (range(ch, 'A', 'F')) return ch - 'A' + 10;
    return -1;
  }

  private boolean r(int ch, int a, int b) {
    return (ch >= a && ch <= b);
  }

  public boolean range(int ch, char a, char b) {
    return (ch >= a && ch <= b);
  }

  public boolean charInArray(int ch, char[] chars) {
    for (int xch : chars) {
      if (ch == xch) return true;
    }
    return false;
  }
}
