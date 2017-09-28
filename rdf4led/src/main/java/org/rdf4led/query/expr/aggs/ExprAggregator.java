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
package org.rdf4led.query.expr.aggs;

import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprNode;
import org.rdf4led.query.expr.ExprVar;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.ExprVisitor;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.common.mapping.Mapping;

/**
 * ExprAggregator.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>26 Oct 2015
 */
public class ExprAggregator<Node> extends ExprNode<Node> {
  protected Aggregator<Node> aggregator;

  protected Node varNode;

  protected ExprVar<Node> exprVar = null;

  public ExprAggregator(Node varAsNode, Aggregator<Node> agg, QueryContext<Node> queryContext) {
    super(queryContext);

    _setVar(varAsNode);

    aggregator = agg;

    if (queryContext == null) {
      throw new RuntimeException(ExprAggregator.class + " need check constructor");
    }
  }

  public Node getVar() {
    return varNode;
  }

  public void setVar(Node varNode) {

    if (this.varNode != null)
      throw new RuntimeException(
          ": Attempt to set variable to " + varNode + " when already set as " + this.varNode);

    if (varNode == null) throw new RuntimeException(": Attempt to set variable to null");

    _setVar(varNode);
  }

  private void _setVar(Node varNode) {
    this.varNode = varNode;

    this.exprVar = new ExprVar<Node>(varNode, queryContext);
  }

  public Aggregator<Node> getAggregator() {
    return aggregator;
  }

  @Override
  public int hashCode() {
    int x = aggregator.hashCode();

    if (varNode != null) {
      x ^= varNode.hashCode();
    }

    return x;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof ExprAggregator)) {
      return false;
    }

    ExprAggregator<Node> agg = (ExprAggregator<Node>) other;

    if (!varNode.equals(agg.varNode)) {
      return false;
    }

    return aggregator.equals(agg.aggregator);
  }

  // Ensure no confusion - in an old design, an ExprAggregator was a subclass of ExprVar.
  @Override
  public ExprVar<Node> getExprVar() {
    throw new RuntimeException();
  }

  @Override
  public Node asVar() {
    throw new RuntimeException();
  }

  public ExprVar<Node> getAggVar() {
    return exprVar;
  }

  // As an expression suitable for outputting the calculation.
  public String asSparqlExpr() {
    return aggregator.toString();
  }

  @Override
  public ExprAggregator<Node> applyNodeTransform(NodeTransform<Node> transform) {
    // Can't rewrite this to a non-variable.
    Node node = transform.convert(varNode);

    if (!queryContext.isVarNode(node)) {
      node = varNode;
    }

    Aggregator<Node> agg = aggregator.copyTransform(transform);

    return new ExprAggregator<Node>(queryContext.allocVarNode(node), agg, queryContext);
  }

  // DEBUGGING
  @Override
  public String toString() {
    return "(AGG "
        + (varNode == null ? "<>" : "?" + queryContext.getVarName(varNode))
        + " "
        + aggregator.toString()
        + ")";
  }

  @Override
  public Node eval(Mapping<Node> mapping) {
    if (mapping == null) {
      throw new RuntimeException(ExprAggregator.class + " Not bound (no binding) ");
    }

    Node nodeValue = mapping.getValue(varNode);

    if (nodeValue == null) {
      throw new RuntimeException(ExprAggregator.class + " Node bound (no varaible) ");
    }

    return nodeValue;
  }

  public Expr<Node> apply(ExprTransform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }
}
