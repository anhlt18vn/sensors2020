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

package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.graph.Triple;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QuadPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/**
 * OpQuadPattern.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpQuadPattern<Node> extends Op0<Node> {

  private Node graphNode;

  private BasicPattern<Node> triples;

  private QuadPattern<Node> quads = null;

  // A QuadPattern is a block of quads with the same graph arg.
  // i.e. a BasicGraphPattern. This gets the blank node scoping right.

  // Quads are for a specific quad store.

  // Later, we may introduce OpQuadBlock for this and OpQuadPattern becomes
  // a sequence of such blocks.

  protected QueryContext<Node> queryContext;

  public OpQuadPattern(Node quadNode, BasicPattern<Node> triples, QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    this.graphNode = quadNode;

    this.triples = triples;
  }

  private void initQuads() {
    if (quads == null) {
      quads = new QuadPattern<Node>();

      for (Triple<Node> t : triples) {
        quads.add(queryContext.createQuad(graphNode, t));
      }
    }
  }

  public QuadPattern<Node> getPattern() {
    initQuads();

    return quads;
  }

  public Node getGraphNode() {
    return graphNode;
  }

  public BasicPattern<Node> getBasicPattern() {
    return triples;
  }

  public boolean isEmpty() {
    return triples.size() == 0;
  }

  public boolean isDefaultGraph() {
    return queryContext.isDefaultGraph(graphNode);
  }

  public boolean isExplicitDefaultGraph() {
    return queryContext.isDefaultGraphExplicit(graphNode);
  }

  public boolean isUnionGraph() {
    return queryContext.isUnionGraph(graphNode);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpQuadPattern<Node>(graphNode, triples, queryContext);
  }

  @Override
  public int hashCode() {
    return graphNode.hashCode() ^ triples.hashCode();
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpQuadPattern)) return false;

    OpQuadPattern<Node> opQuad = (OpQuadPattern<Node>) other;

    if (!graphNode.equals(opQuad.graphNode)) {
      return false;
    }

    return triples.equiv(opQuad.triples);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }
}
