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
import org.rdf4led.query.sparql.syntax.ElementService;

/**
 * OpService.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpService<Node> extends Op1<Node> {
  private final Node serviceNode;

  private final ElementService<Node> serviceElement;

  private final boolean silent;

  public OpService(Node serviceNode, Op<Node> subOp, boolean silent) {
    this(serviceNode, subOp, null, silent);
  }

  public OpService(Node serviceNode, Op<Node> subOp, ElementService<Node> elt, boolean silent) {
    super(subOp);
    this.serviceNode = serviceNode;
    this.serviceElement = elt;
    this.silent = silent;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public Op<Node> copy(Op<Node> newOp) {
    return new OpService<Node>(serviceNode, newOp, silent);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  public Node getService() {
    return serviceNode;
  }

  public ElementService<Node> getServiceElement() {
    return serviceElement;
  }

  public boolean getSilent() {
    return silent;
  }

  @Override
  public int hashCode() {
    return serviceNode.hashCode() ^ getSubOp().hashCode();
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (!(other instanceof OpService)) return false;

    OpService<Node> opService = (OpService<Node>) other;

    if (!(serviceNode.equals(opService.serviceNode))) return false;

    if (opService.getSilent() != getSilent()) return false;

    return getSubOp().equalTo(opService.getSubOp());
  }
}
