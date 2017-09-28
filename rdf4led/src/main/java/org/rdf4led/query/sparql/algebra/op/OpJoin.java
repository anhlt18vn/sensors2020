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

public class OpJoin<Node> extends Op2<Node> {
  /**
   * Create join, removing any joins with the identity table and any nulls. <br>
   * Join.create(null, org.rdf4led.sparql.algebra.op) is org.rdf4led.sparql.algebra.op. <br>
   * Join.create(org.rdf4led.sparql.algebra.op, null) is org.rdf4led.sparql.algebra.op. <br>
   * Join.create(TableUnit, org.rdf4led.sparql.algebra.op) is org.rdf4led.sparql.algebra.op. <br>
   * Join.create(org.rdf4led.sparql.algebra.op, TableUnit) is org.rdf4led.sparql.algebra.op.
   */
  public OpJoin(Op<Node> left, Op<Node> right) {
    super(left, right);
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
    return new OpJoin<Node>(newLeft, newRight);
  }

  @Override
  public boolean equalTo(Op<Node> op2) {
    if (!(op2 instanceof OpJoin)) {
      return false;
    }

    return super.sameArgumentsAs((Op2<Node>) op2);
  }
}
