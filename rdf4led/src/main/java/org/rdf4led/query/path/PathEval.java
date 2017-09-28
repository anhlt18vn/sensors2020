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


import org.rdf4led.common.iterator.Iter;
import org.rdf4led.graph.Graph;
import org.rdf4led.query.QueryContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Path evaluation - public interface */
public class PathEval {
  /** Evaluate a path : SPARQL semantics */
  public static <Node> Iterator<Node> eval(
          Graph<Node> graph, Node node, Path<Node> path, QueryContext<Node> queryContext) {
    return eval$(graph, node, path, new PathEngineSPARQL<Node>(graph, true, queryContext));
    // return eval$(graph, node, path, new PathEngineN(graph, true)) ;
  }

  /** Evaluate a path */
  public static <Node> Iterator<Node> evalReverse(
      Graph<Node> graph, Node node, Path<Node> path, QueryContext<Node> queryContext) {
    return eval$(graph, node, path, new PathEngineSPARQL(graph, false, queryContext));
    // return eval$(graph, node, path, new PathEngineN(graph, false)) ;
  }

  /** Evaluate a path : counting semantics */
  public static <Node> Iterator<Node> evalN(Graph<Node> graph, Node node, Path<Node> path) {
    return eval$(graph, node, path, new PathEngineN(graph, true));
  }

  /** Evaluate a path : counting semantics */
  public static <Node> Iterator<Node> evalReverseN(Graph<Node> graph, Node node, Path<Node> path) {
    return eval$(graph, node, path, new PathEngineN<>(graph, false));
  }

  /** Evaluate a path : unique results */
  public static <Node> Iterator<Node> eval1(Graph<Node> graph, Node node, Path<Node> path) {
    return eval$(graph, node, path, new PathEngine1<>(graph, true));
  }

  /** Evaluate a path : unique results */
  public static <Node> Iterator<Node> evalReverse1(Graph<Node> graph, Node node, Path<Node> path) {
    return eval$(graph, node, path, new PathEngine1<>(graph, false));
  }

  /** Evaluate a path */
  static <Node> void eval$(
      Graph<Node> graph,
      Node node,
      Path<Node> path,
      PathEngine<Node> engine,
      Collection<Node> acc) {
      PathEvaluator<Node> evaluator = new PathEvaluator<>(graph, node, acc, engine);

    path.visit(evaluator);
  }

  /** Evaluate a path */
  static <Node> Iter<Node> eval$(
      Graph<Node> graph, Node node, Path<Node> path, PathEngine<Node> engine) {
    Collection<Node> acc = new ArrayList<>();

    PathEvaluator evaluator = new PathEvaluator<>(graph, node, acc, engine);

    path.visit(evaluator);

    return Iter.iter(acc);
  }
}
