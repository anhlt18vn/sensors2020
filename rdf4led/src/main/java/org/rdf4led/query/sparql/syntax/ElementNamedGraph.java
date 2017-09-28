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

/** Evaluate a query element based on source information in a named collection. */

/**
 * ElementNamedGraph.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>11 Sep 2015
 */
public class ElementNamedGraph<Node> extends Element<Node> {
  private Node sourceNode;

  private Element<Node> element;

  // GRAPH * (not in SPARQL)
  public ElementNamedGraph(Element<Node> el) {
    this(null, el);
  }

  // GRAPH <uri> or GRAPH ?var
  public ElementNamedGraph(Node n, Element<Node> el) {

    sourceNode = n;

    element = el;
  }

  public Node getGraphNameNode() {
    return sourceNode;
  }

  /** @return Returns the element. */
  public Element<Node> getElement() {
    return element;
  }

  @Override
  public int hashCode() {
    return element.hashCode() ^ sourceNode.hashCode();
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (el2 == null) return false;

    if (!(el2 instanceof ElementNamedGraph)) {
      return false;
    }

    ElementNamedGraph<Node> g2 = (ElementNamedGraph<Node>) el2;

    if (!this.getGraphNameNode().equals(g2.getGraphNameNode())) {
      return false;
    }

    if (!this.getElement().equalTo(g2.getElement())) {
      return false;
    }

    return true;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
