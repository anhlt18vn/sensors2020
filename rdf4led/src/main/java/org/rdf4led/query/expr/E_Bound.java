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
import org.rdf4led.query.expr.function.ExprFunction1;
import org.rdf4led.common.mapping.Mapping;

/**
 * E_Bound.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_Bound<Node> extends ExprFunction1<Node> {
  boolean isBound = false;

  public E_Bound(Expr<Node> expr, QueryContext<Node> queryContext) {
    super(expr, queryContext);
  }

  @Override
  public Node evalSpecial(Mapping<Node> binding) {
    try {

      expr.eval(binding);

      return queryContext.getnvTrue();

    } catch (RuntimeException ex) {
      return queryContext.getnvFalse();
    }
  }

  @Override
  public Node eval(Node x) {
    return queryContext.getnvTrue();
  }

  @Override
  public Expr<Node> copy(Expr<Node> expr) {
    return new E_Bound<Node>(expr, queryContext);
  }

  @Override
  public int hashCode() {

    return hashCode();
  }
}
