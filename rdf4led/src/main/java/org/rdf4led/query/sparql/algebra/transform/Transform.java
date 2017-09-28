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
 * Transform.java
 *
 * <p>Modified from Jena
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public interface Transform<Node> {
  // Op0
  public Op<Node> transform(OpTable<Node> opTable);

  public Op<Node> transform(OpBGP<Node> opBGP);

  public Op<Node> transform(OpTriple<Node> opTriple);

  public Op<Node> transform(OpQuad<Node> opQuad);

  public Op<Node> transform(OpPath<Node> opPath);

  // public Op org.rdf4led.sparql.transform(OpDatasetNames dsNames) ;

  public Op<Node> transform(OpQuadPattern<Node> quadPattern);

  public Op<Node> transform(OpNull<Node> opNull);

  // Op1
  public Op<Node> transform(OpFilter<Node> opFilter, Op<Node> subOp);

  public Op<Node> transform(OpGraph<Node> opGraph, Op<Node> subOp);

  public Op<Node> transform(OpStream<Node> opGraph, Op<Node> subOp);

  public Op<Node> transform(OpService<Node> opService, Op<Node> subOp);

  public Op<Node> transform(OpProcedure<Node> opProcedure, Op<Node> subOp);

  // public Op<Node> org.rdf4led.sparql.transform(OpPropFunc<Node> opPropFunc	 , Op<Node> subOp) ;

  public Op<Node> transform(OpLabel<Node> opLabel, Op<Node> subOp);

  public Op<Node> transform(OpAssign<Node> opAssign, Op<Node> subOp);

  public Op<Node> transform(OpExtend<Node> opExtend, Op<Node> subOp);

  // Op2
  public Op<Node> transform(OpJoin<Node> opJoin, Op<Node> left, Op<Node> right);

  public Op<Node> transform(OpLeftJoin<Node> opLeftJoin, Op<Node> left, Op<Node> right);

  public Op<Node> transform(OpDiff<Node> opDiff, Op<Node> left, Op<Node> right);

  public Op<Node> transform(OpMinus<Node> opMinus, Op<Node> left, Op<Node> right);

  public Op<Node> transform(OpUnion<Node> opUnion, Op<Node> left, Op<Node> right);

  public Op<Node> transform(OpConditional<Node> opCondition, Op<Node> left, Op<Node> right);

  // OpN
  public Op<Node> transform(OpSequence<Node> opSequence, List<Op<Node>> elts);

  public Op<Node> transform(OpDisjunction<Node> opDisjunction, List<Op<Node>> elts);

  // Extensions
  // public Op<Node> org.rdf4led.sparql.transform(OpExt<Node> opExt) ;

  // OpModifier
  public Op<Node> transform(OpList<Node> opList, Op<Node> subOp);

  public Op<Node> transform(OpOrder<Node> opOrder, Op<Node> subOp);

  public Op<Node> transform(OpTopN<Node> opTop, Op<Node> subOp);

  public Op<Node> transform(OpProject<Node> opProject, Op<Node> subOp);

  public Op<Node> transform(OpDistinct<Node> opDistinct, Op<Node> subOp);

  public Op<Node> transform(OpReduced<Node> opReduced, Op<Node> subOp);

  public Op<Node> transform(OpSlice<Node> opSlice, Op<Node> subOp);

  public Op<Node> transform(OpGroup<Node> opGroup, Op<Node> subOp);
}
