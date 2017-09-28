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
import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.expr.function.ExprFunctionOp;
import org.rdf4led.query.sparql.syntax.Element;

public class E_NotExists<Node> extends ExprFunctionOp<Node> {
  // Translated to "(not (exists (...)))"

  public E_NotExists(Op<Node> op, QueryContext<Node> queryContext) {
    this(null, op, queryContext);
  }

  public E_NotExists(Element<Node> elt, QueryContext<Node> queryContext) {
    super(elt, null, queryContext);

    throw new RuntimeException("this(elt, queryContext.getAlgebra().compile(elt), queryContext);");
  }

  public E_NotExists(Element<Node> el, Op<Node> op, QueryContext<Node> queryContext) {
    super(el, op, queryContext);
  }

  @Override
  public Expr<Node> applyNodeTransform(NodeTransform<Node> nodeTransform) {
    // Op<Node> op2 = queryContext.getNodeTransformLib().org.rdf4led.sparql.transform(nodeTransform,
    // getGraphPattern());

    // return new E_NotExists<Node>(getElement(), op2, queryContext);
    throw new RuntimeException("applyNodeTransfrom (nodeTransform)");
  }

  @Override
  public int hashCode() {
    return getGraphPattern().hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof E_NotExists)) return false;

    E_NotExists<Node> ex = (E_NotExists<Node>) other;

    return this.getGraphPattern().equals(ex.getGraphPattern());
  }

  @Override
  public ExprFunctionOp<Node> copy(ExprList<Node> args, Op<Node> x) {
    return new E_NotExists<Node>(x, queryContext);
  }
}
