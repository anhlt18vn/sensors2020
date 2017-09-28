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

package org.rdf4led.query.sparql.algebra.transform;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.*;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.expr.ExprVar;
import org.rdf4led.query.expr.aggs.ExprAggregator;

import java.util.List;

public class ExprTransformCopy<Node> implements ExprTransform<Node> {
  public static final boolean COPY_ALWAYS = true;

  public static final boolean COPY_ONLY_ON_CHANGE = false;

  private boolean alwaysCopy = false;

  protected QueryContext<Node> queryContext;

  public ExprTransformCopy(QueryContext<Node> queryContext) {
    this(queryContext, COPY_ONLY_ON_CHANGE);
  }

  public ExprTransformCopy(QueryContext<Node> queryContext, boolean alwaysDuplicate) {
    this.alwaysCopy = alwaysDuplicate;

    this.queryContext = queryContext;
  }

  @Override
  public Expr<Node> transform(ExprFunction0<Node> func) {
    return xform(func);
  }

  @Override
  public Expr<Node> transform(ExprFunction1<Node> func, Expr<Node> expr1) {
    return xform(func, expr1);
  }

  @Override
  public Expr<Node> transform(ExprFunction2<Node> func, Expr<Node> expr1, Expr<Node> expr2) {
    return xform(func, expr1, expr2);
  }

  @Override
  public Expr<Node> transform(
          ExprFunction3<Node> func, Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3) {
    return xform(func, expr1, expr2, expr3);
  }

  @Override
  public Expr<Node> transform(ExprFunctionN<Node> func, ExprList<Node> args) {
    return xform(func, args);
  }

  @Override
  public Expr<Node> transform(ExprFunctionOp<Node> funcOp, ExprList<Node> args, Op<Node> opArg) {
    return xform(funcOp, args, opArg);
  }

  @Override
  public Expr<Node> transform(Node nodeValue) {
    return xform(nodeValue);
  }

  @Override
  public Expr<Node> transform(ExprVar<Node> exprVar) {
    return xform(exprVar);
  }

  @Override
  public Expr<Node> transform(ExprAggregator<Node> eAgg) {
    return xform(eAgg);
  }

  private Expr<Node> xform(ExprFunction0<Node> func) {
    if (!alwaysCopy) return func;
    return func.copy();
  }

  private Expr<Node> xform(ExprFunction1<Node> func, Expr<Node> expr1) {
    if (!alwaysCopy && expr1 == func.getArg()) {
      return func;
    }

    return func.copy(expr1);
  }

  private Expr<Node> xform(ExprFunction2<Node> func, Expr<Node> expr1, Expr<Node> expr2) {
    if (!alwaysCopy && expr1 == func.getArg1() && expr2 == func.getArg2()) {
      return func;
    }

    return func.copy(expr1, expr2);
  }

  private Expr<Node> xform(
      ExprFunction3<Node> func, Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3) {
    if (!alwaysCopy
        && expr1 == func.getArg1()
        && expr2 == func.getArg2()
        && expr3 == func.getArg3()) {
      return func;
    }

    return func.copy(expr1, expr2, expr3);
  }

  private Expr<Node> xform(ExprFunctionN<Node> func, ExprList<Node> args) {
    return func;
  }

  private boolean equals1(List<Expr<Node>> list1, List<Expr<Node>> list2) {
    if (list1 == null && list2 == null) {
      return true;
    }

    if (list1 == null) {
      return false;
    }

    if (list2 == null) {
      return false;
    }

    if (list1.size() != list2.size()) {
      return false;
    }

    for (int i = 0; i < list1.size(); i++) {
      if (list1.get(i) != list2.get(i)) {
        return false;
      }
    }

    return true;
  }

  private Expr<Node> xform(ExprFunctionOp<Node> funcOp, ExprList<Node> args, Op<Node> opArg) {
    if (!alwaysCopy
        && equals1(funcOp.getArgs(), args.getList())
        && funcOp.getGraphPattern() == opArg) return funcOp;

    return funcOp.copy(args, opArg);
  }

  private Expr<Node> xform(Node nodeValue) {
    return queryContext.nodeToExpr(nodeValue);
  }

  private Expr<Node> xform(ExprVar<Node> exprVar) {
    return exprVar;
  }

  private Expr<Node> xform(ExprAggregator<Node> eAgg) {
    {
      return eAgg;
    }
  }
}
