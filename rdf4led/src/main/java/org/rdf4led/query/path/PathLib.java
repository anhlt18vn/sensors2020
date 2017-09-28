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

package org.rdf4led.query.path;

import org.rdf4led.common.Filter;
import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.QueryIterator;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.op.OpBGP;
import org.rdf4led.query.sparql.algebra.op.OpPath;
import org.rdf4led.query.sparql.algebra.op.OpSequence;
import org.rdf4led.common.mapping.Mapping;
import org.rdf4led.common.mapping.MappingFactory;
import org.rdf4led.common.iterator.Iter;

import java.util.*;

public class PathLib {
  /** Convert any paths of exactly one predicate to a triple pattern */
  public static <Node> Op<Node> pathToTriples(PathBlock<Node> pattern) {
    BasicPattern<Node> bp = null;

    Op op = null;

    for (TriplePath<Node> tp : pattern) {
      if (tp.isTriple()) {
        if (bp == null) {
          bp = new BasicPattern<>();
        }

        bp.add(tp.asTriple());

        continue;
      }
      // Path form.

      op = flush(bp, op);

      bp = null;

      OpPath<Node> opPath2 = new OpPath<Node>(tp);

      op = OpSequence.create(op, opPath2);

      continue;
    }

    // End.  Finish off any outstanding BGP.
    op = flush(bp, op);
    return op;
  }

  private static <Node> Op<Node> flush(BasicPattern<Node> bp, Op<Node> op) {
    if (bp == null || bp.isEmpty()) return op;

    OpBGP<Node> opBGP = new OpBGP<>(bp);

    op = OpSequence.create(op, opBGP);

    return op;
  }

  /** Install a path as a property function in the global property function registry */
  //	public static void install(String uri, Path path)
  //	{ install(uri, path, PropertyFunctionRegistry.get()) ; }

  /** Install a path as a property function in a given registry */
  //	public static void install(String uri, final Path path, PropertyFunctionRegistry registry)
  //	{
  //		PropertyFunctionFactory pathPropFuncFactory = new PropertyFunctionFactory()
  //		{
  //			@Override
  //			public PropertyFunction create(String uri)
  //			{
  //				return new PathPropertyFunction(path) ;
  //			}
  //		};
  //
  //		registry.put(uri, pathPropFuncFactory) ;
  //	}

  public static <Node> QueryIterator<Node> execTriplePath(
      Graph<Node> graph,
      Mapping<Node> mapping,
      TriplePath<Node> triplePath,
      final QueryContext<Node> queryContext) {
    if (triplePath.isTriple()) {
      // Fake it.  This happens only for API constructed situations.
      Path<Node> path = new P_Link<Node>(triplePath.getPredicate());

      triplePath =
          new TriplePath<Node>(triplePath.getSubject(), path, triplePath.getObject()) {
            @Override
            public boolean isURI(Node p) {
              return queryContext.dictionary().isURI(p);
            }
          };
    }

    return execTriplePath(
        graph,
        mapping,
        triplePath.getSubject(),
        triplePath.getPath(),
        triplePath.getObject(),
        queryContext);
  }

  public static <Node> QueryIterator<Node> execTriplePath(
      Graph<Node> graph,
      Mapping<Node> mapping,
      Node s,
      Path<Node> path,
      Node o,
      final QueryContext<Node> queryContext) {
    //		Explain.explain(s, path, o, execCxt.getContext()) ;

    s =
        mapping.getValue(s) == null
            ? s
            : mapping.getValue(
                s); // queryContext.isVarNode(s)? mapping.getValue(s) : s; //Var.lookup(binding, s)
                    // ;

    o = mapping.getValue(s) == null ? o : mapping.getValue(o);

    Iterator<Node> iter = null;

    Node endNode = null;

    //		Graph graph = execCxt.getActiveGraph() ;

    if (queryContext.isVarNode(s) && queryContext.isVarNode(o)) {
      if (s.equals(o))
        return ungroundedPathSameVar(
            mapping, graph, queryContext.allocVarNode(s), path, queryContext);
      else
        return ungroundedPath(
            mapping,
            graph,
            queryContext.allocVarNode(s),
            path,
            queryContext.allocVarNode(o),
            queryContext);
    }

    if (!queryContext.isVarNode(s) && !queryContext.isVarNode(o)) {
      return groundedPath(mapping, graph, s, path, o, queryContext);
    }

    if (queryContext.isVarNode(s)) {
      // Var subject, concrete object - do backwards.
      iter = PathEval.evalReverse(graph, o, path, queryContext);

      endNode = s;
    } else {
      iter = PathEval.eval(graph, s, path, queryContext);

      endNode = o;
    }
    return _execTriplePath(mapping, iter, endNode, queryContext);
  }

  private static <Node> QueryIterator<Node> _execTriplePath(
      Mapping<Node> mapping, Iterator<Node> iter, Node endNode, QueryContext<Node> queryContext) {
    List<Mapping<Node>> results = new ArrayList<>();

    if (!queryContext.isVarNode(endNode)) {
      throw new RuntimeException("Non-variable endnode in _execTriplePath");
    }

    Node var = endNode;
    queryContext.allocVarNode(endNode);

    // Assign.
    for (; iter.hasNext(); ) {
      Node n = iter.next();

      results.add(MappingFactory.binding(mapping, var, n, queryContext));
    }

    //return new QueryIterPlainWrapper<>(results.iterator(), queryContext);
    return null;
  }

  // Subject and object are nodes.
  private static <Node> QueryIterator<Node> groundedPath(
      Mapping<Node> mapping,
      Graph<Node> graph,
      Node subject,
      Path<Node> path,
      Node object,
      QueryContext<Node> queryContext) {
    Iterator<Node> iter = PathEval.eval(graph, subject, path, queryContext);
    // Now count the number of matches.

    int count = 0;
    for (; iter.hasNext(); ) {
      Node n = iter.next();

      if (n == object) {
        count++;
      }
    }
    return null;
  }

  // Brute force evaluation of a TriplePath where neither subject nor object are bound
  private static <Node> QueryIterator<Node> ungroundedPath(
      Mapping<Node> binding,
      Graph<Node> graph,
      Node sVar,
      Path<Node> path,
      Node oVar,
      QueryContext<Node> queryContext) {
//    Iterator<Node> iter = allNodes(graph, queryContext);
//
//    QueryIterConcat qIterCat = new QueryIterConcat(queryContext);
//
//    for (; iter.hasNext(); ) {
//      Node n = iter.next();
//
//      Mapping b2 = MappingFactory.binding(binding, sVar, n, queryContext);
//
//      Iterator<Node> pathIter = PathEval.eval(graph, n, path, queryContext);
//
//      QueryIterator<Node> qIter = _execTriplePath(b2, pathIter, oVar, queryContext);
//
//      qIterCat.add(qIter);
//    }
    return null;
//    return qIterCat;
  }

  private static <Node> QueryIterator<Node> ungroundedPathSameVar(
      Mapping<Node> mapping,
      Graph graph,
      Node varNode,
      Path path,
      QueryContext<Node> queryContext) {
    // Try each end, grounded
    // Slightly more efficient would be to add a per-engine to do this.
    Iterator<Node> iter = allNodes(graph, queryContext);

//    QueryIterConcat qIterCat = new QueryIterConcat(queryContext);
//
//    for (; iter.hasNext(); ) {
//      Node n = iter.next();
//
//      Mapping b2 = MappingFactory.binding(mapping, varNode, n, queryContext);
//
//      int x = existsPath(graph, n, path, n, queryContext);
//
//      if (x > 0) {
//        QueryIterator qIter = new QueryIterYieldN(x, b2, queryContext);
//
//        qIterCat.add(qIter);
//      }
//    }

    return null;
  }

  private static <Node> int existsPath(
      Graph<Node> graph,
      Node subject,
      Path path,
      final Node object,
      QueryContext<Node> queryContext) {
    //		if ( ! subject.isConcrete() || !object.isConcrete() )
    //			throw new ARQInternalErrorException("Non concrete node for existsPath evaluation") ;

    Iterator<Node> iter = PathEval.eval(graph, subject, path, queryContext);

    Filter<Node> filter =
        new Filter<Node>() {
          @Override
          public boolean accept(Node node) {
            return node.equals(object);
          }
        };
    // See if we got to the node we're interested in finishing at.
    iter = Iter.filter(iter, filter);

    long x = Iter.count(iter);

    return (int) x;
  }

  public static <Node> Iterator<Node> allNodes(Graph<Node> graph, QueryContext<Node> queryContext) {
    Set<Node> x = new HashSet<>(1000);

    Iterator<Triple<Node>> iter =
        graph.find(
            queryContext.dictionary().getNodeAny(),
            queryContext.dictionary().getNodeAny(),
            queryContext.dictionary().getNodeAny());

    for (; iter.hasNext(); ) {
      Triple<Node> t = iter.next();
      x.add(t.getSubject());
      x.add(t.getObject());
    }

    return x.iterator();
  }
}
