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
import org.rdf4led.query.expr.aggs.acc.AccExpr;

public class AggSampleDistinct<Node> extends AggregatorBase<Node> {
  // ---- Sample(DISTINCT org.rdf4led.common.data.expr)
  private final Expr<Node> expr;

  public AggSampleDistinct(Expr<Node> expr) {
    this.expr = expr;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggSampleDistinct<Node>(expr);
  }

  @Override
  public String toString() {
    return "SAMPLE(DISTINCT ExprUtils.fmtSPARQL(org.rdf4led.common.data.expr))";
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccSampleDistict(expr);
  }

  @Override
  public Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public int hashCode() {
    return HC_AggSample ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof AggSampleDistinct)) {
      return false;
    }

    AggSampleDistinct<Node> agg = (AggSampleDistinct<Node>) other;

    return agg.getExpr().equals(getExpr());
  }

  @Override
  public Node getValueEmpty() {
    return null;
  }

  // ---- Accumulator
  private class AccSampleDistict extends AccExpr<Node> {
    // NOT AccumulatorDistinctExpr - avoid "distinct" overheads.queryContext
    // Sample: first evaluation of the expression that is not an error.
    // For sample, DISTINCT is a no-org.rdf4led.sparql.algebra.op - this code is picks the last
    // element.
    private Node sampleSoFar = null;

    public AccSampleDistict(Expr<Node> expr) {
      super(expr);
    }

    @Override
    public Node getAccValue() {
      return sampleSoFar;
    }

    @Override
    protected void updateArrival(Node nv) {
      sampleSoFar = nv;
    }

    @Override
    protected void updateExpiry(Node nv) {
      sampleSoFar = nv;
    }

    @Override
    protected boolean isUpdate() {
      // TODO Auto-generated method stub
      return true;
    }
  }
}
