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

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

import java.util.List;

/**
 * A "sequence" is a join-like operation where it is know that the the output of one step can be fed
 * into the input of the next (that is, no scoping issues arise).
 */
public class OpSequence<Node> extends OpN<Node> {
  public static OpSequence create() {
    return new OpSequence();
  }

  public static <Node> Op<Node> create(Op<Node> left, Op<Node> right) {
    // Avoid stages of nothing
    if (left == null && right == null) return null;
    // Avoid stages of one.
    if (left == null) return right;
    if (right == null) return left;
    // If left already an OpSequence ... maybe?
    if (left instanceof OpSequence) {
      OpSequence<Node> opSequence = (OpSequence) left;

      opSequence.add(right);

      return opSequence;
    }
    //        if ( right instanceof OpSequence )
    //        {
    //            OpSequence opSequence = (OpSequence)right ;
    //            // Add front.
    //            opSequence.getElements().add(0, left) ;
    //            return opSequence ;
    //        }

    OpSequence<Node> stage = new OpSequence();

    stage.add(left);

    stage.add(right);

    return stage;
  }

  private OpSequence() {
    super();
  }

  private OpSequence(List<Op<Node>> elts) {
    super(elts);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public boolean equalTo(Op op) {
    if (!(op instanceof OpSequence)) return false;
    OpSequence other = (OpSequence) op;
    return super.equalsSubOps(other);
  }

  @Override
  public Op apply(Transform<Node> transform, List<Op<Node>> elts) {
    return transform.transform(this, elts);
  }

  @Override
  public OpN<Node> copy(List<Op<Node>> elts) {
    return new OpSequence(elts);
  }
}
