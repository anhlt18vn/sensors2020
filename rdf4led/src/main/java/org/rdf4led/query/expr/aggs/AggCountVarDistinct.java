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
 * AggCountVarDistinct.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public class AggCountVarDistinct<Node> extends AggregatorBase<Node> {
  // ---- COUNT(DISTINCT ?var)
  private Expr<Node> expr;

  private QueryContext<Node> queryContext;

  public AggCountVarDistinct(Expr<Node> expr, QueryContext<Node> queryContext) {
    this.expr = expr;

    this.queryContext = queryContext;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggCountVarDistinct<Node>(expr, queryContext);
  }

  @Override
  public String toString() {
    return "count(distinct " + expr + ")";
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccCountVarDistinct();
  }

  @Override
  public Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public Node getValueEmpty() {
    return queryContext.getnvZero();
  }

  @Override
  public int hashCode() {
    return HC_AggCountVar ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof AggCountVarDistinct)) {
      return false;
    }

    AggCountVarDistinct<Node> agg = (AggCountVarDistinct<Node>) other;

    return agg.getExpr().equals(getExpr());
  }

  // ---- Accumulator
  class AccCountVarDistinct extends AccDistinctExpr<Node> {
    private long count = 0;

    public AccCountVarDistinct() {
      super(expr);
    }

    @Override
    public Node getValue() {
      return getAccValue();
    }

    @Override
    public Node getAccValue() {
      return queryContext.dictionary().createIntegerNode(BigInteger.valueOf(count));
    }

    @Override
    protected void updateArrivalDistinct(Node nv) {
      count++;
    }

    @Override
    protected void updateExpiryDistinct(Node nv) {
      count--;
    }

    @Override
    protected boolean isUpdate() {
      return true;
    }
  }
}
