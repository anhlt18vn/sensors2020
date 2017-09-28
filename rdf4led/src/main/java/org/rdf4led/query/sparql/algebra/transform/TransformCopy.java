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

package org.rdf4led.query.sparql.algebra.transform;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.op.*;

import java.util.List;

/**
 * TransformCopy.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>1 Oct 2015
 */
public class TransformCopy<Node> implements Transform<Node> {
  public static final boolean COPY_ALWAYS = true;

  public static final boolean COPY_ONLY_ON_CHANGE = false;

  private boolean alwaysCopy = false;

  public TransformCopy() {
    this(COPY_ONLY_ON_CHANGE);
  }

  public TransformCopy(boolean alwaysDuplicate) {
    this.alwaysCopy = alwaysDuplicate;
  }

  @Override
  public Op<Node> transform(OpTable<Node> opTable) {
    return xform(opTable);
  }

  @Override
  public Op<Node> transform(OpBGP<Node> opBGP) {
    return xform(opBGP);
  }

  @Override
  public Op<Node> transform(OpQuadPattern<Node> opQuadPattern) {
    return xform(opQuadPattern);
  }

  @Override
  public Op<Node> transform(OpTriple<Node> opTriple) {
    return xform(opTriple);
  }

  @Override
  public Op<Node> transform(OpQuad<Node> opQuad) {
    return xform(opQuad);
  }

  @Override
  public Op<Node> transform(OpPath<Node> opPath) {
    return xform(opPath);
  }

  @Override
  public Op<Node> transform(OpProcedure<Node> opProc, Op<Node> subOp) {
    return xform(opProc, subOp);
  }

  // @Override
  // public Op<Node> org.rdf4led.sparql.transform(OpPropFunc<Node> opPropFunc, Op<Node> subOp) {
  // return xform(opPropFunc, subOp) ; }

  @Override
  public Op<Node> transform(OpFilter<Node> opFilter, Op<Node> subOp) {
    return xform(opFilter, subOp);
  }

  @Override
  public Op<Node> transform(OpGraph<Node> opGraph, Op<Node> subOp) {
    return xform(opGraph, subOp);
  }

  @Override
  public Op<Node> transform(OpStream<Node> opStream, Op<Node> subOp) {
    return xform(opStream, subOp);
  }

  @Override
  public Op<Node> transform(OpService<Node> opService, Op<Node> subOp) {
    return xform(opService, subOp);
  }

  @Override
  public Op<Node> transform(OpAssign<Node> opAssign, Op<Node> subOp) {
    return xform(opAssign, subOp);
  }

  @Override
  public Op<Node> transform(OpExtend<Node> opExtend, Op<Node> subOp) {
    return xform(opExtend, subOp);
  }

  @Override
  public Op<Node> transform(OpJoin<Node> opJoin, Op<Node> left, Op<Node> right) {
    return xform(opJoin, left, right);
  }

  @Override
  public Op<Node> transform(OpLeftJoin<Node> opLeftJoin, Op<Node> left, Op<Node> right) {
    return xform(opLeftJoin, left, right);
  }

  @Override
  public Op<Node> transform(OpDiff<Node> opDiff, Op<Node> left, Op<Node> right) {
    return xform(opDiff, left, right);
  }

  @Override
  public Op<Node> transform(OpMinus<Node> opMinus, Op<Node> left, Op<Node> right) {
    return xform(opMinus, left, right);
  }

  @Override
  public Op<Node> transform(OpUnion<Node> opUnion, Op<Node> left, Op<Node> right) {
    return xform(opUnion, left, right);
  }

  @Override
  public Op<Node> transform(OpConditional<Node> opCond, Op<Node> left, Op<Node> right) {
    return xform(opCond, left, right);
  }

  @Override
  public Op<Node> transform(OpSequence<Node> opSequence, List<Op<Node>> elts) {
    return xform(opSequence, elts);
  }

  @Override
  public Op<Node> transform(OpDisjunction<Node> opDisjunction, List<Op<Node>> elts) {
    return xform(opDisjunction, elts);
  }

  // @Override
  // public Op<Node> org.rdf4led.sparql.transform(OpExt<Node> opExt) { return opExt ; }

  @Override
  public Op<Node> transform(OpNull<Node> opNull) {
    return opNull.copy();
  }

  @Override
  public Op<Node> transform(OpLabel<Node> opLabel, Op<Node> subOp) {
    return xform(opLabel, subOp);
  }

  @Override
  public Op<Node> transform(OpList<Node> opList, Op<Node> subOp) {
    return xform(opList, subOp);
  }

  @Override
  public Op<Node> transform(OpOrder<Node> opOrder, Op<Node> subOp) {
    return xform(opOrder, subOp);
  }

  @Override
  public Op<Node> transform(OpTopN<Node> opTop, Op<Node> subOp) {
    return xform(opTop, subOp);
  }

  @Override
  public Op<Node> transform(OpProject<Node> opProject, Op<Node> subOp) {
    return xform(opProject, subOp);
  }

  @Override
  public Op<Node> transform(OpDistinct<Node> opDistinct, Op<Node> subOp) {
    return xform(opDistinct, subOp);
  }

  @Override
  public Op<Node> transform(OpReduced<Node> opReduced, Op<Node> subOp) {
    return xform(opReduced, subOp);
  }

  @Override
  public Op<Node> transform(OpSlice<Node> opSlice, Op<Node> subOp) {
    return xform(opSlice, subOp);
  }

  @Override
  public Op<Node> transform(OpGroup<Node> opGroup, Op<Node> subOp) {
    return xform(opGroup, subOp);
  }

  private Op<Node> xform(Op0<Node> op) {
    if (!alwaysCopy) return op;
    return op.copy();
  }

  private Op<Node> xform(Op1<Node> op, Op<Node> subOp) {
    if (!alwaysCopy && op.getSubOp() == subOp) return op;

    return op.copy(subOp);
  }

  private Op<Node> xform(Op2<Node> op, Op<Node> left, Op<Node> right) {
    if (!alwaysCopy && op.getLeft() == left && op.getRight() == right) return op;
    return op.copy(left, right);
  }

  private Op<Node> xform(OpN<Node> op, List<Op<Node>> elts) {
    // Need to do one-deep equality checking.
    if (!alwaysCopy && equals1(elts, op.getElements())) return op;
    return op.copy(elts);
  }

  private boolean equals1(List<Op<Node>> list1, List<Op<Node>> list2) {
    if (list1.size() != list2.size()) return false;
    for (int i = 0; i < list1.size(); i++) {
      if (list1.get(i) != list2.get(i)) return false;
    }
    return true;
  }
}
