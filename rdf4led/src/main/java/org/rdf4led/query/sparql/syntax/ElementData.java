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

import org.rdf4led.common.mapping.Mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElementData<Node> extends Element<Node> {
  private List<Node> nodeVars = new ArrayList<Node>();

  private List<Mapping<Node>> rows = new ArrayList<Mapping<Node>>();

  public ElementData() {}

  public List<Node> getVars() {
    return nodeVars;
  }

  public List<Mapping<Node>> getRows() {
    return rows;
  }

  public void add(Node nodeVar) {
    if (!nodeVars.contains(nodeVar)) {
      nodeVars.add(nodeVar);
    }
  }

  public void add(Mapping<Node> mapping) {
    Iterator<Node> iter = mapping.vars();

    while (iter.hasNext()) {
      Node v = iter.next();

      if (!nodeVars.contains(v)) {
        throw new RuntimeException("Variable " + v + " not already declared for ElementData");
      }
    }

    rows.add(mapping);
  }

  @Override
  public boolean equalTo(Element<Node> el2) // , NodeIsomorphismMap isoMap)
      {
    if (!(el2 instanceof ElementData)) return false;

    ElementData<Node> f2 = (ElementData<Node>) el2;

    if (!nodeVars.equals(f2.nodeVars)) return false;

    System.out.println("ElementedData need check to make sure bug is not from here");
    // if ( ! ResultSetCompare.equalsByTest(rows, f2.rows, new
    // ResultSetCompare.BNodeIso(NodeUtils.sameTerm)) )
    // return false ;

    return true;
  }

  @Override
  public int hashCode() {
    return nodeVars.hashCode() ^ rows.hashCode();
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
