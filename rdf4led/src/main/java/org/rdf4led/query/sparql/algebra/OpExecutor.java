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

import org.rdf4led.graph.Graph;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.QueryIterator;
import org.rdf4led.query.sparql.algebra.op.*;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Turn an Op expression into an execution of QueryIterators.
 *
 * <p>Does not consider optimizing the algebra expression (that should happen elsewhere). BGPs are
 * still subject to StageBuilding during iterator execution.
 *
 * <p>During execution, when a substitution into an algebra expression happens (in other words, a
 * streaming operation, storage-join-like), there is a call into the executor each time so it does not
 * just happen once before a query starts.
 */
public class OpExecutor<Node> {
  //    private static <Node> OpExecutor createOpExecutor(QueryContext<Node> queryContext) {
  //        OpExecutorFactory factory = execCxt.getExecutor() ;
  //        if (factory == null)
  //            factory = stdFactory ;
  //        if (factory == null)
  //            return new OpExecutor(execCxt) ;
  //        return factory.create(execCxt) ;
  //    }

  // -------

  //    static QueryIterator execute(Op org.rdf4led.sparql.algebra.op, QueryContext queryContext) {
  //        return execute(org.rdf4led.sparql.algebra.op, createRootQueryIterator(queryContext), queryContext) ;
  //    }

  /** Public interface is via QC.execute. * */
  //    static QueryIterator execute(Op org.rdf4led.sparql.algebra.op, QueryIterator qIter, QueryContext queryContext) {
  //        OpExecutor exec = new OpExecutor(queryContext) ;
  //        QueryIterator q = exec.exec(org.rdf4led.sparql.algebra.op, qIter) ;
  //        return q ;
  //    }

  // -------- The object starts here --------

  protected QueryContext<Node> queryContext;

  protected OpExecutorDispatch<Node> dispatcher = null;

  protected static final int TOP_LEVEL = 0;

  protected int level = TOP_LEVEL - 1;
  //    private final boolean          hideBNodeVars ;
  //    protected final StageGenerator stageGenerator ;

  Graph<Node> graph;

  public OpExecutor(QueryContext<Node> queryContext, Graph<Node> graph) {
    this.queryContext = queryContext;

    this.dispatcher = new OpExecutorDispatch(this);
    //        this.hideBNodeVars = execCxt.getContext().isTrue(ARQ.hideNonDistiguishedVariables) ;
    //        this.stageGenerator = StageBuilder.chooseStageGenerator(execCxt.getContext()) ;

    this.graph = graph;
  }

  // Public interface
  public QueryIterator<Node> executeOp(Op<Node> op, QueryIterator<Node> input) {
    return exec(op, input);
  }

  // ---- The recursive step.
  protected QueryIterator<Node> exec(Op<Node> op, QueryIterator<Node> input) {
    level++;
    QueryIterator<Node> qIter = dispatcher.exec(op, input);
    // Intentionally not try/finally so exceptions leave some evidence
    // around.
    level--;
    return qIter;
  }

  // ---- All the cases

  protected QueryIterator<Node> execute(OpBGP<Node> opBGP, QueryIterator<Node> input) {
    BasicPattern<Node> pattern = opBGP.getPattern();
    //        QueryIterator qIter = stageGenerator.execute(pattern, input, queryContext) ;
    //        if (hideBNodeVars)
    //            qIter = new QueryIterDistinguishedVars(qIter, execCxt) ;
    //return new QueryIterBlockTriples<Node>(input, pattern, graph, queryContext);
    return null;
  }

  protected QueryIterator<Node> execute(OpTriple<Node> opTriple, QueryIterator<Node> input) {
    return execute(opTriple.asBGP(), input);
  }

  protected QueryIterator<Node> execute(OpGraph<Node> opGraph, QueryIterator<Node> input) {
    //        QueryIterator qIter = specialcase(opGraph.getNode(), opGraph.getSubOp(), input) ;
    //        if (qIter != null)
    //            return qIter ;
    //        return new QueryIterGraph(input, opGraph, execCxt) ;

    return null;
  }

  private QueryIterator specialcase(Node gn, Op subOp, QueryIterator input) {
    // This is a placeholder for code to specially handle explicitly named
    // default graph and union graph.

    //        if (Quad.isDefaultGraph(gn)) {
    //            ExecutionContext cxt2 = new ExecutionContext(execCxt,
    // execCxt.getDataset().getDefaultGraph()) ;
    //            return execute(subOp, input, cxt2) ;
    //        }
    //
    //        if ( Quad.isUnionGraph(gn) )
    //            Log.warn(this, "Not implemented yet: union default graph in general OpExecutor") ;

    // Bad news -- if ( Lib.equals(gn, Quad.tripleInQuad) ) {}
    return null;
  }

  protected QueryIterator execute(OpQuad opQuad, QueryIterator input) {
    return execute(opQuad.asQuadPattern(), input);
  }

  protected QueryIterator execute(OpQuadPattern quadPattern, QueryIterator input) {
    //        // Convert to BGP forms to execute in this graph-centric engine.
    //        if (quadPattern.isDefaultGraph() && execCxt.getActiveGraph() ==
    // execCxt.getDataset().getDefaultGraph()) {
    //            // Note we tested that the containing graph was the dataset's
    //            // default graph.
    //            // Easy case.
    //            OpBGP opBGP = new OpBGP(quadPattern.getBasicPattern()) ;
    //            return execute(opBGP, input) ;
    //        }
    //        // Not default graph - (graph .... )
    //        OpBGP opBGP = new OpBGP(quadPattern.getBasicPattern()) ;
    //        OpGraph org.rdf4led.sparql.algebra.op = new OpGraph(quadPattern.getGraphNode(), opBGP) ;
    //        return execute(org.rdf4led.sparql.algebra.op, input) ;

    return null;
  }

  //    protected QueryIterator execute(OpQuadBlock quadBlock, QueryIterator input) {
  //        Op org.rdf4led.sparql.algebra.op = quadBlock.convertOp() ;
  //        return exec(org.rdf4led.sparql.algebra.op, input) ;
  //    }

  protected QueryIterator<Node> execute(OpPath<Node> opPath, QueryIterator<Node> input) {
    return null;//new QueryIterPath<Node>(graph, opPath.getTriplePath(), input, queryContext);
  }

  //    protected QueryIterator execute(OpProcedure opProc, QueryIterator input) {
  //        Procedure procedure = ProcEval.build(opProc, execCxt) ;
  //        QueryIterator qIter = exec(opProc.getSubOp(), input) ;
  //        // Delay until query starts executing.
  //        return new QueryIterProcedure(qIter, procedure, execCxt) ;
  //    }

  //    protected QueryIterator execute(OpPropFunc opPropFunc, QueryIterator input) {
  //        Procedure procedure = ProcEval.build(opPropFunc.getProperty(),
  // opPropFunc.getSubjectArgs(),
  //                opPropFunc.getObjectArgs(), execCxt) ;
  //        QueryIterator qIter = exec(opPropFunc.getSubOp(), input) ;
  //        return new QueryIterProcedure(qIter, procedure, execCxt) ;
  //    }

  protected QueryIterator execute(OpJoin opJoin, QueryIterator input) {
    // Need to clone input into left and right.
    // Do by evaling for each input case, the left and right and concat'ing
    // the results.

    //        if (false) {
    //            // If needed, applies to OpDiff and OpLeftJoin as well.
    //            List<Mapping> a = all(input) ;
    //            QueryIterator qIter1 = new QueryIterPlainWrapper(a.iterator(), execCxt) ;
    //            QueryIterator qIter2 = new QueryIterPlainWrapper(a.iterator(), execCxt) ;
    //
    //            QueryIterator left = exec(opJoin.getLeft(), qIter1) ;
    //            QueryIterator right = exec(opJoin.getRight(), qIter2) ;
    //            QueryIterator qIter = new QueryIterJoin(left, right, execCxt) ;
    //            return qIter ;
    //        }

    //        QueryIterator left = exec(opJoin.getLeft(), input) ;
    //        QueryIterator right = exec(opJoin.getRight(), root()) ;
    //        QueryIterator qIter = new QueryIterJoin(left, right, queryContext) ;
    //        return qIter ;

    return input;
  }

  // Pass iterator from one step directly into the next.
  protected QueryIterator<Node> execute(OpSequence<Node> opSequence, QueryIterator input) {
    QueryIterator qIter = input;

    for (Iterator<Op<Node>> iter = opSequence.iterator(); iter.hasNext(); ) {
      Op sub = iter.next();
      qIter = exec(sub, qIter);
    }
    return qIter;
  }

  protected QueryIterator execute(OpLeftJoin opLeftJoin, QueryIterator input) {
    //        QueryIterator left = exec(opLeftJoin.getLeft(), input) ;
    //        QueryIterator right = exec(opLeftJoin.getRight(), root()) ;
    //        QueryIterator qIter = new QueryIterLeftJoin(left, right, opLeftJoin.getExprs(),
    // execCxt) ;
    return input;
  }

  protected QueryIterator execute(OpConditional opCondition, QueryIterator input) {
    //        QueryIterator left = exec(opCondition.getLeft(), input) ;
    //        QueryIterator qIter = new QueryIterOptionalIndex(left, opCondition.getRight(),
    // execCxt) ;
    return input;
  }

  protected QueryIterator execute(OpDiff opDiff, QueryIterator input) {
    //        QueryIterator left = exec(opDiff.getLeft(), input) ;
    //        QueryIterator right = exec(opDiff.getRight(), root()) ;
    //        return new QueryIterDiff(left, right, execCxt) ;
    return input;
  }

  protected QueryIterator execute(OpMinus opMinus, QueryIterator input) {
    //        Op lhsOp = opMinus.getLeft() ;
    //        Op rhsOp = opMinus.getRight() ;
    //
    //        QueryIterator left = exec(lhsOp, input) ;
    //        QueryIterator right = exec(rhsOp, root()) ;
    //
    //        Set<Var> commonVars = OpVars.visibleVars(lhsOp) ;
    //        commonVars.retainAll(OpVars.visibleVars(rhsOp)) ;
    //
    //        return new QueryIterMinus(left, right, commonVars, execCxt) ;
    return input;
  }

  protected QueryIterator execute(OpDisjunction opDisjunction, QueryIterator input) {
    //        QueryIterator cIter = new QueryIterUnion(input, opDisjunction.getElements(), execCxt)
    // ;
    //        return cIter ;
    return input;
  }

  protected QueryIterator execute(OpUnion opUnion, QueryIterator input) {
    //        List<Op> x = flattenUnion(opUnion) ;
    //        QueryIterator cIter = new QueryIterUnion(input, x, execCxt) ;
    //        return cIter ;
    return input;
  }

  // Based on code from Olaf Hartig.
  protected List<Op> flattenUnion(OpUnion opUnion) {
    List<Op> x = new ArrayList<>();
    flattenUnion(x, opUnion);
    return x;
  }

  protected void flattenUnion(List<Op> acc, OpUnion opUnion) {
    if (opUnion.getLeft() instanceof OpUnion) flattenUnion(acc, (OpUnion) opUnion.getLeft());
    else acc.add(opUnion.getLeft());

    if (opUnion.getRight() instanceof OpUnion) flattenUnion(acc, (OpUnion) opUnion.getRight());
    else acc.add(opUnion.getRight());
  }

  protected QueryIterator<Node> execute(OpFilter<Node> opFilter, QueryIterator<Node> input) {
    ExprList<Node> exprs = opFilter.getExprs();

    Op<Node> base = opFilter.getSubOp();

    QueryIterator<Node> qIter = exec(base, input);

    for (Expr expr : exprs) {
      //qIter = new QueryIterFilterExpr(qIter, expr, queryContext);
    }
    return qIter;
  }

  protected QueryIterator execute(OpService opService, QueryIterator input) {
    //        return new QueryIterService(input, opService, execCxt) ;
    return input;
  }

  // Quad form, "GRAPH ?g {}" Flip back to OpGraph.
  // Normally quad stores override this.
  //    protected QueryIterator execute(OpDatasetNames dsNames, QueryIterator input) {
  //        if (false) {
  //            OpGraph org.rdf4led.sparql.algebra.op = new OpGraph(dsNames.getGraphNode(), new OpBGP()) ;
  //            return execute(org.rdf4led.sparql.algebra.op, input) ;
  //        }
  //        throw new ARQNotImplemented("execute/OpDatasetNames") ;
  //    }

  protected QueryIterator execute(OpTable opTable, QueryIterator input) {
    //        if (opTable.isJoinIdentity())
    //            return input ;
    //        if (input instanceof QueryIterRoot) {
    //            input.close() ;
    //            return opTable.getTable().iterator(execCxt) ;
    //        }
    //        QueryIterator qIterT = opTable.getTable().iterator(execCxt) ;
    //        QueryIterator qIter = new QueryIterJoin(input, qIterT, execCxt) ;
    //        return qIter ;
    return input;
  }

  //    protected QueryIterator execute(OpExt opExt, QueryIterator input) {
  //        try {
  //            QueryIterator qIter = opExt.eval(input, execCxt) ;
  //            if (qIter != null)
  //                return qIter ;
  //        } catch (UnsupportedOperationException ex) {}
  //        // null or UnsupportedOperationException
  //        throw new QueryExecException("Encountered unsupported OpExt: " + opExt.getName()) ;
  //    }

  protected QueryIterator execute(OpLabel opLabel, QueryIterator input) {
    if (!opLabel.hasSubOp()) return input;

    return exec(opLabel.getSubOp(), input);
  }

  protected QueryIterator execute(OpNull opNull, QueryIterator input) {
    // Loose the input.
    input.close();
    //        return QueryIterNullIterator.create(execCxt) ;
    return null; //new QueryIterNullIterator(queryContext);

  }

  protected QueryIterator execute(OpList opList, QueryIterator input) {
    return exec(opList.getSubOp(), input);
  }

  protected QueryIterator execute(OpOrder opOrder, QueryIterator input) {
    //        QueryIterator qIter = exec(opOrder.getSubOp(), input) ;
    //        qIter = new QueryIterSort(qIter, opOrder.getConditions(), execCxt) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpTopN opTop, QueryIterator input) {
    //        QueryIterator qIter = null ;
    //        // We could also do (reduced) here as well.
    //        // but it's detected in TransformTopN and turned into (distinct)
    //        // there so that code catches that already.
    //        // We leave this to do the strict case of (top (distinct ...))
    //        if (opTop.getSubOp() instanceof OpDistinct) {
    //            OpDistinct opDistinct = (OpDistinct)opTop.getSubOp() ;
    //            qIter = exec(opDistinct.getSubOp(), input) ;
    //            qIter = new QueryIterTopN(qIter, opTop.getConditions(), opTop.getLimit(), true,
    // execCxt) ;
    //        } else {
    //            qIter = exec(opTop.getSubOp(), input) ;
    //            qIter = new QueryIterTopN(qIter, opTop.getConditions(), opTop.getLimit(), false,
    // execCxt) ;
    //        }
    //        return qIter ;
    return input;
  }

  protected QueryIterator<Node> execute(OpProject<Node> opProject, QueryIterator<Node> input) {
    // This may be under a (graph) in which case we need to operate
    // on the active graph.

    // More intelligent QueryIterProject needed.

//    if (input instanceof QueryIterRoot) {
//      QueryIterator<Node> qIter = exec(opProject.getSubOp(), input);
//
//      qIter = new QueryIterProject<Node>(qIter, opProject.getVars(), queryContext);
//
//      return qIter;
//    }
    // Nested projected : need to ensure the input is seen.
    //        QueryIterator qIter = new QueryIterProjectMerge(opProject, input, this, queryContext)
    // ;
    //
    //        return qIter ;

    return input;
  }

  protected QueryIterator execute(OpSlice opSlice, QueryIterator input) {
    //        QueryIterator qIter = exec(opSlice.getSubOp(), input) ;
    //        qIter = new QueryIterSlice(qIter, opSlice.getStart(), opSlice.getLength(), execCxt) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpGroup opGroup, QueryIterator input) {
    //        QueryIterator qIter = exec(opGroup.getSubOp(), input) ;
    //        qIter = new QueryIterGroup(qIter, opGroup.getGroupVars(), opGroup.getAggregators(),
    // execCxt) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpDistinct opDistinct, QueryIterator input) {
    //        QueryIterator qIter = exec(opDistinct.getSubOp(), input) ;
    //        qIter = new QueryIterDistinct(qIter, execCxt) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpReduced opReduced, QueryIterator input) {
    //        QueryIterator qIter = exec(opReduced.getSubOp(), input) ;
    //        qIter = new QueryIterReduced(qIter, execCxt) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpAssign opAssign, QueryIterator input) {
    //        QueryIterator qIter = exec(opAssign.getSubOp(), input) ;
    //        qIter = new QueryIterAssign(qIter, opAssign.getVarExprList(), execCxt, false) ;
    //        return qIter ;
    return input;
  }

  protected QueryIterator execute(OpExtend opExtend, QueryIterator input) {
    //        // We know (parse time checking) the variable is unused so far in
    //        // the query so we can use QueryIterAssign knowing that it behaves
    //        // the same as extend. The boolean should only be a check.
    //        QueryIterator qIter = exec(opExtend.getSubOp(), input) ;
    //        qIter = new QueryIterAssign(qIter, opExtend.getVarExprList(), execCxt, true) ;
    //        return qIter ;
    return input;
  }

  public static QueryIterator createRootQueryIterator(QueryContext queryContext) {
//    return QueryIterRoot.create(queryContext);
    return null;
  }

  protected QueryIterator root() {
    return createRootQueryIterator(queryContext);
  }

  // Use this to debug evaluation
  // Example:
  // input = debug(input) ;
  //    private QueryIterator debug(String marker, QueryIterator input) {
  //        List<Mapping> x = all(input) ;
  //        for (Mapping b : x) {
  //            System.out.print(marker) ;
  //            System.out.print(": ") ;
  //            System.out.println(b) ;
  //        }
  //
  //        return new QueryIterPlainWrapper(x.iterator(), queryContext) ;
  //    }

  //    private static List<Mapping> all(QueryIterator input) {
  //        return Iter.toList(input) ;
  //    }
}
