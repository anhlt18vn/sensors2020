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

import org.rdf4led.graph.Quad;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QuadPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/**
 * Algebra operation for a single quad.
 *
 * @see OpTriple
 */

/**
 * OpQuad.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpQuad<Node> extends Op0<Node> {
  private final Quad<Node> quad;

  private OpQuadPattern<Node> opQuadPattern = null;

  private QueryContext<Node> queryContext;

  public OpQuad(Quad<Node> quad, QueryContext<Node> queryContext) {
    this.quad = quad;
    this.queryContext = queryContext;
  }

  public final Quad<Node> getQuad() {
    return quad;
  }

  public OpQuadPattern<Node> asQuadPattern() {
    if (opQuadPattern == null) {
      BasicPattern<Node> bp = new BasicPattern<Node>();

      bp.add((Triple<Node>) getQuad());

      opQuadPattern = new OpQuadPattern<Node>(quad.getGraphNode(), bp, queryContext);
    }
    return opQuadPattern;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpQuad<Node>(quad, queryContext);
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpQuad)) return false;

    OpQuad<Node> opQuad = (OpQuad<Node>) other;

    return getQuad().equals(opQuad.getQuad());
  }

  @Override
  public int hashCode() {
    return OpBase.HashTriple ^ quad.hashCode();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  public boolean equivalent(OpQuadPattern<Node> opQuads) {
    QuadPattern<Node> quads = opQuads.getPattern();

    if (quads.size() != 1) return false;

    Quad<Node> q = quads.get(0);

    return quad.equals(q);
  }
}
