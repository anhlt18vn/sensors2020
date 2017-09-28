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
 * OpList.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpList<Node> extends OpModifier<Node> {
  public OpList(Op<Node> subOp) {
    super(subOp);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpList<Node>(subOp);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public int hashCode() {
    return getSubOp().hashCode() ^ OpBase.HashToList;
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpList)) {
      return false;
    }

    return getSubOp().equalTo(((OpList<Node>) other).getSubOp());
  }
}
