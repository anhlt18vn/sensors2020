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

package org.rdf4led.query.expr.function;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.common.mapping.Mapping;

/**
 * ExprFunction0.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprFunction0<Node> extends ExprFunction<Node> {
  protected ExprFunction0(QueryContext<Node> queryContext) {
    super(queryContext);
  }

  @Override
  public Expr<Node> getArg(int i) {
    return null;
  }

  @Override
  public int numArgs() {
    return 0;
  }

  @Override
  public final Node eval(Mapping<Node> binding) {
    return eval();
  }

  public abstract Node eval();

  public abstract Expr<Node> copy();

  // @Override
  // final public Expr<Node> copySubstitute(Mapping<Node> binding, boolean
  // foldConstants)
  // {
  // return copy();
  // }

  @Override
  public final Expr<Node> applyNodeTransform(NodeTransform<Node> transform) {
    return copy();
  }

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(ExprTransform<Node> transform) {
    return transform.transform(this);
  }
}
