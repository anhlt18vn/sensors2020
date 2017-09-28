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
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/**
 * Algebra operation for a single triple. Not normally used - triples are contained in basic graph
 * patterns (which is the unit of extension in SPARQL, and also the unit for adapting to other data
 * store in ARQ). But for experimentation, it can be useful to have a convenience direct triple
 * access.
 *
 * @see OpBGP
 */

/**
 * OpTriple.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpTriple<Node> extends Op0<Node> {
  private final Triple<Node> triple;

  private OpBGP<Node> opBGP = null;

  public OpTriple(Triple<Node> triple) {
    this.triple = triple;
  }

  public final Triple<Node> getTriple() {
    return triple;
  }

  public final OpBGP<Node> asBGP() {
    if (opBGP == null) {
      BasicPattern<Node> bp = new BasicPattern<Node>();

      bp.add(getTriple());

      opBGP = new OpBGP<Node>(bp);
    }
    return opBGP;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpTriple<Node>(triple);
  }

  @Override
  public boolean equalTo(Op<Node> other) // , NodeIsomorphismMap labelMap)
      {
    if (!(other instanceof OpTriple)) {
      return false;
    }

    OpTriple<Node> opTriple = (OpTriple<Node>) other;

    return (getTriple().equals(opTriple.getTriple()));
  }

  @Override
  public int hashCode() {
    return OpBase.HashTriple ^ triple.hashCode();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  public boolean equivalent(OpBGP<Node> opBGP) {
    BasicPattern<Node> bgp = opBGP.getPattern();

    if (bgp.size() != 1) return false;

    Triple<Node> t = bgp.get(0);

    return triple.equals(t);
  }
}
