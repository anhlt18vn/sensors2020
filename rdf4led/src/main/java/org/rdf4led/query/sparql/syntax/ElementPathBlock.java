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
import org.rdf4led.query.path.PathBlock;
import org.rdf4led.query.path.TriplePath;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;

import java.util.Iterator;

/** A SPARQL BasicGraphPattern */
public class ElementPathBlock<Node> extends Element<Node> implements TripleCollector<Node> {

  private PathBlock<Node> pattern = new PathBlock<Node>();

  private QueryContext<Node> queryContext;

  public ElementPathBlock(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;
  }

  public ElementPathBlock(BasicPattern<Node> bgp, QueryContext<Node> queryContext) {
    this.queryContext = queryContext;

    for (Triple<Node> t : bgp.getList()) {
      addTriple(t);
    }
  }

  public boolean isEmpty() {
    return pattern.isEmpty();
  }

  public void addTriple(TriplePath<Node> tp) {
    pattern.add(tp);
  }

  @Override
  public int mark() {
    return pattern.size();
  }

  @Override
  public void addTriple(Triple<Node> t) {
    addTriplePath(
        new TriplePath<Node>(t) {
          @Override
          public boolean isURI(Node p) {
            return queryContext.dictionary().isURI(p);
          }
        });
  }

  @Override
  public void addTriple(int index, Triple<Node> t) {
    addTriplePath(
        index,
        new TriplePath<Node>(t) {
          @Override
          public boolean isURI(Node p) {
            return queryContext.dictionary().isURI(p);
          }
        });
  }

  // @Override
  public void addTriplePath(TriplePath<Node> tPath) {
    pattern.add(tPath);
  }

  // @Override
  public void addTriplePath(int index, TriplePath<Node> tPath) {
    pattern.add(index, tPath);
  }

  public PathBlock<Node> getPattern() {
    return pattern;
  }

  public Iterator<TriplePath<Node>> patternElts() {
    return pattern.iterator();
  }

  @Override
  public int hashCode() {
    int calcHashCode = Element.HashBasicGraphPattern;

    calcHashCode ^= pattern.hashCode();

    return calcHashCode;
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (!(el2 instanceof ElementPathBlock)) return false;

    ElementPathBlock<Node> eg2 = (ElementPathBlock<Node>) el2;

    return this.pattern.equiv(eg2.pattern);
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
