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
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.common.mapping.Mapping;

import java.util.ArrayList;
import java.util.List;

/** A function which takes N arguments (N may be variable e.g. regex) */

/**
 * ExprFunctionN.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprFunctionN<Node> extends ExprFunction<Node> {
  protected ExprList<Node> args = null;

  protected ExprFunctionN(QueryContext<Node> queryContext, Expr<Node>... args) {
    super(queryContext);

    this.args = argList(args);
  }

  protected ExprFunctionN(QueryContext<Node> queryContext, ExprList<Node> args) {
    super(queryContext);

    this.args = args;
  }

  private ExprList<Node> argList(Expr<Node>... args) {
    ExprList<Node> exprList = new ExprList<Node>();

    for (Expr<Node> e : args) {

      if (e != null) exprList.add(e);
    }

    return exprList;
  }

  @Override
  public Expr<Node> getArg(int i) {
    i = i - 1;

    if (i >= args.size()) return null;

    return args.get(i);
  }

  @Override
  public int numArgs() {
    return args.size();
  }

  @Override
  public List<Expr<Node>> getArgs() {
    return args.getList();
  }

  @Override
  public Expr<Node> applyNodeTransform(NodeTransform<Node> transform) {
    ExprList<Node> newArgs = new ExprList<Node>();

    for (int i = 1; i <= numArgs(); i++) {
      Expr<Node> e = getArg(i);

      e = e.applyNodeTransform(transform);

      newArgs.add(e);
    }

    return copy(newArgs);
  }

  /** Special form evaluation (example, don't eval the arguments first) */
  protected Node evalSpecial(Mapping<Node> mapping) {
    return null;
  }

  @Override
  public final Node eval(Mapping<Node> mapping) {
    Node s = evalSpecial(mapping);

    if (s != null) return s;

    List<Node> argsEval = new ArrayList<Node>();

    for (int i = 1; i <= numArgs(); i++) {

      Node x = eval(mapping, getArg(i));

      argsEval.add(x);
    }

    return eval(argsEval);
  }

  protected abstract Node eval(List<Node> args);

  protected abstract Expr<Node> copy(ExprList<Node> newArgs);

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(ExprTransform<Node> transform, ExprList<Node> exprList) {
    return transform.transform(this, exprList);
  }
}
