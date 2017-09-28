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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * E_Call.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan
 *
 * <p>Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_Call<Node> extends ExprFunctionN<Node> {
  private Map<String, Expr<Node>> functionCache = new HashMap<String, Expr<Node>>();

  private Expr<Node> identExpr;

  private List<Expr<Node>> argExprs;

  protected E_Call(ExprList<Node> args, QueryContext<Node> queryContext) {
    super(queryContext, args);

    if (args.size() == 0) {
      identExpr = null;

    } else {
      identExpr = args.get(0);

      argExprs = args.getList().subList(1, args.size());
    }
  }

  @Override
  public Node evalSpecial(Mapping<Node> binding) {
    // No argument returns unbound
    if (identExpr == null) throw new RuntimeException("CALL() has no arguments");

    // One/More arguments means invoke a function dynamically
    Node func = identExpr.eval(binding);

    if (func == null) throw new RuntimeException("CALL: Function identifier unbound");

    if (queryContext.dictionary().isURI(func)) {

      Expr<Node> e = buildFunction(queryContext.dictionary().getURI(func), argExprs);

      if (e == null)
        throw new RuntimeException(
            "CALL: Function identifier <"
                + queryContext.dictionary().getURI(func)
                + "> does not identify a known function");

      // Calling this may throw an error which we will just let bubble up

      return e.eval(binding);
    } else {
      throw new RuntimeException("CALL: Function identifier not an IRI");
    }
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    return new E_Call<Node>(newArgs, queryContext);
  }

  @Override
  public Node eval(List<Node> args) {
    // Instead of evalSpecial, we can rely on the machinery to evaluate the
    // arguments to CALL first.
    // This precludes special forms for CALL first argument.
    // This code here is not usually called - evalSpecial is more general
    // and is the main code path,
    Node func = args.get(0);

    if (func == null) throw new RuntimeException("CALL: Function identifier unbound");

    if (queryContext.dictionary().isURI(func)) {

      ExprList<Node> a = new ExprList<Node>();

      for (int i = 1; i < args.size(); i++) {
        a.add(queryContext.nodeToExpr(args.get(i)));
      }

      // Expr e = null ;
      Expr<Node> e = new E_Function<Node>(queryContext.dictionary().getURI(func), a, queryContext);

      // Calling this may throw an error which we will just let bubble up
      return e.eval(null);
    } else throw new RuntimeException("CALL: Function identifier not an IRI");
  }

  /**
   * Returns the org.rdf4led.common.data.expr representing the dynamic function to be invoked
   *
   * <p>Uses caching wherever possible to avoid
   */
  private Expr<Node> buildFunction(String functionIRI, List<Expr<Node>> args) {
    // Use our cached version of the expression wherever possible
    if (functionCache.containsKey(functionIRI)) {
      return functionCache.get(functionIRI);
    }

    // Otherwise generate a new function and cache it
    try {
      E_Function<Node> e =
          new E_Function<Node>(functionIRI, new ExprList<Node>(args), queryContext);

      e.buildFunction();

      functionCache.put(functionIRI, e);

      return e;

    } catch (Throwable e) {
      // Anything goes wrong in creating the function cache a null so we
      // don't retry every time we see this IRI
      functionCache.put(functionIRI, null);

      return null;
    }
  }

  @Override
  public int hashCode() {
    return hashCode();
  }
}
