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
import org.rdf4led.query.sparql.NodeTransform;

/**
 * Aggregate that does everything except the per-group aggregation that is needed for each operation
 */

/**
 * AggregatorBase.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public abstract class AggregatorBase<Node> implements Aggregator<Node> {
  // Aggregator -- handles one aggregation over one group, and is the org.rdf4led.sparql.algebra.syntax unit.

  // AggregateFactory -- delays the creating of Aggregator so multiple mentions over the same group
  // gives the same Aggregator

  // Accumulator -- the per-group, per-key accumulator for the aggregate
  // queries track their aggregators so if one is used twice, the calculataion is only done once.
  // For distinct, that means only uniquefier.

  // Built-ins: COUNT, SUM, MIN, MAX, AVG, GROUP_CONCAT, and SAMPLE
  // but COUNT(*) and COUNT(Expr) are different beasts
  // each in DISTINCT and non-DISTINCT versions

  protected AggregatorBase() {}

  @Override
  public abstract Acc<Node> createAccumulator();

  @Override
  public abstract Node getValueEmpty();

  //    @Override
  //    public String key()
  //    {
  //    	return "key ??? to use hashcode instead of using a String" ;
  //    }

  @Override
  public final Aggregator<Node> copyTransform(NodeTransform<Node> transform) {
    Expr<Node> e = getExpr();

    if (e != null) {
      e = e.applyNodeTransform(transform);
    }

    return copy(e);
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object other);

  protected static final int HC_AggAvg = 0x170;
  protected static final int HC_AggAvgDistinct = 0x171;

  protected static final int HC_AggCount = 0x172;
  protected static final int HC_AggCountDistinct = 0x173;

  protected static final int HC_AggCountVar = 0x174;
  protected static final int HC_AggCountVarDistinct = 0x175;

  protected static final int HC_AggMin = 0x176;
  protected static final int HC_AggMinDistinct = 0x177;

  protected static final int HC_AggMax = 0x178;
  protected static final int HC_AggMaxDistinct = 0x179;

  protected static final int HC_AggSample = 0x17A;
  protected static final int HC_AggSampleDistinct = 0x17B;

  protected static final int HC_AggSum = 0x17C;
  protected static final int HC_AggSumDistinct = 0x17D;

  protected static final int HC_AggGroupConcat = 0x17E;
  protected static final int HC_AggGroupConcatDistinct = 0x17F;

  protected static final int HC_AggNull = 0x180;
}
