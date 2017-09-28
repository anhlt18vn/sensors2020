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

import org.rdf4led.query.expr.aggs.ExprAggregator;
import org.rdf4led.query.expr.function.ExprFunction;
import org.rdf4led.query.expr.function.ExprFunctionOp;
import org.rdf4led.query.expr.function.ExprVisitor;
import org.rdf4led.query.expr.function.ExprVisitorFunction;

/**
 * ExprWalker.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class ExprWalker<Node> // implements ExprVisitor
{
  public ExprWalker() {
    // this.queryContext = queryContext;
  }

  public void walk(ExprVisitor<Node> visitor, Expr<Node> expr) {
    expr.visit(new WalkerBottomUp(visitor));
  }

  class Walker extends ExprVisitorFunction<Node> {
    ExprVisitor<Node> visitor;

    boolean topDown = true;

    private Walker(ExprVisitor<Node> visitor, boolean topDown) {
      this.visitor = visitor;

      this.topDown = topDown;
    }

    @Override
    public void startVisit() {}

    @Override
    protected void visitExprFunction(ExprFunction<Node> func) {
      if (topDown) {
        func.visit(visitor);
      }

      for (int i = 1; i <= func.numArgs(); i++) {
        Expr<Node> expr = func.getArg(i);

        if (expr == null)
        // Put a dummy in, e.g. to keep the org.rdf4led.sparql.transform stack aligned.
        {
          throw new RuntimeException("queryContext.getnvNothing().visit(this)");
        } else {
          expr.visit(this);
        }
      }

      if (!topDown) {
        func.visit(visitor);
      }
    }

    @Override
    public void visit(ExprFunctionOp<Node> funcOp) {
      funcOp.visit(visitor);
    }

    @Override
    public void visit(Node nv) {
      // nv.visit(visitor) ;
      visitor.visit(nv);
      // throw new NotImplementedException(" or Do nothing???? ");
    }

    @Override
    public void visit(ExprVar<Node> v) {
      v.visit(visitor);
    }

    @Override
    public void visit(ExprAggregator<Node> eAgg) {
      eAgg.visit(visitor);
    }

    @Override
    public void finishVisit() {}
  }

  // Visit current element then visit subelements
  public class WalkerTopDown extends Walker {
    private WalkerTopDown(ExprVisitor<Node> visitor) {
      super(visitor, true);
    }
  }

  // Visit current element then visit subelements
  public class WalkerBottomUp extends Walker {
    private WalkerBottomUp(ExprVisitor<Node> visitor) {
      super(visitor, false);
    }
  }
}
