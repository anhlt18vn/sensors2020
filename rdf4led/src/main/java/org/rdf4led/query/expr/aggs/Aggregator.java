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
 * An Aggregator is the processor for the whole result stream. BindingKeys identify which section of
 * a group we're in.
 */

/**
 * Aggregator.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public interface Aggregator<Node> {
  // -- Aggregator - per query (strictly, one per SELECT level), unique even
  // if mentioned several times.
  // -- Accumulator - per group per key section processors (from
  // AggregatorBase)

  /** Create an accumulator for this aggregator */
  public Acc<Node> createAccumulator();

  /** Value if there are no elements in any group : return null for no result */
  public Node getValueEmpty();

  // public String toPrefixString();

  // Key to identify an aggregator as org.rdf4led.sparql.algebra.syntax for duplicate use in a query.
  // public String key();

  /** Get the expression - may be null (e.g COUNT(*)) ; */
  public Expr<Node> getExpr();

  public Aggregator<Node> copy(Expr<Node> expr);

  public Aggregator<Node> copyTransform(NodeTransform<Node> transform);

  @Override
  public int hashCode();

  @Override
  public boolean equals(Object other);
}
