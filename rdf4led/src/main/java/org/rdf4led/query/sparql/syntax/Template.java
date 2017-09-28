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
import org.rdf4led.query.BasicPattern;

import java.util.List;

/** Triples template. */
public class Template<Node> {
  static final int HashTemplateGroup = 0xB1;

  private final BasicPattern<Node> bgp;

  public Template(BasicPattern<Node> bgp) {
    this.bgp = bgp;
  }

  // public void addTriple(Triple t) { quads.addTriple(t) ; }
  // public int mark() { return quads.mark() ; }
  // public void addTriple(int storage, Triple t) { quads.addTriple(storage, t) ;
  // }
  // public void addTriplePath(TriplePath path)
  // { throw new ARQException("Triples-only collector") ; }
  //
  // public void addTriplePath(int storage, TriplePath path)
  // { throw new ARQException("Triples-only collector") ; }

  public BasicPattern<Node> getBGP() {
    return bgp;
  }

  public List<Triple<Node>> getTriples() {
    return bgp.getList();
  }

  // -------------------------

  // public void subst(Collection<Triple<Node>> org.rdf4led.common.data.incremental.acc, Map<Node, Node> bNodeMap,
  // Binding<Node> b)
  // {
  // for ( Triple<> t : bgp.getList() )
  // {
  // t = TemplateLib.subst(t, b, bNodeMap) ;
  //
  // org.rdf4led.common.data.incremental.acc.add(t) ;
  // }
  // }

  @Override
  public int hashCode() {
    // BNode invariant hashCode.
    int calcHashCode = Template.HashTemplateGroup;

    for (Triple<Node> t : bgp.getList()) {
      calcHashCode ^= hash(t) ^ calcHashCode << 1;
    }

    return calcHashCode;
  }

  private int hash(Triple<Node> triple) {
    int hash = 0;

    hash = hashNode(triple.getSubject()) ^ hash << 1;

    hash = hashNode(triple.getPredicate()) ^ hash << 1;

    hash = hashNode(triple.getObject()) ^ hash << 1;

    return hash;
  }

  private int hashNode(Node node) {
    return node.hashCode();
  }

  public boolean equalIso(Object temp2) {
    if (!(temp2 instanceof Template)) return false;

    Template<Node> tg2 = (Template<Node>) temp2;

    List<Triple<Node>> list1 = this.bgp.getList();

    List<Triple<Node>> list2 = tg2.bgp.getList();

    if (list1.size() != list2.size()) return false;

    for (int i = 0; i < list1.size(); i++) {
      Triple<Node> t1 = list1.get(i);
      Triple<Node> t2 = list2.get(i);

      if (!t1.equals(t2)) return false;
    }
    return true;
  }
}
