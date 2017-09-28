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

public interface OpVisitor<Node> {
  // Op0
  public void visit(OpBGP<Node> opBGP);

  public void visit(OpQuadPattern<Node> quadPattern);

  public void visit(OpTriple<Node> opTriple);

  public void visit(OpQuad<Node> opQuad);

  public void visit(OpPath<Node> opPath);

  public void visit(OpTable<Node> opTable);

  public void visit(OpNull<Node> opNull);

  // Op1
  public void visit(OpProcedure<Node> opProc);

  public void visit(OpStream<Node> opStream);

  // public void visit(OpPropFunc<Node> opPropFunc) ;

  public void visit(OpFilter<Node> opFilter);

  public void visit(OpGraph<Node> opGraph);

  public void visit(OpService<Node> opService);

  // public void visit(OpDatasetNames dsNames) ;

  public void visit(OpLabel<Node> opLabel);

  public void visit(OpAssign<Node> opAssign);

  public void visit(OpExtend<Node> opExtend);

  // Op2
  public void visit(OpJoin<Node> opJoin);

  public void visit(OpLeftJoin<Node> opLeftJoin);

  public void visit(OpUnion<Node> opUnion);

  public void visit(OpDiff<Node> opDiff);

  public void visit(OpMinus<Node> opMinus);

  public void visit(OpConditional<Node> opCondition);

  // OpN
  public void visit(OpSequence<Node> opSequence);

  public void visit(OpDisjunction<Node> opDisjunction);

  // public void visit(OpExt<Node> opExt) ;

  // OpModifier
  public void visit(OpList<Node> opList);

  public void visit(OpOrder<Node> opOrder);

  public void visit(OpProject<Node> opProject);

  public void visit(OpReduced<Node> opReduced);

  public void visit(OpDistinct<Node> opDistinct);

  public void visit(OpSlice<Node> opSlice);

  public void visit(OpGroup<Node> opGroup);

  public void visit(OpTopN<Node> opTop);
}
