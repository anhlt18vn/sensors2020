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
import org.rdf4led.query.expr.function.ExprFunctionN;

import java.util.List;

/**
 * E_StrSubstring.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_StrSubstring<Node> extends ExprFunctionN<Node> {
  public E_StrSubstring(
      Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3, QueryContext<Node> queryContext) {
    super(queryContext, expr1, expr2, expr3);
  }

  @Override
  protected Node eval(List<Node> args) {
    if (args.size() == 2) {
      return queryContext.getXSDFuncOp().substring(args.get(0), args.get(1));
    }

    // return NodeFunctions.substring(args.get(0), args.get(1), args.get(2))
    // ;
    return queryContext.getXSDFuncOp().substring(args.get(0), args.get(1), args.get(2));
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    if (newArgs.size() == 2) {
      return new E_StrSubstring<Node>(newArgs.get(0), newArgs.get(1), null, queryContext);
    }

    return new E_StrSubstring<Node>(newArgs.get(0), newArgs.get(1), newArgs.get(2), queryContext);
  }
}
