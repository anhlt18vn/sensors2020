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

package org.rdf4led.query.expr;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.ExprFunction2;

/**
 * E_NotEquals.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_NotEquals<Node> extends ExprFunction2<Node> {

  public E_NotEquals(Expr<Node> left, Expr<Node> right, QueryContext<Node> queryContext) {
    super(left, right, queryContext);
  }

  @Override
  public Node eval(Node x, Node y) {
    boolean b = (!(x.equals(y)));

    // Note: notSameValueAs means "known to be different"
    // sameValueAs means "know to be the same value"
    // so they are not opposites
    if (b) {
      if (queryContext.dictionary().isURI(x)) {
        return queryContext.getnvTrue();
      }

      if (queryContext.dictionary().isPlainLiteral(x)) {
        return queryContext.getnvTrue();
      }

      if (queryContext.dictionary().isLiteral(x)) {
        int k = queryContext.getXSDFuncOp().compare(x, y);

        if (k != QueryContext.CMP_EQUAL) {
          return queryContext.getnvTrue();
        } else {
          return queryContext.getnvFalse();
        }
      }

      return queryContext.getnvTrue();
    } else {
      return queryContext.getnvFalse();
    }
  }

  @Override
  public Expr<Node> copy(Expr<Node> e1, Expr<Node> e2) {
    return new E_NotEquals<Node>(e1, e2, queryContext);
  }
}
