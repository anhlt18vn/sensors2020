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

package org.rdf4led.query;


import org.rdf4led.graph.Triple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class whose purpose is to give a name to a collection of triples. Reduces the use of bland
 * "List" in APIs (Java 1.4)
 */
public class BasicPattern<Node> implements Iterable<Triple<Node>> {
  private List<Triple<Node>> triples;

  public BasicPattern() {
    this(new ArrayList<Triple<Node>>());
  }

  public BasicPattern(BasicPattern<Node> other) {
    this();

    triples.addAll(other.triples);
  }

  public BasicPattern(List<Triple<Node>> triples) {
    this.triples = triples;
  }

  public void add(Triple<Node> t) {
    triples.add(t);
  }

  public void addAll(BasicPattern<Node> other) {
    triples.addAll(other.triples);
  }

  public void add(int i, Triple<Node> t) {
    triples.add(i, t);
  }

  public Triple<Node> get(int i) {
    return triples.get(i);
  }

  @Override
  public Iterator<Triple<Node>> iterator() {
    return triples.listIterator();
  }

  public int size() {
    return triples.size();
  }

  public boolean isEmpty() {
    return triples.isEmpty();
  }

  public List<Triple<Node>> getList() {
    return triples;
  }

  @Override
  public int hashCode() {
    return triples.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof BasicPattern)) return false;
    BasicPattern<Node> bp = (BasicPattern<Node>) other;

    return triples.equals(bp.triples);
  }

  public boolean equiv(BasicPattern<Node> other) // , NodeIsomorphismMap
        // isoMap)
      {
    if (this.triples.size() != other.triples.size()) return false;

    for (int i = 0; i < this.triples.size(); i++) {
      Triple<Node> t1 = get(i);

      Triple<Node> t2 = other.get(i);

      if (!t1.equals(t2)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    throw new UnsupportedOperationException(
        "BasicPattern.toString() is not implemented in the version for embedded devices");
  }
}
