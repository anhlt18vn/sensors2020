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
 * TransformWrapper.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>1 Oct 2015
 */
public class TransformWrapper<Node> implements Transform<Node> {
  protected final Transform<Node> transform;

  public TransformWrapper(Transform<Node> transform) {
    this.transform = transform;
  }

  @Override
  public Op<Node> transform(OpTable<Node> opTable) {
    return transform.transform(opTable);
  }

  @Override
  public Op<Node> transform(OpBGP<Node> opBGP) {
    return transform.transform(opBGP);
  }

  @Override
  public Op<Node> transform(OpTriple<Node> opTriple) {
    return transform.transform(opTriple);
  }

  @Override
  public Op<Node> transform(OpQuad<Node> opQuad) {
    return transform.transform(opQuad);
  }

  @Override
  public Op<Node> transform(OpPath<Node> opPath) {
    return transform.transform(opPath);
  }

  @Override
  public Op<Node> transform(OpProcedure<Node> opProc, Op<Node> subOp) {
    return transform.transform(opProc, subOp);
  }

  // @Override
  // public Op<Node> org.rdf4led.sparql.transform(OpPropFunc<Node> opPropFunc, Op<Node> subOp) {
  // return org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(opPropFunc, subOp) ; }

  // @Override
  // public Op org.rdf4led.sparql.transform(OpDatasetNames dsNames) { return
  // org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(dsNames) ; }
  //
  @Override
  public Op<Node> transform(OpQuadPattern<Node> quadPattern) {
    return transform.transform(quadPattern);
  }

  @Override
  public Op<Node> transform(OpFilter<Node> opFilter, Op<Node> subOp) {
    return transform.transform(opFilter, subOp);
  }

  @Override
  public Op<Node> transform(OpGraph<Node> opGraph, Op<Node> subOp) {
    return transform.transform(opGraph, subOp);
  }

  @Override
  public Op<Node> transform(OpStream<Node> opStream, Op<Node> subOp) {
    return transform.transform(opStream, subOp);
  }

  @Override
  public Op<Node> transform(OpService<Node> opService, Op<Node> subOp) {
    return transform.transform(opService, subOp);
  }

  @Override
  public Op<Node> transform(OpAssign<Node> opAssign, Op<Node> subOp) {
    return transform.transform(opAssign, subOp);
  }

  @Override
  public Op<Node> transform(OpExtend<Node> opExtend, Op<Node> subOp) {
    return transform.transform(opExtend, subOp);
  }

  @Override
  public Op<Node> transform(OpJoin<Node> opJoin, Op<Node> left, Op<Node> right) {
    return transform.transform(opJoin, left, right);
  }

  @Override
  public Op<Node> transform(OpLeftJoin<Node> opLeftJoin, Op<Node> left, Op<Node> right) {
    return transform.transform(opLeftJoin, left, right);
  }

  @Override
  public Op<Node> transform(OpDiff<Node> opDiff, Op<Node> left, Op<Node> right) {
    return transform.transform(opDiff, left, right);
  }

  @Override
  public Op<Node> transform(OpMinus<Node> opMinus, Op<Node> left, Op<Node> right) {
    return transform.transform(opMinus, left, right);
  }

  @Override
  public Op<Node> transform(OpUnion<Node> opUnion, Op<Node> left, Op<Node> right) {
    return transform.transform(opUnion, left, right);
  }

  @Override
  public Op<Node> transform(OpConditional<Node> opCond, Op<Node> left, Op<Node> right) {
    return transform.transform(opCond, left, right);
  }

  @Override
  public Op<Node> transform(OpSequence<Node> opSequence, List<Op<Node>> elts) {
    return transform.transform(opSequence, elts);
  }

  @Override
  public Op<Node> transform(OpDisjunction<Node> opDisjunction, List<Op<Node>> elts) {
    return transform.transform(opDisjunction, elts);
  }

  // @Override
  // public Op<Node> org.rdf4led.sparql.transform(OpExt<Node> opExt) { return
  // org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(opExt) ; }

  @Override
  public Op<Node> transform(OpNull<Node> opNull) {
    return transform.transform(opNull);
  }

  @Override
  public Op<Node> transform(OpLabel<Node> opLabel, Op<Node> subOp) {
    return transform.transform(opLabel, subOp);
  }

  @Override
  public Op<Node> transform(OpList<Node> opList, Op<Node> subOp) {
    return transform.transform(opList, subOp);
  }

  @Override
  public Op<Node> transform(OpOrder<Node> opOrder, Op<Node> subOp) {
    return transform.transform(opOrder, subOp);
  }

  @Override
  public Op<Node> transform(OpTopN<Node> opTop, Op<Node> subOp) {
    return transform.transform(opTop, subOp);
  }

  @Override
  public Op<Node> transform(OpProject<Node> opProject, Op<Node> subOp) {
    return transform.transform(opProject, subOp);
  }

  @Override
  public Op<Node> transform(OpDistinct<Node> opDistinct, Op<Node> subOp) {
    return transform.transform(opDistinct, subOp);
  }

  @Override
  public Op<Node> transform(OpReduced<Node> opReduced, Op<Node> subOp) {
    return transform.transform(opReduced, subOp);
  }

  @Override
  public Op<Node> transform(OpSlice<Node> opSlice, Op<Node> subOp) {
    return transform.transform(opSlice, subOp);
  }

  @Override
  public Op<Node> transform(OpGroup<Node> opGroup, Op<Node> subOp) {
    return transform.transform(opGroup, subOp);
  }
}
