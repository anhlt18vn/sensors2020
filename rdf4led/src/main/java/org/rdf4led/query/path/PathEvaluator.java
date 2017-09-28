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
import org.rdf4led.common.iterator.Iter;
import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;

import java.util.Collection;
import java.util.Iterator;

final class PathEvaluator<Node> implements PathVisitor<Node> {
  protected final Graph<Node> graph;
  protected Node node;
  protected Node ANY;
  protected final Collection<Node> output;
  private PathEngine engine;

  protected PathEvaluator(Graph g, Node n, Collection<Node> output, PathEngine<Node> engine) {
    this.graph = g;

    this.node = n;

    this.output = output;

    this.engine = engine;

    this.ANY = engine.ANY;
  }

  protected final void fill(Iterator<Node> iter) {
    for (; iter.hasNext(); ) {
      output.add(iter.next());
    }
  }

  // These operations yield the same results regardless of counting
  // (their subpaths may not).

  @Override
  public void visit(P_Link<Node> pathNode) {
    Iterator<Node> nodes = engine.doOne(node, pathNode.getNode());

    fill(nodes);
  }

  @Override
  public void visit(P_ReverseLink<Node> pathNode) {
    engine.flipDirection();

    Iterator<Node> nodes = engine.doOne(node, pathNode.getNode());

    fill(nodes);

    engine.flipDirection();
  }

  @Override
  public void visit(P_Inverse<Node> inversePath) {
    // boolean b = forwardMode ;
    // Flip direction and evaluate
    engine.flipDirection();

    engine.eval(inversePath.getSubPath(), node, output);

    engine.flipDirection();
  }

  @Override
  public void visit(P_NegPropSet<Node> pathNotOneOf) {
    engine.doNegatedPropertySet(pathNotOneOf, node, output);
  }

  @Override
  public void visit(P_Mod<Node> pathMod) {
    // do..Or.. need to take a visited set.

    if (pathMod.isZeroOrMore()) {
      // :p{0,}
      engine.doOneOrMoreN(pathMod.getSubPath(), node, output);
      return;
    }

    if (pathMod.isOneOrMore()) {
      engine.doOneOrMoreN(pathMod.getSubPath(), node, output);
      return;
    }

    if (pathMod.isFixedLength()) {
      engine.doFixedLengthPath(pathMod.getSubPath(), node, pathMod.getFixedLength(), output);
    } else {
      engine.doMultiLengthPath(
          pathMod.getSubPath(), node, pathMod.getMin(), pathMod.getMax(), output);
    }
  }

  @Override
  public void visit(P_FixedLength<Node> pFixedLength) {
    engine.doFixedLengthPath(pFixedLength.getSubPath(), node, pFixedLength.getCount(), output);
  }

  @Override
  public void visit(P_ZeroOrOne<Node> path) {
    engine.doZeroOrOne(path.getSubPath(), node, output);
  }

  @Override
  public void visit(P_ZeroOrMore1<Node> path) {
    engine.doZeroOrMore(path.getSubPath(), node, output);
  }

  @Override
  public void visit(P_ZeroOrMoreN<Node> path) {
    engine.doZeroOrMoreN(path.getSubPath(), node, output);
  }

  @Override
  public void visit(P_OneOrMore1<Node> path) {
    engine.doOneOrMore(path.getSubPath(), node, output);
  }

  @Override
  public void visit(P_OneOrMoreN<Node> path) {
    engine.doOneOrMoreN(path.getSubPath(), node, output);
  }

  @Override
  public void visit(P_Alt<Node> pathAlt) {
    engine.doAlt(pathAlt.getLeft(), pathAlt.getRight(), node, output);
  }

  @Override
  public void visit(P_Distinct<Node> pathDistinct) {
    PathEngine engine2 = engine;

    engine = new PathEngine1<Node>(graph, engine.direction());

    engine.eval(pathDistinct.getSubPath(), node, output);

    engine = engine2;
  }

  @Override
  public void visit(P_Multi<Node> pathMulti) {
    PathEngine<Node> engine2 = engine;
    engine = new PathEngineN<Node>(graph, engine.direction());

    engine.eval(pathMulti.getSubPath(), node, output);

    engine = engine2;
  }

  @Override
  public void visit(P_Shortest path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visit(P_Seq<Node> pathSeq) {
    engine.doSeq(pathSeq.getLeft(), pathSeq.getRight(), node, output);
  }

  // Other operations can produce duplicates and so may be executed in
  // different ways depending on cardibnality requirements.

  protected static class FilterExclude<Node> implements Filter<Triple<Node>> {
    private Collection<Node> excludes;

    public FilterExclude(Collection<Node> excludes) {
      this.excludes = excludes;
    }

    @Override
    public boolean accept(Triple<Node> triple) {
      return !excludes.contains(triple.getPredicate());
    }
  }

  protected final Iter<Triple<Node>> between(Node x, Node z) {
    return Iter.iter(engine.graphFind(x, ANY, z));
  }

  protected final void doZero(Path<Node> path, Node node, Collection<Node> output) {
    output.add(node);
  }
}
