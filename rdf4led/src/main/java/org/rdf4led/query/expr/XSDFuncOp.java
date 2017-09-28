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

package org.rdf4led.query.expr;

import org.rdf4led.common.DateTimeStruct;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;
import org.rdf4led.rdf.dictionary.vocab.LangTag;
import org.rdf4led.rdf.dictionary.vocab.XSD;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.RegexJava;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static javax.xml.datatype.DatatypeConstants.*;

/**
 * XSDFuncOp.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */

/** Implementation of XQuery/XPath functions and operators. http://www.w3.org/TR/xpath-functions/ */
public class XSDFuncOp<Node> {
  private final byte OP_INTEGER = 1;
  private final byte OP_DECIMAL = 2;
  private final byte OP_FLOAT = 3;
  private final byte OP_DOUBLE = 4;
  private final byte OP_UNVALID_NUMBER = 5;

  QueryContext<Node> queryContext;

  public XSDFuncOp(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;
  }

  // The choice of "24" is arbitrary but more than 18 as required by F&O
  private final int DIVIDE_PRECISION = 24;
  // --------------------------------
  // Numeric operations
  // http://www.w3.org/TR/xpath-functions/#op.numeric
  // http://www.w3.org/TR/xpath-functions/#comp.numeric

  private double double1 = 0;
  private double double2 = 0;

  private float float1 = 0;
  private float float2 = 0;

  private int integer1 = 0;
  private int integer2 = 0;

  private BigDecimal decimal1;
  private BigDecimal decimal2;

  private boolean isInteger1(String number) {
    try {
      integer1 = Integer.parseInt(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isInteger2(String number) {
    try {
      integer2 = Integer.parseInt(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isDecimal1(String number) {
    try {
      decimal1 = new BigDecimal(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isDecimal2(String number) {
    try {
      decimal2 = new BigDecimal(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isFloat1(String number) {
    try {
      float1 = Float.parseFloat(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isFloat2(String number) {
    try {
      float2 = Float.parseFloat(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isDouble1(String number) {
    try {
      double1 = Double.parseDouble(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isDouble2(String number) {
    try {
      double2 = Double.parseDouble(number);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private byte classifyNumeric(String lex1, String lex2) {
    if (isInteger1(lex1)) {
      if (isInteger2(lex2)) {
        return OP_INTEGER;
      }

      if (isFloat2(lex2)) {
        isFloat1(lex1);
        return OP_FLOAT;
      }

      if (isDouble2(lex2)) {
        isDouble1(lex1);
        return OP_DOUBLE;
      }

      if (isDecimal2(lex2)) {
        isDecimal1(lex1);
        return OP_DECIMAL;
      }

      return OP_UNVALID_NUMBER;
    }

    if (isFloat1(lex1)) {
      if (isFloat2(lex2)) {
        return OP_FLOAT;
      }

      if (isDouble2(lex2)) {
        isDouble1(lex1);
        return OP_DOUBLE;
      }

      if (isDecimal2(lex2)) {
        isDecimal1(lex1);
        return OP_DECIMAL;
      }

      return OP_UNVALID_NUMBER;
    }

    if (isDouble1(lex1)) {
      if (isDouble2(lex2)) {
        return OP_DOUBLE;
      }

      if (isDecimal2(lex2)) {
        isDecimal1(lex1);
        return OP_DECIMAL;
      }

      return OP_UNVALID_NUMBER;
    }

    if (isDecimal1(lex1)) {
      isDecimal2(lex2);
      return OP_DECIMAL;
    }

    return OP_UNVALID_NUMBER;
  }

  private byte classifyNumeric(String lex1) {
    if (isInteger1(lex1)) {
      return OP_INTEGER;
    }

    if (isFloat1(lex1)) {
      return OP_FLOAT;
    }

    if (isDouble1(lex1)) {
      return OP_DECIMAL;
    }

    if (isDecimal1(lex1)) {
      return OP_DECIMAL;
    }

    return OP_UNVALID_NUMBER;
  }

  public Node numAdd(Node nv1, Node nv2) {
    String lex1 = queryContext.dictionary().getLexicalValue(nv1);

    String lex2 = queryContext.dictionary().getLexicalValue(nv2);

    switch (classifyNumeric(lex1, lex2)) {
      case OP_INTEGER:
        {
          return queryContext
              .dictionary()
              .createIntegerNode(BigInteger.valueOf(integer1 + integer2));
        }

      case OP_DECIMAL:
        {
          //				decimal1 = new BigDecimal(lex1);
          //
          //				decimal2 = new BigDecimal(lex2);

          return queryContext.dictionary().createDecimalNode(decimal1.add(decimal2));
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(float1 + float2);
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(double1 + double2);
        }

      default:
        throw new RuntimeException(
            "Unrecognized numeric operation : " + nv1 + " " + lex1 + " ," + nv2 + " : " + lex2);
    }
  }

  public Node numSubtract(Node nv1, Node nv2) // F&O numeric-subtract
      {
    String lex1 = queryContext.dictionary().getLexicalValue(nv1);

    String lex2 = queryContext.dictionary().getLexicalValue(nv2);

    //		if (!isNumeric(lex1))
    //			throw new RuntimeException(lex1 + " is not number");

    //		if (!isNumeric(lex2))
    //			throw new RuntimeException(lex1 + " is not number");

    switch (classifyNumeric(lex1, lex2)) {
      case OP_INTEGER:
        {
          return queryContext
              .dictionary()
              .createIntegerNode(BigInteger.valueOf(integer1 - integer2));
        }

      case OP_DECIMAL:
        {
          //				decimal1 = new BigDecimal(lex1);
          //
          //				decimal2 = new BigDecimal(lex2);

          return queryContext.dictionary().createDecimalNode(decimal1.subtract(decimal2));
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(float1 - float2);
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(double1 - double2);
        }

      default:
        throw new RuntimeException(
            "Unrecognized subtract numeric operation : (" + nv1 + " ," + nv2 + ")");
    }
  }

  public Node numMultiply(Node nv1, Node nv2) // F&O numeric-multiply
      {
    String lex1 = queryContext.dictionary().getLexicalValue(nv1);

    String lex2 = queryContext.dictionary().getLexicalValue(nv2);

    //		if (!isNumeric(lex1))
    //			throw new RuntimeException(lex1 + " is not number");
    //
    //		if (!isNumeric(lex2))
    //			throw new RuntimeException(lex1 + " is not number");

    switch (classifyNumeric(lex1, lex2)) {
      case OP_INTEGER:
        {
          return queryContext
              .dictionary()
              .createIntegerNode(BigInteger.valueOf(integer1 * integer2));
        }

      case OP_DECIMAL:
        {
          //				decimal1 = new BigDecimal(lex1);

          //				decimal2 = new BigDecimal(lex2);

          return queryContext.dictionary().createDecimalNode(decimal1.subtract(decimal2));
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(float1 * float2);
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(double1 * double2);
        }

      default:
        throw new RuntimeException(
            "Unrecognized subtract numeric operation : (" + nv1 + " ," + nv2 + ")");
    }
  }

  /*
   * Quote from XQuery/XPath F&O: For xs:float or xs:double values, a positive
   * number divided by positive zero returns INF. A negative number divided by
   * positive zero returns -INF. Division by negative zero returns -INF and
   * INF, respectively. Positive or negative zero divided by positive or
   * negative zero returns NaN. Also, INF or -INF divided by INF or -INF
   * returns NaN.
   */

  public Node numDivide(Node nv1, Node nv2) // F&O numeric-divide
      {
    String lex1 = queryContext.dictionary().getLexicalValue(nv1);

    String lex2 = queryContext.dictionary().getLexicalValue(nv2);

    //		if (!isNumeric(lex1))
    //			throw new RuntimeException(lex1 + " is not number");

    //		if (!isNumeric(lex2))
    //			throw new RuntimeException(lex1 + " is not number");

    switch (classifyNumeric(lex1, lex2)) {
      case OP_INTEGER:
        {
          if (integer2 == 0) throw new RuntimeException("Divide by zero in devide");

          // if ( nv2.getInteger().equals(BigInteger.ZERO) )
          // throw new ExprEvalException("Divide by zero in divide") ;
          // Note: result is a decimal
          BigDecimal d1 = new BigDecimal(integer1);

          BigDecimal d2 = new BigDecimal(integer2);

          return decimalDivide(d1, d2);
        }

      case OP_DECIMAL:
        {
          BigDecimal d1 = new BigDecimal(lex1);

          BigDecimal d2 = new BigDecimal(lex2);

          if (d2.equals(BigDecimal.ZERO)) throw new RuntimeException("Divide by zero in devide");

          return decimalDivide(d1, d2);
        }

      case OP_FLOAT:
        {
          if (float2 == 0) throw new RuntimeException("Divide by zero in devide");

          return queryContext.dictionary().createFloatNode(float1 / float2);
        }

      case OP_DOUBLE:
        {
          if (float2 == 0) throw new RuntimeException("Divide by zero in devide");

          return queryContext.dictionary().createFloatNode(float1 / float2);
        }
      default:
        throw new RuntimeException(
            "Unrecognized numeric operation : " + nv1 + " : " + lex1 + " , " + nv2 + " : " + lex2);
    }
  }

  private Node decimalDivide(BigDecimal d1, BigDecimal d2) {
    try {
      BigDecimal d3 = d1.divide(d2, DIVIDE_PRECISION, BigDecimal.ROUND_FLOOR);

      return messAroundWithBigDecimalFormat(d3);

    } catch (ArithmeticException ex) {

      // Log.warn(XSDFuncOp.class,
      // "ArithmeticException in decimal divide - attempting to treat as doubles")
      // ;

      BigDecimal d3 = new BigDecimal(d1.doubleValue() / d2.doubleValue());

      return queryContext.dictionary().createDecimalNode(d3);
    }
  }

  private Node messAroundWithBigDecimalFormat(BigDecimal d) {
    String x = d.toPlainString();

    // The part after the "."
    int dotIdx = x.indexOf('.');

    if (dotIdx < 0)
      // No DOT.
      return queryContext.dictionary().createDecimalNode(new BigDecimal(x));

    // Has a DOT.
    int i = x.length() - 1;
    // i+1 at least leave ".0"
    while (i + 1 > dotIdx && x.charAt(i) == '0') i--;
    if (i < x.length() - 1)
      // And trailing zeros.
      x = x.substring(0, i + 1);

    // Avoid as expensive.
    // x = x.replaceAll("0+$", "") ;
    // return NodeValue.makeNode(x, XSDDatatype.XSDdecimal) ;
    return queryContext.dictionary().createDecimalNode(new BigDecimal(x));
  }

  public Node max(Node nv1, Node nv2) {
    int x = compareNumeric(nv1, nv2);

    if (x == QueryContext.CMP_LESS) return nv2;
    return nv1;
  }

  public Node min(Node nv1, Node nv2) {
    int x = compareNumeric(nv1, nv2);
    if (x == QueryContext.CMP_GREATER) return nv2;
    return nv1;
  }

  public Node not(Node nv) // F&O fn:not
      {
    boolean b = booleanEffectiveValue(nv);

    return queryContext.dictionary().createBooleanNode(b);
  }

  public Node booleanEffectiveValueAsNodeValue(Node nv) // F&O fn:boolean
      {
    if (queryContext.dictionary().isBooleanNode(nv)) // "Optimization" (saves on object
    // churn)
    {
      return nv;
    }

    return queryContext.dictionary().createBooleanNode(booleanEffectiveValue(nv));
  }

  public boolean booleanEffectiveValue(Node nv) // F&O fn:boolean
      {
    // Apply the "boolean effective value" rules
    // boolean: value of the boolean (strictly, if derived from xsd:boolean)
    // string: length(string) > 0
    // numeric: number != Nan && number != 0
    // http://www.w3.org/TR/xquery/#dt-ebv

    if (queryContext.dictionary().isBooleanNode(nv)) {
      return nv.equals(queryContext.dictionary().getXSDTrue());
    }

    String lex = queryContext.dictionary().getLexicalValue(nv);

    if (queryContext.dictionary().isStringNode(nv)) {
      return (lex.length() > 0);
    }

    if (isInteger1(lex)) {
      return BigInteger.valueOf(integer1).equals(BigInteger.ZERO);
    }

    if (isDouble1(lex)) {
      return double1 != 0.0;
    }

    //		if (isNumeric(lex))
    //		{
    //			return new BigDecimal(lex).equals(BigDecimal.ZERO);
    //		}

    try {
      return new BigDecimal(lex).equals(BigDecimal.ZERO);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public Node unaryMinus(Node nv) // F&O numeric-unary-minus
      {
    String lex = queryContext.dictionary().getLexicalValue(nv);

    //		if (!isNumeric(lex))
    //		{
    //			throw new RuntimeException(lex + " is not number");
    //		}

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:
        {
          return queryContext.dictionary().createIntegerNode(BigInteger.valueOf(integer1).negate());
        }

      case OP_DECIMAL:
        {
          return queryContext.dictionary().createDecimalNode(new BigDecimal(lex).negate());
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(-float1);
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(-double1);
        }

      default:
        throw new RuntimeException("Unrecognized numeric operation : " + nv);
    }
  }

  public Node unaryPlus(Node nv) // F&O numeric-unary-plus
      {
    // Not quite a no-org.rdf4led.sparql.algebra.op - tests for a number
    // byte opType = classifyNumeric("unaryPlus", nv) ;
    return nv;
  }

  public Node abs(Node nv) {
    String lex = queryContext.dictionary().getLexicalValue(nv);

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:
        {
          return queryContext.dictionary().createIntegerNode(BigInteger.valueOf(integer1).abs());
        }

      case OP_DECIMAL:
        {
          return queryContext.dictionary().createDecimalNode(decimal1.abs());
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(Math.abs(float1));
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(Math.abs(double1));
        }

      default:
        throw new RuntimeException("Unrecognized numeric operation : " + nv + " : " + lex);
    }
  }

  public Node ceiling(Node v) {
    String lex = queryContext.dictionary().getLexicalValue(v);

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:
        {
          return v;
        }

      case OP_DECIMAL:
        {
          BigDecimal dec = new BigDecimal(lex).setScale(0, BigDecimal.ROUND_CEILING);

          return queryContext.dictionary().createDecimalNode(dec);
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode((float) Math.ceil(float1));
        }
      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(Math.ceil(double1));
        }
      default:
        throw new RuntimeException("Unrecognized numeric operation : " + v);
    }
  }

  public Node floor(Node v) {
    String lex = queryContext.dictionary().getLexicalValue(v);

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:
        {
          return v;
        }

      case OP_DECIMAL:
        {
          return queryContext
              .dictionary()
              .createDecimalNode(decimal1.setScale(0, BigDecimal.ROUND_FLOOR));
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode((float) Math.floor(float1));
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(Math.floor(double1));
        }

      default:
        throw new RuntimeException("Unrecognized numeric operation : " + v + " : " + lex);
    }
  }

  public Node round(Node v) {
    String lex = queryContext.dictionary().getLexicalValue(v);

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:
        return v;

      case OP_DECIMAL:
        {
          //				int sgn = new BigDecimal(lex).signum();
          int sgn = decimal1.signum();

          BigDecimal dec;

          if (sgn < 0) dec = new BigDecimal(lex).setScale(0, BigDecimal.ROUND_HALF_DOWN);
          else dec = new BigDecimal(lex).setScale(0, BigDecimal.ROUND_HALF_UP);

          return queryContext.dictionary().createDecimalNode(dec);
        }

      case OP_FLOAT:
        {
          return queryContext.dictionary().createFloatNode(Math.round(float1));
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(Math.round(double1));
        }

      default:
        throw new RuntimeException("Unrecognized numeric operation : " + v);
    }
  }

  public Node sqrt(Node v) {
    String lex = queryContext.dictionary().getLexicalValue(v);

    switch (classifyNumeric(lex)) {
      case OP_INTEGER:

      case OP_DECIMAL:
        {
          double dec = new BigDecimal(lex).doubleValue();

          return queryContext.dictionary().createDecimalNode(BigDecimal.valueOf(Math.sqrt(dec)));
        }

      case OP_FLOAT:
        { // NB - returns a double
          return queryContext.dictionary().createDoubleNode(Math.sqrt(float1));
        }

      case OP_DOUBLE:
        {
          return queryContext.dictionary().createDoubleNode(Math.sqrt(double1));
        }

      default:
        throw new RuntimeException("Unrecognized numeric operation : " + v);
    }
  }

  // NB Java string start from zero and uses start/end
  // F&O strings start from one and uses start/length

  public Node javaSubstring(Node v1, Node v2) {
    return javaSubstring(v1, v2, null);
  }

  public Node javaSubstring(Node nvString, Node nvStart, Node nvFinish) {
    String sStart = queryContext.dictionary().getLexicalValue(nvStart);

    String sFinish = queryContext.dictionary().getLexicalValue(nvFinish);

    try {

      String string = queryContext.dictionary().getLexicalValue(nvString);

      int start = Integer.parseInt(sStart);

      int finish = Integer.parseInt(sFinish);

      return queryContext.dictionary().createStringNode(string.substring(start, finish));

    } catch (IndexOutOfBoundsException ex) {

      throw new RuntimeException(" " + ex.toString());
    }
  }

  public Node strlen(Node nvString) {
    Node n = queryContext.checkAndGetStringLiteral("strlen", nvString);

    int len = queryContext.dictionary().getLexicalValue(n).length();

    return queryContext.dictionary().createIntegerNode(BigInteger.valueOf(len));
  }

  public Node strReplace(Node nvStr, Node nvPattern, Node nvReplacement, Node nvFlags) {
    Node n = queryContext.checkAndGetStringLiteral("replace", nvPattern);

    String pat = queryContext.dictionary().getLexicalValue(n);

    int flags = 0;

    if (nvFlags != null) {
      n = queryContext.checkAndGetStringLiteral("replace", nvFlags);

      String flagsStr = queryContext.dictionary().getLexicalValue(n);

      flags = RegexJava.makeMask(flagsStr);
    }

    return strReplace(nvStr, Pattern.compile(pat, flags), nvReplacement);
  }

  public Node strReplace(Node nvStr, Pattern pattern, Node nvReplacement) {
    String n =
        queryContext
            .dictionary()
            .getLexicalValue(queryContext.checkAndGetStringLiteral("replace", nvStr));

    String rep =
        queryContext
            .dictionary()
            .getLexicalValue(queryContext.checkAndGetStringLiteral("replace", nvReplacement));

    String x = pattern.matcher(n).replaceAll(rep);

    return calcReturn(x, nvStr);
  }

  public Node strReplace(Node nvStr, Node nvPattern, Node nvReplacement) {
    return strReplace(nvStr, nvPattern, nvReplacement, null);
  }

  public Node substring(Node v1, Node v2) {
    return substring(v1, v2, null);
  }

  public Node substring(Node nvString, Node nvStart, Node nvLength) {
    Node n = queryContext.checkAndGetStringLiteral("substring", nvString);

    try {
      // NaN, float and double.

      String string = queryContext.dictionary().getLexicalValue(n);

      int start = intValueStr(nvStart, string.length() + 1);

      int length;

      if (nvLength != null) length = intValueStr(nvLength, 0);
      else {
        length = string.length();
        if (start < 0) length = length - start; // Address to end of string.
      }

      int finish = start + length;

      // Adjust for zero and negative rules for XSD.
      // Calculate the finish, regardless of whether start is zero of
      // negative ...

      // Adjust to java - and ensure within the string.
      // F&O strings are one-based ; convert to java, 0 based.

      // java needs indexes in-bounds.
      if (start <= 0) {
        start = 1;
      }

      start--;

      finish--;

      if (finish > string.length()) {
        finish = string.length(); // Java storage must be within bounds.
      }

      if (finish < start) {
        finish = start;
      }

      if (finish < 0) {
        finish = 0;
      }

      if (string.length() == 0) {
        return calcReturn("", n);
      }

      String lex2 = string.substring(start, finish);

      return calcReturn(lex2, n);

    } catch (IndexOutOfBoundsException ex) {

      throw new RuntimeException("IndexOutOfBounds", ex);
    }
  }

  private int intValueStr(Node nv, int valueNan) {
    String lex = queryContext.dictionary().getLexicalValue(nv);

    if (isInteger1(lex)) return integer1;

    if (isFloat1(lex)) {
      if (Float.isNaN(float1)) return valueNan;

      return (int) Math.round(float1);
    }

    if (isDouble1(lex)) {
      if (Double.isNaN(double1)) return valueNan;

      return (int) Math.round(double1);
    }

    return (int) Math.round(decimal1.doubleValue());
  }

  public Node strContains(Node string, Node match) {
    String lex1 = queryContext.dictionary().getLexicalValue(string);

    String lex2 = queryContext.dictionary().getLexicalValue(match);

    boolean b = lex1.contains(lex2);

    return queryContext.dictionary().createBooleanNode(b);
  }

  public Node strStartsWith(Node string, Node match) {
    String lex1 = queryContext.dictionary().getLexicalValue(string);

    String lex2 = queryContext.dictionary().getLexicalValue(match);

    return queryContext.dictionary().createBooleanNode(lex1.startsWith(lex2));
  }

  public Node strEndsWith(Node string, Node match) {
    String lex1 = queryContext.dictionary().getLexicalValue(string);

    String lex2 = queryContext.dictionary().getLexicalValue(match);

    return queryContext.dictionary().createBooleanNode(lex1.endsWith(lex2));
  }

  private Node calcReturn(String result, Node arg) {
    if (queryContext.dictionary().isPlainLiteral(arg)) {
      Node n2 =
          queryContext
              .dictionary()
              .createTypedLiteral(result, queryContext.dictionary().getLangTag(arg));

      return n2;
    } else {
      Node n2 =
          queryContext
              .dictionary()
              .createTypedLiteral(result, queryContext.dictionary().getNodeType(arg));

      return n2;
    }
  }

  public Node strBefore(Node string, Node match) {
    String lex1 = queryContext.dictionary().getLexicalValue(string);

    String lex2 = queryContext.dictionary().getLexicalValue(match);

    if (lex2.length() == 0) {
      return calcReturn("", string);
    }

    int i = lex1.indexOf(lex2);

    if (i < 0) {
      return queryContext.getnvEmptyString();
    }

    String s = lex1.substring(0, i);

    return calcReturn(s, string);
  }

  public Node strAfter(Node string, Node match) {
    String lex1 = queryContext.dictionary().getLexicalValue(string);

    String lex2 = queryContext.dictionary().getLexicalValue(match);

    if (lex2.length() == 0) {
      return calcReturn(lex1, string);
    }

    int i = lex1.indexOf(lex2);

    if (i < 0) {
      return queryContext.getnvEmptyString();
    }

    i += lex2.length();

    String s = lex1.substring(i);

    return calcReturn(s, string);
  }

  public Node strLowerCase(Node string) {
    String lex = queryContext.dictionary().getLexicalValue(string);

    String lex2 = lex.toLowerCase();

    return calcReturn(lex2, string);
  }

  public Node strUpperCase(Node string) {
    String lex = queryContext.dictionary().getLexicalValue(string);

    String lex2 = lex.toUpperCase();

    return calcReturn(lex2, string);
  }

  public Node fnConcat(List<Node> args) {
    StringBuilder sb = new StringBuilder();

    for (Node arg : args) {
      String x = queryContext.dictionary().getLexicalValue(arg);

      sb.append(x);
    }
    return queryContext.dictionary().createStringNode(sb.toString());
  }

  // SPARQL CONCAT
  public Node strConcat(List<Node> args) {
    // Step 1 : Choose type.
    // One org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag -> that org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag
    String lang = null;

    boolean mixedLang = false;

    boolean xsdString = false;

    boolean simpleLiteral = false;

    StringBuilder sb = new StringBuilder();

    for (Node nv : args) {
      String lang1 = queryContext.dictionary().toLangTag(queryContext.dictionary().getLangTag(nv));

      if (!lang1.equals("")) {
        if (lang != null && !lang1.equals(lang)) {
          mixedLang = true;
        }

        lang = lang1;
      } else if (queryContext.dictionary().getNodeType(nv) != 0) {
        xsdString = true;
      } else {
        simpleLiteral = true;
      }

      sb.append(queryContext.dictionary().getLexicalValue(nv));
    }

    if (mixedLang) {
      return queryContext.dictionary().createStringNode(sb.toString());
    }

    // Must be all one org.rdf4led.rdf.org.rdf4led.sparql.parser.lang.
    if (lang != null) {
      if (!xsdString && !simpleLiteral) {
        return queryContext
            .dictionary()
            .createLangLiteral(sb.toString(), LangTag.langtag.getTag(lang));

      } else
      // Lang and one or more of xsd:string or simpleLiteral.
      {
        return queryContext.dictionary().createStringNode(sb.toString());
      }
    }

    if (simpleLiteral && xsdString) {
      return queryContext.dictionary().createStringNode(sb.toString());
    }
    // All xsdString

    if (xsdString) {
      return queryContext.dictionary().createStringNode(sb.toString());
    }

    if (simpleLiteral) {
      return queryContext.dictionary().createStringNode(sb.toString());
    }

    // No types - i.e. no arguments
    return queryContext.dictionary().createStringNode(sb.toString());
  }

  // public byte cla

  public boolean isNumericType(Byte xsdDatatype) {
    if (XSD.XSDfloat == (xsdDatatype)) return true;

    if (XSD.XSDdouble == (xsdDatatype)) return true;

    return isDecimalType(xsdDatatype);
  }

  public boolean isDecimalType(Byte xsdDatatype) {
    if (XSD.XSDdecimal == (xsdDatatype)) return true;

    return isIntegerType(xsdDatatype);
  }

  public boolean isIntegerType(Byte xsdDatatype) {
    return integerSubTypes.contains(xsdDatatype);
  }

  // --------------------------------
  // Comparisons operations
  // Do not confuse with sameValueAs/notSamevalueAs

  private int calcReturn(int x) {
    if (x < 0) return QueryContext.CMP_LESS;

    if (x > 0) return QueryContext.CMP_GREATER;

    return QueryContext.CMP_EQUAL;
  }

  public int compareNumeric(Node nv1, Node nv2) {
    String lex1 = queryContext.dictionary().getLexicalValue(nv1);

    String lex2 = queryContext.dictionary().getLexicalValue(nv2);

    byte opType = classifyNumeric(lex1, lex2);

    switch (opType) {
      case OP_INTEGER:
        {
          return calcReturn(BigInteger.valueOf(integer1).compareTo(BigInteger.valueOf(integer2)));
        }

      case OP_DECIMAL:
        {
          //				BigDecimal dec1 = new BigDecimal(lex1);
          //
          //				BigDecimal dec2 = new BigDecimal(lex2);

          return calcReturn(decimal1.compareTo(decimal2));
        }

      case OP_FLOAT:
        {
          return calcReturn(Float.compare(float1, float2));
        }

      case OP_DOUBLE:
        {
          return calcReturn(Double.compare(double1, double2));
        }

      default:
        throw new RuntimeException(
            "Unrecognized numeric operation : ( "
                + nv1
                + " : "
                + lex1
                + " ,"
                + nv2
                + " : "
                + lex2
                + " )");
    }
  }

  // public int compareDatetime(Node nv1, Node nv2)

  // --------------------------------
  // Functions on strings
  // http://www.w3.org/TR/xpath-functions/#d1e2222
  // http://www.w3.org/TR/xpath-functions/#substring.functions

  // String operations
  // stringCompare = fn:compare
  // fn:length
  // fn:string-concat
  // fn:substring
  // langMatch

  public int compareString(Node nv1, Node nv2) {
    // return calcReturn(nv1.getString().compareTo(nv2.getString())) ;
    return calcReturn(
        queryContext
            .dictionary()
            .getLexicalValue(nv1)
            .compareTo(queryContext.dictionary().getLexicalValue(nv2)));
  }

  // --------------------------------
  // Date/DateTime operations
  // http://www.w3.org/TR/xpath-functions/#comp.duration.datetime
  // dateTimeCompare
  // works for dates as well because they are implemented as dateTimes on
  // their start point.

  /**
   * Under strict F&O, dateTimes and dates with no timezone have one magically applied. This default
   * tiemzoine is implementation dependent and can lead to different answers to queries depending on
   * the timezone. Normally, ARQ uses XMLSchema dateTime comparions, which an yield "indeterminate",
   * which in turn is an evaluation error. F&O insists on true/false so can lead to false positves
   * and negatives.
   */
  public int compareDateTime(Node nv1, Node nv2) {
    return compareXSDDateTime(queryContext.getDateTime(nv1), queryContext.getDateTime(nv2));
  }

  public int compareDuration(Node nv1, Node nv2) {
    return compareDuration(queryContext.getDuration(nv1), queryContext.getDuration(nv2));
  }

  public int compareGYear(Node nv1, Node nv2) {
    return -99;
  }

  public int compareGYearMonth(Node nv1, Node nv2) {
    return -99;
  }

  public int compareGMonth(Node nv1, Node nv2) {
    return -99;
  }

  public int compareGMonthDay(Node nv1, Node nv2) {
    return -99;
  }

  public int compareGDay(Node nv1, Node nv2) {
    return -99;
  }

  public final String defaultTimezone = "Z";

  private int compareDateTimeFO(Node nv1, Node nv2) {
    XMLGregorianCalendar dt1 = queryContext.getDateTime(nv1);

    XMLGregorianCalendar dt2 = queryContext.getDateTime(nv2);

    int x = compareXSDDateTime(dt1, dt2);

    if (x == QueryContext.CMP_INDETERMINATE) {
      Node nv3 = fixupDateTime(nv1);

      if (nv3 != null) {
        XMLGregorianCalendar dt3 = queryContext.getDateTime(nv3);

        x = compareXSDDateTime(dt3, dt2);

        if (x == QueryContext.CMP_INDETERMINATE) {
          throw new RuntimeException("Still get indeterminate comparison");
        }

        return x;
      }

      nv3 = fixupDateTime(nv2);

      if (nv3 != null) {
        XMLGregorianCalendar dt3 = queryContext.getDateTime(nv3);

        x = compareXSDDateTime(dt1, dt3);

        if (x == QueryContext.CMP_INDETERMINATE) {
          throw new RuntimeException("Still get indeterminate comparison");
        }

        return x;
      }

      throw new RuntimeException("Failed to fixup dateTimes");
    }
    return x;
  }

  // XXX Remove??
  // This only differs by some "dateTime" => "date"
  private int compareDateFO(Node nv1, Node nv2) {
    XMLGregorianCalendar dt1 = queryContext.getDateTime(nv1);

    XMLGregorianCalendar dt2 = queryContext.getDateTime(nv2);

    int x = compareXSDDateTime(dt1, dt2); // Yes - compareDateTIme

    if (x == QueryContext.CMP_INDETERMINATE) {
      Node nv3 = fixupDate(nv1);

      if (nv3 != null) {
        XMLGregorianCalendar dt3 = queryContext.getDateTime(nv3);

        x = compareXSDDateTime(dt3, dt2);

        if (x == QueryContext.CMP_INDETERMINATE) {
          throw new RuntimeException("Still get indeterminate comparison");
        }

        return x;
      }

      nv3 = fixupDate(nv2);

      if (nv3 != null) {
        XMLGregorianCalendar dt3 = queryContext.getDateTime(nv3);

        x = compareXSDDateTime(dt1, dt3);

        if (x == QueryContext.CMP_INDETERMINATE) {
          throw new RuntimeException("Still get indeterminate comparison");
        }

        return x;
      }

      throw new RuntimeException("Failed to fixup dateTimes");
    }
    return x;
  }

  private Node fixupDateTime(Node nv) {
    byte nodeType = queryContext.dictionary().getNodeType(nv);

    DateTimeStruct dts =
        DateTimeStruct.parseDateTime(queryContext.dictionary().getLexicalValue(nv));

    if (dts.timezone != null) {
      return null;
    }

    dts.timezone = defaultTimezone;

    nv = queryContext.dictionary().createTypedLiteral(dts.toString(), XSD.XSDdateTime);

    if (!(nodeType == XSD.XSDdateTime)) {
      throw new RuntimeException("Failed to reform an xsd:dateTime");
    }

    return nv;
  }

  private Node fixupDate(Node nv) {
    byte nt = queryContext.dictionary().getNodeType(nv);

    String lex = queryContext.dictionary().getLexicalValue(nv);

    DateTimeStruct dts = DateTimeStruct.parseDate(lex);

    if (dts.timezone != null) {
      return null;
    }

    dts.timezone = defaultTimezone;

    nv = queryContext.dictionary().createDateNode(dts.toString());

    if (nt != XSD.XSDdate) {
      throw new RuntimeException("Failed to reform an xsd:date");
    }

    return nv;
  }

  private int compareXSDDateTime(XMLGregorianCalendar dt1, XMLGregorianCalendar dt2) {
    // Returns codes are -1/0/1 but also 2 for "Indeterminate"
    // which occurs when one has a timezone and one does not
    // and they are less then 14 hours apart.

    // F&O has an "implicit timezone" - this code implements the XMLSchema
    // compare algorithm.

    int x = dt1.compare(dt2);

    return convertComparison(x);
  }

  private int compareDuration(Duration duration1, Duration duration2) {
    // Returns codes are -1/0/1 but also 2 for "Indeterminate"
    // Not fully sure when Indeterminate is returned with regards to a
    // duration

    int x = duration1.compare(duration2);

    return convertComparison(x);
  }

  private int convertComparison(int x) {
    if (x == DatatypeConstants.EQUAL) {
      return QueryContext.CMP_EQUAL;
    }

    if (x == DatatypeConstants.LESSER) {
      return QueryContext.CMP_LESS;
    }

    if (x == DatatypeConstants.GREATER) {
      return QueryContext.CMP_GREATER;
    }

    if (x == DatatypeConstants.INDETERMINATE) {
      return QueryContext.CMP_INDETERMINATE;
    }

    throw new RuntimeException("Unexpected return from XSDDuration.compare: " + x);
  }

  // --------------------------------
  // Boolean operations

  /*
   * Logical OR and AND is special with respect to handling errors truth
   * table. AND they take effective boolean values, not boolean
   *
   * A B | NOT A A && B A || B ------------------------------------- E E | E E
   * E E T | E E T E F | E F E T E | F E T T T | F T T T F | F F T F E | T F E
   * F T | T F T F F | T F F
   */

  // Not possible because of error masking.
  // public Node logicalOr(Node x,Node y)
  // public Node logicalAnd(Node x,Node y)

  public int compareBoolean(Node nv1, Node nv2) {
    boolean b1 = (nv1 == queryContext.getnvTrue());

    boolean b2 = (nv2 == queryContext.getnvTrue());

    if (b1 == b2) return QueryContext.CMP_EQUAL;

    if (!b1 && b2) return QueryContext.CMP_LESS;
    if (b1 && !b2) return QueryContext.CMP_GREATER;

    throw new RuntimeException("Weird boolean comparison: " + nv1 + ", " + nv2);
  }

  // public boolean dateTimeCastCompatible(Node nv, byte xsd)
  // {
  // return nv.hasDateTime() ;
  // }

  /**
   * Cast aNode to a date/time type (xsd dateTime, date, time, g*) according to F&O <a
   * href="http://www.w3.org/TR/xpath-functions/#casting-to-datetimes">17.1.5 Casting to date and
   * time types</a> Throws an exception on incorrect case.
   */
  public Node dateTimeCast(Node nv, String typeURI) {
    byte t = queryContext.dictionary().getXSDType(typeURI);

    return dateTimeCast(nv, t);
  }

  /**
   * Cast aNode to a date/time type (xsd dateTime, date, time, g*) according to F&O <a
   * href="http://www.w3.org/TR/xpath-functions/#casting-to-datetimes">17.1.5 Casting to date and
   * time types</a> Throws an exception on incorrect case.
   *
   * @throws ExprEvalTypeException
   */

  // public Node dateTimeCast(Node nv, byte rdfDatatype)
  // {
  // if ( ! ( rdfDatatype instanceof XSDDatatype ) )
  // throw new ExprEvalTypeException("Can't cast to XSDDatatype: "+nv) ;
  //
  // XSDDatatype xsd = (XSDDatatype)rdfDatatype ;
  //
  // if (rdfDatatype > XSD.XMLLiteral)
  // {
  // throw new RuntimeException();
  // }
  //
  // return dateTimeCast(nv, rdfDatatype) ;
  // }

  /**
   * Cast aNode to a date/time type (xsd dateTime, date, time, g*) according to F&O <a
   * href="http://www.w3.org/TR/xpath-functions/#casting-to-datetimes">17.1.5 Casting to date and
   * time types</a> Throws an exception on incorrect case.
   */
  private boolean hasDateTime(Node nv) {
    byte nodeType = queryContext.dictionary().getNodeType(nv);
    return (nodeType == XSD.XSDdateTime)
        || (nodeType == XSD.XSDdate)
        || (nodeType == XSD.XSDtime)
        || (nodeType == XSD.XSDgYear)
        || (nodeType == XSD.XSDgYearMonth)
        || (nodeType == XSD.XSDgMonth)
        || (nodeType == XSD.XSDgMonthDay)
        || (nodeType == XSD.XSDgDay);
  }

  public Node dateTimeCast(Node nv, byte xsd) {
    if (xsd > XSD.XMLLiteral) {
      throw new RuntimeException();
    }

    byte nodeType = queryContext.dictionary().getNodeType(nv);

    // http://www.w3.org/TR/xpath-functions/#casting-to-datetimes
    if (!hasDateTime(nv)) throw new RuntimeException("Not a date/time type: " + nv);

    // XMLGregorianCalendar xsdDT = nv.getDateTime() ;
    XMLGregorianCalendar xsdDT = queryContext.getDateTime(nv);

    if (XSD.XSDdateTime == (xsd)) {
      // ==> DateTime
      if (nodeType == XSD.XSDdateTime) {
        return nv;
      }

      if (nodeType != XSD.XSDdate) {
        throw new RuntimeException("Can't cast to XSD:dateTime: " + nv);
      }

      // DateTime with time 00:00:00

      String x =
          String.format(
              "%04d-%02d-%02dT00:00:00", xsdDT.getYear(), xsdDT.getMonth(), xsdDT.getDay());

      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDdate == (xsd)) {
      // ==> Date
      if (nodeType == XSD.XSDdate) return nv;
      if (!(nodeType == XSD.XSDdateTime))
        throw new RuntimeException("Can't cast to XSD:date: " + nv);
      String x = String.format("%04d-%02d-%02d", xsdDT.getYear(), xsdDT.getMonth(), xsdDT.getDay());

      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDtime == (xsd)) {
      // ==> time
      if (nodeType == XSD.XSDtime) return nv;
      if (!(nodeType == XSD.XSDdateTime))
        throw new RuntimeException("Can't cast to XSD:time: " + nv);
      // Careful foratting
      DecimalFormat nf = new DecimalFormat("00.####");
      nf.setDecimalSeparatorAlwaysShown(false);
      String x = nf.format(xsdDT.getSecond());
      x = String.format("%02d:%02d:%s", xsdDT.getHour(), xsdDT.getMinute(), x);
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDgYear == (xsd)) {
      // ==> Year
      if ((nodeType == XSD.XSDgYear)) return nv;
      if (!(nodeType == XSD.XSDdateTime) && !(nodeType == XSD.XSDdate))
        throw new RuntimeException("Can't cast to XSD:gYear: " + nv);
      String x = String.format("%04d", xsdDT.getYear());
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDgYearMonth == (xsd)) {
      // ==> YearMonth
      if (nodeType == XSD.XSDgYearMonth) return nv;
      if (!(nodeType == XSD.XSDdateTime) && !(nodeType == XSD.XSDdate))
        throw new RuntimeException("Can't cast to XSD:gYearMonth: " + nv);
      String x = String.format("%04d-%02d", xsdDT.getYear(), xsdDT.getMonth());
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDgMonth == (xsd)) {
      // ==> Month
      if (nodeType == XSD.XSDgMonth) return nv;
      if (!(nodeType == XSD.XSDdateTime) && !(nodeType == XSD.XSDdate))
        throw new RuntimeException("Can't cast to XSD:gMonth: " + nv);
      String x = String.format("--%02d", xsdDT.getMonth());
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDgMonthDay == (xsd)) {
      // ==> MonthDay
      if (nodeType == XSD.XSDgMonthDay) return nv;
      if (!(nodeType == XSD.XSDdateTime) && !(nodeType == XSD.XSDdate))
        throw new RuntimeException("Can't cast to XSD:gMonthDay: " + nv);
      String x = String.format("--%02d-%02d", xsdDT.getMonth(), xsdDT.getDay());
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    if (XSD.XSDgDay == (xsd)) {
      // Day
      if ((nodeType == XSD.XSDgDay)) return nv;
      if (!(nodeType == XSD.XSDdateTime) && !(nodeType == XSD.XSDdate))
        throw new RuntimeException("Can't cast to XSD:gDay: " + nv);
      String x = String.format("---%02d", xsdDT.getDay());
      return queryContext.dictionary().createTypedLiteral(x, xsd);
    }

    throw new RuntimeException("Can't case to <" + xsd + ">: " + nv);
  }

  public Node dtGetYear(Node nv) {
    // if ( nv.isDateTime() || nv.isDate() || (nodeType == XSD.XSDgYear) ||
    // nv.isGYearMonth() )

    byte nodeType = queryContext.dictionary().getNodeType(nv);

    if ((nodeType == XSD.XSDdateTime)
        || (nodeType == XSD.XSDdate)
        || (nodeType == XSD.XSDgYearMonth)
        || (nodeType == XSD.XSDgMonth)) {
      DateTimeStruct dts = parseAnyDT(nv);
      return queryContext.dictionary().createTypedLiteral(dts.year, XSD.XSDint);
    }
    // else if (nv.isGMonth() )
    // dts = DateTimeStruct.parseGMonth(lex) ;
    // else if (nv.isGMonthDay() )
    // dts = DateTimeStruct.parseGMonthDay(lex) ;
    // else if ((nodeType == XSD.XSDgDay))
    // dts = DateTimeStruct.parseGDay(lex) ;

    throw new RuntimeException("Not a year datatype");
  }

  public Node dtGetMonth(Node nv) {
    byte nodeType = queryContext.dictionary().getNodeType(nv);

    if ((nodeType == XSD.XSDdateTime)
        || (nodeType == XSD.XSDdate)
        || (nodeType == XSD.XSDgYearMonth)
        || (nodeType == XSD.XSDgMonth)
        || (nodeType == XSD.XSDgMonthDay)) {
      DateTimeStruct dts = parseAnyDT(nv);
      return queryContext.dictionary().createTypedLiteral(dts.month, XSD.XSDint);
      // return NodeValue.makeNode(dts.month, XSDDatatype.XSDinteger) ;
    }
    throw new RuntimeException("Not a month datatype");
  }

  public Node dtGetDay(Node nv) {
    byte nodeType = queryContext.dictionary().getNodeType(nv);

    if ((nodeType == XSD.XSDdateTime)
        || (nodeType == XSD.XSDdate)
        || (nodeType == XSD.XSDgMonthDay)
        || (nodeType == XSD.XSDgDay)) {
      DateTimeStruct dts = parseAnyDT(nv);

      return queryContext.dictionary().createTypedLiteral(dts.day, XSD.XSDint);
    }
    throw new RuntimeException("Not a month datatype");
  }

  private DateTimeStruct parseAnyDT(Node nv) {

    String lex = queryContext.dictionary().getLexicalValue(nv);

    byte nodeType = queryContext.dictionary().getNodeType(nv);

    if (nodeType == XSD.XSDdateTime) {
      return DateTimeStruct.parseDateTime(lex);
    }

    if (nodeType == XSD.XSDdate) {
      return DateTimeStruct.parseDate(lex);
    }

    if (nodeType == XSD.XSDgYear) {
      return DateTimeStruct.parseGYear(lex);
    }

    if (nodeType == XSD.XSDgYearMonth) {
      return DateTimeStruct.parseGYearMonth(lex);
    }

    if (nodeType == XSD.XSDgMonth) {
      return DateTimeStruct.parseGMonth(lex);
    }

    if (nodeType == XSD.XSDgMonthDay) {
      return DateTimeStruct.parseGMonthDay(lex);
    }

    if (nodeType == XSD.XSDgDay) {
      return DateTimeStruct.parseGDay(lex);
    }

    if (nodeType == XSD.XSDtime) {
      return DateTimeStruct.parseTime(lex);
    }

    return null;
  }

  private DateTimeStruct parseTime(Node nv) {
    // String lex = nv.getNode().getLiteralLexicalForm() ;
    //
    // if ( (nodeType == XSD.XSDdateTime) )
    // return DateTimeStruct.parseDateTime(lex) ;
    // else if ( nv.isTime() )
    // return DateTimeStruct.parseTime(lex) ;
    // else
    // throw new ExprEvalException("Not a datatype for time") ;

    String lex = queryContext.dictionary().getLexicalValue(nv);

    byte nodeType = queryContext.dictionary().getNodeType(nv);

    if (nodeType == XSD.XSDdateTime) {
      return DateTimeStruct.parseDateTime(lex);
    } else if (nodeType == XSD.XSDtime) {
      return DateTimeStruct.parseTime(lex);
    } else throw new RuntimeException("Not a datatype for time");
  }

  public Node dtGetHours(Node nv) {
    DateTimeStruct dts = parseTime(nv);

    return queryContext.dictionary().createTypedLiteral(dts.hour, XSD.XSDint);
  }

  public Node dtGetMinutes(Node nv) {
    DateTimeStruct dts = parseTime(nv);
    return queryContext.dictionary().createTypedLiteral(dts.minute, XSD.XSDint);
  }

  public Node dtGetSeconds(Node nv) {
    DateTimeStruct dts = parseTime(nv);

    return queryContext.dictionary().createTypedLiteral(dts.second, XSD.XSDdecimal);
  }

  public Node dtGetTZ(Node nv) {
    DateTimeStruct dts = parseAnyDT(nv);

    if (dts == null) {
      throw new RuntimeException("Not a data/time value: " + nv);
    }
    if (dts.timezone == null) {
      return queryContext.getnvEmptyString();
    }

    return queryContext.dictionary().createStringNode(dts.timezone);
    // return NodeValue.makeString(dts.timezone) ;
  }

  public Node dtGetTimezone(Node nv) {
    DateTimeStruct dts = parseAnyDT(nv);

    if (dts == null || dts.timezone == null) {
      throw new RuntimeException("Not a datatype with a timezone: " + nv);
    }

    if ("".equals(dts.timezone)) return null;
    if ("Z".equals(dts.timezone)) {
      Node n =
          queryContext
              .dictionary()
              .createTypedLiteral(
                  "PT0S", XSD.XSDduration); // Node.getType(XSDDatatype.XSD+"#dayTimeDuration"))
      // ;
      return n;
    }
    if ("+00:00".equals(dts.timezone)) {
      Node n =
          queryContext
              .dictionary()
              .createTypedLiteral(
                  "PT0S", XSD.XSDduration); // Node.getType(XSDDatatype.XSD+"#dayTimeDuration"))
      // ;
      return n;
    }
    if ("-00:00".equals(dts.timezone)) {
      Node n =
          queryContext
              .dictionary()
              .createTypedLiteral(
                  "-PT0S", XSD.XSDduration); // Node.getType(XSDDatatype.XSD+"#dayTimeDuration"))
      // ;
      return n;
    }

    String s = dts.timezone;

    int idx = 0;

    StringBuilder sb = new StringBuilder();

    if (s.charAt(0) == '-') {
      sb.append('-');
    }

    idx++; // Skip '-' or '+'

    sb.append("PT");

    digitsTwo(s, idx, sb, 'H');

    idx += 2;

    idx++; // The ":"

    digitsTwo(s, idx, sb, 'M');

    idx += 2;

    // return NodeValue.makeNode(sb.toString(), null,
    // XSDDatatype.XSD+"#dayTimeDuration") ;
    return queryContext.dictionary().createTypedLiteral(sb.toString(), XSD.XSDduration);
  }

  private void digitsTwo(String s, int idx, StringBuilder sb, char indicator) {
    if (s.charAt(idx) == '0') {
      idx++;
      if (s.charAt(idx) != '0') {
        sb.append(s.charAt(idx));
        sb.append(indicator);
      }
      idx++;
    } else {
      sb.append(s.charAt(idx));
      idx++;
      sb.append(s.charAt(idx));
      idx++;
      sb.append(indicator);
    }
  }

  public boolean isYearMonth(Duration dur) {
    // Not dur.getXMLSchemaType()
    return (dur.isSet(YEARS) || dur.isSet(MONTHS))
        && !dur.isSet(DAYS)
        && !dur.isSet(HOURS)
        && !dur.isSet(MINUTES)
        && !dur.isSet(SECONDS);
  }

  public boolean isDayTime(Duration dur) {
    return !dur.isSet(YEARS)
        && !dur.isSet(MONTHS)
        && (dur.isSet(DAYS) || dur.isSet(HOURS) || dur.isSet(MINUTES) || dur.isSet(SECONDS));
  }

  private static enum valueType {
    VSPACE_NODE,
    VSPACE_NUM,
    VSPACE_DATETIME,
    VSPACE_DATE,
    VSPACE_TIME,
    VSPACE_DURATION,

    // Collapse to VSPACE_DATETIME?
    VSPACE_G_YEAR,
    VSPACE_G_YEARMONTH,
    VSPACE_G_MONTHDAY,
    VSPACE_G_MONTH,
    VSPACE_G_DAY,

    VSPACE_STRING,
    VSPACE_LANG,
    VSPACE_BOOLEAN,
    VSPACE_UNKNOWN,
    VSPACE_DIFFERENT
  };

  public int compare(Node nv1, Node nv2) {
    boolean sortOrderingCompare = false;

    valueType compType = getValueType(nv1, nv2);

    switch (compType) {
      case VSPACE_DATETIME:
      case VSPACE_DATE:
      case VSPACE_TIME:
      case VSPACE_G_DAY:
      case VSPACE_G_MONTH:
      case VSPACE_G_MONTHDAY:
      case VSPACE_G_YEAR:
      case VSPACE_G_YEARMONTH:
        {
          int x = compareDateTime(nv1, nv2);

          if (x != QueryContext.CMP_INDETERMINATE) return x;
          // Indeterminate => can't compare as strict values.
          compType = valueType.VSPACE_DIFFERENT;
          break;
        }
      case VSPACE_DURATION:
        {
          int x = compareDuration(nv1, nv2);

          if (x != QueryContext.CMP_INDETERMINATE) return x;
          // Indeterminate => can't compare as strict values.
          compType = valueType.VSPACE_DIFFERENT;
          break;
        }

        // default:
      case VSPACE_BOOLEAN:
      case VSPACE_DIFFERENT:
      case VSPACE_LANG:
      case VSPACE_NODE:
      case VSPACE_NUM:
      case VSPACE_STRING:
      case VSPACE_UNKNOWN:
        // Drop through.
    }

    switch (compType) {
      case VSPACE_DATETIME:
      case VSPACE_DATE:
      case VSPACE_TIME:
      case VSPACE_G_DAY:
      case VSPACE_G_MONTH:
      case VSPACE_G_MONTHDAY:
      case VSPACE_G_YEAR:
      case VSPACE_G_YEARMONTH:
      case VSPACE_DURATION:
        throw new RuntimeException("Still seeing date/dateTime/time/duration compare type");

      case VSPACE_NUM:
        return compareNumeric(nv1, nv2);

      case VSPACE_STRING:
        {
          int cmp = compareString(nv1, nv2);

          // Split plain literals and xsd:strings for sorting purposes.
          if (!false) return cmp;

          if (cmp != QueryContext.CMP_EQUAL) return cmp;

          // Same by string value.
          // String dt1 = nv1.asNode().getLiteralDatatypeURI() ;
          //
          // String dt2 = nv2.asNode().getLiteralDatatypeURI() ;
          //
          // if ( dt1 == null && dt2 != null )
          // return Expr.CMP_LESS ;
          //
          // if ( dt2 == null && dt1 != null )
          // return Expr.CMP_GREATER ;
          // return Expr.CMP_EQUAL; // Both plain or both xsd:string.
        }
      case VSPACE_BOOLEAN:
        return compareBoolean(nv1, nv2);

      case VSPACE_LANG:
        {
          // Two literals, both with language tags.
          Node node1 = nv1;

          Node node2 = nv2;

          if (node1 == node2) return QueryContext.CMP_EQUAL;

          String lex_1 = queryContext.dictionary().getLexicalValue(node1);

          String lex_2 = queryContext.dictionary().getLexicalValue(node2);

          int x = lex_1.compareTo(lex_2);

          if (x > 0) return QueryContext.CMP_GREATER;

          if (x < 0) return QueryContext.CMP_LESS;

          if (x == 0) throw new RuntimeException(" Same node but different encoded value");

          //				int x = StrUtils.strCompareIgnoreCase(node1.getLiteralLanguage(),
          // node2.getLiteralLanguage());
          //
          //				if (x != Expr.CMP_EQUAL)
          //				{
          //					// Different org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tags
          //					if (!sortOrderingCompare)
          //					{
          //						throw new RuntimeException("Can't compare (different languages) " + nv1 + " and "
          // + nv2);
          //					}
          //					// Different org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tags - sorting
          //					return x;
          //				}
          //
          //				// same org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag (case insensitive)
          //				x = StrUtils.strCompare(node1.getLiteralLexicalForm(),
          // node2.getLiteralLexicalForm());
          //				if (x != Expr.CMP_EQUAL)
          //					return x;
          //				// Same lexcial forms, same org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag by value
          //				// Try to split by syntactic org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tags.
          //				x = StrUtils.strCompare(node1.getLiteralLanguage(), node2.getLiteralLanguage());
          //				// Maybe they are the same after all!
          //				// Should be node.equals by now.
          //				if (x == Expr.CMP_EQUAL && !NodeFunctions.sameTerm(node1, node2))
          //				{
          //					throw new RuntimeException("Look the same (org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tags) but no node equals");
          //				}
          //
          //				return x;
        }

      case VSPACE_NODE:
        // Two non-literals don't compare except for sorting.
        if (sortOrderingCompare) {
          // return NodeUtils.compareRDFTerms(nv1.asNode(), nv2.asNode());
        } else {
          throw new RuntimeException("NodeValue.raise returned");
        }

      case VSPACE_UNKNOWN:
        {
          // One or two unknown value spaces.
          System.out.println(queryContext.dictionary().getLexicalValue(nv1));

          System.out.println(queryContext.dictionary().getLexicalValue(nv2));

          // Two unknown literals can be equal.
          if (nv1 == nv2) {
            return QueryContext.CMP_EQUAL;
          }

          if (sortOrderingCompare) {
            // return NodeUtils.compareRDFTerms(node1, node2);
          }

          throw new RuntimeException("Unkown type");
        }

      case VSPACE_DIFFERENT:
        // Two literals, from different known value spaces
        if (sortOrderingCompare) {
          // return NodeUtils.compareRDFTerms(nv1.asNode(), nv2.asNode());
        }

        System.out.println(queryContext.dictionary().getLexicalValue(nv1));
        System.out.println(queryContext.dictionary().getLexicalValue(nv2));
        throw new RuntimeException(
            "NodeValue.raise returned " + "Compare failure " + nv1 + " and " + nv2);
    }
    throw new RuntimeException("Compare failure " + nv1 + " and " + nv2);
  }

  private valueType getValueType(Node nv1, Node nv2) {
    valueType vt1 = classifyValueSpace(nv1);

    valueType vt2 = classifyValueSpace(nv2);

    if (vt1 == vt2) return vt1;

    if (vt1 == valueType.VSPACE_UNKNOWN || vt2 == valueType.VSPACE_UNKNOWN)
      return valueType.VSPACE_UNKNOWN;

    // Known values spaces but incompatible
    return valueType.VSPACE_DIFFERENT;
  }

  private static Set<Byte> isNumber = new HashSet<>();

  static {
    isNumber.add(XSD.XSDfloat);
    isNumber.add(XSD.XSDdouble);
    isNumber.add(XSD.XSDint);
    isNumber.add(XSD.XSDlong);
    isNumber.add(XSD.XSDshort);
    isNumber.add(XSD.XSDbyte);
    isNumber.add(XSD.XSDunsignedByte);
    isNumber.add(XSD.XSDunsignedShort);
    isNumber.add(XSD.XSDunsignedInt);
    isNumber.add(XSD.XSDunsignedLong);
    isNumber.add(XSD.XSDdecimal);
    isNumber.add(XSD.XSDinteger);
    isNumber.add(XSD.XSDnonPositiveInteger);
    isNumber.add(XSD.XSDnonNegativeInteger);
    isNumber.add(XSD.XSDpositiveInteger);
    isNumber.add(XSD.XSDnegativeInteger);
  }

  private static Set<Byte> integerSubTypes = new HashSet<>();

  static {
    integerSubTypes.add(XSD.XSDint);
    integerSubTypes.add(XSD.XSDlong);
    integerSubTypes.add(XSD.XSDshort);
    integerSubTypes.add(XSD.XSDbyte);
    integerSubTypes.add(XSD.XSDunsignedByte);
    integerSubTypes.add(XSD.XSDunsignedShort);
    integerSubTypes.add(XSD.XSDunsignedInt);
    integerSubTypes.add(XSD.XSDunsignedLong);
    integerSubTypes.add(XSD.XSDinteger);
    integerSubTypes.add(XSD.XSDdecimal);
    integerSubTypes.add(XSD.XSDnonPositiveInteger);
    integerSubTypes.add(XSD.XSDnonNegativeInteger);
    integerSubTypes.add(XSD.XSDpositiveInteger);
    integerSubTypes.add(XSD.XSDnegativeInteger);
  }

  private valueType classifyValueSpace(Node nv) {
    Byte nodeType = queryContext.dictionary().getNodeType(nv);

    if (isNumber.contains(nodeType)) return valueType.VSPACE_NUM;

    if (nodeType == XSD.XSDdateTime) return valueType.VSPACE_DATETIME;

    if (nodeType == XSD.XSDdate) return valueType.VSPACE_DATE;

    if (nodeType == XSD.XSDtime) return valueType.VSPACE_TIME;

    if (nodeType == XSD.XSDduration) return valueType.VSPACE_DURATION;

    if (nodeType == XSD.XSDgYear) return valueType.VSPACE_G_YEAR;

    if (nodeType == XSD.XSDgYearMonth) return valueType.VSPACE_G_YEARMONTH;

    if (nodeType == XSD.XSDgMonth) return valueType.VSPACE_G_MONTH;

    if (nodeType == XSD.XSDgMonthDay) return valueType.VSPACE_G_MONTHDAY;

    if (nodeType == XSD.XSDgDay) return valueType.VSPACE_G_DAY;

    if (nodeType == XSD.XSDstring) return valueType.VSPACE_STRING;

    if (nodeType == XSD.XSDboolean) return valueType.VSPACE_BOOLEAN;

    if (nodeType == RDFNodeType.LITERALHASLANG) return valueType.VSPACE_LANG;

    if ((nodeType == RDFNodeType.LITERAL) || (nodeType == RDFNodeType.LITERALHASLANG))
      return valueType.VSPACE_NODE;

    return valueType.VSPACE_UNKNOWN;
  }
}
