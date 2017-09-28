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

import org.rdf4led.query.sparql.SortCondition;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

import java.util.List;

public class OpOrder<Node> extends OpModifier<Node> {
  private List<SortCondition<Node>> conditions;

  public OpOrder(Op<Node> subOp, List<SortCondition<Node>> conditions) {
    super(subOp);

    this.conditions = conditions;
  }

  public List<SortCondition<Node>> getConditions() {
    return conditions;
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpOrder<Node>(subOp, conditions);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public int hashCode() {
    return conditions.hashCode() ^ getSubOp().hashCode();
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpOrder)) {
      return false;
    }

    OpOrder<Node> opOrder = (OpOrder<Node>) other;

    if (!opOrder.getConditions().equals(this.getConditions())) {
      return false;
    }

    return getSubOp().equalTo(opOrder.getSubOp());
  }
}
