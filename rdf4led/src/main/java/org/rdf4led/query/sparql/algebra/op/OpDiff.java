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

import org.rdf4led.query.sparql.Tags;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

public class OpDiff<Node> extends Op2<Node> {
  //    public static Op<Node> create(Op<Node> left, Op<Node> right)
  //    {
  //        return new OpDiff<Node>(left, right) ;
  //    }

  public OpDiff(Op<Node> left, Op<Node> right) {
    super(left, right);
  }

  public byte getName() {
    return Tags.tagDiff;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> left, Op<Node> right) {
    return transform.transform(this, left, right);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy(Op<Node> newLeft, Op<Node> newRight) {
    return new OpDiff<Node>(newLeft, newRight);
  }

  @Override
  public boolean equalTo(Op<Node> op2) {
    if (!(op2 instanceof OpDiff)) return false;

    return super.sameArgumentsAs((Op2<Node>) op2);
  }
}
