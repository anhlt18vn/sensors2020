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
import org.rdf4led.query.sparql.algebra.OpVars;
import org.rdf4led.query.expr.function.ExprFunctionOp;
import org.rdf4led.query.expr.function.ExprVisitorBase;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * ExprVars.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan
 *
 * <p>Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 16, 2015
 */
public class ExprVars<Node> {
  private QueryContext<Node> queryContext;

  private OpVars<Node> opVars;

  public ExprVars(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    opVars = new OpVars<Node>(queryContext);
  }

  interface Action<T, Node> {
    void var(Collection<T> acc, Node var);
  }

  public Set<Node> getVarsMentioned(Expr<Node> expr) {
    Set<Node> acc = new HashSet<Node>();

    varsMentioned(acc, expr);

    return acc;
  }

  public Set<Node> getVarsMentioned(ExprList<Node> exprs) {
    Set<Node> acc = new HashSet<Node>();

    for (Expr<Node> expr : exprs) {
      varsMentioned(acc, expr);
    }

    return acc;
  }

  public void varsMentioned(Collection<Node> acc, Expr<Node> expr) {
    ExprVars.Action<Node, Node> action =
        new ExprVars.Action<Node, Node>() {
          @Override
          public void var(Collection<Node> acc, Node var) {
            acc.add(var);
          }
        };

    ExprVarsWorker<Node> vv = new ExprVarsWorker<Node>(acc, action);

    new ExprWalker<Node>().walk(vv, expr);
  }

  public Set<String> getVarNamesMentioned(Expr<Node> expr) {
    Set<String> acc = new HashSet<String>();

    varNamesMentioned(acc, expr);

    return acc;
  }

  public void varNamesMentioned(Collection<String> acc, Expr<Node> expr) {
    ExprVars.Action<String, Node> action =
        new ExprVars.Action<String, Node>() {
          @Override
          public void var(Collection<String> acc, Node var) {
            acc.add(queryContext.getVarName(var));
          }
        };

    ExprVarsWorker<String> vv = new ExprVarsWorker<String>(acc, action);

    new ExprWalker<Node>().walk(vv, expr);
  }

  class ExprVarsWorker<T> extends ExprVisitorBase<Node> {
    final Collection<T> acc;

    final Action<T, Node> action;

    public ExprVarsWorker(Collection<T> acc, Action<T, Node> action) {
      this.acc = acc;

      this.action = action;
    }

    @Override
    public void visit(ExprVar<Node> nv) {
      action.var(acc, nv.asVar());
    }

    @Override
    public void visit(ExprFunctionOp<Node> funcOp) {
      Collection<Node> vars = opVars.allVars(funcOp.getGraphPattern());

      for (Node v : vars) {
        action.var(acc, v);
      }
    }
  }
}
