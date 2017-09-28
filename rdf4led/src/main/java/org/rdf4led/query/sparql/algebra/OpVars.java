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

import org.rdf4led.graph.Quad;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.SortCondition;
import org.rdf4led.query.sparql.algebra.op.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Get vars for a pattern */
public class OpVars<Node> {
  private OpWalker<Node> opWalker;

  private QueryContext<Node> queryContext;

  public OpVars(QueryContext<Node> queryContext) {
    opWalker = new OpWalker<Node>();

    this.queryContext = queryContext;
  }

  public Set<Node> patternVars(Op<Node> op) {
    Set<Node> acc = new HashSet<Node>();

    patternVars(op, acc);

    return acc;
  }

  public void patternVars(Op<Node> op, Set<Node> acc) {
    OpVisitor<Node> visitor = new OpVarsPattern(acc);

    opWalker.walk(new WalkerVisitorSkipMinus(visitor), op, visitor);
  }

  public Collection<Node> allVars(Op<Node> op) {
    Set<Node> acc = new HashSet<Node>();

    allVars(op, acc);

    return acc;
  }

  public void allVars(Op<Node> op, Set<Node> acc) {
    opWalker.walk(op, new OpVarsQuery(acc));
  }

  public Collection<Node> vars(BasicPattern<Node> pattern) {
    Set<Node> acc = new HashSet<Node>();

    vars(pattern, acc);

    return acc;
  }

  public void vars(BasicPattern<Node> pattern, Collection<Node> acc) {
    for (Triple<Node> triple : pattern) {
      addVarsFromTriple(acc, triple);
    }
  }

  public void addVarsFromTriple(Collection<Node> acc, Triple<Node> t) {
    addVar(acc, t.getSubject());

    addVar(acc, t.getPredicate());

    addVar(acc, t.getObject());
  }

  private void addVarsFromQuad(Collection<Node> acc, Quad<Node> q) {
    addVar(acc, q.getSubject());
    addVar(acc, q.getPredicate());
    addVar(acc, q.getObject());
    addVar(acc, q.getGraphNode());
  }

  public void addVar(Collection<Node> acc, Node node) {
    if (node == null) {
      return;
    }

    if (queryContext.isVarNode(node)) {
      acc.add(queryContext.allocVarNode(node));
    }
  }

  public void addVars(Collection<Node> acc, BasicPattern<Node> pattern) {
    addVars(acc, pattern.getList());
  }

  public void addVars(Collection<Node> acc, Collection<Triple<Node>> triples) {
    for (Triple<Node> triple : triples) {
      addVarsFromTriple(acc, triple);
    }
  }

  /** Don't accumulate RHS of OpMinus */
  class WalkerVisitorSkipMinus extends WalkerVisitor<Node> {
    public WalkerVisitorSkipMinus(OpVisitor<Node> visitor) {
      super(visitor);
    }

    @Override
    public void visit(OpMinus<Node> op) {
      before(op);

      if (op.getLeft() != null) op.getLeft().visit(this);

      // Skip right.
      // if ( org.rdf4led.sparql.algebra.op.getRight() != null ) org.rdf4led.sparql.algebra.op.getRight().visit(this) ;
      if (visitor != null) {
        op.visit(visitor);
      }

      after(op);
    }
  }

  private class OpVarsPattern extends OpVisitorBase<Node> {
    // The possibly-set-vars
    protected Set<Node> acc;

    OpVarsPattern(Set<Node> acc) {
      this.acc = acc;
    }

    @Override
    public void visit(OpBGP<Node> opBGP) {
      vars(opBGP.getPattern(), acc);
    }

    // @Override
    // public void visit(OpPath<Node> opPath)
    // {
    // addVar(org.rdf4led.common.data.incremental.acc, opPath.getTriplePath().getSubject());
    //
    // addVar(org.rdf4led.common.data.incremental.acc, opPath.getTriplePath().getObject());
    // }

    @Override
    public void visit(OpQuadPattern<Node> quadPattern) {
      addVar(acc, quadPattern.getGraphNode());

      vars(quadPattern.getBasicPattern(), acc);
    }

    @Override
    public void visit(OpGraph<Node> opGraph) {
      addVar(acc, opGraph.getNode());
    }

    @Override
    public void visit(OpProject<Node> opProject) {
      acc.clear();

      acc.addAll(opProject.getVars());
    }

    @Override
    public void visit(OpAssign<Node> opAssign) {
      acc.addAll(opAssign.getVarExprList().getVars());
    }

    @Override
    public void visit(OpExtend<Node> opExtend) {
      acc.addAll(opExtend.getVarExprList().getVars());
    }

    @Override
    public void visit(OpProcedure<Node> opProc) {
      opProc.getArgs().varsMentioned(acc);
    }
  }

  private class OpVarsQuery extends OpVarsPattern {
    OpVarsQuery(Set<Node> acc) {
      super(acc);
    }

    @Override
    public void visit(OpFilter<Node> opFilter) {
      opFilter.getExprs().varsMentioned(acc);
    }

    @Override
    public void visit(OpOrder<Node> opOrder) {
      for (Iterator<SortCondition<Node>> iter = opOrder.getConditions().iterator();
          iter.hasNext(); ) {
        SortCondition<Node> sc = iter.next();

        Set<Node> x = sc.getExpression().getVarsMentioned();

        acc.addAll(x);
      }
    }
  }
}
