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
import org.rdf4led.query.expr.function.ExprFunction3;
import org.rdf4led.common.mapping.Mapping;

/** IF(org.rdf4led.common.data.expr, org.rdf4led.common.data.expr, org.rdf4led.common.data.expr) */

/**
 * E_Conditional.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_Conditional<Node> extends ExprFunction3<Node> {

  private final Expr<Node> condition;
  private final Expr<Node> thenExpr;
  private final Expr<Node> elseExpr;

  public E_Conditional(
      Expr<Node> condition,
      Expr<Node> thenExpr,
      Expr<Node> elseExpr,
      QueryContext<Node> queryContext) {
    // Don't let the parent eval the theEpxr or ifExpr.
    super(condition, thenExpr, elseExpr, queryContext);
    // Better names,
    this.condition = condition;

    this.thenExpr = thenExpr;

    this.elseExpr = elseExpr;
  }

  @Override
  public Expr<Node> copy(Expr<Node> arg1, Expr<Node> arg2, Expr<Node> arg3) {
    return new E_Conditional<Node>(arg1, arg2, arg3, queryContext);
  }

  /** Special form evaluation (example, don't eval the arguments first) */
  @Override
  protected Node evalSpecial(Mapping<Node> mapping) {
    // Node nv = condition.eval(binding) ;

    if (condition.isSatisfied(mapping)) {
      return thenExpr.eval(mapping);
    } else {
      return elseExpr.eval(mapping);
    }
  }

  @Override
  public Node eval(Node x, Node y, Node z) {
    throw new RuntimeException();
  }
}
