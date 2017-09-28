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

package org.rdf4led.query.sparql.syntax;

import org.rdf4led.query.expr.Expr;

public class ElementAssign<Node> extends Element<Node> {
  private Node varNode;

  private Expr<Node> expr;

  public ElementAssign(Node varNode, Expr<Node> expr) {
    this.varNode = varNode;
    this.expr = expr;
  }

  public Node getVar() {
    return varNode;
  }

  public Expr<Node> getExpr() {
    return expr;
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (!(el2 instanceof ElementAssign)) return false;

    ElementAssign<Node> f2 = (ElementAssign<Node>) el2;

    if (!this.getVar().equals(f2.getVar())) return false;

    if (!this.getExpr().equals(f2.getExpr())) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return varNode.hashCode() ^ expr.hashCode();
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
