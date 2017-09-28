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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** Tokenizer for all sorts of things RDF-ish */
public final class Tokenizer implements Iterator<Token>, Closeable {
  public static final int CTRL_CHAR = Chars.CH_STAR;

  private Token token = null;

  private final StringBuilder stringBuilder = new StringBuilder(200);

  private final PeekReader reader;

  private boolean finished = false;

  public Tokenizer(PeekReader reader) {
    this.reader = reader;
  }

  @Override
  public final boolean hasNext() {
    if (finished) return false;

    if (token != null) return true;

    skip();

    if (reader.eof()) {
      finished = true;
      return false;
    }

    token = parseToken();

    if (token == null) {
      finished = true;
      return false;
    }

    return true;
  }

  public final boolean eof() {
    return hasNext();
  }

  @Override
  public final Token next() {
    if (!hasNext()) throw new NoSuchElementException();

    Token t = token;

    token = null;

    return t;
  }

  public final Token peek() {
    if (!hasNext()) return null;

    return token;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    if (reader != null)
      try {
        reader.close();
      } catch (IOException e) {
        throw new RuntimeException(e.toString());
      }
  }

  // ---- Machinary

  private void skip() {
    int ch = Chars.EOF;

    for (; ; ) {
      if (reader.eof()) return;

      ch = reader.peekChar();

      if (ch == Chars.CH_HASH) {
        reader.readChar();

        for (; ; ) {
          ch = reader.peekChar();

          if (ch == Chars.EOF || isNewlineChar(ch)) break;

          reader.readChar();
        }
      }

      // Including excess newline org.rdf4led.rdf.org.rdf4led.sparql.parser.lang.chars from comment.
      if (!isWhitespace(ch)) break;

      reader.readChar();
    }
  }

  private Token parseToken() {
    token = new Token();

    int ch = reader.peekChar();

    // ---- IRI
    if (ch == Chars.CH_LT) {
      reader.readChar();

      token.setImage(readIRI());

      token.setType(TokenType.IRI);

      return token;
    }

    // ---- Literal
    if (ch == Chars.CH_QUOTE1 || ch == Chars.CH_QUOTE2) {
      reader.readChar();

      int ch2 = reader.peekChar();

      if (ch2 == ch) {
        reader.readChar(); // Read potential second quote.
        int ch3 = reader.peekChar();

        if (ch3 == ch) {
          reader.readChar();

          token.setImage(readLongString(ch, false));

          TokenType tt = (ch == Chars.CH_QUOTE1) ? TokenType.LONG_STRING1 : TokenType.LONG_STRING2;

          token.setType(tt);
        } else {
          token.setImage("");

          token.setType((ch == Chars.CH_QUOTE1) ? TokenType.STRING1 : TokenType.STRING2);
        }
      } else {
        token.setImage(readString(ch, ch, true));

        token.setType((ch == Chars.CH_QUOTE1) ? TokenType.STRING1 : TokenType.STRING2);
      }

      if (reader.peekChar() == Chars.CH_AT) {
        reader.readChar();

        token.setImage2(langTag());

        token.setType(TokenType.LITERAL_LANG);
      } else if (reader.peekChar() == '^') {
        expect("^^");

        int nextCh = reader.peekChar();

        if (isWhitespace(nextCh)) exception("No whitespace after ^^ in literal with datatype");

        Token mainToken = token;

        Token subToken = parseToken();

        if (!subToken.isIRI())
          exception("Datatype URI required after ^^ - URI or prefixed name expected");

        token = mainToken;

        token.setSubToken(subToken);

        token.setType(TokenType.LITERAL_DT);

      } else {
        // Was a simple string.
      }

      return token;
    }

    if (ch == Chars.CH_UNDERSCORE) // Blank node :label must be at least one char
    {
      expect("_:");

      token.setImage(readBlankNodeLabel());

      token.setType(TokenType.BNODE);

      return token;
    }

    // TODO remove and make a symbol/keyword.
    // Control
    if (ch == CTRL_CHAR) {
      reader.readChar();
      token.setType(TokenType.CNTRL);
      ch = reader.readChar();
      if (ch == Chars.EOF) exception("EOF found after " + CTRL_CHAR);

      if (isWhitespace(ch)) token.cntrlCode = -1;
      else token.cntrlCode = (char) ch;

      return token;
    }

    // A directive (not part of a literal as org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag)
    if (ch == Chars.CH_AT) {
      reader.readChar();

      token.setType(TokenType.DIRECTIVE);

      token.setImage(readWord(false));

      return token;
    }

    // Variable
    if (ch == Chars.CH_QMARK) {
      reader.readChar();

      token.setType(TokenType.VAR);

      // Character set?
      token.setImage(readVarName());

      return token;
    }

    // Symbol?
    switch (ch) {
        // DOT can start a decimal.  Check for digit.
      case Chars.CH_DOT:
        reader.readChar();

        ch = reader.peekChar();

        if (range(ch, '0', '9')) {
          // Not a DOT after all.
          reader.pushbackChar(Chars.CH_DOT);

          readNumber();

          return token;
        }

        token.setType(TokenType.DOT);

        return token;

      case Chars.CH_SEMICOLON:
        reader.readChar();
        token.setType(TokenType.SEMICOLON); /*token.setImage(CH_SEMICOLON) ;*/
        return token;
      case Chars.CH_COMMA:
        reader.readChar();
        token.setType(TokenType.COMMA); /*token.setImage(CH_COMMA) ;*/
        return token;
      case Chars.CH_LBRACE:
        reader.readChar();
        token.setType(TokenType.LBRACE); /*token.setImage(CH_LBRACE) ;*/
        return token;
      case Chars.CH_RBRACE:
        reader.readChar();
        token.setType(TokenType.RBRACE); /*token.setImage(CH_RBRACE) ;*/
        return token;
      case Chars.CH_LPAREN:
        reader.readChar();
        token.setType(TokenType.LPAREN); /*token.setImage(CH_LPAREN) ;*/
        return token;
      case Chars.CH_RPAREN:
        reader.readChar();
        token.setType(TokenType.RPAREN); /*token.setImage(CH_RPAREN) ;*/
        return token;
      case Chars.CH_LBRACKET:
        reader.readChar();
        token.setType(TokenType.LBRACKET); /*token.setImage(CH_LBRACKET) ;*/
        return token;
      case Chars.CH_RBRACKET:
        reader.readChar();
        token.setType(TokenType.RBRACKET); /*token.setImage(CH_RBRACKET) ;*/
        return token;
      case Chars.CH_EQUALS:
        reader.readChar();
        token.setType(TokenType.EQUALS); /*token.setImage(CH_EQUALS) ;*/
        return token;

        // Specials (if blank node processing off)
        // case CH_COLON:      reader.readChar() ; token.setType(TokenType.COLON) ; return token ;
      case Chars.CH_UNDERSCORE:
        reader.readChar();
        token.setType(TokenType.UNDERSCORE); /*token.setImage(CH_UNDERSCORE) ;*/
        return token;
      case Chars.CH_LT:
        reader.readChar();
        token.setType(TokenType.LT); /*token.setImage(CH_LT) ;*/
        return token;
      case Chars.CH_GT:
        reader.readChar();
        token.setType(TokenType.GT); /*token.setImage(CH_GT) ;*/
        return token;
      case Chars.CH_STAR:
        reader.readChar();
        token.setType(TokenType.STAR); /*token.setImage(CH_STAR) ;*/
        return token;

        // TODO : Multi character symbols
        // Two character tokens && || GE >= , LE <=
        // Single character symbols for * /
        // +/- may start numbers.

        //            case CH_PLUS:
        //            case CH_MINUS:
        //            case CH_STAR:
        //            case CH_SLASH:
        //            case CH_RSLASH:

    }

    // ---- Numbers.
    // But a plain "+" and "-" are symbols.

    /*
    [16]    integer         ::=     ('-' | '+') ? [0-9]+
    [17]    double          ::=     ('-' | '+') ? ( [0-9]+ '.' [0-9]* exponent | '.' ([0-9])+ exponent | ([0-9])+ exponent )
                                    0.e0, .0e0, 0e0
    [18]    decimal         ::=     ('-' | '+')? ( [0-9]+ '.' [0-9]* | '.' ([0-9])+ | ([0-9])+ )
                                    0.0 .0 0.
    [19]    exponent        ::=     [eE] ('-' | '+')? [0-9]+
    []      hex             ::=     0x0123456789ABCDEFG

    */

    // TODO readNumberNoSign

    if (ch == Chars.CH_PLUS || ch == Chars.CH_MINUS) {
      reader.readChar();
      int ch2 = reader.peekChar();

      if (!range(ch2, '0', '9')) {
        // ch was end of symbol.
        // reader.readChar() ;
        if (ch == Chars.CH_PLUS) token.setType(TokenType.PLUS);
        else token.setType(TokenType.MINUS);

        return token;
      }

      // Already got a + or - ...
      // readNumberNoSign

      // Because next, old code proceses signs.
      // FIXME
      reader.pushbackChar(ch);
    }

    if (ch == Chars.CH_PLUS || ch == Chars.CH_MINUS || range(ch, '0', '9')) {
      // readNumberNoSign
      readNumber();

      return token;
    }

    // Plain words and prefixes.
    //   Can't start with a number due to numeric test above.
    //   Can't start with a '_' due to blank node test above.
    // If we see a :, the first time it means a prefixed name else it's a token break.

    // (eventually) Make this a very wide definition, including symbols like <=
    // Prefixed names are the difficulty.

    readPrefixedNameOrKeyword(token);

    return token;
  }

  // static char[] badIRIchars = { '<', '>', '{', '}', '|', '\\', '`', '^', ' ' } ;
  private String readIRI() {
    // token.setImage(readString(CH_LT, CH_GT, false)) ;
    stringBuilder.setLength(0);

    for (; ; ) {
      int ch = reader.readChar();

      if (ch == Chars.EOF) {
        // if ( endNL ) return stringBuilder.toString() ;
        exception("Broken IRI: " + stringBuilder.toString());
      }

      if (ch == '\n') exception("Broken IRI (newline): " + stringBuilder.toString());

      if (ch == Chars.CH_GT) {
        // sb.append(((char)ch)) ;
        return stringBuilder.toString();
      }

      if (ch == '\\') {
        // N-Triples strictly allows \t\n etc in IRIs (grammar says "string escapes")
        // ch = strEscapes ? readLiteralEscape() : readUnicodeEscape() ;
        ch = readUnicodeEscape();
        // Drop through.
      }
      // Ban certain very bad characters
      if (ch == '<')
        exception("Broken IRI (bad character: '" + (char) ch + "'): " + stringBuilder.toString());

      insertCodepoint(stringBuilder, ch);
    }
  }

  private void readPrefixedNameOrKeyword(Token token) {
    long posn = reader.getPosition();

    String prefixPart = readPrefixPart(); // (nameStartChar - '_') nameChar*

    token.setImage(prefixPart);

    token.setType(TokenType.KEYWORD);

    int ch = reader.peekChar();

    if (ch == Chars.CH_COLON) {
      reader.readChar();

      token.setType(TokenType.PREFIXED_NAME);

      String ln = readLocalPart(); // nameStartChar nameChar*

      token.setImage2(ln);
    }

    // If we made no progress, nothing found, not even a keyword -- it's an error.
    if (posn == reader.getPosition()) exception(String.format("Unknown char: %c(%d)", ch, ch));
  }

  /*
  The token rules from SPARQL and Turtle.
  PNAME_NS       ::=  PN_PREFIX? ':'
  PNAME_LN       ::=  PNAME_NS PN_LOCAL

  PN_CHARS_BASE  ::=  [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
  PN_CHARS_U     ::=  PN_CHARS_BASE | '_'
  VARNAME        ::=  ( PN_CHARS_U  | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
  PN_CHARS       ::=  PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
  PN_PREFIX      ::=  PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?
  PN_LOCAL       ::=  ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?
  */

  private String readPrefixPart()
        // { return readWordSub(false, false) ; }
      {
    return readSegment(false);
  }

  private String readLocalPart()
        // { return readWordSub(true, false) ; }
      {
    return readSegment(true);
  }

  private String readSegment(boolean isLocalPart) {
    // PN_CHARS_BASE          ((PN_CHARS|'.')* PN_CHARS)?
    // ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?
    // RiotChars has isPNChars_U_N for   ( PN_CHARS_U | [0-9] )
    stringBuilder.setLength(0);

    // -- Test first character
    int ch = reader.peekChar();
    if (ch == Chars.EOF) return "";
    if (isLocalPart) {
      if (!isPNChars_U_N(ch)) return "";
    } else {
      if (!isPNCharsBase(ch)) return "";
    }
    // ch is not added to the buffer until ...
    // -- Do remainer
    stringBuilder.append((char) ch);
    reader.readChar();
    int chDot = 0;

    for (; ; ) {
      ch = reader.peekChar();
      if (isPNChars(ch)) {
        reader.readChar();
        // Was there also a DOT?
        if (chDot != 0) {
          stringBuilder.append((char) chDot);
          chDot = 0;
        }
        stringBuilder.append((char) ch);
        continue;
      }
      // Not isPNChars
      if (ch != Chars.CH_DOT) break;
      // DOT
      reader.readChar();
      chDot = ch;
    }
    // On exit, chDot may hold a character.

    if (chDot == Chars.CH_DOT)
      // Unread it.
      reader.pushbackChar(chDot);

    // stringBuilder.deleteCharAt(chDot)

    return stringBuilder.toString();
  }

  // Get characters between two markers.
  // strEscapes may be processed
  // endNL end of line as an ending is OK
  private String readString(int startCh, int endCh, boolean strEscapes) {
    stringBuilder.setLength(0);

    // Assumes first char read already.
    //        int ch0 = reader.readChar() ;
    //        if ( ch0 != startCh )
    //            exception("Broken org.rdf4led.sparql.parser", y, x) ;

    for (; ; ) {
      int ch = reader.readChar();
      if (ch == Chars.EOF) {
        // if ( endNL ) return stringBuilder.toString() ;
        exception("Broken token: " + stringBuilder.toString());
      }

      if (ch == '\n') exception("Broken token (newline): " + stringBuilder.toString());

      if (ch == endCh) {
        // sb.append(((char)ch)) ;
        return stringBuilder.toString();
      }

      if (ch == '\\') {
        ch = strEscapes ? readLiteralEscape() : readUnicodeEscape();
        // Drop through.
      }
      insertCodepoint(stringBuilder, ch);
    }
  }

  private String readLongString(int quoteChar, boolean endNL) {
    stringBuilder.setLength(0);
    for (; ; ) {
      int ch = reader.readChar();
      if (ch == Chars.EOF) {
        if (endNL) return stringBuilder.toString();
        exception("Broken long string");
      }

      if (ch == quoteChar) {
        if (threeQuotes(quoteChar)) return stringBuilder.toString();
      }

      if (ch == '\\') ch = readLiteralEscape();
      insertCodepoint(stringBuilder, ch);
    }
  }

  private String readWord(boolean leadingDigitAllowed) {
    return readWordSub(leadingDigitAllowed, false);
  }

  // A 'word' is used in several places:
  //   keyword
  //   prefix part of prefix name
  //   local part of prefix name (allows digits)

  private static char[] extraCharsWord = new char[] {'_', '.', '-'};

  private String readWordSub(boolean leadingDigitAllowed, boolean leadingSignAllowed) {
    return readCharsAnd(leadingDigitAllowed, leadingSignAllowed, extraCharsWord, false);
  }

  private static char[] extraCharsVar = new char[] {'_', '.', '-', '?', '@', '+'};

  private String readVarName() {
    return readCharsAnd(true, true, extraCharsVar, true);
  }

  // See also readBlankNodeLabel

  private String readCharsAnd(
      boolean leadingDigitAllowed,
      boolean leadingSignAllowed,
      char[] extraChars,
      boolean allowFinalDot) {
    stringBuilder.setLength(0);
    int idx = 0;
    if (!leadingDigitAllowed) {
      int ch = reader.peekChar();
      if (Character.isDigit(ch)) return "";
    }

    // Used for local part of prefix names =>
    if (!leadingSignAllowed) {
      int ch = reader.peekChar();
      if (ch == '-' || ch == '+') return "";
    }

    for (; ; idx++) {
      int ch = reader.peekChar();

      if (isAlphaNumeric(ch) || charInArray(ch, extraChars)) {
        reader.readChar();
        stringBuilder.append((char) ch);
        continue;
      } else
        // Inappropriate character.
        break;
    }

    if (!allowFinalDot) {
      // BAD : assumes pushbackChar is infinite.
      // Check is ends in "."
      while (idx > 0 && stringBuilder.charAt(idx - 1) == Chars.CH_DOT) {
        // Push back the dot.
        reader.pushbackChar(Chars.CH_DOT);
        stringBuilder.setLength(idx - 1);
        idx--;
      }
    }
    return stringBuilder.toString();
  }

  // Blank node label: letters, numbers and '-', '_'
  // Strictly, can't start with "-" or digits.

  private String readBlankNodeLabel() {
    stringBuilder.setLength(0);
    // First character.
    {
      int ch = reader.peekChar();
      if (ch == Chars.EOF) exception("Blank node label missing (EOF found)");
      if (isWhitespace(ch)) exception("Blank node label missing");
      // if ( ! isAlpha(ch) && ch != '_' )
      // Not strict
      if (!isAlphaNumeric(ch) && ch != '_')
        exception("Blank node label does not start with alphabetic or _ :" + (char) ch);
      reader.readChar();
      stringBuilder.append((char) ch);
    }
    // Remainder.
    for (; ; ) {
      int ch = reader.peekChar();
      if (ch == Chars.EOF) break;
      if (!isAlphaNumeric(ch) && ch != '-' && ch != '_') break;
      reader.readChar();
      stringBuilder.append((char) ch);
    }
    //        if ( ! seen )
    //            exception("Blank node label missing") ;
    return stringBuilder.toString();
  }

  // Make better!
  /*
         [16]    integer         ::=     ('-' | '+') ? [0-9]+
         [17]    double          ::=     ('-' | '+') ? ( [0-9]+ '.' [0-9]* exponent | '.' ([0-9])+ exponent | ([0-9])+ exponent )
                                         0.e0, .0e0, 0e0
         [18]    decimal         ::=     ('-' | '+')? ( [0-9]+ '.' [0-9]* | '.' ([0-9])+ | ([0-9])+ )
                                         0.0 .0
         [19]    exponent        ::=     [eE] ('-' | '+')? [0-9]+
         []      hex             ::=     0x0123456789ABCDEFG

  */
  private void readNumber() {
    // One entry, definitely a number.
    // Beware of '.' as a (non) decimal.
    /*
    maybeSign()
    digits()
    if dot ==> decimal, digits
    if e   ==> double, maybeSign, digits
    else
        check not "." for decimal.
    */
    boolean isDouble = false;
    boolean isDecimal = false;
    stringBuilder.setLength(0);

    /*
    readPossibleSign(stringBuilder) ;
    readDigits may be hex
    readDot
    readDigits
    readExponent.
    */

    int x = 0; // Digits before a dot.
    int ch = reader.peekChar();
    if (ch == '0') {
      x++;
      reader.readChar();
      stringBuilder.append((char) ch);
      ch = reader.peekChar();
      if (ch == 'x' || ch == 'X') {
        reader.readChar();
        stringBuilder.append((char) ch);
        readHex(reader, stringBuilder);
        token.setImage(stringBuilder.toString());
        token.setType(TokenType.HEX);
        return;
      }
    } else if (ch == '-' || ch == '+') {
      readPossibleSign(stringBuilder);
    }

    x += readDigits(stringBuilder);
    //        if ( x == 0 )
    //        {
    //
    //        }
    ch = reader.peekChar();
    if (ch == Chars.CH_DOT) {
      reader.readChar();
      stringBuilder.append(Chars.CH_DOT);
      isDecimal = true; // Includes things that will be doubles.
      readDigits(stringBuilder);
    }

    if (x == 0 && !isDecimal)
      // Possible a tokenizer error - should not have entered readNumber in the first place.
      exception("Unrecognized as number");

    if (exponent(stringBuilder)) {
      isDouble = true;
      isDecimal = false;
    }

    token.setImage(stringBuilder.toString());
    if (isDouble) token.setType(TokenType.DOUBLE);
    else if (isDecimal) token.setType(TokenType.DECIMAL);
    else token.setType(TokenType.INTEGER);
  }

  private void readHex(PeekReader reader, StringBuilder sb) {
    // Just after the 0x, which are in sb
    int x = 0;
    for (; ; ) {
      int ch = reader.peekChar();

      if (!range(ch, '0', '9') && !range(ch, 'a', 'f') && !range(ch, 'A', 'F')) break;
      reader.readChar();
      sb.append((char) ch);
      x++;
    }
    if (x == 0) exception("No hex characters after " + sb.toString());
  }

  private int readDigits(StringBuilder buffer) {
    int count = 0;
    for (; ; ) {
      int ch = reader.peekChar();
      if (!range(ch, '0', '9')) break;
      reader.readChar();
      buffer.append((char) ch);
      count++;
    }
    return count;
  }

  private void readPossibleSign(StringBuilder sb) {
    int ch = reader.peekChar();
    if (ch == '-' || ch == '+') {
      reader.readChar();
      sb.append((char) ch);
    }
  }

  // Assume have read the first quote char.
  // On return:
  //   If false, have moved over no more characters (due to pushbacks)
  //   If true, at end of 3 quotes
  private boolean threeQuotes(int ch) {
    // reader.readChar() ;         // Read first quote.
    int ch2 = reader.peekChar();
    if (ch2 != ch) {
      // reader.pushbackChar(ch2) ;
      return false;
    }

    reader.readChar(); // Read second quote.
    int ch3 = reader.peekChar();
    if (ch3 != ch) {
      // reader.pushbackChar(ch3) ;
      reader.pushbackChar(ch2);
      return false;
    }

    // Three quotes.
    reader.readChar(); // Read third quote.
    return true;
  }

  private boolean exponent(StringBuilder sb) {
    int ch = reader.peekChar();
    if (ch != 'e' && ch != 'E') return false;
    reader.readChar();
    sb.append((char) ch);
    readPossibleSign(sb);
    int x = readDigits(sb);
    if (x == 0) exception("Malformed double: " + sb);
    return true;
  }

  private String langTag() {
    stringBuilder.setLength(0);
    a2z(stringBuilder);
    if (stringBuilder.length() == 0) exception("Bad language tag");
    for (; ; ) {
      int ch = reader.peekChar();
      if (ch == '-') {
        reader.readChar();
        stringBuilder.append('-');
        int x = stringBuilder.length();
        a2zN(stringBuilder);
        if (stringBuilder.length() == x) exception("Bad language tag");
      } else break;
    }
    return stringBuilder.toString();
  }

  // ASCII-only e.g. in org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tags.
  private void a2z(StringBuilder sb2) {
    for (; ; ) {
      int ch = reader.peekChar();
      if (isA2Z(ch)) {
        reader.readChar();
        stringBuilder.append((char) ch);
      } else return;
    }
  }

  private void a2zN(StringBuilder sb2) {
    for (; ; ) {
      int ch = reader.peekChar();
      if (isA2ZN(ch)) {
        reader.readChar();
        stringBuilder.append((char) ch);
      } else return;
    }
  }

  private void insertCodepoint(StringBuilder buffer, int ch) {
    if (Character.charCount(ch) == 1) buffer.append((char) ch);
    else {
      // Convert to UTF-16.  Note that the rest of any system this is used
      // in must also respect codepoints and surrogate pairs.
      if (!Character.isDefined(ch) && !Character.isSupplementaryCodePoint(ch))
        exception(String.format("Illegal codepoint: 0x%04X", ch));
      char[] chars = Character.toChars(ch);
      buffer.append(chars);
    }
  }

  //    @Override
  //    public long getColumn()
  //    {
  //        return reader.getColNum() ;
  //    }
  //
  //    @Override
  //    public long getLine()
  //    {
  //        return reader.getLineNum() ;
  //    }

  // ---- Routines to check tokens

  //    private void checkBlankNode(String blankNodeLabel)
  //    {
  //        if ( checker != null ) checker.checkBlankNode(blankNodeLabel) ;
  //    }
  //
  //    private void checkLiteralLang(String lexicalForm, String langTag)
  //    {
  //       if ( checker != null ) checker.checkLiteralLang(lexicalForm, langTag) ;
  //    }
  //
  //    private void checkLiteralDT(String lexicalForm, Token datatype)
  //    {
  //       if ( checker != null ) checker.checkLiteralDT(lexicalForm, datatype) ;
  //    }
  //
  //    private void checkString(String string)
  //    {
  //       if ( checker != null ) checker.checkString(string) ;
  //    }
  //
  //    private void checkURI(String uriStr)
  //    {
  //       if ( checker != null ) checker.checkURI(uriStr) ;
  //    }
  //
  //    private void checkNumber(String image, String datatype)
  //    {
  //       if ( checker != null ) checker.checkNumber(image, datatype) ;
  //    }
  //
  //    private void checkVariable(String tokenImage)
  //    {
  //       if ( checker != null ) checker.checkVariable(tokenImage) ;
  //    }

  //    private void checkDirective(int cntrlCode)
  //    {
  //       if ( checker != null ) checker.checkDirective(cntrlCode) ;
  //    }
  //
  //    private void checkKeyword(String tokenImage)
  //    {
  //       if ( checker != null ) checker.checkKeyword(tokenImage) ;
  //    }
  //
  //    private void checkPrefixedName(String tokenImage, String tokenImage2)
  //    {
  //       if ( checker != null ) checker.checkPrefixedName(tokenImage, tokenImage2) ;
  //    }
  //
  //    private void checkControl(int code)
  //    {
  //       if ( checker != null ) checker.checkControl(code) ;
  //    }

  // ---- Escape sequences

  private final int readLiteralEscape() {
    int c = reader.readChar();
    if (c == Chars.EOF) exception("Escape sequence not completed");

    switch (c) {
      case 'n':
        return Chars.NL;
      case 'r':
        return Chars.CR;
      case 't':
        return '\t';
      case 'f':
        return '\f';
      case '"':
        return '"';
      case '\'':
        return '\'';
      case '\\':
        return '\\';
      case 'u':
        return readUnicode4Escape();
      case 'U':
        return readUnicode8Escape();
      default:
        exception(String.format("illegal escape sequence value: %c (0x%02X)", c, c));
        return 0;
    }
  }

  private final int readUnicodeEscape() {
    int ch = reader.readChar();

    if (ch == Chars.EOF) exception("Broken escape sequence");

    switch (ch) {
      case '\\':
        return '\\';

      case 'u':
        return readUnicode4Escape();

      case 'U':
        return readUnicode8Escape();

      default:
        exception(String.format("illegal escape sequence value: %c (0x%02X)", ch, ch));
    }
    return 0;
  }

  private final int readUnicode4Escape() {
    return readUnicodeEscape(4);
  }

  private final int readUnicode8Escape() {
    int ch8 = readUnicodeEscape(8);

    if (ch8 > Character.MAX_CODE_POINT)
      exception(String.format("illegal code point in \\U sequence value: 0x%08X", ch8));

    return ch8;
  }

  private final int readUnicodeEscape(int N) {
    int x = 0;
    for (int i = 0; i < N; i++) {
      int d = readHexChar();
      if (d < 0) return -1;
      x = (x << 4) + d;
    }
    return x;
  }

  private final int readHexChar() {
    int ch = reader.readChar();
    if (ch == Chars.EOF) exception("Not a hexadecimal character (end of file)");

    int x = valHexChar(ch);

    if (x != -1) return x;

    exception("Not a hexadecimal character: " + (char) ch);
    return -1;
  }

  private boolean expect(String str) {
    for (int i = 0; i < str.length(); i++) {
      char want = str.charAt(i);
      if (reader.eof()) {
        exception("End of input during expected string: " + str);
        return false;
      }
      int inChar = reader.readChar();
      if (inChar != want) {
        // System.err.println("N-triple reader error");
        exception("expected \"" + str + "\"");
        return false;
      }
    }
    return true;
  }

  public void exception(String message) {
    throw new RuntimeException(message);
  }

  public static boolean isAlpha(int codepoint) {
    return Character.isLetter(codepoint);
  }

  public static boolean isAlphaNumeric(int codepoint) {
    return Character.isLetterOrDigit(codepoint);
  }

  /** ASCII A-Z */
  public static boolean isA2Z(int ch) {
    return range(ch, 'a', 'z') || range(ch, 'A', 'Z');
  }

  /** ASCII A-Z or 0-9 */
  public static boolean isA2ZN(int ch) {
    return range(ch, 'a', 'z') || range(ch, 'A', 'Z') || range(ch, '0', '9');
  }

  /** ASCII 0-9 */
  public static boolean isDigit(int ch) {
    return range(ch, '0', '9');
  }

  public static boolean isWhitespace(int ch) {
    // ch = ch | 0xFF ;
    return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '\f';
  }

  public static boolean isNewlineChar(int ch) {
    return ch == '\r' || ch == '\n';
  }

  /*
  The token rules from SPARQL and Turtle.
  PNAME_NS       ::=  PN_PREFIX? ':'
  PNAME_LN       ::=  PNAME_NS PN_LOCAL[131]  BLANK_NODE_LABEL  ::=  '_:' PN_LOCAL
  PN_CHARS_BASE  ::=  [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
  PN_CHARS_U     ::=  PN_CHARS_BASE | '_'
  VARNAME        ::=  ( PN_CHARS_U  | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
  PN_CHARS       ::=  PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
  PN_PREFIX      ::=  PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?
  PN_LOCAL       ::=  ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?
       */

  public static boolean isPNCharsBase(int ch) {
    // PN_CHARS_BASE ::= [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] |
    //                   [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
    //                   [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
    //                   [#x10000-#xEFFFF]
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

  public static boolean isPNChars_U(int ch) {
    // PN_CHARS_BASE | '_'
    return isPNCharsBase(ch) || (ch == '_');
  }

  public static boolean isPNChars_U_N(int ch) {
    // PN_CHARS_U | [0-9]
    return isPNCharsBase(ch) || (ch == '_') || isDigit(ch);
  }

  public static boolean isPNChars(int ch) {
    // PN_CHARS ::=  PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
    return isPNChars_U(ch)
        || isDigit(ch)
        || (ch == '-')
        || ch == 0x00B7
        || r(ch, 0x300, 0x036F)
        || r(ch, 0x203F, 0x2040);
  }

  public static int valHexChar(int ch) {
    if (range(ch, '0', '9')) return ch - '0';
    if (range(ch, 'a', 'f')) return ch - 'a' + 10;
    if (range(ch, 'A', 'F')) return ch - 'A' + 10;
    return -1;
  }

  private static boolean r(int ch, int a, int b) {
    return (ch >= a && ch <= b);
  }

  public static boolean range(int ch, char a, char b) {
    return (ch >= a && ch <= b);
  }

  public static boolean charInArray(int ch, char[] chars) {
    for (int xch : chars) {
      if (ch == xch) return true;
    }
    return false;
  }
}
