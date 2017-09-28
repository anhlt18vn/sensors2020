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

/**
 * Do-nothing class that means that tags/labels/comments can be left in the algebra tree. If
 * serialized, toString called on the object, reparsing yields a string. Can have zero one one sub
 * ops.
 */

/**
 * OpLabel.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpLabel<Node> extends Op1<Node> {
  // Beware : while this is a Op1, it might have no sub operation.
  // (label "foo") and (label "foo" (other ...)) are legal.

  // Better: string+(object for internal use only)+org.rdf4led.sparql.algebra.op?
  // public static Op create(Object label, Op org.rdf4led.sparql.algebra.op) { return new OpLabel(label,
  // org.rdf4led.sparql.algebra.op) ; }

  private Object object;

  public OpLabel(Object thing) {
    this(thing, null);
  }

  public OpLabel(Object thing, Op<Node> op) {
    super(op);

    this.object = thing;
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpLabel)) {
      return false;
    }

    OpLabel<Node> opLabel = (OpLabel<Node>) other;

    if (!object.equals(opLabel.object)) {
      return false;
    }

    return getSubOp().equals(opLabel.getSubOp());
  }

  @Override
  public int hashCode() {
    int x = HashLabel;
    x ^= object.hashCode();
    x ^= getSubOp().hashCode();
    return x;
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  public Object getObject() {
    return object;
  }

  public boolean hasSubOp() {
    return getSubOp() != null;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpLabel<Node>(object, subOp);
  }
}
