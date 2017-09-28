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

import org.rdf4led.query.path.TriplePath;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

public class OpPath<Node> extends Op0<Node> {
  private TriplePath<Node> triplePath;

  // public OpPath(Node start, Path path, Node end)
  // {
  // this.subject = start ;
  // this.path = path ;
  // this.object = object ;
  // }

  public OpPath(TriplePath<Node> triplePath) {
    this.triplePath = triplePath;
  }

  public TriplePath<Node> getTriplePath() {
    return triplePath;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpPath<Node>(triplePath);
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpPath)) return false;

    OpPath<Node> p = (OpPath<Node>) other;

    if (triplePath.isTriple() ^ p.triplePath.isTriple()) return false;

    return triplePath.asTriple().equals(p.triplePath.asTriple());
  }

  @Override
  public int hashCode() {
    return triplePath.hashCode();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }
}
