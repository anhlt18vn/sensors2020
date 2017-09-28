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

/** A triples-only TripleCollector. */
public class TripleCollectorBGP<Node> implements TripleCollector<Node> {
  BasicPattern<Node> bgp = new BasicPattern<Node>();

  public TripleCollectorBGP() {}

  public BasicPattern<Node> getBGP() {
    return bgp;
  }

  @Override
  public void addTriple(Triple<Node> t) {
    bgp.add(t);
  }

  @Override
  public int mark() {
    return bgp.size();
  }

  @Override
  public void addTriple(int index, Triple<Node> t) {
    bgp.add(index, t);
  }

  @Override
  public void addTriplePath(TriplePath<Node> path) {
    throw new RuntimeException("Triples-only collector");
  }
  //
  @Override
  public void addTriplePath(int index, TriplePath<Node> path) {
    throw new RuntimeException("Triples-only collector");
  }
}
