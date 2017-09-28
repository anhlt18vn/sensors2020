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

/**
 * ElementFetch.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>11 Sep 2015
 */
public class ElementFetch<Node> extends Element<Node> {
  private Node sourceNode;

  // FETCH <uri> or FETCH ?var
  public ElementFetch(Node n) {
    sourceNode = n;
  }

  public Node getFetchNode() {
    return sourceNode;
  }

  @Override
  public int hashCode() {
    return sourceNode.hashCode() ^ Element.HashFetch;
  }

  @Override
  public boolean equalTo(Element<Node> el2) // , NodeIsomorphismMap isoMap)
      {
    if (el2 == null) return false;

    if (!(el2 instanceof ElementFetch)) return false;
    ElementFetch<Node> g2 = (ElementFetch<Node>) el2;

    if (!this.getFetchNode().equals(g2.getFetchNode())) return false;
    return true;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
