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
package org.rdf4led.query.sparql.algebra.transform;

import org.rdf4led.query.expr.function.*;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.expr.ExprVar;
import org.rdf4led.query.expr.aggs.ExprAggregator;

/**
 * ExprTransform.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public interface ExprTransform<Node> {
  public Expr<Node> transform(ExprFunction0<Node> func);

  public Expr<Node> transform(ExprFunction1<Node> func, Expr<Node> expr1);

  public Expr<Node> transform(ExprFunction2<Node> func, Expr<Node> expr1, Expr<Node> expr2);

  public Expr<Node> transform(
          ExprFunction3<Node> func, Expr<Node> expr1, Expr<Node> expr2, Expr<Node> expr3);

  public Expr<Node> transform(ExprFunctionN<Node> func, ExprList<Node> args);

  public Expr<Node> transform(ExprFunctionOp<Node> funcOp, ExprList<Node> args, Op<Node> opArg);

  public Expr<Node> transform(Node nodeValue);

  public Expr<Node> transform(ExprVar<Node> nv);

  public Expr<Node> transform(ExprAggregator<Node> eAgg);
}
