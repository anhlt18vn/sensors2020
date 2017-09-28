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

import java.util.ArrayList;
import java.util.List;

public class OpProject<Node> extends OpModifier<Node> {
  private List<Node> vars = new ArrayList<Node>();

  public OpProject(Op<Node> subOp, List<Node> vars) {
    super(subOp);

    this.vars = vars;
  }

  public List<Node> getVars() {
    return vars;
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpProject<Node>(subOp, vars);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public int hashCode() {
    return vars.hashCode() ^ getSubOp().hashCode();
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpProject)) {
      return false;
    }

    OpProject<Node> opProject = (OpProject<Node>) other;

    if (!vars.equals(opProject.vars)) {
      return false;
    }

    return getSubOp().equalTo(opProject.getSubOp());
  }
}
