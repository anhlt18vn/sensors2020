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

import org.rdf4led.query.expr.Expr;
import org.rdf4led.common.mapping.Mapping;

import java.util.HashMap;

/**
 * AccDistinctExpr.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>26 Oct 2015
 */
public abstract class AccDistinctExpr<Node> implements Acc<Node> {
  private final HashMap<Node, Count> values = new HashMap<Node, Count>();

  private final Expr<Node> expr;

  protected Node result, result_old;

  protected AccDistinctExpr(Expr<Node> expr) {
    this.expr = expr;
  }

  @Override
  public synchronized boolean updateArrival(Mapping<Node> mapping) {
    Node nodeValue = expr.eval(mapping);

    if (values.containsKey(nodeValue)) {
      values.get(nodeValue).inc();

      return false;
    } else {
      values.put(nodeValue, new Count());
    }

    updateArrivalDistinct(nodeValue); // , mapping);

    return isUpdate();
  }

  @Override
  public synchronized boolean updateExpiry(Mapping<Node> mapping) {
    Node nodeValue = expr.eval(mapping);

    if (values.containsKey(nodeValue)) {
      Count count = values.get(nodeValue);

      count.dec();

      if (count.getCount() == 0) {

        updateExpiryDistinct(nodeValue); // , mapping);

        values.remove(nodeValue);

        return isUpdate();
      } else {
        return false;
      }
    } else {
      throw new RuntimeException("Expire non-arrival mapping  ???");
    }
  }

  // Count(DISTINCT ?v) is different
  @Override
  public Node getValue() {
    return getAccValue();
  }

  protected abstract Node getAccValue();

  protected abstract boolean isUpdate();

  protected abstract void updateArrivalDistinct(Node nv); // , Mapping<Node> mapping);

  protected abstract void updateExpiryDistinct(Node nv); // , Mapping<Node> mapping);

  private class Count {
    int count;

    private Count() {
      count = 1;
    }

    public int getCount() {
      return count;
    }

    public void inc() {
      count++;
    }

    public void dec() {
      count--;
    }
  }
}
