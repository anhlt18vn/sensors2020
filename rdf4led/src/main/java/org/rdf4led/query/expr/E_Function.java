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
import org.rdf4led.query.expr.function.Function;
import org.rdf4led.common.mapping.Mapping;

import java.util.List;

/** SPARQL filter function */

/**
 * E_Function.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_Function<Node> extends ExprFunctionN<Node> {
  public static boolean WarnOnUnknownFunction = true;

  private String functionIRI;

  // Only set after a copySubstitute has been done by PlanFilter.
  // at which point this instance if not part of the query abstract org.rdf4led.sparql.algebra.syntax.
  private Function<Node> function = null;

  private boolean functionBound = false;

  public E_Function(String functionIRI, ExprList<Node> args, QueryContext<Node> queryContext) {
    super(queryContext, args);

    this.functionIRI = functionIRI;
  }

  // @Override
  // public String getFunctionIRI()
  // {
  // return functionIRI ;
  // }

  // The Function subsystem takes over evaluation via SpecialForms.
  // This is merely to allow "function" to behave as special forms
  // (this is discouraged).
  // Doing the function call in evalSpecial maintains the old
  // interface to functions.

  public Node evalSpecial(Mapping<Node> mapping) {
    // Only needed because some tests call straight in.
    // Otherwise, the buildFunction() calls should have done everything
    if (!functionBound) {
      buildFunction();
    }

    if (function == null) {
      throw new RuntimeException("URI <" + functionIRI + "> not bound");
    }

    Node r = function.exec(mapping, args, functionIRI);

    return r;
  }

  @Override
  protected Node eval(List<Node> args) {
    // For functions, we delay argument evaluation to the "Function"
    // heierarchy
    // so applications can add their own functional forms.
    throw new RuntimeException();
  }

  public void buildFunction() {
    try {
      bindFunction();
    } catch (RuntimeException ex) {

    }
  }

  private void bindFunction() {
    if (functionBound) {
      return;
    }

    // FunctionRegistry registry = chooseRegistry(cxt) ;
    //
    // FunctionFactory ff = registry.get(functionIRI) ;

    // if ( ff == null )
    // {
    // functionBound = true ;
    //
    throw new RuntimeException("URI <" + functionIRI + "> not found as a function");
    // }
    //
    // function = ff.create(functionIRI) ;

    // function.build(functionIRI, args) ;
    //
    // functionBound = true ;
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    return new E_Function<Node>(functionIRI, newArgs, queryContext);
  }

  @Override
  public int hashCode() {
    return hashCode();
  }
}
