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
import org.rdf4led.query.expr.function.ExprFunctionN;
import org.rdf4led.common.mapping.Mapping;

/**
 * E_OneOfBase.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class E_OneOfBase<Node> extends ExprFunctionN<Node> {
  /*
   * This operation stores it's arguments as a single list. The first element
   * of the array is the expression being tested, the rest are the items to be
   * used to test against. There are cached copies of the LHS (car) and RHS
   * (cdr).
   */

  // Cached values.
  protected final Expr<Node> expr;

  protected final ExprList<Node> possibleValues;

  protected E_OneOfBase(Expr<Node> expr, ExprList<Node> args, QueryContext<Node> queryContext) {
    super(queryContext, args);

    args = fixup(expr, args);

    this.expr = expr;

    this.possibleValues = args;
  }

  // All ArgList, first arg is the expression.
  protected E_OneOfBase(ExprList<Node> args, QueryContext<Node> queryContext) {
    super(queryContext, args);

    this.expr = args.get(0);

    this.possibleValues = args.tail(1);
  }

  private ExprList<Node> fixup(Expr<Node> expr2, ExprList<Node> args) {
    ExprList<Node> allArgs = new ExprList<Node>(expr2);

    allArgs.addAll(args);

    return allArgs;
  }

  public Expr<Node> getLHS() {
    return expr;
  }

  public ExprList<Node> getRHS() {
    return possibleValues;
  }

  // public Expr getLHS() { return org.rdf4led.common.data.expr ; }
  // public ExprList getRHS() { return possibleValues ; }

  protected boolean evalOneOf(Mapping<Node> mapping) {
    // Special form.
    // Like ( org.rdf4led.common.data.expr = expr1 ) || ( org.rdf4led.common.data.expr = expr2 ) || ...

    Node nodeValue = expr.eval(mapping);

    for (Expr<Node> inExpr : possibleValues) {
      Node maybe = inExpr.eval(mapping);

      if (nodeValue == maybe) {
        return true;
      }
    }

    return false;
  }

  protected boolean evalNotOneOf(Mapping<Node> mapping) {
    return !evalOneOf(mapping);
  }
}
