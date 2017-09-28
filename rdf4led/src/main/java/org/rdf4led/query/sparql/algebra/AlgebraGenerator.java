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

import org.rdf4led.common.Pair;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.path.PathBlock;
import org.rdf4led.query.path.PathLib;
import org.rdf4led.query.path.TriplePath;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.Query;
import org.rdf4led.query.sparql.SortCondition;

import org.rdf4led.query.sparql.algebra.op.OpAssign;
import org.rdf4led.query.sparql.algebra.op.OpBGP;
import org.rdf4led.query.sparql.algebra.op.OpDistinct;
import org.rdf4led.query.sparql.algebra.op.OpExtend;
import org.rdf4led.query.sparql.algebra.op.OpFilter;
import org.rdf4led.query.sparql.algebra.op.OpGraph;
import org.rdf4led.query.sparql.algebra.op.OpGroup;
import org.rdf4led.query.sparql.algebra.op.OpJoin;
import org.rdf4led.query.sparql.algebra.op.OpLabel;
import org.rdf4led.query.sparql.algebra.op.OpLeftJoin;
import org.rdf4led.query.sparql.algebra.op.OpList;
import org.rdf4led.query.sparql.algebra.op.OpMinus;
import org.rdf4led.query.sparql.algebra.op.OpNull;
import org.rdf4led.query.sparql.algebra.op.OpOrder;
import org.rdf4led.query.sparql.algebra.op.OpProject;
import org.rdf4led.query.sparql.algebra.op.OpReduced;
import org.rdf4led.query.sparql.algebra.op.OpService;
import org.rdf4led.query.sparql.algebra.op.OpSlice;
import org.rdf4led.query.sparql.algebra.op.OpTable;
import org.rdf4led.query.sparql.algebra.op.OpUnion;

import org.rdf4led.query.sparql.algebra.transform.Transform;
import org.rdf4led.query.sparql.algebra.transform.TransformSimplify;
import org.rdf4led.query.sparql.algebra.transform.Transformer;

import org.rdf4led.query.expr.E_Exists;
import org.rdf4led.query.expr.E_LogicalNot;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.query.expr.VarExprList;
import org.rdf4led.query.expr.aggs.ExprAggregator;

import org.rdf4led.query.sparql.syntax.Element;
import org.rdf4led.query.sparql.syntax.ElementAssign;
import org.rdf4led.query.sparql.syntax.ElementBind;
import org.rdf4led.query.sparql.syntax.ElementData;
import org.rdf4led.query.sparql.syntax.ElementExists;
import org.rdf4led.query.sparql.syntax.ElementFetch;
import org.rdf4led.query.sparql.syntax.ElementFilter;
import org.rdf4led.query.sparql.syntax.ElementGroup;
import org.rdf4led.query.sparql.syntax.ElementMinus;
import org.rdf4led.query.sparql.syntax.ElementNamedGraph;
import org.rdf4led.query.sparql.syntax.ElementNotExists;
import org.rdf4led.query.sparql.syntax.ElementOptional;
import org.rdf4led.query.sparql.syntax.ElementPathBlock;
import org.rdf4led.query.sparql.syntax.ElementService;
import org.rdf4led.query.sparql.syntax.ElementStream;
import org.rdf4led.query.sparql.syntax.ElementSubQuery;
import org.rdf4led.query.sparql.syntax.ElementTriplesBlock;
import org.rdf4led.query.sparql.syntax.ElementUnion;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class AlgebraGenerator<Node> {
  private boolean fixedFilterPosition = false;

  private int subQueryDepth;

  private static boolean applySimplification = true; // False allows raw
  // algebra to be
  // generated (testing)

  private static boolean simplifyTooEarlyInAlgebraGeneration = false; // False
  // is
  // the
  // correct
  // setting.

  private QueryContext<Node> queryContext;

  protected Transform<Node> simplify;


  public AlgebraGenerator(QueryContext<Node> queryContext) {
    this(queryContext, 0);
  }


  public AlgebraGenerator(QueryContext<Node> queryContext, int subQueryDepth) {
    this.queryContext = queryContext;

    this.subQueryDepth = subQueryDepth;

    simplify = new TransformSimplify<Node>();
  }


  public Op<Node> compile(Query<Node> query) {
    Op<Node> op = compile(query.getQueryPattern());

    op = compileModifiers(query, op);

    return op;
  }


  public Op<Node> compile(Element<Node> elt) {
    Op<Node> op = compileElement(elt);

    Op<Node> op2 = op;

    op2 = simplify(op);

    return op2;
  }

  private Op<Node> simplify(Op<Node> op) {
    return new Transformer<Node>(queryContext).transform(simplify, op);
  }

  // This is the operation to call for recursive application.
  protected Op<Node> compileElement(Element<Node> elt) {

    if (elt instanceof ElementGroup) {
      return compileElementGroup((ElementGroup<Node>) elt);
    }

    if (elt instanceof ElementUnion) {
      return compileElementUnion((ElementUnion<Node>) elt);
    }

    if (elt instanceof ElementStream) {
      return compileElementStream((ElementStream<Node>) elt);
    }

    if (elt instanceof ElementNamedGraph) {
      return compileElementGraph((ElementNamedGraph<Node>) elt);
    }

    if (elt instanceof ElementService) {
      return compileElementService((ElementService<Node>) elt);
    }

    if (elt instanceof ElementFetch) {
      return compileElementFetch((ElementFetch<Node>) elt);
    }

    if (elt instanceof ElementTriplesBlock) {
      return compileBasicPattern(((ElementTriplesBlock<Node>) elt).getPattern());
    }

    if (elt instanceof ElementPathBlock) {
      return compilePathBlock(((ElementPathBlock<Node>) elt).getPattern());
    }

    if (elt instanceof ElementSubQuery) {
      return compileElementSubquery((ElementSubQuery<Node>) elt);
    }

    if (elt instanceof ElementData) {
      return compileElementData((ElementData<Node>) elt);
    }

    if (elt == null) {
      return new OpNull<>();
    }

    return null;
  }

  // Produce the algebra for a single group.
  // <a href="http://www.w3.org/TR/rdf-sparql-query/#sparqlQuery">Translation
  // to the SPARQL Algebra</a>
  //
  // Step : (URI resolving and triple pattern org.rdf4led.sparql.algebra.syntax forms) was done during
  // parsing
  // Step : Collection FILTERS [prepareGroup]
  // Step : (Paths) e.g. simple links become triple patterns. Done later in
  // [compileOneInGroup]
  // Step : (BGPs) Merge PathBlocks - these are SPARQL 1.1's TriplesBlock
  // [prepareGroup]
  // Step : (BIND/LET) Associate with BGP [??]
  // Step : (Groups and unions) Was done during parsing to get ElementUnion.
  // Step : Graph Patterns [compileOneInGroup]
  // Step : Filters [here]
  // Simplification: Done later
  // If simplification is done now, it changes OPTIONAL { { ?x :p ?w .
  // FILTER(?w>23) } } because it removes the
  // (join Z (filter...)) that in turn stops the filter getting moved into the
  // LeftJoin.
  // It need a depth of 2 or more {{ }} for this to happen.

  protected Op<Node> compileElementGroup(ElementGroup<Node> groupElt) {
    Pair<List<Expr<Node>>, List<Element<Node>>> pair = prepareGroup(groupElt);

    List<Expr<Node>> filters = pair.getLeft();

    List<Element<Node>> groupElts = pair.getRight();

    // Compile the consolidated group elements.
    // "current" is the completed part only - there may be thing pushed into
    // the accumulator.
    Op<Node> current = new OpTable<>();

    Deque<Op<Node>> acc = new ArrayDeque<>();

    for (Iterator<Element<Node>> iter = groupElts.listIterator(); iter.hasNext(); ) {
      Element<Node> elt = iter.next();

      if (elt != null) {
        current = compileOneInGroup(elt, current, acc);
      }
    }

    // Deal with any remaining ops.
    current = joinOpAcc(current, acc);

    if (filters != null) {
      // Put filters round the group.
      for (Expr<Node> expr : filters) {

        OpFilter<Node> filterOp = filter(current);

        filterOp.getExprs().add(expr);

        current = filterOp;
      }
    }
    return current;
  }

  OpFilter<Node> filter(Op<Node> op) {
    if (op instanceof OpFilter) {
      return (OpFilter<Node>) op;
    } else {
      return new OpFilter<>(op);
    }
  }

  private Op<Node> filter(Expr<Node> expr, Op<Node> op) {
    OpFilter<Node> opFilter;

    if (op instanceof OpFilter) {
      opFilter = (OpFilter<Node>) op;
    } else {
      opFilter = new OpFilter<>(op);
    }

    opFilter.getExprs().add(expr);

    return opFilter;
  }

  /*
   * Extract filters, merge adjacent BGPs, do BIND. When extracting filters,
   * BGP or PathBlocks may become adjacent so merge them into one. Return a
   * list of elements with any filters at the end.
   */

  private Pair<List<Expr<Node>>, List<Element<Node>>> prepareGroup(ElementGroup<Node> groupElt) {
    List<Element<Node>> groupElts = new ArrayList<>();

    // PathBlock<Node> currentBGP = null ;

    PathBlock<Node> currentPathBlock = null;

    List<Expr<Node>> filters = null;

    for (Element<Node> elt : groupElt.getElements()) {
      if (!fixedFilterPosition && elt instanceof ElementFilter) {

        // For fixed position filters, drop through to general element
        // processing.
        // It's also illegal SPARQL - filters operate over the whole
        // group.
        ElementFilter<Node> f = (ElementFilter<Node>) elt;

        if (filters == null) {
          filters = new ArrayList<>();
        }

        filters.add(f.getExpr());

        // Collect filters but do not place them yet.
        continue;
      }

      // The org.rdf4led.sparql.parser does not generate ElementTriplesBlock (SPARQL 1.1)
      // but SPARQL 1.0 does and also we cope for programmtically built
      // queries

      if (elt instanceof ElementTriplesBlock) {
        ElementTriplesBlock<Node> etb = (ElementTriplesBlock<Node>) elt;

        if (currentPathBlock == null) {
          ElementPathBlock<Node> etb2 = new ElementPathBlock<>(queryContext);

          currentPathBlock = etb2.getPattern();

          groupElts.add(etb2);
        }

        for (Triple<Node> t : etb.getPattern()) {
          currentPathBlock.add(
              new TriplePath<Node>(t) {
                @Override
                public boolean isURI(Node p) {
                  return queryContext.dictionary().isURI(p);
                }
              });
        }

        continue;
      }

      // To PathLib

      if (elt instanceof ElementPathBlock) {
        ElementPathBlock<Node> epb = (ElementPathBlock<Node>) elt;

        if (currentPathBlock == null) {
          ElementPathBlock<Node> etb2 = new ElementPathBlock<Node>(queryContext);

          currentPathBlock = etb2.getPattern();

          groupElts.add(etb2);
        }

        currentPathBlock.addAll(epb.getPattern());

        continue;
      }

      // else

      // Not BGP, path or filters.
      // Clear any BGP-related triple accumulators.
      currentPathBlock = null;
      // Add this element
      groupElts.add(elt);
    }

    return new Pair<>(filters, groupElts);
  }

  // Flush the org.rdf4led.sparql.algebra.op accumulator - and clear it
  private void accumulate(Deque<Op<Node>> acc, Op<Node> op) {
    acc.addLast(op);
  }

  // Accumulate stored ops, return unit if none.
  private Op<Node> popAccumulated(Deque<Op<Node>> acc) {
    if (acc.size() == 0) {
      // return OpTable.unit() ;
      // return new OpTable<Node>(new TableUnit<Node>());
    }

    Op<Node> joined = null;

    // First first to last.
    for (Op<Node> op : acc) {
      // joined = OpJoin.create(joined,org.rdf4led.sparql.algebra.op) ;
      joined = new OpJoin<>(joined, op);
    }

    acc.clear();

    return joined;
  }

  // Join stored ops to the current state
  private Op<Node> joinOpAcc(Op<Node> current, Deque<Op<Node>> acc) {
    if (acc.size() == 0) {
      return current;
    }

    Op<Node> joined = current;

    // First first to last.
    for (Op<Node> op : acc) {
      joined = new OpJoin<Node>(joined, op);
    }

    acc.clear();

    return joined;
  }

  private Op<Node> compileOneInGroup(Element<Node> elt, Op<Node> current, Deque<Op<Node>> acc) {
    // PUSH
    // Replace triple patterns by OpBGP (i.e. SPARQL translation step 1)
    if (elt instanceof ElementTriplesBlock) {
      // Should not happen.
      ElementTriplesBlock<Node> etb = (ElementTriplesBlock<Node>) elt;

      Op<Node> op = compileBasicPattern(etb.getPattern());

      accumulate(acc, op);

      return current;
    }

    // PUSH
    if (elt instanceof ElementPathBlock) {
      ElementPathBlock<Node> epb = (ElementPathBlock<Node>) elt;

      Op<Node> op = compilePathBlock(epb.getPattern());

      accumulate(acc, op);

      return current;
    }

    // POP
    if (elt instanceof ElementAssign) {
      // This step and the similar BIND step needs to access the
      // preceeding
      // element if it is a BGP.
      // That might 'current', or in the left side of a join.
      // If not a BGP, insert a empty one.

      Op<Node> op = popAccumulated(acc);

      ElementAssign<Node> assign = (ElementAssign<Node>) elt;

      Op<Node> opAssign = new OpAssign<Node>().assign(op, assign.getVar(), assign.getExpr());

      accumulate(acc, opAssign);

      return current;
    }

    // POP
    if (elt instanceof ElementBind) {
      Op<Node> op = popAccumulated(acc);

      ElementBind<Node> bind = (ElementBind<Node>) elt;

      Op<Node> opExtend = (new OpExtend<Node>()).extend(op, bind.getVar(), bind.getExpr());

      accumulate(acc, opExtend);

      return current;
    }

    // Flush.
    current = joinOpAcc(current, acc);

    if (elt instanceof ElementOptional) {
      ElementOptional<Node> eltOpt = (ElementOptional<Node>) elt;

      return compileElementOptional(eltOpt, current);
    }

    if (elt instanceof ElementSubQuery) {
      ElementSubQuery<Node> elQuery = (ElementSubQuery<Node>) elt;

      Op<Node> op = compileElementSubquery(elQuery);

      return join(current, op);
    }

    if (elt instanceof ElementMinus) {
      ElementMinus<Node> elt2 = (ElementMinus<Node>) elt;

      Op<Node> op = compileElementMinus(current, elt2);

      return op;
    }

    // All elements that simply "join" into the algebra.
    if (elt instanceof ElementGroup
        || elt instanceof ElementNamedGraph
        || elt instanceof ElementService
        || elt instanceof ElementFetch
        || elt instanceof ElementUnion
        || elt instanceof ElementSubQuery
        || elt instanceof ElementData
        || elt instanceof ElementStream) {
      Op<Node> op = compileElement(elt);
      return join(current, op);
    }

    if (elt instanceof ElementExists) {
      ElementExists<Node> elt2 = (ElementExists<Node>) elt;

      Op<Node> op = compileElementExists(current, elt2);

      return op;
    }

    if (elt instanceof ElementNotExists) {
      ElementNotExists<Node> elt2 = (ElementNotExists<Node>) elt;

      Op<Node> op = compileElementNotExists(current, elt2);

      return op;
    }

    // Filters were collected together by prepareGroup
    // This only handels filters left in place by some magic.
    if (elt instanceof ElementFilter) {
      ElementFilter<Node> f = (ElementFilter<Node>) elt;

      return filter(f.getExpr(), current);
    }

    // // SPARQL 1.1 UNION -- did not make SPARQL
    if (elt instanceof ElementUnion) {
      ElementUnion<Node> elt2 = (ElementUnion<Node>) elt;

      if (elt2.getElements().size() == 1) {
        Op<Node> op = compileElementUnion(current, elt2);
        return op;
      }
    }

    // broken("compile/Element not recognized: "+Utils.className(elt)) ;
    return null;
  }

  private Op<Node> compileElementUnion(ElementUnion<Node> el) {
    Op<Node> current = null;

    for (Element<Node> subElt : el.getElements()) {
      Op<Node> op = compileElement(subElt);

      current = union(current, op);
    }

    return current;
  }

  private Op<Node> compileElementUnion(Op<Node> current, ElementUnion<Node> elt2) {
    // Special SPARQL 1.1 case.
    Op<Node> op = compile(elt2.getElements().get(0));

    // Op<Node> opUnion = OpUnion.create(current, org.rdf4led.sparql.algebra.op) ;

    if (current == null) {
      return op;
    }

    if (op == null) {
      return current;
    }

    return new OpUnion<>(current, op);
  }

  private Op<Node> compileElementNotExists(Op<Node> current, ElementNotExists<Node> elt2) {
    Op<Node> op = compile(elt2.getElement()); // "compile", not
    // "compileElement" -- do
    // simpliifcation

    Expr<Node> expr = new E_Exists<Node>(elt2, op, queryContext);

    expr = new E_LogicalNot<Node>(expr, queryContext);

    return filter(expr, current);
  }

  private Op<Node> compileElementExists(Op<Node> current, ElementExists<Node> elt2) {
    Op<Node> op = compile(elt2.getElement()); // "compile", not
    // "compileElement" -- do
    // simpliifcation

    Expr<Node> expr = new E_Exists<Node>(elt2, op, queryContext);

    return filter(expr, op);
  }

  private Op<Node> compileElementMinus(Op<Node> current, ElementMinus<Node> elt2) {
    Op<Node> op = compile(elt2.getMinusElement());

    return new OpMinus<Node>(current, op);
  }

  private Op<Node> compileElementData(ElementData<Node> elt) {
    throw new UnsupportedOperationException(elt.toString());
    // return new OpTable<Node>(elt.getTable());
  }

  protected Op<Node> compileElementOptional(ElementOptional<Node> eltOpt, Op<Node> current) {
    Element<Node> subElt = eltOpt.getOptionalElement();

    Op<Node> op = compileElement(subElt);

    ExprList<Node> exprs = null;

    if (op instanceof OpFilter) {
      OpFilter<Node> f = (OpFilter<Node>) op;

      Op<Node> sub = f.getSubOp();

      if (sub instanceof OpFilter) {
        // broken("compile/Optional/nested filters - unfinished");
      }

      exprs = f.getExprs();

      op = sub;
    }

    // current = OpLeftJoin.create(current, org.rdf4led.sparql.algebra.op, exprs) ;
    current = new OpLeftJoin<Node>(current, op, exprs);

    return current;
  }

  protected Op<Node> compileBasicPattern(BasicPattern<Node> pattern) {
    return new OpBGP<Node>(pattern);
  }

  protected Op<Node> compilePathBlock(PathBlock<Node> pathBlock) {
    // Empty path block : the org.rdf4led.sparql.parser does not generate this case.
    if (pathBlock.size() == 0) {
      throw new UnsupportedOperationException();
      // return new OpTable<Node>(new TableUnit<Node>());
      // return OpTable.unit() ;
    }

    // Always turns the most basic paths to triples.
    return PathLib.pathToTriples(pathBlock);
  }

  protected Op<Node> compileElementStream(ElementStream<Node> eltStream) {

    return new OpNull<>();
    // Node streamUri = eltStream.getStreamUri();
    // Op<Node> sub  = compileElement(eltStream.getElement());
    // return new OpStream<Node>(sub, streamUri, eltStream.getWindowType(),
    // eltStream.getMaxCount(), eltStream.getTimeRange());
  }

  protected Op<Node> compileElementGraph(ElementNamedGraph<Node> eltGraph) {
    Node graphNode = eltGraph.getGraphNameNode();

    Op<Node> sub = compileElement(eltGraph.getElement());

    return new OpGraph<>(graphNode, sub);
  }

  protected Op<Node> compileElementService(ElementService<Node> eltService) {
    Node serviceNode = eltService.getServiceNode();

    Op<Node> sub = compileElement(eltService.getElement());

    return new OpService<Node>(serviceNode, sub, eltService, eltService.getSilent());
  }

  private Op<Node> compileElementFetch(ElementFetch<Node> elt) {
    Node serviceNode = elt.getFetchNode();

    return new OpLabel<Node>("fetch/" + serviceNode, new OpTable<Node>());
  }
  // Probe to see if enabled.
  // OpExtBuilder builder = OpExtRegistry.builder("fetch") ;

  // if ( builder == null )
  // {
  // Log.warn(this,
  // "Attempt to use OpFetch - need to enable first with a call to OpFetch.enable()")
  // ;
  // return OpLabel.create("fetch/"+serviceNode, OpTable.unit()) ;
  // return queryContext.createOpLabel("fetch/" + serviceNode, new OpTable<Node>(new
  // TableUnit<Node>()));
  // }
  //
  // Item item = Item.createNode(elt.getFetchNode()) ;
  //
  // ItemList args = new ItemList() ;
  //
  // args.add(item) ;
  //
  // return builder.make(args) ;

  protected Op<Node> compileElementSubquery(ElementSubQuery<Node> eltSubQuery) {
    // AlgebraGenerator<Node> gen = new AlgebraGenerator<Node>(queryContext, subQueryDepth + 1);

    return this.compile(eltSubQuery.getQuery());
  }

  // Compile query modifiers
  private Op<Node> compileModifiers(Query<Node> query, Op<Node> pattern) {

    /* The modifier order in algebra is:
     *
     * Limit/Offset
     *   Distinct/reduce
     *     project
     *       OrderBy
     *         Bindings
     *           having
     *             select expressions
     *               group
     */

    // Preparation: sort SELECT clause into assignments and projects.
    VarExprList<Node> projectVars = query.getProject();

    VarExprList<Node> exprs = new VarExprList<Node>(); // Assignments to be
    // done.

    List<Node> varNodes = new ArrayList<Node>(); // projection variables

    Op<Node> op = pattern;

    // ---- GROUP BY

    if (query.hasGroupBy()) {
      // When there is no GroupBy but there are some aggregates, it's a
      // group of no variables.
      op = new OpGroup<Node>(op, query.getGroupBy(), query.getAggregators());
    }

    // ---- Assignments from SELECT and other places (so available to ORDER
    // and HAVING)
    // Now do assignments from expressions
    // Must be after "group by" has introduced it's variables.

    // Look for assignments in SELECT expressions.
    if (!projectVars.isEmpty() && !query.isQueryResultStar()) {
      // Don't project for QueryResultStar so initial bindings show
      // through in SELECT *
      if (projectVars.size() == 0 && query.isSelectType()) {
        // Log.warn(this,"No project variables") ;
      }
      // Separate assignments and variable projection.
      for (Node varNode : query.getProject().getVars()) {
        Expr<Node> e = query.getProject().getExpr(varNode);

        if (e != null) {

          Expr<Node> e2 = null;

          if (e instanceof ExprAggregator) {
            e2 = ((ExprAggregator<Node>) e).getAggVar();
          } else {
            e2 = e;
          }

          exprs.add(varNode, e2);
        }
        // Include in project
        varNodes.add(varNode);
      }
    }

    // ---- Assignments from SELECT and other places (so available to ORDER
    // and HAVING)
    if (!exprs.isEmpty()) {
      // Potential rewrites based of assign introducing aliases.
      // org.rdf4led.sparql.algebra.op = OpExtend.extend(org.rdf4led.sparql.algebra.op, exprs) ;
      op = (new OpExtend<Node>()).extend(op, exprs);
      // org.rdf4led.sparql.algebra.op = queryContext.createOpExtend(org.rdf4led.sparql.algebra.op, exprs);
    }

    // ---- HAVING
    if (query.hasHaving()) {
      for (Expr<Node> expr : query.getHavingExprs()) {

        //// HAVING expression to refer to the aggregate via the variable.
        // if ()
        //// Expr<Node> expr2 = queryContext.getExprLib().replaceAggregateByVariable(org.rdf4led.common.data.expr);
        // org.rdf4led.sparql.algebra.op = OpFilter.filter(expr2 , org.rdf4led.sparql.algebra.op) ;
        // queryContext.createOpFilter_filter(expr2, org.rdf4led.sparql.algebra.op);

        Expr<Node> e2 = null;

        if (expr instanceof ExprAggregator) {
          e2 = ((ExprAggregator<Node>) expr).getAggVar();
        } else {
          e2 = expr;
        }

        op = filter(e2, op);
      }
    }
    // ---- VALUES
    // if ()query.hasValues())
    //
    // Table<Node> table =
    // TableFactory.create(query.getValuesVariables()) ;
    //
    // Table<Node> table = new TableN<Node>(query.getValuesVariables());
    // for (Mapping<Node> mapping : query.getValuesData())
    // {
    // table.addMapping(mapping);
    // }
    //
    // OpTable<Node> opTable = new OpTable<Node>(table);// OpTable.create(table)
    //
    //
    //// org.rdf4led.sparql.algebra.op = OpJoin.create(org.rdf4led.sparql.algebra.op, opTable) ;
    //
    //  org.rdf4led.sparql.algebra.op = queryContext.createOpJoin(org.rdf4led.sparql.algebra.op, opTable);
    // }

    // ---- ToList
    if (false) {
      op = new OpList<Node>(op);
    }

    // ---- ORDER BY
    if (query.getOrderBy() != null) {
      List<SortCondition<Node>> scList = new ArrayList<SortCondition<Node>>();

      // Aggregates in ORDER BY
      for (SortCondition<Node> sc : query.getOrderBy()) {
        Expr<Node> expr = sc.getExpression();

        if (expr instanceof ExprAggregator) {
          expr = ((ExprAggregator<Node>) expr).getAggVar();
        }

        scList.add(new SortCondition<Node>(expr, sc.getDirection()));
      }

      op = new OpOrder<Node>(op, scList);
    }

    // ---- PROJECT
    // No projection => initial variables are exposed.
    // Needed for CONSTRUCT and initial bindings + SELECT *

    if (varNodes.size() > 0) {
      op = new OpProject<Node>(op, varNodes);
    }

    // ---- DISTINCT
    if (query.isDistinct()) {
      if (!(op instanceof OpDistinct)) {
        op = new OpDistinct<Node>(op);
      }
    }

    // ---- REDUCED
    if (query.isReduced()) {
      if (!(op instanceof OpReduced)) {
        op = new OpReduced<Node>(op);
      }
    }

    // ---- LIMIT/OFFSET
    if (query.hasLimit() || query.hasOffset()) {
      op = new OpSlice<>(op, query.getOffset() /* start */, query.getLimit() /* length */);
    }

    return op;
  }

  // --------

  private Op<Node> join(Op<Node> current, Op<Node> newOp) {
    if (simplifyTooEarlyInAlgebraGeneration && applySimplification) {

      if (isJoinIdentify(current)) {
        return current;
      }

      if (isJoinIdentify(newOp)) {
        return newOp;
      }
    }

    return new OpJoin<Node>(current, newOp);
  }

  private boolean isJoinIdentify(Op<Node> op) {
    // if (! (org.rdf4led.sparql.algebra.op instanceof OpTable))
    // {
    //
    // return false;
    // }
    // Table<Node> t = ((OpTable<Node>)org.rdf4led.sparql.algebra.op).getTable();
    //
    // return (t instanceof TableUnit);

    return false;
  }

  protected Op<Node> union(Op<Node> current, Op<Node> newOp) {
    if (current == null) {
      return newOp;
    }

    if (newOp == null) {
      return current;
    }

    return new OpUnion<>(current, newOp);
  }
}
