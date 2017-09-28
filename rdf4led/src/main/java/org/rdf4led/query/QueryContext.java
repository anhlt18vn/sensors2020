package org.rdf4led.query;

import org.rdf4led.graph.Quad;
import org.rdf4led.graph.Triple;
import org.rdf4led.rdf.dictionary.RDFDictionaryAbstract;
import org.rdf4led.query.sparql.RegexEngine;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprWalker;
import org.rdf4led.query.expr.XSDFuncOp;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * org.rdf4led.query
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 02/05/17.
 */
public interface QueryContext<Node> {
  // ==========================================================================================//
  // PREFIX MAPPING																			//
  // ==========================================================================================//
  public void setPrefix(String prefix, String uri);

  public String expandPrefix(String qname);

  // ==========================================================================================//
  // DICTIONARY																				//
  // ==========================================================================================//
  RDFDictionaryAbstract<Node> dictionary();

  // CONST NODE
  public Node strDatatype(Node nv1, Node nv2);

  public Node getDataTypeAsNode(Node node);

  public Node getLangTagAsNode(Node node);

  // NODEVALUE
  public Node getnvTrue();

  public Node getnvFalse();

  public Node getnvVersion();

  public Node getnvNothing();

  public Node getnvZero();

  public Node getnvEmptyString();

  // VAR NODE
  public Node allocVarNode(Node node);

  public Node allocVar(String varName);

  public Node allocInternVar();

  public boolean isVarNode(Node node);

  public boolean isAnonVar(Node node);

  public Node checkAndGetStringLiteral(String s, Node nvString);

  public String getVarName(Node varNode);

  // ==========================================================================================//
  // QUAD
  public Quad<Node> createQuad(Node graphNode, Triple<Node> triple);

  public boolean isDefaultGraph(Node graphNode);

  public boolean isDefaultGraphExplicit(Node graphNode);

  public boolean isUnionGraph(Node graphNode);

  // ==========================================================================================//
  // XSD Function
  public XSDFuncOp<Node> getXSDFuncOp();

  // ==========================================================================================//
  // NODEVALUE
  public XMLGregorianCalendar getDateTime(Node nodeValue);

  public Duration getDuration(Node nodeValue);

  // ==========================================================================================//
  // EXPR
  public static int CMP_LESS = -1;

  public static int CMP_GREATER = 1;

  public static int CMP_EQUAL = 0;

  public static int CMP_INDETERMINATE = 2;

  public ExprWalker<Node> getExprWalker();

  public Expr<Node> nodeToExpr(Node node);

  // ==========================================================================================//
  // TRANSFORMER

  // ==========================================================================================//
  // REGEX ENGINE
  public RegexEngine makeRegexEngine(Expr<Node> pattern, Expr<Node> flags);

  public RegexEngine makeRegexEngine(Node pattern, Node flags);

  int copmpareNodeasValue(Node nv1, Node nv2);

  int compareRDFTerms(Node n1, Node n2);

  // =============================================================toLangTag=============================//

}
