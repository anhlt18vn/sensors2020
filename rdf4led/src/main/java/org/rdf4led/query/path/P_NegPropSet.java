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

import java.util.ArrayList;
import java.util.List;

public class P_NegPropSet<Node> extends PathBase<Node> {
  List<P_Path0<Node>> nodes;
  List<Node> forwardNodes;
  List<Node> backwardNodes;

  public P_NegPropSet() {
    nodes = new ArrayList<P_Path0<Node>>();

    forwardNodes = new ArrayList<Node>();
    backwardNodes = new ArrayList<Node>();
  }

  // addFwd, addBkwd?
  public void add(P_Path0<Node> p) {
    nodes.add(p);

    if (p.isForward()) forwardNodes.add(p.getNode());
    else backwardNodes.add(p.getNode());
  }

  // public List<Node> getExcludedNodes() { return forwardNodes ; }

  public List<P_Path0<Node>> getNodes() {
    return nodes;
  }

  public List<Node> getFwdNodes() {
    return forwardNodes;
  }

  public List<Node> getBwdNodes() {
    return backwardNodes;
  }

  @Override
  public void visit(PathVisitor<Node> visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equalTo(Path<Node> path2) // , NodeIsomorphismMap isoMap)
      {
    if (!(path2 instanceof P_NegPropSet)) return false;

    P_NegPropSet<Node> other = (P_NegPropSet<Node>) path2;

    return nodes.equals(other.nodes);
  }

  @Override
  public int hashCode() {
    return nodes.hashCode() ^ hashNegPropClass;
  }
}
