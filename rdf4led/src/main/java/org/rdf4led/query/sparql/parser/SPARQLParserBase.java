package org.rdf4led.query.sparql.parser;

import org.rdf4led.query.path.Path;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.sparql.Query;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.expr.ExprVar;
import org.rdf4led.query.expr.aggs.Aggregator;
import org.rdf4led.query.sparql.syntax.*;
import org.rdf4led.common.mapping.Mapping;

import java.util.List;

public abstract class SPARQLParserBase<Node> {
  protected void startQuery() {}

  protected void finishQuery() {}

  protected void startWherePattern() {}

  protected void finishWherePattern() {}

  protected void startValuesClause(int beginLine, int beginColumn) {}

  protected void finishValuesClause(int finishLine, int finishColumn) {}

  protected long integerValue(String image) {
    return 0;
  }

  protected void startSubSelect(int beginLine, int beginColumn) {}

  protected void startGroup(ElementGroup<Node> elg) {}

  protected void endGroup(ElementGroup<Node> elg) {}

  protected void startTriplesBlock() {}

  protected void endTriplesBlock() {}

  protected void emitDataBlockVariable(Node v) {}

  protected Node createNode(String iri) {
    // TODO
    return null;
  }

  protected void emitDataBlockValue(Node v, int Line, int Column) {}

  protected void startDataBlockValueRow(int line, int column) {}

  protected void finishDataBlockValueRow(int line, int column) {}

  protected void startInlineData(
      List<Node> vars, List<Mapping<Node>> rows, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
  }

  protected void finishInlineData(int beginLine, int beginColumn) {
    // TODO Auto-generated method stub

  }

  protected void throwParseException(String message, int beginLine, int beginColumn) {}

  public Query<Node> endSubSelect(int beginLine, int beginColumn) {
    return null;
  }

  protected Template<Node> createTemplate(BasicPattern<Node> acc) {
    return null;
  }

  protected ElementPathBlock<Node> createElementPathBlock() {
    return null;
  }

  protected ElementPathBlock<Node> createElementPathBlock(BasicPattern<Node> acc) {
    return null;
  }

  protected ElementGroup<Node> createElementGroup() {
    return null;
  }

  protected Query<Node> getPrologue() {
    return null;
  }

  // Node Constant
  protected Node nRDFnil = null;

  protected Node nRDFrest = null;

  protected Node nRDFfirst = null;

  protected Node nRDFtype = null;

  protected Node XSD_TRUE = null;

  protected Node XSD_FALSE = null;

  protected Element<Node> createElementOptional(Object object, Element<Node> el) {
    // TODO Auto-generated method stub
    return null;
  }

  protected TripleCollectorBGP<Node> createTripleCollectorBGP() {
    // TODO Auto-generated method stub
    return null;
  }

  protected Query<Node> getQuery() {
    // TODO Auto-generated method stub
    return null;
  }

  protected String getURI(Node node) {
    // TODO Auto-generate
    return null;
  }

  protected Node createListNode(int beginLine, int beginColumn) {
    return null;
  }

  protected Node createVariable(String image, long beginLine, long beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createNodeFromURI(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createBNode(int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createBNode(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createNodeFromPrefixedName(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createLiteralInteger(String image) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createLiteralDecimal(String image) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createLiteralDouble(String image) {
    // TODO Auto-generated method stub
    return null;
  }

  protected String stripQuotes(String image) {
    // TODO Aut-generated method stub
    return null;
  }

  protected String stripQuotes3(String image) {
    // TODO Aut-generated method stub
    return null;
  }

  protected String stripChars(String image, int index) {
    // TODO Aut-generated method stub
    return null;
  }

  protected Expr<Node> asExpr(Node gn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_SameTerm(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createNodeVar(Node gn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Datatype(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_LogicalNot(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_UnaryPlus(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_UnaryMinus(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_Str(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_LangMatches(Expr<Node> expr, Expr<Node> patExpr) {
    return null;
  }

  protected Expr<Node> create_E_NumCeiling(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_NumFloor(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_NumRound(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_StrConcat(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_StrConcat(ExprList<Node> exprList) {
    return null;
  }

  protected Expr<Node> create_E_StrLength(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_StrUpperCase(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_StrLowerCase(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_StrEncodeForURI(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_DateTimeYear(Expr<Node> expr1) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeMonth(Expr<Node> expr1) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeDay(Expr<Node> expr1) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeHours(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeMinutes(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeSeconds(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeTimezone(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_DateTimeTZ(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Now() {
    return null;
  }

  protected Expr<Node> create_E_UUID() {
    return null;
  }

  protected Expr<Node> create_E_StrUUID() {
    return null;
  }

  protected Expr<Node> create_E_MD5(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_SHA1(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_SHA256(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_SHA384(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_SHA512(Expr<Node> expr) {
    return null;
  }

  protected Expr<Node> create_E_Coalesce(ExprList<Node> exprList) {
    return null;
  }

  protected Expr<Node> create_E_Conditional(Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3) {
    return null;
  }

  protected Expr<Node> create_E_NotOneOf(Expr<Node> expr, ExprList<Node> exprList) {
    return null;
  }

  protected Expr<Node> create_E_Regex(Expr<Node> expr, Expr<Node> patExpr, Expr<Node> flagsExpr) {
    return null;
  }

  protected Expr<Node> create_E_Multiply(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Divide(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Subtract(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Add(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Bound(Node createNodeVar) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IsLiteral(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IsNumeric(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IsBlank(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> createExprExists(Element<Node> el) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IsURI(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_LogicalOr(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_LogicalAnd(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Equals(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_NotEquals(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_LessThan(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_GreaterThan(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_LessThanOrEqual(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_GreaterThanOrEqual(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_StrSubstring(Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_OneOf(Expr<Node> expr1, ExprList<Node> exprList) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Lang(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_URI(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IRI(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected ExprVar<Node> create_ExprVar(Node graphNode) {
    return null;
  }

  protected Expr<Node> create_E_Bound(ExprVar<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_NumAbs(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Random() {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_Function(String uri, ExprList<Node> a) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_IsIRI(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_BNode(Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_BNode() {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node stripSign(Node n) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node makeNode(String lex, String lang, Node uri) {
    // TODO Auto-generated method stub
    return null;
  }

  protected String unescapeStr(String lex, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected String fixupPrefix(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected void setInConstructTemplate(boolean b) {
    // TODO Auto-generated method stub

  }

  protected void insert(TripleCollector<Node> acc, ElementPathBlock<Node> tempAcc) {
    // TODO Auto-generated method stub

  }

  protected void insert(
      TripleCollector<Node> acc, int index, Node s, Node p, Path<Node> path, Node o) {
    // TODO Auto-generated method stub
  }

  protected void insert(TripleCollector<Node> acc, Node s, Node p, Node o) {
    // TODO org.rdf4led.common.data.incremental.acc.addTriple(exeContext.createTriple(s, p, o));
  }

  protected void insert(TripleCollector<Node> acc, int index, Node s, Node p, Node o) {
    // TODO org.rdf4led.common.data.incremental.acc.addTriple(storage, exeContext.createTriple(s, p, o));
  }

  protected Aggregator<Node> createGroupConcat(
      boolean distinct, Expr<Node> expr, String sep, ExprList<Node> ordered) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createSample(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Node createLiteral(String lex, String lang, String uri) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createAvg(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createMax(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createMin(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createSum(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createCountExpr(boolean distinct, Expr<Node> expr) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Aggregator<Node> createCount(boolean distinct) {
    // TODO Auto-generated method stub
    return null;
  }

  protected String resolvePName(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected String resolveQuotedIRI(String image, int beginLine, int beginColumn) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_StrContains(Expr<Node> expr1, Expr<Node> expr2) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> create_E_StrStartsWith(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrEndsWith(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrBefore(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrAfter(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrLang(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrDatatype(Expr<Node> expr1, Expr<Node> expr2) {
    return null;
  }

  protected Expr<Node> create_E_StrReplace(
      Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3, Expr<Node> expr4) {
    // TODO Auto-generated method stub
    return null;
  }

  protected Expr<Node> createExprNotExists(Element<Node> el) {
    // TODO Auto-generated method stub
    return null;
  }
}
