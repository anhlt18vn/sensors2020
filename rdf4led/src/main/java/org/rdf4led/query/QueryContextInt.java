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

package org.rdf4led.query;

import org.rdf4led.common.mapping.Mapping;
import org.rdf4led.graph.Quad;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.expr.*;
import org.rdf4led.rdf.dictionary.RDFDictionaryAbstract;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;
import org.rdf4led.rdf.dictionary.vocab.XSD;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.RegexEngine;
import org.rdf4led.query.sparql.RegexJava;
import org.rdf4led.query.expr.function.ExprVisitor;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * TODO: ExecutionContextInt
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 30 Jan 2015
 */
public class QueryContextInt implements QueryContext<Integer> {
  protected static String pathOfDictionary;

  protected static String pathOfStorage;

  protected static HashMap<Integer, String> varIntStr;

  protected static HashMap<String, Integer> varStrInt;

  protected static HashMap<String, String> prefixMapping;

  protected RDFDictionaryAbstract<Integer> dictionary;

  public QueryContextInt(RDFDictionaryAbstract<Integer> dictionary) {
    varIntStr = new HashMap<Integer, String>();
    //
    varStrInt = new HashMap<String, Integer>();

    this.dictionary = dictionary;
    //
    prefixMapping = new HashMap<String, String>();
  }

  @Override
  public void setPrefix(String prefix, String uri) {
    prefixMapping.put(prefix, uri);
  }

  @Override
  public String expandPrefix(String qname) {
    int colon = qname.indexOf(':');

    if (colon < 0) {
      return null;
    } else {
      String prefix = qname.substring(0, colon);

      String uri = prefixMapping.get(prefix);

      if (uri == null) {
        return null;
      }

      return uri + qname.substring(colon + 1);
    }
  }

  @Override
  public RDFDictionaryAbstract<Integer> dictionary() {
    return dictionary;
  }

  @Override
  public Integer strDatatype(Integer nv1, Integer nv2) {
    return null;
  }

  @Override
  public Integer getDataTypeAsNode(Integer integer) {
    return null;
  }

  @Override
  public Integer getLangTagAsNode(Integer integer) {
    return null;
  }

  @Override
  public Integer getnvTrue() {
    return dictionary.getXSDTrue();
  }

  @Override
  public Integer getnvFalse() {
    return dictionary.getXSDTrue();
  }

  @Override
  public Integer getnvVersion() {
    return null;
  }

  private static int nvNothing = -1;

  @Override
  public Integer getnvNothing() {
    if (nvNothing != -1) {
      return nvNothing;
    }

    nvNothing =
        dictionary.getRDFnil(); // createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");

    return nvNothing;
  }

  @Override
  public Integer getnvZero() {
    return dictionary.createIntegerNode(BigInteger.valueOf(0));
  }

  @Override
  public Integer getnvEmptyString() {
    return null;
  }

  // ======================================================================================================================//
  // VAR
  @Override
  public Integer allocVarNode(Integer node) {
    return node;
  }

  @Override
  public synchronized Integer allocVar(String varName) {
    int varId;

    if (varStrInt.containsKey(varName)) {
      varId = varStrInt.get(varName);
    } else {
      varId = varStrInt.size() + 1;

      varIntStr.put(varId, varName);

      varStrInt.put(varName, varId);
    }

    int nodeVar = dictionary.encodeVar(varId);

    return nodeVar;
  }

  @Override
  public Integer allocInternVar() {
    return allocVar("." + varStrInt.size());
  }

  @Override
  public boolean isVarNode(Integer node) {
    byte nodeType = dictionary.getNodeType(node);

    return (nodeType == RDFNodeType.VARIABLE);
  }

  @Override
  public boolean isAnonVar(Integer integer) {
    return false;
  }

  @Override
  public Integer checkAndGetStringLiteral(String s, Integer nvString) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getVarName(Integer varNode) {
    int stringId = dictionary.decodeVar(varNode);

    return varIntStr.get(stringId);
  }

  @Override
  public Quad<Integer> createQuad(Integer graphNode, Triple<Integer> triple) {
    return null;
  }

  @Override
  public boolean isDefaultGraph(Integer graphNode) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isDefaultGraphExplicit(Integer graphNode) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isUnionGraph(Integer graphNode) {
    // TODO Auto-generated method stub
    return false;
  }

  static XSDFuncOp<Integer> xsdFuncOp;

  @Override
  public XSDFuncOp<Integer> getXSDFuncOp() {
    if (xsdFuncOp != null) {
      return xsdFuncOp;
    } else {
      xsdFuncOp = new XSDFuncOp<Integer>(this);
    }

    return xsdFuncOp;
  }

  static DatatypeFactory datatypeFactory = null;

  @Override
  public XMLGregorianCalendar getDateTime(Integer nodeValue) {
    XMLGregorianCalendar dateTime = null;

    if (datatypeFactory == null) {
      try {
        datatypeFactory = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException ex) {
        throw new RuntimeException("can't create a javax.xml dataype factory");
      }
    }

    String lex = dictionary.getLexicalValue(nodeValue);

    byte nodeType = dictionary.getNodeType(nodeValue);

    if (nodeType == XSD.XSDgMonth) {
      if (lex.endsWith("Z")) {
        lex = lex.substring(0, lex.length() - 1);

        dateTime = datatypeFactory.newXMLGregorianCalendar();

        dateTime.setTimezone(0);

        return dateTime;
      }
    } else if (nodeType == XSD.XSDdateTime) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
      try {
        Date dob = df.parse(lex);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dob);
        dateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
      } catch (ParseException | DatatypeConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else dateTime = datatypeFactory.newXMLGregorianCalendar();

    return dateTime;
  }

  @Override
  public Duration getDuration(Integer nodeValue) {
    if (datatypeFactory == null) {
      try {
        datatypeFactory = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException ex) {
        throw new RuntimeException("can't create a javax.xml dataype factory");
      }
    }

    String lex = dictionary.getLexicalForm(nodeValue);

    return datatypeFactory.newDuration(lex);
  }

  static ExprWalker<Integer> exprWalker;

  public ExprWalker<Integer> getExprWalker() {
    if (exprWalker == null) {
      exprWalker = new ExprWalker<Integer>();
    }

    return exprWalker;
  }

  static HashMap<Integer, Expr<Integer>> exprNodeStore = null;

  @Override
  public Expr<Integer> nodeToExpr(Integer node) {
    if (isVarNode(node)) {
      return new ExprVar<Integer>(node, this);

    } else {
      if (exprNodeStore == null) {
        exprNodeStore = new HashMap<Integer, Expr<Integer>>();
      }

      Expr<Integer> expr;

      expr = exprNodeStore.get(node);

      if (expr != null) return expr;

      final Integer n = node;

      expr =
          new ExprNode<Integer>(this) {

            @Override
            public Expr<Integer> applyNodeTransform(NodeTransform<Integer> transform) {
              Integer newNode = transform.convert(n);

              return queryContext.nodeToExpr(newNode);
            }

            @Override
            public void visit(ExprVisitor<Integer> visitor) {
              visitor.visit(n);
            }

            @Override
            public Integer eval(Mapping<Integer> mapping) {
              return n;
            }

            @Override
            public Integer getConstant() {
              return n;
            }

            @Override
            public boolean equals(Object other) {
              throw new RuntimeException("equals");
            }
          };

      exprNodeStore.put(n, expr);

      return expr;
    }
  }

  @Override
  public RegexEngine makeRegexEngine(Expr<Integer> pattern, Expr<Integer> flags) {
    String patternString, flagsString;

    if (pattern == null) {
      throw new RuntimeException("pattern is null");

    } else {
      Integer con = pattern.getConstant();

      if (con == null) {
        throw new RuntimeException("pattern is not a node value");
      }

      patternString = dictionary.getLexicalValue(con);
    }

    if (flags == null) {
      flagsString = null;

    } else {
      Integer con = flags.getConstant();

      if (con == null) {
        throw new RuntimeException("pattern is not a node value");
      }

      flagsString = dictionary.getLexicalValue(con);
    }

    return new RegexJava(patternString, flagsString);
  }

  @Override
  public RegexEngine makeRegexEngine(Integer pattern, Integer flags) {
    String patternString, flagsString;

    if (pattern == null) {
      throw new RuntimeException("pattern is null");

    } else {
      patternString = dictionary.getLexicalValue(pattern);
    }

    if (flags == null) {
      flagsString = null;

    } else {

      flagsString = dictionary.getLexicalValue(flags);
    }

    return new RegexJava(patternString, flagsString);
  }

  @Override
  public int copmpareNodeasValue(Integer nv1, Integer nv2) {
    return 0;
  }

  @Override
  public int compareRDFTerms(Integer n1, Integer n2) {
    return 0;
  }
}
