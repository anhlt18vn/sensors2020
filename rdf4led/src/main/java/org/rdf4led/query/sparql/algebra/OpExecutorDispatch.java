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

import org.rdf4led.query.sparql.QueryIterator;
import org.rdf4led.query.sparql.algebra.op.*;

import java.util.ArrayDeque;
import java.util.Deque;

/** Class to provide type-safe execution dispatch using the visitor support of Op */
class OpExecutorDispatch<Node> implements OpVisitor<Node> {
  private Deque<QueryIterator<Node>> stack = new ArrayDeque<QueryIterator<Node>>();

  private OpExecutor<Node> opExecutor;

  OpExecutorDispatch(OpExecutor<Node> exec) {
    opExecutor = exec;
  }

  QueryIterator<Node> exec(Op<Node> op, QueryIterator<Node> input) {
    push(input);

    int x = stack.size();

    op.visit(this);

    int y = stack.size();

    QueryIterator<Node> qIter = pop();

    return qIter;
  }

  @Override
  public void visit(OpBGP<Node> opBGP) {
    QueryIterator<Node> input = pop();

    QueryIterator<Node> qIter = opExecutor.execute(opBGP, input);

    push(qIter);
  }

  @Override
  public void visit(OpQuadPattern<Node> quadPattern) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(quadPattern, input);
    push(qIter);
  }

  @Override
  public void visit(OpTriple<Node> opTriple) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opTriple, input);
    push(qIter);
  }

  @Override
  public void visit(OpQuad<Node> opQuad) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opQuad, input);
    push(qIter);
  }

  @Override
  public void visit(OpPath<Node> opPath) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opPath, input);
    push(qIter);
  }

  @Override
  public void visit(OpProcedure<Node> opProc) {
    //        QueryIterator<Node> input = pop() ;
    //        QueryIterator<Node> qIter = opExecutor.execute(opProc, input) ;
    //        push(qIter) ;
  }

  @Override
  public void visit(OpStream<Node> opStream) {}

  //    @Override
  //    public void visit(OpPropFunc<Node> opPropFunc)
  //    {
  //        QueryIterator<Node> input = pop() ;
  //        QueryIterator<Node> qIter = opExecutor.execute(opPropFunc, input) ;
  //        push(qIter) ;
  //    }

  @Override
  public void visit(OpJoin<Node> opJoin) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opJoin, input);
    push(qIter);
  }

  @Override
  public void visit(OpSequence<Node> opSequence) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opSequence, input);
    push(qIter);
  }

  @Override
  public void visit(OpDisjunction<Node> opDisjunction) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opDisjunction, input);
    push(qIter);
  }

  @Override
  public void visit(OpLeftJoin<Node> opLeftJoin) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opLeftJoin, input);
    push(qIter);
  }

  @Override
  public void visit(OpDiff<Node> opDiff) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opDiff, input);
    push(qIter);
  }

  @Override
  public void visit(OpMinus<Node> opMinus) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opMinus, input);
    push(qIter);
  }

  @Override
  public void visit(OpUnion<Node> opUnion) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opUnion, input);
    push(qIter);
  }

  @Override
  public void visit(OpConditional<Node> opCondition) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opCondition, input);
    push(qIter);
  }

  @Override
  public void visit(OpFilter<Node> opFilter) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opFilter, input);
    push(qIter);
  }

  @Override
  public void visit(OpGraph<Node> opGraph) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opGraph, input);
    push(qIter);
  }

  @Override
  public void visit(OpService<Node> opService) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opService, input);
    push(qIter);
  }

  //    @Override
  //    public void visit(OpDatasetNames dsNames)
  //    {
  //        QueryIterator input = pop() ;
  //        QueryIterator qIter = opExecutor.execute(dsNames, input) ;
  //        push(qIter) ;
  //    }

  @Override
  public void visit(OpTable<Node> opTable) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opTable, input);
    push(qIter);
  }

  //    @Override
  //    public void visit(OpExt<Node> opExt)
  //    {
  //        QueryIterator<Node> input = pop() ;
  //        QueryIterator<Node> qIter = opExecutor.execute(opExt, input) ;
  //        push(qIter) ;
  //    }

  @Override
  public void visit(OpNull<Node> opNull) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opNull, input);
    push(qIter);
  }

  @Override
  public void visit(OpLabel<Node> opLabel) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opLabel, input);
    push(qIter);
  }

  @Override
  public void visit(OpList<Node> opList) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opList, input);
    push(qIter);
  }

  @Override
  public void visit(OpOrder<Node> opOrder) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opOrder, input);
    push(qIter);
  }

  @Override
  public void visit(OpProject<Node> opProject) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opProject, input);
    push(qIter);
  }

  @Override
  public void visit(OpDistinct<Node> opDistinct) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opDistinct, input);
    push(qIter);
  }

  @Override
  public void visit(OpReduced<Node> opReduced) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opReduced, input);
    push(qIter);
  }

  @Override
  public void visit(OpAssign<Node> opAssign) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opAssign, input);
    push(qIter);
  }

  @Override
  public void visit(OpExtend<Node> opExtend) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opExtend, input);
    push(qIter);
  }

  @Override
  public void visit(OpSlice<Node> opSlice) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opSlice, input);
    push(qIter);
  }

  @Override
  public void visit(OpGroup<Node> opGroup) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opGroup, input);
    push(qIter);
  }

  @Override
  public void visit(OpTopN<Node> opTop) {
    QueryIterator<Node> input = pop();
    QueryIterator<Node> qIter = opExecutor.execute(opTop, input);
    push(qIter);
  }

  private void push(QueryIterator<Node> qIter) {
    stack.push(qIter);
  }

  private QueryIterator<Node> pop() {
    return stack.pop();
  }
}
