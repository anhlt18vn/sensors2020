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

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.query.expr.function.ExprVisitor;
import org.rdf4led.common.mapping.Mapping;

/** An expression that is a variable in an expression. */

/**
 * ExprVar.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan
 *
 * <p>Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class ExprVar<Node> extends ExprNode<Node> {
  protected Node varNode;

  public ExprVar(Node varNode, QueryContext<Node> queryContext) {
    super(queryContext);

    this.varNode = varNode;
  }

  @Override
  public Node eval(Mapping<Node> mapping) {
    return eval(varNode, mapping);
  }

  private Node eval(Node varNode, Mapping<Node> mapping) {
    if (mapping == null) throw new RuntimeException("Not bound: (no binding): " + varNode);

    // check varNode is Var
    if (!queryContext.isVarNode(varNode)) {
      throw new RuntimeException(varNode + " is not Var");
    }

    Node nv = mapping.getValue(varNode);

    if (nv == null)
      throw new RuntimeException("Not bound: variable " + queryContext.getVarName(varNode));

    // Wrap as a NodeValue.
    // return queryContext.makeNodeValue(nv);
    return nv;
  }

  @Override
  public Expr<Node> applyNodeTransform(NodeTransform<Node> transform) {
    Node node = transform.convert(varNode);

    if (queryContext.isVarNode(node)) {
      return new ExprVar<Node>(queryContext.allocVarNode(node), queryContext);
    }

    return queryContext.nodeToExpr(node);
  }

  public Expr<Node> copy(Node varNode) {
    return new ExprVar<Node>(varNode, queryContext);
  }

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(ExprTransform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public int hashCode() {
    return varNode.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof ExprVar)) {
      return false;
    }

    ExprVar<Node> nvar = (ExprVar<Node>) other;

    return getVarName().equals(nvar.getVarName());
  }

  @Override
  public boolean isVariable() {
    return true;
  }

  public String getVarName() {
    throw new RuntimeException("return varNode.getVarName() ;");
  }

  @Override
  public ExprVar<Node> getExprVar() {
    return this;
  }

  @Override
  public Node asVar() {
    return varNode;
  }
}
