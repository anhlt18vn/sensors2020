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
package org.rdf4led.query.expr.aggs.acc;

/** Accumulator that passes down every value of an expression */
import org.rdf4led.query.expr.Expr;
import org.rdf4led.common.mapping.Mapping;

/**
 * AccExpr.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public abstract class AccExpr<Node> implements Acc<Node> {
  private final Expr<Node> expr;

  protected AccExpr(Expr<Node> expr) {
    this.expr = expr;
  }

  @Override
  public final synchronized boolean updateArrival(Mapping<Node> mapping) {
    Node nv = expr.eval(mapping);

    updateArrival(nv);

    return isUpdate();
  }

  @Override
  public final synchronized boolean updateExpiry(Mapping<Node> mapping) {
    Node nv = expr.eval(mapping);

    updateExpiry(nv);

    return isUpdate();
  }

  @Override
  public Node getValue() {
    return getAccValue();
  }

  protected abstract Node getAccValue();

  protected abstract boolean isUpdate();

  protected abstract void updateArrival(Node nv);

  protected abstract void updateExpiry(Node nv);
}
