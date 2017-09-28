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
 * AggMaxBase.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
abstract class AggMaxBase<Node> extends AggregatorBase<Node> {
  // ---- MAX(org.rdf4led.common.data.expr) and MAX(DISTINCT org.rdf4led.common.data.expr)
  protected final Expr<Node> expr;

  protected QueryContext<Node> queryContext;

  public AggMaxBase(Expr<Node> expr, QueryContext<Node> queryContext) {
    this.expr = expr;

    this.queryContext = queryContext;
  }

  @Override
  public final Acc<Node> createAccumulator() {
    return new AccMax(expr);
  }

  @Override
  public final Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public final Node getValueEmpty() {
    return null;
  }

  // ---- Accumulator
  private class AccMax extends AccExpr<Node> {
    private Node maxSoFar;

    boolean isUpdate = false;

    private Object[] arrV;

    int length = 0;

    public AccMax(Expr<Node> expr) {
      super(expr);

      arrV = new Object[50];
    }

    @Override
    public Node getAccValue() {
      if (length == 0) {
        return null;
        // return queryContext.getnvNothing();
      }

      if (arrV[length - 1] == null) {
        return null;
        // return queryContext.getnvNothing();
      }

      return (Node) arrV[length - 1];
    }

    @Override
    protected void updateArrival(Node nv) {
      if (length == 0) {
        arrV[length] = nv;

        length++;

        isUpdate = true;

        return;
      }

      add(nv);
    }

    @Override
    protected void updateExpiry(Node nv) {
      remove(nv);

      if (length == 0) {
        isUpdate = true;

        return;
      }

      if (arrV[length - 1].equals(maxSoFar)) {
        isUpdate = false;
      } else {
        isUpdate = true;
      }
    }

    @Override
    protected boolean isUpdate() {
      if (isUpdate) {
        if (length != 0) {
          maxSoFar = (Node) arrV[length - 1];
        } else {
          maxSoFar = null;
        }
      }
      return isUpdate;
    }

    private int slot(Node nv) {
      int low = 0;

      int high = length - 1;

      while (low <= high) {
        int mid = (low + high) >>> 1;

        int x = queryContext.getXSDFuncOp().compare(((Node) arrV[mid]), nv);

        if (x == QueryContext.CMP_LESS) {
          low = mid + 1;
        } else {
          if (x == QueryContext.CMP_GREATER) {
            high = mid - 1;
          } else {
            return mid;
          }
        }
      }

      return -(low + 1);
    }

    private void add(Node nv) {

      int slot = slot(nv);

      if (slot < 0) {
        slot = -(slot + 1);
      }

      if (slot == length) {
        isUpdate = true;
      } else {
        isUpdate = false;
      }

      if (length == arrV.length) {
        appendArray();
      }

      System.arraycopy(arrV, slot, arrV, slot + 1, arrV.length - (slot + 1));

      arrV[slot] = nv;

      length++;
    }

    private void remove(Node nv) {
      int slot = slot(nv);

      if (slot < 0) {
        return;
      }

      System.arraycopy(arrV, slot + 1, arrV, slot, arrV.length - (slot + 1));

      arrV[length - 1] = null;

      length--;

      if ((arrV.length - length) > 50) {
        concatArray();
      }
    }

    // Allocate 50 elements more to array
    private void appendArray() {
      Object[] arrVnew = new Object[arrV.length + 50];

      System.arraycopy(arrV, 0, arrVnew, 0, arrV.length);

      arrV = arrVnew;
    }

    private void concatArray() {
      Object[] arrVnew = new Object[arrV.length - 50];

      System.arraycopy(arrV, 0, arrVnew, 0, arrV.length - 50);

      arrV = arrVnew;
    }
  }
}
