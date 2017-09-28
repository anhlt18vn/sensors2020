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
import org.rdf4led.query.sparql.RegexJava;
import org.rdf4led.query.expr.function.ExprFunctionN;

import java.util.List;
import java.util.regex.Pattern;

/**
 * E_StrReplace.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_StrReplace<Node> extends ExprFunctionN<Node> {
  private Pattern pattern = null;

  public E_StrReplace(
      Expr<Node> expr1,
      Expr<Node> expr2,
      Expr<Node> expr3,
      Expr<Node> expr4,
      QueryContext<Node> queryContext) {
    super(queryContext, expr1, expr2, expr3, expr4);

    if (isString(expr2) && (expr4 == null || isString(expr4))) {
      int flags = 0;
      if (expr4 != null
          && expr4.isConstant()
          && queryContext.dictionary().isStringNode(expr4.getConstant())) {
        flags = RegexJava.makeMask(queryContext.dictionary().getLexicalValue(expr4.getConstant()));
      }

      pattern =
          Pattern.compile(queryContext.dictionary().getLexicalValue(expr2.getConstant()), flags);
    }
  }

  private boolean isString(Expr<Node> expr) {
    return expr.isConstant() && queryContext.dictionary().isStringNode(expr.getConstant());
  }

  @Override
  protected Node eval(List<Node> args) {
    if (pattern != null) {
      return queryContext.getXSDFuncOp().strReplace(args.get(0), pattern, args.get(2));
    }

    if (args.size() == 3) {
      return queryContext.getXSDFuncOp().strReplace(args.get(0), args.get(1), args.get(2));
    }

    return queryContext
        .getXSDFuncOp()
        .strReplace(args.get(0), args.get(1), args.get(2), args.get(3));
  }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    if (newArgs.size() == 3) {
      return new E_StrReplace<Node>(
          newArgs.get(0), newArgs.get(1), newArgs.get(2), null, queryContext);
    }

    return new E_StrReplace<Node>(
        newArgs.get(0), newArgs.get(1), newArgs.get(2), newArgs.get(3), queryContext);
  }

  // @Override
  // public NodeValue eval(NodeValue x, NodeValue y, NodeValue z)
  // {
  // if ( pattern == null )
  // return XSDFuncOp.strReplace(x, y, z) ;
  // return XSDFuncOp.strReplace(x, pattern, z) ;
  // }
  //
  // @Override
  // public Expr copy(Expr arg1, Expr arg2, Expr arg3)
  // {
  // return new E_StrReplace(arg1, arg2, arg3) ;
  // }
}
