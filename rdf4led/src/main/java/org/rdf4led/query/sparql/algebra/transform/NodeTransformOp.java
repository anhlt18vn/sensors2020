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

import org.rdf4led.query.sparql.NodeTransform;

public class NodeTransformOp<Node> extends TransformCopy<Node> {
  // This finds everywhere that node can lurk in an alegra expression:
  //   BGPs, paths, triples, quads
  //   GRAPH, GRAPH{} (DatasetNames)
  //   Filters, including inside EXISTS and expressions in LeftJoin
  //   OrderBy, GroupBy
  //   Extend, Assign
  //   Tables
  //   Projects
  // Not:
  //   Conditional (no expression)

  private final NodeTransform<Node> transform;

  public NodeTransformOp(NodeTransform<Node> transform) {
    this.transform = transform;
  }

  //    @Override public Op<Node> org.rdf4led.sparql.transform(OpTriple<Node> opTriple)
  //    {
  //        Triple t2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, opTriple.getTriple()) ;
  //
  //        if ( t2 == opTriple.getTriple())
  //            return super.org.rdf4led.sparql.transform(opTriple) ;
  //        return new OpTriple(t2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpFilter opFilter, Op subOp)
  //    {
  //        ExprList exprList = opFilter.getExprs() ;
  //        ExprList exprList2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, exprList) ;
  //        if ( exprList2 == exprList )
  //            return super.org.rdf4led.sparql.transform(opFilter, subOp) ;
  //        return OpFilter.filter(exprList2, subOp) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpBGP opBGP)
  //    {
  //        BasicPattern bgp2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, opBGP.getPattern()) ;
  //        if ( bgp2 == opBGP.getPattern())
  //            return super.org.rdf4led.sparql.transform(opBGP) ;
  //        return new OpBGP(bgp2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpPath opPath)
  //    {
  //        TriplePath tp = opPath.getTriplePath() ;
  //        Node s = tp.getSubject() ;
  //        Node s1 = org.rdf4led.sparql.transform.convert(s) ;
  //        Node o = tp.getObject() ;
  //        Node o1 = org.rdf4led.sparql.transform.convert(o) ;
  //
  //        if ( s1 == s && o1 == o )
  //            // No change.
  //            return super.org.rdf4led.sparql.transform(opPath) ;
  //
  //        Path path = tp.getPath() ;
  //        TriplePath tp2 ;
  //
  //        if ( path != null )
  //            tp2 = new TriplePath(s1, path, o1) ;
  //        else
  //        {
  //            Triple t = new Triple(s1, tp.getPredicate(), o1) ;
  //            tp2 = new TriplePath(t) ;
  //        }
  //        return new OpPath(tp2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpQuadPattern opQuadPattern)
  //    {
  //        // The internal representation is (graph, BGP)
  //        BasicPattern bgp2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform,
  // opQuadPattern.getBasicPattern()) ;
  //        Node g2 = opQuadPattern.getGraphNode() ;
  //        g2 = org.rdf4led.sparql.transform.convert(g2) ;
  //
  //        if ( g2 == opQuadPattern.getGraphNode() && bgp2 == opQuadPattern.getBasicPattern() )
  //            return super.org.rdf4led.sparql.transform(opQuadPattern) ;
  //        return new OpQuadPattern(g2, bgp2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpGraph opGraph, Op subOp)
  //    {
  //        Node g2 = org.rdf4led.sparql.transform.convert(opGraph.getNode()) ;
  //        if ( g2 == opGraph.getNode() )
  //            return super.org.rdf4led.sparql.transform(opGraph, subOp) ;
  //        return new OpGraph(g2, subOp) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpTable opTable)
  //    {
  //        if ( opTable.isJoinIdentity() )
  //            return opTable ;
  //        return opTable ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpLeftJoin opLeftJoin, Op left, Op right)
  //    {
  //        ExprList exprList = opLeftJoin.getExprs() ;
  //        ExprList exprList2 = exprList ;
  //        if ( exprList != null )
  //            exprList2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, exprList) ;
  //        if ( exprList2 == exprList )
  //            return super.org.rdf4led.sparql.transform(opLeftJoin, left, right) ;
  //        return OpLeftJoin.create(left, right, exprList2) ;
  //    }

  // Not OpConditional - no expression.

  //    @Override public Op org.rdf4led.sparql.transform(OpProject opProject, Op subOp)
  //    {
  //        List<Var> x = opProject.getVars() ;
  //        List<Var> x2 = NodeTransformLib.transformVars(org.rdf4led.sparql.transform, x) ;
  //        if ( x == x2 )
  //            return super.org.rdf4led.sparql.transform(opProject, subOp) ;
  //        return new OpProject(subOp, x2) ;
  //    }
  //
  //    @Override public Op org.rdf4led.sparql.transform(OpAssign opAssign, Op subOp)
  //    {
  //        VarExprList varExprList = opAssign.getVarExprList() ;
  //        VarExprList varExprList2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, varExprList) ;
  //        if ( varExprList == varExprList2 )
  //            return super.org.rdf4led.sparql.transform(opAssign, subOp) ;
  //        return OpAssign.assign(subOp, varExprList2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpExtend opExtend, Op subOp)
  //    {
  //        VarExprList varExprList = opExtend.getVarExprList() ;
  //        VarExprList varExprList2 = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, varExprList) ;
  //        if ( varExprList == varExprList2 )
  //            return super.org.rdf4led.sparql.transform(opExtend, subOp) ;
  //        return OpExtend.extend(subOp, varExprList2) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpOrder opOrder, Op subOp)
  //    {
  //        List<SortCondition> conditions = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform,
  // opOrder.getConditions()) ;
  //
  //        if ( conditions == opOrder.getConditions() )
  //            return super.org.rdf4led.sparql.transform(opOrder, subOp) ;
  //        return new OpOrder(subOp, conditions) ;
  //    }

  //    @Override public Op org.rdf4led.sparql.transform(OpGroup opGroup, Op subOp)
  //    {
  //        VarExprList groupVars = NodeTransformLib.org.rdf4led.sparql.transform(org.rdf4led.sparql.transform, opGroup.getGroupVars()) ;
  //
  //
  //        // Rename the vars in the expression as well.
  //        // .e.g max(?y) ==> max(?/y)
  //        // These need renaming as well.
  //        List<ExprAggregator> aggregators = new ArrayList<ExprAggregator>() ;
  //        for ( ExprAggregator agg : opGroup.getAggregators() )
  //            aggregators.add(agg.applyNodeTransform(org.rdf4led.sparql.transform)) ;
  //
  //        if ( aggregators.equals(opGroup.getAggregators()))
  //        {
  //            if ( groupVars == opGroup.getGroupVars() )
  //                return super.org.rdf4led.sparql.transform(opGroup, subOp) ;
  //        }
  //
  //        return new OpGroup(subOp, groupVars, aggregators) ;
  //    }
}
