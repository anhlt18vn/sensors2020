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
import org.rdf4led.common.mapping.Mapping;

import java.util.List;

/**
 * E_BNode.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_BNode<Node> extends ExprFunctionN<Node> // 0 or one
{

  public E_BNode(QueryContext<Node> queryContext) {
    this(null, queryContext);
  }

  public E_BNode(Expr<Node> expr, QueryContext<Node> queryContext) {
    super(queryContext, expr);
  }

  // Not really a special form but we need access to
  // the binding to use a key.
  @Override
  public Node evalSpecial(Mapping<Node> binding) {
    Expr<Node> expr = null;

    if (args.size() == 1) {
      expr = getArg(1);
    }

    if (expr == null) {
      return queryContext.dictionary().createBNode();
    }

    Node x = expr.eval(binding);

    if (!queryContext.dictionary().isStringNode(x)) // if ( ! x.isString() )
    {
      throw new RuntimeException("Not a string: " + x);
    }

    return x;
  }

  @Override
  protected Node eval(List<Node> args) {
    throw new RuntimeException("");
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    if (newArgs.size() == 0) return new E_BNode<Node>(queryContext);
    else return new E_BNode<Node>(newArgs.get(0), queryContext);
  }
}
