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

package org.rdf4led.query.path;

import org.rdf4led.graph.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/** A class whose purpose is to give a name to a collection of triple paths. */
public class PathBlock<Node> implements Iterable<TriplePath<Node>> {
  private List<TriplePath<Node>> triplePaths = new ArrayList<TriplePath<Node>>();

  public PathBlock() {}

  public PathBlock(PathBlock<Node> other) {
    triplePaths.addAll(other.triplePaths);
  }

  public void add(TriplePath<Node> tp) {
    triplePaths.add(tp);
  }

  public void addAll(PathBlock<Node> other) {
    triplePaths.addAll(other.triplePaths);
  }

  public void add(int i, TriplePath<Node> tp) {
    triplePaths.add(i, tp);
  }

  public TriplePath<Node> get(int i) {
    return triplePaths.get(i);
  }

  @Override
  public ListIterator<TriplePath<Node>> iterator() {
    return triplePaths.listIterator();
  }

  public int size() {
    return triplePaths.size();
  }

  public boolean isEmpty() {
    return triplePaths.isEmpty();
  }

  public List<TriplePath<Node>> getList() {
    return triplePaths;
  }

  @Override
  public int hashCode() {
    return triplePaths.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof PathBlock)) return false;

    PathBlock<Node> bp = (PathBlock<Node>) other;

    return triplePaths.equals(bp.triplePaths);
  }

  public boolean equiv(PathBlock<Node> other) // , NodeIsomorphismMap isoMap)
      {
    if (this.triplePaths.size() != other.triplePaths.size()) return false;

    for (int i = 0; i < this.triplePaths.size(); i++) {
      TriplePath<Node> tp1 = get(i);
      TriplePath<Node> tp2 = other.get(i);

      if (!triplePathIso(tp1, tp2)) return false;
    }
    return true;
  }

  private boolean triplePathIso(TriplePath<Node> tp1, TriplePath<Node> tp2) {
    if (tp1.isTriple() ^ tp2.isTriple()) return false;

    if (tp1.isTriple()) return tripleIso(tp1.asTriple(), tp2.asTriple());
    else
      return tp1.getSubject().equals(tp2.getSubject())
          && tp1.getObject().equals(tp2.getObject())
          && tp1.getPath().equalTo(tp2.getPath());
  }

  private boolean tripleIso(Triple<Node> t1, Triple<Node> t2) {
    Node s1 = t1.getSubject();
    Node p1 = t1.getPredicate();
    Node o1 = t1.getObject();

    Node s2 = t2.getSubject();
    Node p2 = t2.getPredicate();
    Node o2 = t2.getObject();

    if (!s1.equals(s2)) return false;
    if (!p1.equals(p2)) return false;
    if (!o1.equals(o2)) return false;

    return true;
  }

  @Override
  public String toString() {
    return triplePaths.toString();
  }
}
