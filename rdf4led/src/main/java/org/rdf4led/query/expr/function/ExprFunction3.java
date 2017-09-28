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

/** A function of three arguments */

/**
 * ExprFunction3.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprFunction3<Node> extends ExprFunction<Node> {
  protected final Expr<Node> expr1;

  protected final Expr<Node> expr2;

  protected final Expr<Node> expr3;

  //    protected ExprFunction3(Expr<Node> expr1,
  //    				 		Expr<Node> expr2,
  //    				 		Expr<Node> expr3,
  //    				 		QueryContext<Node> queryContext)
  //    {
  //    	this(expr1, expr2, expr3, (byte) 0, queryContext) ;
  //    }

  protected ExprFunction3(
      Expr<Node> expr1,
      Expr<Node> expr2,
      Expr<Node> expr3,
      //    						byte opSign,
      QueryContext<Node> queryContext) {
    super(queryContext);

    this.expr1 = expr1;

    this.expr2 = expr2;

    this.expr3 = expr3;
  }

  public Expr<Node> getArg1() {
    return expr1;
  }

  public Expr<Node> getArg2() {
    return expr2;
  }

  public Expr<Node> getArg3() {
    return expr3;
  }

  @Override
  public Expr<Node> getArg(int i) {
    if (i == 1) return expr1;

    if (i == 2) return expr2;

    if (i == 3) return expr3;

    return null;
  }

  @Override
  public int numArgs() {
    return 3;
  }

  // ---- Evaluation

  //    @Override
  //    public int hashCode()
  //    {
  //        return getFunctionSymbol().hashCode() ^
  //               Lib.hashCodeObject(expr1) ^
  //               Lib.hashCodeObject(expr2) ^
  //               Lib.hashCodeObject(expr3) ;
  //    }

  @Override
  public final Node eval(Mapping<Node> mapping) {
    Node s = evalSpecial(mapping);

    if (s != null) return s;

    Node x = eval(mapping, expr1);

    Node y = eval(mapping, expr2);

    Node z = eval(mapping, expr3);

    return eval(x, y, z);
  }

  /** Special form evaluation (example, don't eval the arguments first) */
  protected Node evalSpecial(Mapping<Node> mapping) {
    return null;
  }

  public Node eval(Node x, Node y, Node z, QueryContext<Node> queryContext) {
    return eval(x, y, z);
  }

  public abstract Node eval(Node x, Node y, Node z);

  // ---- Duplication

  //    @Override
  //    final public Expr<Node> copySubstitute(Mapping<Node> mapping, boolean foldConstants)
  //    {
  //        Expr<Node> e1 = (expr1 == null ? null : expr1.copySubstitute(binding, foldConstants)) ;
  //
  //        Expr<Node> e2 = (expr2 == null ? null : expr2.copySubstitute(binding, foldConstants)) ;
  //
  //        Expr<Node> e3 = (expr3 == null ? null : expr3.copySubstitute(binding, foldConstants)) ;
  //
  //        if ( foldConstants)
  //        {
  //            try {
  //                if ( e1 != null && e2 != null && e3 != null &&
  //
  //                		e1.isConstant() && e2.isConstant() && e3.isConstant() )
  //
  //                	return queryContext.nodeToExpr(eval(e1.getConstant(), e2.getConstant(),
  // e3.getConstant())) ;
  //
  //            } catch (NotImplementedException ex) { /* Drop through */ }
  //
  //        }
  //
  //
  //        return copy(e1, e2, e3) ;
  //    }

  @Override
  public final Expr<Node> applyNodeTransform(NodeTransform<Node> transform) {
    Expr<Node> e1 = (expr1 == null ? null : expr1.applyNodeTransform(transform));

    Expr<Node> e2 = (expr2 == null ? null : expr2.applyNodeTransform(transform));

    Expr<Node> e3 = (expr3 == null ? null : expr3.applyNodeTransform(transform));

    return copy(e1, e2, e3);
  }

  public abstract Expr<Node> copy(Expr<Node> arg1, Expr<Node> arg2, Expr<Node> arg3);

  @Override
  public void visit(ExprVisitor<Node> visitor) {
    visitor.visit(this);
  }

  public Expr<Node> apply(
      ExprTransform<Node> transform, Expr<Node> arg1, Expr<Node> arg2, Expr<Node> arg3) {
    return transform.transform(this, arg1, arg2, arg3);
  }
}
