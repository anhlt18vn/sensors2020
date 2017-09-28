/// *
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
package org.rdf4led.query.sparql.algebra;

import org.rdf4led.query.sparql.algebra.op.*;

public class OpVisitorBase<Node> implements OpVisitor<Node> {

  @Override
  public void visit(OpBGP<Node> opBGP) {}

  @Override
  public void visit(OpQuadPattern<Node> quadPattern) {}

  @Override
  public void visit(OpTriple<Node> opTriple) {}

  @Override
  public void visit(OpQuad<Node> opQuad) {}

  @Override
  public void visit(OpPath<Node> opPath) {}

  @Override
  public void visit(OpProcedure<Node> opProc) {}

  //    @Override public void visit(OpPropFunc<Node> opPropFunc)         	{}

  @Override
  public void visit(OpJoin<Node> opJoin) {}

  @Override
  public void visit(OpSequence<Node> opSequence) {}

  @Override
  public void visit(OpDisjunction<Node> opDisjunction) {}

  @Override
  public void visit(OpLeftJoin<Node> opLeftJoin) {}

  @Override
  public void visit(OpConditional<Node> opCond) {}

  @Override
  public void visit(OpMinus<Node> opMinus) {}

  @Override
  public void visit(OpDiff<Node> opDiff) {}

  @Override
  public void visit(OpUnion<Node> opUnion) {}

  @Override
  public void visit(OpFilter<Node> opFilter) {}

  @Override
  public void visit(OpStream<Node> opStream) {}

  @Override
  public void visit(OpGraph<Node> opGraph) {}

  @Override
  public void visit(OpService<Node> opService) {}

  //    @Override public void visit(OpDatasetNames dsNames)         		{}

  @Override
  public void visit(OpTable<Node> opTable) {}

  //    @Override public void visit(OpExt<Node> opExt)                    {}

  @Override
  public void visit(OpNull<Node> opNull) {}

  @Override
  public void visit(OpLabel<Node> opLabel) {}

  @Override
  public void visit(OpAssign<Node> opAssign) {}

  @Override
  public void visit(OpExtend<Node> opExtend) {}

  @Override
  public void visit(OpList<Node> opList) {}

  @Override
  public void visit(OpOrder<Node> opOrder) {}

  @Override
  public void visit(OpProject<Node> opProject) {}

  @Override
  public void visit(OpDistinct<Node> opDistinct) {}

  @Override
  public void visit(OpReduced<Node> opReduced) {}

  @Override
  public void visit(OpSlice<Node> opSlice) {}

  @Override
  public void visit(OpGroup<Node> opGroup) {}

  @Override
  public void visit(OpTopN<Node> opTop) {}
}
