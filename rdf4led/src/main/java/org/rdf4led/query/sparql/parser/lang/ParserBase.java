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

package org.rdf4led.query.sparql.parser.lang;

import org.rdf4led.query.expr.aggs.*;
import org.rdf4led.rdf.dictionary.vocab.LangTag;
import org.rdf4led.rdf.dictionary.vocab.XSD;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.path.Path;
import org.rdf4led.query.path.TriplePath;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.QueryParseException;
import org.rdf4led.query.expr.E_Exists;
import org.rdf4led.query.expr.E_NotExists;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.sparql.syntax.Element;
import org.rdf4led.query.sparql.syntax.ElementGroup;
import org.rdf4led.query.sparql.syntax.TripleCollector;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class ParserBase<Node> {

  protected QueryContext<Node> queryContext;

  protected final Node XSD_TRUE; // = queryContext.org.rdf4led.rdf.dictionary().getXSD_TRUE();

  protected final Node XSD_FALSE; // = queryContext.org.rdf4led.rdf.dictionary().getXSD_FALSE();

  protected final Node RDFtype; // = queryContext.org.rdf4led.rdf.dictionary().getRDFtype();

  protected final Node RDFnil; // = queryContext.org.rdf4led.rdf.dictionary().getRDFnil();

  protected final Node RDFfirst; // = queryContext.org.rdf4led.rdf.dictionary().getRDFfirst();

  protected final Node RDFrest; // = queryContext.org.rdf4led.rdf.dictionary().getRDFrest();

  protected final Node RDFsubject; // = queryContext.org.rdf4led.rdf.dictionary().getRDFsubject();

  protected final Node RDFpredicate; // = queryContext.org.rdf4led.rdf.dictionary().getRDFpredicate();

  protected final Node RDFobject; // = queryContext.org.rdf4led.rdf.dictionary().getRDFobject();

  // Graph patterns, true; in templates, false.
  private boolean bNodesAreVariables = true;

  // In DELETE, false.
  private boolean bNodesAreAllowed = true;

  Set<String> oldLabels = new HashSet<String>();

  // LabelToNodeMap listLabelMap = new LabelToNodeMap(true, new VarAlloc("L"))
  // ;
  // ----

  public ParserBase(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    XSD_TRUE = queryContext.dictionary().getXSDTrue();

    XSD_FALSE = queryContext.dictionary().getXSDFalse();

    RDFtype = queryContext.dictionary().getRDFtype();

    RDFnil = queryContext.dictionary().getRDFnil();

    RDFfirst = queryContext.dictionary().getRDFfirst();

    RDFrest = queryContext.dictionary().getRDFrest();

    RDFsubject = queryContext.dictionary().getRDFsubject();

    RDFpredicate = queryContext.dictionary().getRDFpredicate();

    RDFobject = queryContext.dictionary().getRDFobject();
  }

  public void init() {}

  protected void setBaseURI(String iri) {}

  protected void setPrefix(String prefix, String iri) {
    queryContext.setPrefix(prefix, iri);
  }

  protected void setInConstructTemplate(boolean b) {}

  protected boolean getBNodesAreVariables() {
    return bNodesAreVariables;
  }

  protected boolean getBNodesAreAllowed() {
    return bNodesAreAllowed;
  }

  protected void setBNodesAreAllowed(boolean bNodesAreAllowed) {
    this.bNodesAreAllowed = bNodesAreAllowed;
  }

  protected Element<Integer> compressGroupOfOneGroup(ElementGroup<Integer> elg) {
    // remove group of one group.
    if (elg.getElements().size() == 1) {
      Element<Integer> e1 = elg.getElements().get(0);
      if (e1 instanceof ElementGroup) return e1;
    }

    return elg;
  }

  protected Node createLiteralInteger(String image) {
    return queryContext.dictionary().createTypedLiteral(image, XSD.XSDint);
  }

  protected Node createLiteralDecimal(String image) {
    return queryContext.dictionary().createTypedLiteral(image, XSD.XSDdecimal);
  }

  protected Node createLiteralDouble(String image) {
    return queryContext.dictionary().createTypedLiteral(image, XSD.XSDdouble);
  }

  protected Node stripSign(Node node) {
    if (!(queryContext.dictionary().isLiteral(node))) {
      return node;
    }

    String lex = queryContext.dictionary().getLexicalForm(node);

    byte rdfdatatype = queryContext.dictionary().getDataType(node);

    if (!lex.startsWith("-") && !lex.startsWith("+")) {
      throw new RuntimeException("Literal does not start with sign	: " + lex);
    }

    lex = lex.substring(1);

    return queryContext.dictionary().createTypedLiteral(lex, rdfdatatype);
  }

  protected Node createLiteral(String lex, String lang, String nodeType) {
    if (lang != null) {
      return queryContext.dictionary().createLangLiteral(lex, LangTag.langtag.getTag(lang));
    } else {
      if (nodeType != null) {
        return queryContext.dictionary().createTypedLiteral2(lex, nodeType);
      }

      return queryContext.dictionary().createPlainLiteral(lex);
    }
  }

  protected long integerValue(String s) {
    try {
      if (s.startsWith("+")) {
        s = s.substring(1);
      }

      if (s.startsWith("0x")) {
        // Hex
        s = s.substring(2);

        return Long.parseLong(s, 16);
      }
      return Long.parseLong(s);
    } catch (NumberFormatException ex) {
      try {
        new BigInteger(s);

        throwParseException(
            "Number '" + s + "' is a valid number but can't not be stored in a long");
      } catch (NumberFormatException ex2) {

      }
      throw new QueryParseException(ex, -1, -1);
    }
  }

  protected double doubleValue(String s) {
    if (s.startsWith("+")) {
      s = s.substring(1);
    }

    double valDouble = Double.parseDouble(s);

    return valDouble;
  }

  /** Remove first and last characters (e.g. ' or "") from a string */
  protected static String stripQuotes(String s) {
    return s.substring(1, s.length() - 1);
  }

  /** Remove first 3 and last 3 characters (e.g. ''' or """) from a string */
  protected static String stripQuotes3(String s) {
    return s.substring(3, s.length() - 3);
  }

  /** remove the first n charcacters from the string */
  public static String stripChars(String s, int n) {
    return s.substring(n, s.length());
  }

  protected Node createVariable(String s, int line, int column) {
    s = s.substring(1); // Drop the marker

    return queryContext.allocVar(s);
  }

  // ---- IRIs and Nodes

  static final String bNodeLabelStart = "_:";

  // boolean skolomizedBNodes = ARQ.isTrue(ARQ.constantBNodeLabels) ;

  protected String resolveQuotedIRI(String iriStr, int line, int column) {
    iriStr = stripQuotes(iriStr);
    return resolveIRI(iriStr, line, column);
  }

  protected String resolveIRI(String iriStr, int line, int column) {
    if (isBNodeIRI(iriStr)) return iriStr;

    return iriStr;
  }

  protected String resolvePName(String qname, int line, int column) {
    // It's legal.
    int idx = qname.indexOf(':');

    // -- Escapes in local name
    String prefix = qname.substring(0, idx);

    String local = qname.substring(idx + 1);

    local = unescapePName(local, line, column);

    qname = prefix + ":" + local;
    // --

    String s = queryContext.expandPrefix(qname);

    if (s == null) throwParseException("Unresolved prefixed name: " + qname, line, column);

    return s;
  }

  protected Node createNode(String iri) {
    // Is it a bNode label? i.e. <_:xyz>
    if (isBNodeIRI(iri)) {
      return queryContext.dictionary().createBNode(iri);
      // String s = iri.substring(bNodeLabelStart.length()) ;
      // Node n = Node.createAnon(new AnonId(s)) ;
      // return n ;
    }

    return queryContext.dictionary().createURI(iri);
    // return Node.createURI(iri) ;
  }

  protected boolean isBNodeIRI(String iri) {
    return iri.startsWith(bNodeLabelStart);
  }

  // -------- Basic Graph Patterns and Blank Node label scopes

  // A BasicGraphPattern is any sequence of TripleBlocks, separated by
  // filters,
  // but not by other graph patterns

  // SPARQL does not have a straight syntactic unit for BGPs - but prefix
  // does.

  protected void startTriplesBlock() {}

  protected void endTriplesBlock() {}

  // On entry to a new group, the current BGP is ended.
  protected void startGroup(ElementGroup<Node> elg) {
    // endBasicGraphPattern() ;
    // startBasicGraphPattern() ;
  }

  protected void endGroup(ElementGroup<Node> elg) {
    // endBasicGraphPattern() ;
  }

  // --------
  protected Node createListNode(int line, int column) {
    return createBNode(line, column);
  }

  // Unlabelled bNode.
  protected Node createBNode(int line, int column) {
    throw new RuntimeException("line " + line);
  }

  // Labelled bNode.
  protected Node createBNode(String label, int line, int column) {
    if (!bNodesAreAllowed) {
      throwParseException("Blank nodes not allowed in DELETE templates: " + label, line, column);
    }
    if (oldLabels.contains(label)) {
      throwParseException("Blank node reused across basic graph patterns: " + label, line, column);
    }

    return queryContext.dictionary().createBNode(label);
  }

  protected Expr<Node> createExprExists(Element<Node> element) {
    return new E_Exists<>(element, queryContext);
  }

  protected Expr<Node> createExprNotExists(Element<Node> element) {
    return new E_NotExists<>(element, queryContext);
  }

  protected String fixupPrefix(String prefix, int line, int column) {
    // \ u processing!
    if (prefix.endsWith(":")) {
      prefix = prefix.substring(0, prefix.length() - 1);
    }

    return prefix;
  }

  protected void insert(TripleCollector<Node> acc, Node s, Node p, Node o) {
    acc.addTriple(new Triple<>(s, p, o));
  }

  protected void insert(TripleCollector<Node> acc, int index, Node s, Node p, Node o) {
    acc.addTriple(index, new Triple<>(s, p, o));
  }

  protected void insert(TripleCollector<Node> acc, Node s, Node p, Path<Node> path, Node o) {
    if (p == null)
      acc.addTriplePath(
          new TriplePath<Node>(s, path, o) {
            @Override
            public boolean isURI(Node p) {
              return queryContext.dictionary().isURI(p);
            }
          });
    else acc.addTriple(new Triple<Node>(s, p, o));
  }

  protected void insert(
      TripleCollector<Node> acc, int index, Node s, Node p, Path<Node> path, Node o) {
    if (p == null) {
      acc.addTriplePath(
          index,
          new TriplePath<Node>(s, path, o) {
            @Override
            public boolean isURI(Node p) {
              return queryContext.dictionary().isURI(p);
            }
          });
    } else {
      acc.addTriple(index, new Triple<>(s, p, o));
    }
  }

  protected Expr<Node> asExpr(Node n) {
    return queryContext.nodeToExpr(n);
  }

  protected Expr<Node> asExprNoSign(Node node) {

    return queryContext.nodeToExpr(node);
  }

  public static String unescapeStr(String s) {
    return unescape(s, '\\', false, 1, 1);
  }

  // public static String unescapeCodePoint(String s)
  // { return unescape(s, '\\', true, 1, 1) ; }
  //
  // protected String unescapeCodePoint(String s, int line, int column)
  // { return unescape(s, '\\', true, line, column) ; }

  public static String unescapeStr(String s, int line, int column) {
    return unescape(s, '\\', false, line, column);
  }

  // Worker function
  public static String unescape(
      String s, char escape, boolean pointCodeOnly, int line, int column) {
    int i = s.indexOf(escape);

    if (i == -1) return s;

    // Dump the initial part straight into the string buffer
    StringBuffer sb = new StringBuffer(s.substring(0, i));

    for (; i < s.length(); i++) {
      char ch = s.charAt(i);
      // Keep line and column numbers.
      switch (ch) {
        case '\n':
        case '\r':
          line++;
          column = 1;
          break;
        default:
          column++;
          break;
      }

      if (ch != escape) {
        sb.append(ch);
        continue;
      }

      // Escape
      if (i >= s.length() - 1) throwParseException("Illegal escape at end of string", line, column);
      char ch2 = s.charAt(i + 1);
      column = column + 1;
      i = i + 1;

      // \\u and \\U
      if (ch2 == 'u') {
        // i points to the \ so i+6 is next character
        if (i + 4 >= s.length()) throwParseException("\\u escape too short", line, column);
        int x = hex(s, i + 1, 4, line, column);
        sb.append((char) x);
        // Jump 1 2 3 4 -- already skipped \ and u
        i = i + 4;
        column = column + 4;
        continue;
      }
      if (ch2 == 'U') {
        // i points to the \ so i+6 is next character
        if (i + 8 >= s.length()) throwParseException("\\U escape too short", line, column);
        int x = hex(s, i + 1, 8, line, column);
        // Convert to UTF-16 codepoint pair.
        sb.append((char) x);
        // Jump 1 2 3 4 5 6 7 8 -- already skipped \ and u
        i = i + 8;
        column = column + 8;
        continue;
      }

      // Are we doing just point code escapes?
      // If so, \X-anything else is legal as a literal "\" and "X"

      if (pointCodeOnly) {
        sb.append('\\');
        sb.append(ch2);
        i = i + 1;
        continue;
      }

      // Not just codepoints. Must be a legal escape.
      char ch3 = 0;
      switch (ch2) {
        case 'n':
          ch3 = '\n';
          break;
        case 't':
          ch3 = '\t';
          break;
        case 'r':
          ch3 = '\r';
          break;
        case 'b':
          ch3 = '\b';
          break;
        case 'f':
          ch3 = '\f';
          break;
        case '\'':
          ch3 = '\'';
          break;
        case '\"':
          ch3 = '\"';
          break;
        case '\\':
          ch3 = '\\';
          break;
        default:
          throwParseException("Unknown escape: \\" + ch2, line, column);
      }
      sb.append(ch3);
    }
    return sb.toString();
  }

  // Line and column that started the escape
  public static int hex(String s, int i, int len, int line, int column) {
    // if ( i+len >= s.length() )
    // {
    //
    // }
    int x = 0;
    for (int j = i; j < i + len; j++) {
      char ch = s.charAt(j);
      column++;
      int k = 0;
      switch (ch) {
        case '0':
          k = 0;
          break;
        case '1':
          k = 1;
          break;
        case '2':
          k = 2;
          break;
        case '3':
          k = 3;
          break;
        case '4':
          k = 4;
          break;
        case '5':
          k = 5;
          break;
        case '6':
          k = 6;
          break;
        case '7':
          k = 7;
          break;
        case '8':
          k = 8;
          break;
        case '9':
          k = 9;
          break;
        case 'A':
        case 'a':
          k = 10;
          break;
        case 'B':
        case 'b':
          k = 11;
          break;
        case 'C':
        case 'c':
          k = 12;
          break;
        case 'D':
        case 'd':
          k = 13;
          break;
        case 'E':
        case 'e':
          k = 14;
          break;
        case 'F':
        case 'f':
          k = 15;
          break;
        default:
          throwParseException("Illegal hex escape: " + ch, line, column);
      }
      x = (x << 4) + k;
    }
    return x;
  }

  public static String unescapePName(String s, int line, int column) {
    char escape = '\\';
    int idx = s.indexOf(escape);

    if (idx == -1) return s;

    int len = s.length();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i++) {
      // Copied form unescape abobve - share!
      char ch = s.charAt(i);
      // Keep line and column numbers.
      switch (ch) {
        case '\n':
        case '\r':
          line++;
          column = 1;
          break;
        default:
          column++;
          break;
      }

      if (ch != escape) {
        sb.append(ch);
        continue;
      }

      // Escape
      if (i >= s.length() - 1) throwParseException("Illegal escape at end of string", line, column);
      char ch2 = s.charAt(i + 1);
      column = column + 1;
      i = i + 1;

      switch (ch2) {
        case '~':
        case '.':
        case '-':
        case '!':
        case '$':
        case '&':
        case '\'':
        case '(':
        case ')':
        case '*':
        case '+':
        case ',':
        case ';':
        case '=':
        case ':':
        case '/':
        case '?':
        case '#':
        case '@':
        case '%':
          sb.append(ch2);
          break;
        default:
          throwParseException("Illegal prefix name escape: " + ch2, line, column);
      }
    }
    return sb.toString();
  }

  public static void throwParseException(String msg, int line, int column) {
    throw new QueryParseException("Line " + line + ", column " + column + ": " + msg, line, column);
  }

  public static void throwParseException(String msg) {
    throw new QueryParseException(msg, -1, -1);
  }

  public Aggregator<Node> createCount(boolean distinct) {
    if (distinct) {
      return new AggCount<>(queryContext);
    } else {
      return new AggCountDistinct<>(queryContext);
    }
  }

  public Aggregator<Node> createCountExpr(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggCountVarDistinct<>(expr, queryContext);
    } else {
      return new AggCountVar<>(expr, queryContext);
    }
  }

  public Aggregator<Node> createSum(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggSumDistinct<>(expr, queryContext);
    } else {
      return new AggSum<>(expr, queryContext);
    }
  }

  public Aggregator<Node> createMin(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggMinDistinct<>(expr, queryContext);
    } else {
      return new AggMin<>(expr, queryContext);
    }
  }

  public Aggregator<Node> createMax(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggMaxDistinct<>(expr, queryContext);
    } else {
      return new AggMax<>(expr, queryContext);
    }
  }

  public Aggregator<Node> createAvg(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggAvg<>(expr, queryContext);
    } else {
      return new AggAvgDistinct<>(expr, queryContext);
    }
  }

  public Aggregator<Node> createSample(boolean distinct, Expr<Node> expr) {
    if (distinct) {
      return new AggSampleDistinct<>(expr);
    } else {
      return new AggSample<>(expr);
    }
  }

  public Aggregator<Node> createAggGroupConcat(
      boolean distinct, Expr<Node> expr, String separator, ExprList<Node> ordered) {
    if (distinct) {
      return new AggGroupConcatDistinct<>(expr, separator, queryContext);
    } else {
      return new AggGroupConcat<>(expr, separator, queryContext);
    }
  }
}
