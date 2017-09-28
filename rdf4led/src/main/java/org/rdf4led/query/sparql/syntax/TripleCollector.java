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

public interface TripleCollector<Node> {
  public void addTriple(Triple<Node> t);
  // The contract with the mark is that there should be no disturbing
  // triples 0..(mark-1) before using a mark. That is, use marks in
  // LIFO (stack) order.
  public int mark();

  public void addTriple(int index, Triple<Node> t);

  public void addTriplePath(TriplePath<Node> tPath);

  public void addTriplePath(int index, TriplePath<Node> tPath);
}
