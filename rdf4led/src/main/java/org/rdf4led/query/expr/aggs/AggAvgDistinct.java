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

import java.math.BigInteger;

/**
 * AggAvgDistinct.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>26 Oct 2015
 */
public class AggAvgDistinct<Node> extends AggregatorBase<Node> {
  // ---- AVG(DISTINCT org.rdf4led.common.data.expr)
  private Expr<Node> expr;

  private QueryContext<Node> queryContext;

  public AggAvgDistinct(Expr<Node> expr, QueryContext<Node> queryContext) {
    this.expr = expr;

    this.queryContext = queryContext;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggAvgDistinct<Node>(expr, queryContext);
  }

  @Override
  public String toString() {
    return "AggAvgDistinct";
  }

  public String toPrefixString() {
    return "(avg distinct +WriterExpr.asString(org.rdf4led.common.data.expr)+)";
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccAvgDistinct(expr);
  }

  @Override
  public final Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public Node getValueEmpty() {
    return queryContext.getnvZero();
  }

  @Override
  public int hashCode() {
    return HC_AggAvgDistinct ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof AggAvgDistinct)) {
      return false;
    }

    AggAvgDistinct<Node> a = (AggAvgDistinct<Node>) other;

    return expr.equals(a.expr);
  }

  // ---- Accumulator
  class AccAvgDistinct extends AccDistinctExpr<Node> {
    // Non-empty case but still can be nothing because the expression may be
    // undefined.
    private Node total = queryContext.getnvZero();

    private Node result, result_old;

    private int count = 0;

    public AccAvgDistinct(Expr<Node> expr) {
      super(expr);
    }

    @Override
    protected void updateArrivalDistinct(Node nv) // , Mapping<Node> mapping)
        {
      count++;

      if (total == queryContext.getnvZero()) {
        total = nv;
      } else {
        total = queryContext.getXSDFuncOp().numAdd(total, nv);
      }

      result = getResult();
    }

    @Override
    protected void updateExpiryDistinct(Node nv) // , Mapping<Node> mapping)
        {
      count--;

      if (count == 0) {
        total = queryContext.getnvZero();
      } else {
        total = queryContext.getXSDFuncOp().numSubtract(total, nv);
      }

      result = getResult();
    }

    @Override
    public Node getAccValue() {
      if (count == 0) return queryContext.getnvZero();

      return result;
    }

    private Node getResult() {
      if (count == 0) {
        return queryContext.getnvZero();
      }

      Node nvCount = queryContext.dictionary().createIntegerNode(BigInteger.valueOf(count));

      return queryContext.getXSDFuncOp().numDivide(total, nvCount);
    }

    @Override
    protected boolean isUpdate() {
      if (result.equals(result_old)) {
        return false;
      }

      result_old = result;

      return true;
    }
  }
}
