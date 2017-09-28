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
import org.rdf4led.query.expr.aggs.acc.Acc;
import org.rdf4led.query.expr.aggs.acc.AccDistinctExpr;
import org.rdf4led.query.QueryContext;

/**
 * AggSumDistinct.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>26 Oct 2015
 */
public class AggSumDistinct<Node> extends AggregatorBase<Node> {
  // ---- SUM(DISTINCT org.rdf4led.common.data.expr)
  private Expr<Node> expr;

  private QueryContext<Node> queryContext;

  public AggSumDistinct(Expr<Node> expr, QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    this.expr = expr;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggSumDistinct<Node>(expr, queryContext);
  }

  // private static final NodeValue noValuesToSum = NodeValue.nvZERO ;

  @Override
  public String toString() {
    return "sum(distinct +ExprUtils.fmtSPARQL(org.rdf4led.common.data.expr)+)";
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccSumDistinct(expr);
  }

  @Override
  public Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public int hashCode() {
    return HC_AggSumDistinct ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof AggSumDistinct)) {
      return false;
    }

    AggSumDistinct<Node> agg = (AggSumDistinct<Node>) other;

    return agg.getExpr().equals(getExpr());
  }

  @Override
  public Node getValueEmpty() {
    // return NodeValue.toNode(noValuesToSum) ;
    return queryContext.getnvZero();
  }

  // ---- Accumulator
  class AccSumDistinct extends AccDistinctExpr<Node> {
    // Non-empty case but still can be nothing because the expression may be undefined.
    private Node total = null;

    private Node total_old = null;

    public AccSumDistinct(Expr<Node> expr) {
      super(expr);
    }

    @Override
    public Node getAccValue() {
      return total;
    }

    @Override
    protected void updateArrivalDistinct(Node nv) {
      if (total == null) {
        total = nv;
      } else {
        total = queryContext.getXSDFuncOp().numAdd(nv, total);
      }
    }

    @Override
    protected void updateExpiryDistinct(Node nv) {
      if (total == null) {
        throw new RuntimeException("Can not update expire in Sum Aggregator");
      } else {
        total = queryContext.getXSDFuncOp().numSubtract(total, nv);
      }
    }

    @Override
    protected boolean isUpdate() {
      if (!(total.equals(total_old))) {
        total_old = total;

        return true;
      } else return false;
    }
  }
}
