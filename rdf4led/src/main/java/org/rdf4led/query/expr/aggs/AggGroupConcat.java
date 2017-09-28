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
import org.rdf4led.query.QueryContext;

/**
 * AggGroupConcat.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public class AggGroupConcat<Node> extends AggregatorBase<Node> {
  static final String SeparatorDefault = " ";

  private final Expr<Node> expr;

  private final String separator;

  private final String effectiveSeparator;

  private QueryContext<Node> queryContext;

  public AggGroupConcat(Expr<Node> expr, String separator, QueryContext<Node> queryContext) {
    this(expr, (separator != null) ? separator : SeparatorDefault, separator, queryContext);
  }

  private AggGroupConcat(
      Expr<Node> expr,
      String effectiveSeparator,
      String separatorSeen,
      QueryContext<Node> queryContext) {
    this.expr = expr;

    this.separator = separatorSeen;

    this.effectiveSeparator = effectiveSeparator;
  }

  @Override
  public Aggregator<Node> copy(Expr<Node> expr) {
    return new AggGroupConcat<Node>(expr, effectiveSeparator, separator, queryContext);
  }

  @Override
  public Acc<Node> createAccumulator() {
    return new AccGroupConcat(expr, effectiveSeparator);
  }

  @Override
  public Expr<Node> getExpr() {
    return expr;
  }

  public String getSeparator() {
    return separator;
  }

  @Override
  public Node getValueEmpty() {
    return queryContext.getnvEmptyString();
    // return NodeConst.emptyString ;
  }

  @Override
  public int hashCode() {
    return HC_AggCountVar ^ expr.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof AggGroupConcat)) {
      return false;
    }

    AggGroupConcat<Node> agg = (AggGroupConcat<Node>) other;

    return (agg.getSeparator().equals(getSeparator())) && agg.getExpr().equals(getExpr());
  }

  // ---- Accumulator
  private class AccGroupConcat extends AccExpr<Node> {
    private StringBuilder stringSoFar = new StringBuilder();

    private boolean first = true;

    private final String separator;

    public AccGroupConcat(Expr<Node> expr, String sep) {
      super(expr);

      this.separator = sep;
    }

    @Override
    protected void updateArrival(Node nv) {
      String str = queryContext.dictionary().getLexicalValue(nv);

      if (!first) {
        stringSoFar.append(separator);
      }

      stringSoFar.append(str);

      first = false;
    }

    @Override
    public Node getAccValue() {
      return queryContext.dictionary().createStringNode(stringSoFar.toString());
    }

    @Override
    protected void updateExpiry(Node nv) {
      throw new RuntimeException("need to implement ");
    }

    @Override
    protected boolean isUpdate() {
      return true;
    }
  }
}
