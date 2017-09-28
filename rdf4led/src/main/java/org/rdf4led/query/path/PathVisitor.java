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

public interface PathVisitor<Node> {
  public void visit(P_Link<Node> pathNode);

  public void visit(P_ReverseLink<Node> pathNode);

  public void visit(P_NegPropSet<Node> pathNotOneOf);

  public void visit(P_Inverse<Node> inversePath);

  public void visit(P_Mod<Node> pathMod);

  public void visit(P_FixedLength<Node> pFixedLength);

  public void visit(P_Distinct<Node> pathDistinct);

  public void visit(P_Multi<Node> pathMulti);

  public void visit(P_Shortest<Node> pathShortest);

  public void visit(P_ZeroOrOne<Node> path);

  public void visit(P_ZeroOrMore1<Node> path);

  public void visit(P_ZeroOrMoreN<Node> path);

  public void visit(P_OneOrMore1<Node> path);

  public void visit(P_OneOrMoreN<Node> path);

  public void visit(P_Alt<Node> pathAlt);

  public void visit(P_Seq<Node> pathSeq);
}
