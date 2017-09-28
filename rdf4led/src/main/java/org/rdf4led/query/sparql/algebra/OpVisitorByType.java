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

package org.rdf4led.query.sparql.algebra;

import org.rdf4led.query.sparql.algebra.op.*;

/** A visitor helper that maps all visits to a few general ones */
public abstract class OpVisitorByType<Node> implements OpVisitor<Node> {
  protected abstract void visitN(OpN<Node> op);

  protected abstract void visit2(Op2<Node> op);

  protected abstract void visit1(Op1<Node> op);

  protected abstract void visit0(Op0<Node> op);

  //    protected abstract void visitExt(OpExt<Node> org.rdf4led.sparql.algebra.op) ;

  protected void visitModifer(OpModifier<Node> opMod) {
    visit1(opMod);
  }

  @Override
  public void visit(OpBGP<Node> opBGP) {
    visit0(opBGP);
  }

  @Override
  public void visit(OpQuadPattern<Node> quadPattern) {
    visit0(quadPattern);
  }

  @Override
  public void visit(OpTriple<Node> opTriple) {
    visit0(opTriple);
  }

  @Override
  public void visit(OpQuad<Node> opQuad) {
    visit0(opQuad);
  }

  @Override
  public void visit(OpPath<Node> opPath) {
    visit0(opPath);
  }

  @Override
  public void visit(OpProcedure<Node> opProcedure) {
    visit1(opProcedure);
  }

  //    @Override
  //    public void visit(OpPropFunc<Node> opPropFunc)
  //    { visit1(opPropFunc) ; }

  @Override
  public void visit(OpJoin<Node> opJoin) {
    visit2(opJoin);
  }

  @Override
  public void visit(OpSequence<Node> opSequence) {
    visitN(opSequence);
  }

  @Override
  public void visit(OpDisjunction<Node> opDisjunction) {
    visitN(opDisjunction);
  }

  @Override
  public void visit(OpLeftJoin<Node> opLeftJoin) {
    visit2(opLeftJoin);
  }

  @Override
  public void visit(OpDiff<Node> opDiff) {
    visit2(opDiff);
  }

  @Override
  public void visit(OpMinus<Node> opMinus) {
    visit2(opMinus);
  }

  @Override
  public void visit(OpUnion<Node> opUnion) {
    visit2(opUnion);
  }

  @Override
  public void visit(OpConditional<Node> opCond) {
    visit2(opCond);
  }

  @Override
  public void visit(OpFilter<Node> opFilter) {
    visit1(opFilter);
  }

  @Override
  public void visit(OpGraph<Node> opGraph) {
    visit1(opGraph);
  }

  @Override
  public void visit(OpStream<Node> opStream) {
    visit1(opStream);
  }

  @Override
  public void visit(OpService<Node> opService) {
    visit1(opService);
  }

  //    @Override
  //    public void visit(OpDatasetNames dsNames)
  //    { visit0(dsNames) ; }

  @Override
  public void visit(OpTable<Node> opUnit) {
    visit0(opUnit);
  }

  //    @Override
  //    public void visit(OpExt<Node> opExt)
  //    { visitExt(opExt) ; }

  @Override
  public void visit(OpNull<Node> opNull) {
    visit0(opNull);
  }

  @Override
  public void visit(OpLabel<Node> opLabel) {
    visit1(opLabel);
  }

  @Override
  public void visit(OpAssign<Node> opAssign) {
    visit1(opAssign);
  }

  @Override
  public void visit(OpExtend<Node> opExtend) {
    visit1(opExtend);
  }

  @Override
  public void visit(OpList<Node> opList) {
    visitModifer(opList);
  }

  @Override
  public void visit(OpOrder<Node> opOrder) {
    visitModifer(opOrder);
  }

  @Override
  public void visit(OpProject<Node> opProject) {
    visitModifer(opProject);
  }

  @Override
  public void visit(OpReduced<Node> opReduced) {
    visitModifer(opReduced);
  }

  @Override
  public void visit(OpDistinct<Node> opDistinct) {
    visitModifer(opDistinct);
  }

  @Override
  public void visit(OpSlice<Node> opSlice) {
    visitModifer(opSlice);
  }

  @Override
  public void visit(OpGroup<Node> opGroup) {
    visit1(opGroup);
  }

  @Override
  public void visit(OpTopN<Node> opTop) {
    visit1(opTop);
  }
}
