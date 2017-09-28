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
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.query.sparql.syntax.Element;
import org.rdf4led.common.mapping.Mapping;

/** A "function" that executes over a pattern */

/**
 * ExprFunctionOp.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprFunctionOp<Node> extends ExprFunction<Node> {
  private final Op<Node> op;

  private final Element<Node> element;

  protected ExprFunctionOp(Element<Node> el, Op<Node> op, QueryContext<Node> queryContext) {
    super(queryContext);

    this.op = op;

    this.element = el;
  }

  @Override
  public Expr<Node> getArg(int i) {
    return null;
  }

  @Override
  public boolean isGraphPattern() {
    return true;
  }

  @Override
  public Op<Node> getGraphPattern() {
    return op;
  }

  public Element<Node> getElement() {
    return element;
  }

  @Override
  public int numArgs() {
    return 0;
  }

  // ---- Evaluation

  @Override
  public final Node eval(Mapping<Node> binding) {

    //		QueryIterator<Node> qIter1 = new QueryIterSingleton<Node>(binding, queryContext);
    //
    //		QueryIterator<Node> qIter = queryContext.execute(org.rdf4led.sparql.algebra.op, qIter1);
    //
    //		if (!(qIter instanceof QueryIteratorCheck))
    //		{
    //
    //			qIter = new QueryIteratorCheck<Node>(qIter, queryContext);
    //
    //		}
    //
    //		// Call the per-operation functionality.
    //		Node nodeValue = eval(binding, qIter);
    //
    //		qIter.close();
    //
    //		return nodeValue;
    throw new RuntimeException();
  }

  // protected abstract Node eval(Mapping<Node> binding, QueryIterator<Node> iter);

  public abstract ExprFunctionOp<Node> copy(ExprList<Node> args, Op<Node> x);

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(ExprTransform<Node> transform, ExprList<Node> args, Op<Node> x) {
    return transform.transform(this, args, x);
  }
}
