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

import org.rdf4led.graph.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * ElementGroup.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>11 Sep 2015
 */
public class ElementGroup<Node> extends Element<Node> {
  List<Element<Node>> elements = new ArrayList<Element<Node>>();

  public ElementGroup() {}

  public void addElement(Element<Node> el) {
    elements.add(el);
  }

  public void addTriplePattern(Triple<Node> t) {
    ensureBGP().addTriple(t);
  }

  public void addElementFilter(ElementFilter<Node> el) {
    addElement(el);
  }

  private ElementTriplesBlock<Node> ensureBGP() {
    if (elements.size() == 0) return pushBGP();

    Element<Node> top = top();

    if (top instanceof ElementTriplesBlock) {
      return (ElementTriplesBlock<Node>) top;
    }

    return pushBGP();
  }

  private ElementTriplesBlock<Node> pushBGP() {
    ElementTriplesBlock<Node> bgp = new ElementTriplesBlock<Node>();

    elements.add(bgp);

    return bgp;
  }

  private Element<Node> top() {
    return elements.get(elements.size() - 1);
  }

  public int mark() {
    return elements.size();
  }

  public List<Element<Node>> getElements() {
    return elements;
  }

  public boolean isEmpty() {
    return elements.size() == 0;
  }

  @Override
  public int hashCode() {
    int calcHashCode = Element.HashGroup; // So the empty group isn't zero.
    calcHashCode ^= getElements().hashCode();
    return calcHashCode;
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (el2 == null) return false;

    if (!(el2 instanceof ElementGroup)) return false;

    ElementGroup<Node> eg2 = (ElementGroup<Node>) el2;

    if (this.getElements().size() != eg2.getElements().size()) return false;

    for (int i = 0; i < this.getElements().size(); i++) {
      Element<Node> e1 = getElements().get(i);

      Element<Node> e2 = eg2.getElements().get(i);

      if (!e1.equalTo(e2)) return false;
    }

    return true;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
