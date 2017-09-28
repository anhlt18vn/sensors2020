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

package org.rdf4led.query.expr;

import org.rdf4led.common.mapping.Mapping;

import java.util.*;

public class VarExprList<Node> {
  private List<Node> varNodes;

  private Map<Node, Expr<Node>> exprs;

  public VarExprList(List<Node> varNodes) {
    this.varNodes = varNodes;

    this.exprs = new HashMap<Node, Expr<Node>>();
  }

  public VarExprList(VarExprList<Node> other) {
    this.varNodes = new ArrayList<Node>(other.varNodes);

    this.exprs = new HashMap<Node, Expr<Node>>(other.exprs);
  }

  public VarExprList() {
    this.varNodes = new ArrayList<Node>();

    this.exprs = new HashMap<Node, Expr<Node>>();
  }

  public VarExprList(Node varNode, Expr<Node> expr) {
    this();

    add(varNode, expr);
  }

  public List<Node> getVars() {
    return varNodes;
  }

  public Map<Node, Expr<Node>> getExprs() {
    return exprs;
  }

  public boolean contains(Node varNode) {
    return varNodes.contains(varNode);
  }

  public boolean hasExpr(Node varNode) {
    return exprs.containsKey(varNode);
  }

  public Expr<Node> getExpr(Node varNode) {
    return exprs.get(varNode);
  }

  // Or Binding.get(var, NamedExprList)
  public Node get(Node varNode, Mapping<Node> mapping) // , QueryContext<Node> queryContext)
      {
    Expr<Node> expr = exprs.get(varNode);

    if (expr == null) {
      return mapping.getValue(varNode);
    }

    Node nodeValue = expr.eval(mapping);

    if (nodeValue == null) {
      return null;
    }

    return nodeValue;
  }

  public void add(Node varNode) {
    // Checking here controls whether duplicate variables are allowed.
    // Duplicates with expressions are not allowed (add(Var, Expr))
    // See ARQ.allowDuplicateSelectColumns

    // Every should work either way round if this is enabled.
    // Checking is done in Query for adding result vars, and group vars.
    // if ( vars.contains(var) )
    varNodes.add(varNode);
  }

  public void add(Node varNode, Expr<Node> expr) {
    if (expr == null) {
      add(varNode);

      return;
    }

    if (varNode == null)
      throw new RuntimeException("Attempt to add a named expression with a null variable");

    if (exprs.containsKey(varNode))
      throw new RuntimeException("Attempt to assign an expression again");

    add(varNode);

    exprs.put(varNode, expr);
  }

  public void addAll(VarExprList<Node> other) {
    for (Iterator<Node> iter = other.varNodes.iterator(); iter.hasNext(); ) {
      Node varNode = iter.next();

      Expr<Node> e = other.getExpr(varNode);

      add(varNode, e);
    }
  }

  public int size() {
    return varNodes.size();
  }

  public boolean isEmpty() {
    return varNodes.isEmpty();
  }

  @Override
  public int hashCode() {
    int x = varNodes.hashCode() ^ exprs.hashCode();

    return x;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof VarExprList)) {
      return false;
    }

    VarExprList<Node> x = (VarExprList<Node>) other;

    return varNodes.equals(x.varNodes) && exprs.equals(x.exprs);
  }

  @Override
  public String toString() {
    return varNodes.toString() + " // " + exprs.toString();
  }
}
