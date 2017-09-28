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

package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.VarExprList;

/** This is the operation in stadard SPARQL 1.1 OpAssign is specifically in support of LET. */

/**
 * OpExtend.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpExtend<Node> extends Op1<Node> {
  private VarExprList<Node> assignments;

  public OpExtend() {
    super(null);
  }

  // There factory operations compress nested assignments if possible.
  // Not possible if it's the reassignment of something already assigned.
  // Or we could implement something like (let*).

  // static
  public Op<Node> extend(Op<Node> op, Node varNode, Expr<Node> expr) {
    if (!(op instanceof OpExtend)) {
      return createExtend(op, varNode, expr);
    }

    OpExtend<Node> opAssign = (OpExtend<Node>) op;

    if (opAssign.assignments.contains(varNode)) {
      return createExtend(op, varNode, expr);
    }

    opAssign.assignments.add(varNode, expr);

    return opAssign;
  }

  // static
  public Op<Node> extend(Op<Node> op, VarExprList<Node> exprs) {
    if (!(op instanceof OpExtend)) {
      return createExtend(op, exprs);
    }

    OpExtend<Node> opAssign = (OpExtend<Node>) op;
    for (Node varNode : exprs.getVars()) {
      if (opAssign.assignments.contains(varNode)) {
        return createExtend(op, exprs);
      }
    }

    opAssign.assignments.addAll(exprs);

    return opAssign;
  }

  /** Make a OpExtend - guaranteed to return an OpExtend */
  // static
  public OpExtend<Node> extendDirect(Op<Node> op, VarExprList<Node> exprs) {
    return new OpExtend<Node>(op, exprs);
  }

  // static
  private Op<Node> createExtend(Op<Node> op, Node varNode, Expr<Node> expr) {
    VarExprList<Node> x = new VarExprList<Node>();

    x.add(varNode, expr);

    return new OpExtend<Node>(op, x);
  }

  // static
  private Op<Node> createExtend(Op<Node> op, VarExprList<Node> exprs) {
    // Create, copying the var-org.rdf4led.common.data.expr list
    VarExprList<Node> x = new VarExprList<Node>();

    x.addAll(exprs);

    return new OpExtend<Node>(op, x);
  }

  private OpExtend(Op<Node> subOp) {
    super(subOp);
    assignments = new VarExprList<Node>();
  }

  private OpExtend(Op<Node> subOp, VarExprList<Node> exprs) {
    super(subOp);

    assignments = exprs;
  }

  public VarExprList<Node> getVarExprList() {
    return assignments;
  }

  @Override
  public int hashCode() {
    return assignments.hashCode() ^ getSubOp().hashCode();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    OpExtend<Node> op = new OpExtend<Node>(subOp, new VarExprList<Node>(getVarExprList()));
    return op;
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpExtend)) return false;

    OpExtend<Node> assign = (OpExtend<Node>) other;

    if (!assignments.equals(assign.assignments)) return false;

    return getSubOp().equalTo(assign.getSubOp());
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }
}
