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
import org.rdf4led.common.mapping.Mapping;

/*
Logical OR and AND is special with respect to handling errors truth table.

A       B   |   NOT A   A && B  A || B
-------------------------------------
E       E   |   E       E       E
E       T   |   E       E       T
E       F   |   E       F       E
T       E   |   F       E       T
T       T   |   F       T       T
T       F   |   F       F       T
F       E   |   T       F       E
F       T   |   T       F       T
F       F   |   T       F       F
*/

/**
 * E_LogicalOr.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_LogicalOr<Node> extends ExprFunction2<Node> {

  public E_LogicalOr(Expr<Node> left, Expr<Node> right, QueryContext<Node> queryContext) {
    super(left, right, queryContext);
  }

  @Override
  public Node evalSpecial(Mapping<Node> mapping) {
    RuntimeException error = null;

    try {

      Node x = getArg1().eval(mapping);

      if (queryContext.getXSDFuncOp().booleanEffectiveValue(x)) {
        return queryContext.getnvTrue();
      }

    } catch (RuntimeException eee) {
      // RHS Must be true else error.
      error = eee;
    }

    // LHS was false or error.

    try {

      Node y = getArg2().eval(mapping);

      if (queryContext.getXSDFuncOp().booleanEffectiveValue(y)) {
        return queryContext.getnvTrue();
      }

      // RHS is false but was there an error earlier?
      if (error != null) {
        throw error;
      }

      return queryContext.getnvFalse();

    } catch (RuntimeException eee) {
      // LHS an error, RHS was not true => error
      // Throw the first
      if (error != null) throw error;
      // RHS was false - throw this error.
      throw eee;
    }
  }

  @Override
  public Node eval(Node x, Node y) {
    // Evaluation only happens as part of copySubstitute.
    // Proper evaluation is a special form as above.

    if (!queryContext.dictionary().isBooleanNode(x)) {
      throw new RuntimeException("Not a boolean: " + x);
    }

    if (!queryContext.dictionary().isBooleanNode(y)) {
      throw new RuntimeException("Not a boolean: " + y);
    }

    boolean boolX = (x == queryContext.getnvTrue()); // x.getBoolean() ;

    boolean boolY = (y == queryContext.getnvTrue());

    boolean bool = (boolX || boolY);

    if (bool) return queryContext.getnvTrue();
    else return queryContext.getnvFalse();
  }

  @Override
  public Expr<Node> copy(Expr<Node> e1, Expr<Node> e2) {
    return new E_LogicalOr<Node>(e1, e2, queryContext);
  }
}
