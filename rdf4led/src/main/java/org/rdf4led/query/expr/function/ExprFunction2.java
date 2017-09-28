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

package org.rdf4led.query.expr.function;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.common.mapping.Mapping;

/**
 * ExprFunction2.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public abstract class ExprFunction2<Node> extends ExprFunction<Node> {
  protected final Expr<Node> expr1;

  protected final Expr<Node> expr2;

  protected ExprFunction2(Expr<Node> expr1, Expr<Node> expr2, QueryContext<Node> queryContext) {
    super(queryContext);

    this.expr1 = expr1;

    this.expr2 = expr2;
  }

  public Expr<Node> getArg1() {
    return expr1;
  }

  public Expr<Node> getArg2() {
    return expr2;
  }

  @Override
  public Expr<Node> getArg(int i) {
    if (i == 1) return expr1;

    if (i == 2) return expr2;

    return null;
  }

  @Override
  public int numArgs() {
    return 2;
  }

  @Override
  public final Node eval(Mapping<Node> mapping) {
    Node s = evalSpecial(mapping);

    if (s != null) return s;

    Node x = eval(mapping, expr1);

    Node y = eval(mapping, expr2);

    return eval(x, y);
  }

  protected Node evalSpecial(Mapping<Node> mapping) {
    return null;
  }

  public abstract Node eval(Node x, Node y);

  @Override
  public final Expr<Node> applyNodeTransform(NodeTransform<Node> transform) {
    Expr<Node> e1 = (expr1 == null ? null : expr1.applyNodeTransform(transform));

    Expr<Node> e2 = (expr2 == null ? null : expr2.applyNodeTransform(transform));

    return copy(e1, e2);
  }

  public abstract Expr<Node> copy(Expr<Node> arg1, Expr<Node> arg2);

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(ExprTransform<Node> transform, Expr<Node> arg1, Expr<Node> arg2) {
    return transform.transform(this, arg1, arg2);
  }
}
