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

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.SortCondition;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.expr.VarExprList;
import org.rdf4led.query.expr.aggs.Aggregator;
import org.rdf4led.query.expr.aggs.ExprAggregator;
import org.rdf4led.query.sparql.algebra.*;
import org.rdf4led.query.sparql.algebra.op.*;

import java.util.*;

/** A botton-top application of a transformation of SPARQL algebra */
public class Transformer<Node> {
  QueryContext<Node> exeContext;
  OpWalker<Node> opWalker;

  public Transformer(QueryContext<Node> exeContext) {
    this.exeContext = exeContext;

    opWalker = new OpWalker<Node>();
  }

  // private Transformer<Node> singleton = new Transformer<Node>();

  // TopQuadrant extend Transformer for use in their SPARQL debugger.
  /** Get the current transformer */
  // public Transformer<Node> get()
  // {
  // return singleton;
  // }

  /** Set the current transformer - use with care */
  // public void set(Transformer<Node> value)
  // {
  // this.singleton = value;
  // }

  /** Transform an algebra expression */
  public Op<Node> transform(Transform<Node> transform, Op<Node> op) {
    return transformation(transform, op, null, null);
  }

  public Op<Node> transform(
      Transform<Node> transform,
      Op<Node> op,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    return transformation(transform, op, beforeVisitor, afterVisitor);
  }

  /** Transform an algebra expression except skip (leave alone) any OpService nodes */
  public Op<Node> transformSkipService(Transform<Node> transform, Op<Node> op) {
    return transformSkipService(transform, op, null, null);
  }

  /** Transform an algebra expression except skip (leave alone) any OpService nodes */
  public Op<Node> transformSkipService(
      Transform<Node> transform,
      Op<Node> op,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    // Skip SERVICE
    if (true) {
      // Simplest way but still walks the OpService subtree (and throws
      // away the transformation).
      transform = new TransformSkipService(transform);

      return transform(transform, op, beforeVisitor, afterVisitor);

    } else {
      // Don't org.rdf4led.sparql.transform OpService and don't walk the sub-org.rdf4led.sparql.algebra.op
      ApplyTransformVisitorServiceAsLeaf v = new ApplyTransformVisitorServiceAsLeaf(transform);

      WalkerVisitorSkipService walker =
          new WalkerVisitorSkipService(v, beforeVisitor, afterVisitor);

      opWalker.walk(walker, op, v);

      return v.result();
    }
  }

  // To allow subclassing this class, we use a singleton pattern
  // and theses protected methods.
  protected Op<Node> transformation(
      Transform<Node> transform,
      Op<Node> op,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    ApplyTransformVisitor v = new ApplyTransformVisitor(transform);

    return transformation(v, op, beforeVisitor, afterVisitor);
  }

  protected Op<Node> transformation(
      ApplyTransformVisitor transformApply,
      Op<Node> op,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    if (op == null) {
      return op;
    }
    return applyTransformation(transformApply, op, beforeVisitor, afterVisitor);
  }

  /** The primitive operation to apply a transformation to an Op */
  protected Op<Node> applyTransformation(
      ApplyTransformVisitor transformApply,
      Op<Node> op,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    opWalker.walk(op, transformApply, beforeVisitor, afterVisitor);
    // OpWalker.walk(org.rdf4led.sparql.algebra.op, transformApply, beforeVisitor, afterVisitor);

    Op<Node> r = transformApply.result();

    return r;
  }

  public class ApplyTransformVisitor extends OpVisitorByType<Node> {
    protected final Transform<Node> transform;

    // private final ExprTransformApplyTransform<Node> exprTransform;

    private final Deque<Op<Node>> stack = new ArrayDeque<Op<Node>>();

    protected final Op<Node> pop() {
      return stack.pop();
    }

    protected final void push(Op<Node> op) {
      // Including nulls
      stack.push(op);
    }

    public ApplyTransformVisitor(Transform<Node> transform) {
      this.transform = transform;

      // this.exprTransform = new ExprTransformApplyTransform<Node>(org.rdf4led.sparql.transform, exeContext);

    }

    final Op<Node> result() {
      if (stack.size() != 1) {
        // Log.warn(this, "Stack is not aligned");
      }

      return pop();
    }

    // ----
    // Algebra operations that involve an Expr, and so might include NOT
    // EXISTS

    @Override
    public void visit(OpFilter<Node> opFilter) {
      ExprList<Node> ex = new ExprList<Node>();

      boolean changed = false;

      for (Expr<Node> e : opFilter.getExprs()) {
        Expr<Node> e2 = e; // exeContext.getExprTransformer().org.rdf4led.sparql.transform(exprTransform, e);
        // ExprTransformer.org.rdf4led.sparql.transform(exprTransform, e);
        ex.add(e2);

        if (e != e2) {
          changed = true;
        }
      }

      OpFilter<Node> f = opFilter;

      if (changed) {
        // f = OpFilter.filter(ex, opFilter.getSubOp());

        Op<Node> subOp = opFilter.getSubOp();

        if (!(subOp instanceof OpFilter)) subOp = new OpFilter<Node>(subOp);

        f = (OpFilter<Node>) subOp;

        f.getExprs().addAll(ex);
      }
      visit1(f);
    }

    @Override
    public void visit(OpOrder<Node> opOrder) {
      List<SortCondition<Node>> conditions = opOrder.getConditions();

      List<SortCondition<Node>> conditions2 = new ArrayList<SortCondition<Node>>();

      boolean changed = false;

      for (SortCondition<Node> sc : conditions) {
        Expr<Node> e = sc.getExpression();

        Expr<Node> e2 = e; // exeContext.getExprTransformer().org.rdf4led.sparql.transform(exprTransform, e);
        // ExprTransformer.org.rdf4led.sparql.transform(exprTransform, e);

        conditions2.add(new SortCondition<Node>(e2, sc.getDirection()));

        if (e != e2) {
          changed = true;
        }
      }

      OpOrder<Node> x = opOrder;

      if (changed) {
        x = new OpOrder<Node>(opOrder.getSubOp(), conditions2);
      }

      visit1(x);
    }

    @Override
    public void visit(OpAssign<Node> opAssign) {
      VarExprList<Node> varExpr = opAssign.getVarExprList();

      List<Node> vars = varExpr.getVars();

      VarExprList<Node> varExpr2 = process(varExpr);

      OpAssign<Node> opAssign2 = opAssign;

      if (varExpr != varExpr2) {
        opAssign2 = new OpAssign<Node>(opAssign.getSubOp(), varExpr2);
      }

      visit1(opAssign2);
    }

    private VarExprList<Node> process(VarExprList<Node> varExpr) {
      List<Node> vars = varExpr.getVars();

      VarExprList<Node> varExpr2 = new VarExprList<Node>();

      boolean changed = false;

      for (Node v : vars) {
        Expr<Node> e = varExpr.getExpr(v);

        Expr<Node> e2 = e;
        if (e != null) {
          e2 = e; // exeContext.getExprTransformer().org.rdf4led.sparql.transform(exprTransform, e);
          // ExprTransformer.org.rdf4led.sparql.transform(exprTransform, e);
        }

        if (e2 == null) {
          varExpr2.add(v);
        } else {
          varExpr2.add(v, e2);
        }

        if (e != e2) {
          changed = true;
        }
      }

      if (!changed) {
        return varExpr;
      }

      return varExpr2;
    }

    @Override
    public void visit(OpGroup<Node> opGroup) {
      boolean changed = false;

      VarExprList<Node> varExpr = opGroup.getGroupVars();

      VarExprList<Node> varExpr2 = process(varExpr);

      if (varExpr != varExpr2) {
        changed = true;
      }

      List<ExprAggregator<Node>> aggs = opGroup.getAggregators();

      List<ExprAggregator<Node>> aggs2 = aggs;

      // And the aggregators...
      aggs2 = new ArrayList<ExprAggregator<Node>>();

      for (ExprAggregator<Node> agg : aggs) {
        Aggregator<Node> aggregator = agg.getAggregator();

        Node v = agg.getVar();

        // Variable associated with the aggregate
        Expr<Node> eVar = agg.getAggVar(); // Not .getExprVar()

        Expr<Node> eVar2 = eVar; // exeContext.getExprTransformer().org.rdf4led.sparql.transform(exprTransform, eVar);
        // ExprTransformer.org.rdf4led.sparql.transform(exprTransform, eVar);

        if (eVar != eVar2) {
          changed = true;
        }

        // The Aggregator expression
        Expr<Node> e = aggregator.getExpr();

        Expr<Node> e2 = e;

        if (e != null) // Null means "no relevant expression" e.g.
        // COUNT(*)
        {
          // exeContext.getExprTransformer().org.rdf4led.sparql.transform(exprTransform, e);
        }

        if (e != e2) {
          changed = true;
        }

        Aggregator<Node> a2 = aggregator.copy(e2);

        aggs2.add(new ExprAggregator<Node>(eVar2.asVar(), a2, exeContext));
      }

      OpGroup<Node> opGroup2 = opGroup;

      if (changed) {
        opGroup2 = new OpGroup<Node>(opGroup.getSubOp(), varExpr2, aggs2);
      }

      visit1(opGroup2);
    }

    // ----

    @Override
    protected void visit0(Op0<Node> op) {
      push(op.apply(transform));
    }

    @Override
    protected void visit1(Op1<Node> op) {
      Op<Node> subOp = null;

      if (op.getSubOp() != null) {
        subOp = pop();
      }

      push(op.apply(transform, subOp));
    }

    @Override
    protected void visit2(Op2<Node> op) {
      Op<Node> left = null;

      Op<Node> right = null;

      // Must do right-left because the pushes onto the stack were
      // left-right.
      if (op.getRight() != null) {
        right = pop();
      }

      if (op.getLeft() != null) {
        left = pop();
      }

      Op<Node> opX = op.apply(transform, left, right);

      push(opX);
    }

    @Override
    protected void visitN(OpN<Node> op) {
      List<Op<Node>> x = new ArrayList<Op<Node>>(op.size());

      for (Iterator<Op<Node>> iter = op.iterator(); iter.hasNext(); ) {
        Op<Node> sub = iter.next();

        Op<Node> r = pop();
        // Skip nulls.
        if (r != null)
        // Add in reverse.
        {
          x.add(0, r);
        }
      }

      Op<Node> opX = op.apply(transform, x);

      push(opX);
    }

    //		@Override
    //		protected void visitExt(OpExt<Node> org.rdf4led.sparql.algebra.op)
    //		{
    //			push(org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(org.rdf4led.sparql.algebra.op));
    //		}
  }

  // --------------------------------
  // Transformations that avoid touching SERVICE.
  // Modified classes to avoid transforming SERVICE/OpService.
  // Plan A: In the application of the org.rdf4led.sparql.transform, skip OpService.

  /** Treat OpService as a leaf of the tree */
  class ApplyTransformVisitorServiceAsLeaf extends ApplyTransformVisitor {
    public ApplyTransformVisitorServiceAsLeaf(Transform<Node> transform) {
      super(transform);
    }

    @Override
    public void visit(OpService<Node> op) {
      // Treat as a leaf that does not change.
      push(op);
    }
  }

  // Plan B: The walker skips walking into OpService nodes.

  /** Don't walk down an OpService sub-operation */
  class WalkerVisitorSkipService extends WalkerVisitor<Node> {
    public WalkerVisitorSkipService(
        OpVisitor<Node> visitor, OpVisitor<Node> beforeVisitor, OpVisitor<Node> afterVisitor) {
      super(visitor, beforeVisitor, afterVisitor);
    }

    public WalkerVisitorSkipService(OpVisitor<Node> visitor) {
      super(visitor);
    }

    @Override
    public void visit(OpService<Node> op) {
      before(op);
      // visit1 code from WalkerVisitor
      // if ( org.rdf4led.sparql.algebra.op.getSubOp() != null ) org.rdf4led.sparql.algebra.op.getSubOp().visit(this) ;

      // Just visit the OpService node itself.
      // The transformer needs to push the code as a result (see
      // ApplyTransformVisitorSkipService)
      if (visitor != null) op.visit(visitor);

      after(op);
    }
  }

  // --------------------------------
  // Safe: ignore transformation of OpService and return the original.
  // Still walks the sub-org.rdf4led.sparql.algebra.op of OpService
  class TransformSkipService extends TransformWrapper<Node> {
    public TransformSkipService(Transform<Node> transform) {
      super(transform);
    }

    @Override
    public Op<Node> transform(OpService<Node> opService, Op<Node> subOp) {
      return opService;
    }
  }
}
