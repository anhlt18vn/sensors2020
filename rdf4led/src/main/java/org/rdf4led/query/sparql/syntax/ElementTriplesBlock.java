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
import org.rdf4led.query.path.TriplePath;
import org.rdf4led.query.BasicPattern;

import java.util.Iterator;

/** The org.rdf4led.sparql.algebra.syntax eleemnt for a SPARQL BasicGraphPattern */
public class ElementTriplesBlock<Node> extends Element<Node> implements TripleCollector<Node> {
  private final BasicPattern<Node> pattern;

  public ElementTriplesBlock() {
    pattern = new BasicPattern<Node>();
  }

  public ElementTriplesBlock(BasicPattern<Node> bgp) {
    pattern = bgp;
  }

  public boolean isEmpty() {
    return pattern.isEmpty();
  }

  @Override
  public void addTriple(Triple<Node> t) {
    pattern.add(t);
  }

  @Override
  public int mark() {
    return pattern.size();
  }

  @Override
  public void addTriple(int index, Triple<Node> t) {
    pattern.add(index, t);
  }

  @Override
  public void addTriplePath(TriplePath<Node> path) {
    throw new RuntimeException("Triples-only collector");
  }

  @Override
  public void addTriplePath(int index, TriplePath<Node> path) {
    throw new RuntimeException("Triples-only collector");
  }

  public BasicPattern<Node> getPattern() {
    return pattern;
  }

  public Iterator<Triple<Node>> patternElts() {
    return pattern.iterator();
  }

  @Override
  public int hashCode() {
    int calcHashCode = Element.HashBasicGraphPattern;

    calcHashCode ^= pattern.hashCode();

    return calcHashCode;
  }

  @Override
  public boolean equalTo(Element<Node> el2) // , NodeIsomorphismMap isoMap)
      {
    if (!(el2 instanceof ElementTriplesBlock)) {
      return false;
    }

    ElementTriplesBlock<Node> eg2 = (ElementTriplesBlock<Node>) el2;

    return this.pattern.equiv(eg2.pattern);
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
