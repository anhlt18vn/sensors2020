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

public class E_Exists<Node> extends ExprFunctionOp<Node> {
  public E_Exists(Op<Node> op, QueryContext<Node> queryContext) {
    this(null, op, queryContext);
  }

  public E_Exists(Element<Node> elt, QueryContext<Node> queryContext) {
    super(elt, null, queryContext);

    throw new RuntimeException(
        "		//this(elt, queryContext.getAlgebra().compile(elt), queryContext);");
  }

  public E_Exists(Element<Node> el, Op<Node> op, QueryContext<Node> queryContext) {
    super(el, op, queryContext);
  }

  //	@Override
  //	public Expr<Node> copySubstitute(Binding<Node> binding, boolean foldConstants)
  //	{
  //		// Does not pass down fold constants. Oh well.
  //		Op<Node> op2 = queryContext.getSubstitute().substitute(getGraphPattern(), binding);
  //
  //		return new E_Exists<Node>(getElement(), op2, queryContext);
  //	}

  @Override
  public Expr<Node> applyNodeTransform(NodeTransform<Node> nodeTransform) {
    //		Op<Node> op2 = queryContext.getNodeTransformLib().org.rdf4led.sparql.transform(nodeTransform,
    // getGraphPattern());
    //		return new E_Exists<Node>(getElement(), op2, queryContext);

    throw new RuntimeException("applyNodeTransfrom (nodeTransform)");
  }

  //	@Override
  //	protected Node eval(Mapping<Node> mapping, QueryIterator<Node> qIter)
  //	{
  //		boolean b = qIter.hasNext();
  //
  //		if (b)
  //			return queryContext.getnvTrue();
  //
  //		else
  //			return queryContext.getnvFalse();
  //	}

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof E_Exists)) return false;

    E_Exists<Node> ex = (E_Exists<Node>) other;

    return this.getGraphPattern().equals(ex.getGraphPattern());
  }

  @Override
  public ExprFunctionOp<Node> copy(ExprList<Node> args, Op<Node> x) {
    return new E_Exists<Node>(x, queryContext);
  }
}
