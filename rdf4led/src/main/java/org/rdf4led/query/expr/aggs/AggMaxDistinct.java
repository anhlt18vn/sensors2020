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
import org.rdf4led.query.QueryContext;

/**
 * AggMaxDistinct.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public class AggMaxDistinct<Node> extends AggMaxBase<Node> {
  // ---- MAX( DISTINCT org.rdf4led.common.data.expr)
  public AggMaxDistinct(Expr<Node> expr, QueryContext<Node> queryContext) {
    super(expr, queryContext);
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggMaxDistinct<Node>(expr, queryContext);
  }

  @Override
  public String toString() {
    return "max(distinct +ExprUtils.fmtSPARQL(getExpr())+)";
  }

  @Override
  public int hashCode() {
    return HC_AggMaxDistinct ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof AggMaxDistinct)) {
      return false;
    }

    AggMaxDistinct<Node> agg = (AggMaxDistinct<Node>) other;

    return expr.equals(agg.expr);
  }
}
