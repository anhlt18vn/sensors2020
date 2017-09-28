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

/** Create a literal from lexical form and datatype URI */
public class E_StrDatatype<Node> extends ExprFunction2<Node> {
  public E_StrDatatype(Expr<Node> expr1, Expr<Node> expr2, QueryContext<Node> queryContext) {
    super(expr1, expr2, queryContext);
  }

  @Override
  public Node eval(Node v1, Node v2) {
    return queryContext.strDatatype(v1, v2);
  }

  @Override
  public Expr<Node> copy(Expr<Node> expr1, Expr<Node> expr2) {
    return new E_StrDatatype<Node>(expr1, expr2, queryContext);
  }
}
