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

import org.rdf4led.rdf.dictionary.RDFDictionaryAbstract;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.expr.function.ExprFunction;
import org.rdf4led.common.mapping.Mapping;

import java.util.Collection;
import java.util.Set;

/**
 * ExprNode.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 16, 2015
 */
public abstract class ExprNode<Node> implements Expr<Node> {
  protected QueryContext<Node> queryContext;

  protected RDFDictionaryAbstract<Node> dictionary;

  public ExprNode(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    this.dictionary = queryContext.dictionary();
  }

  public boolean isSatisfied(Mapping<Node> binding) {
    Node nv = eval(binding);

    boolean b = queryContext.getXSDFuncOp().booleanEffectiveValue(nv);

    return b;
  }

  public boolean isExpr() {
    return true;
  }

  public final Expr<Node> getExpr() {
    return this;
  }

  // --- interface Constraint

  @Override
  public abstract Node eval(Mapping<Node> binding);

  @Override
  public Set<Node> getVarsMentioned() {
    throw new RuntimeException("ExprNode");
  }

  @Override
  public void varsMentioned(Collection<Node> acc) {
    return;
    // ExprVars.varsMentioned(org.rdf4led.common.data.incremental.acc, this) ;
  }

  @Override
  public abstract boolean equals(Object other);

  @Override
  public abstract Expr<Node> applyNodeTransform(NodeTransform<Node> transform);

  // ---- Default implementations
  @Override
  public boolean isVariable() {
    return false;
  }

  @Override
  public ExprVar<Node> getExprVar() {
    return null;
  }

  @Override
  public Node asVar() {
    return null;
  }

  @Override
  public boolean isConstant() {
    return false;
  }

  @Override
  public Node getConstant() {
    return null;
  }

  @Override
  public boolean isFunction() {
    return false;
  }

  @Override
  public ExprFunction<Node> getFunction() {
    return null;
  }

  public boolean isGraphPattern() {
    return false;
  }

  public Op<Node> getGraphPattern() {
    return null;
  }

  // ----

  protected Node eval(Mapping<Node> mapping, Expr<Node> expr) {
    if (expr == null) return null;

    return expr.eval(mapping);
  }
}
