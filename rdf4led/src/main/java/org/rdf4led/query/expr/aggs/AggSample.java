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

public class AggSample<Node> extends AggregatorBase<Node> {
  // ---- Sample(org.rdf4led.common.data.expr)
  private final Expr<Node> expr;

  public AggSample(Expr<Node> expr) {
    this.expr = expr;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggSample<Node>(expr);
  }

  @Override
  public String toString() {
    return "sample( ExprUtils.fmtSPARQL(org.rdf4led.common.data.expr) )";
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccSample(expr);
  }

  @Override
  public Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public Node getValueEmpty() {
    return null;
  }

  @Override
  public int hashCode() {
    return HC_AggSample ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof AggSample)) return false;
    AggSample<Node> agg = (AggSample<Node>) other;

    return agg.getExpr().equals(getExpr());
  }

  // ---- Accumulator
  private class AccSample extends AccExpr<Node> {
    // Sample: first evaluation of the expression that is not an error.
    private Node sampleSoFar = null;

    public AccSample(Expr<Node> expr) {
      super(expr);
    }

    @Override
    public Node getAccValue() {
      return sampleSoFar;
    }

    @Override
    protected void updateArrival(Node nv) {
      if (sampleSoFar == null) {
        sampleSoFar = nv;
      }
    }

    @Override
    protected void updateExpiry(Node nv) {
      //			if (sampleSoFar == null)
      //			{
      //				sampleSoFar = nv;
      //			}
    }

    @Override
    protected boolean isUpdate() {
      return true;
    }
  }
}
