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

/**
 * E_FunctionDynamic.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_FunctionDynamic<Node> extends E_Call<Node> {
  public E_FunctionDynamic(
      Expr<Node> function, ExprList<Node> args, QueryContext<Node> queryContext) {
    this(null, queryContext);

    args = concatArgs(function, args);
  }

  public E_FunctionDynamic(ExprList<Node> args, QueryContext<Node> queryContext) {
    super(args, queryContext);
  }

  private ExprList<Node> concatArgs(Expr<Node> expr, ExprList<Node> args) {
    args = new ExprList<Node>(args);

    args.getList().add(0, expr);

    return args;
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    return new E_FunctionDynamic<Node>(newArgs, queryContext);
  }
}
