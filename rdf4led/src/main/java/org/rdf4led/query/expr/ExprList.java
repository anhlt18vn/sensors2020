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

import org.rdf4led.query.QueryContext;
import org.rdf4led.common.mapping.Mapping;

import java.util.*;

public class ExprList<Node> implements Iterable<Expr<Node>> {

  private final List<Expr<Node>> expressions;

  public ExprList() {
    expressions = new ArrayList<Expr<Node>>();
  }

  public ExprList(ExprList<Node> other) {
    this();

    expressions.addAll(other.expressions);
  }

  public ExprList(Expr<Node> expr) {
    this();

    expressions.add(expr);
  }

  public ExprList(List<Expr<Node>> x) {
    expressions = x;
  }

  public boolean isSatisfied(Mapping<Node> mapping) {
    for (Expr<Node> expr : expressions) {
      if (!expr.isSatisfied(mapping)) return false;
    }
    return true;
  }

  public Expr<Node> get(int idx) {
    return expressions.get(idx);
  }

  public int size() {
    return expressions.size();
  }

  public boolean isEmpty() {
    return expressions.isEmpty();
  }

  public ExprList<Node> subList(int fromIdx, int toIdx) {
    return new ExprList<Node>(expressions.subList(fromIdx, toIdx));
  }

  public ExprList<Node> tail(int fromIdx) {
    return subList(fromIdx, expressions.size());
  }

  public Set<Node> getVarsMentioned() {
    Set<Node> x = new HashSet<Node>();

    varsMentioned(x);

    return x;
  }

  public void varsMentioned(Collection<Node> acc) {
    for (Expr<Node> expr : expressions) expr.varsMentioned(acc);
  }

  //	public ExprList<Node> copySubstitute(Mapping<Node> binding)
  //	{
  //		return copySubstitute(binding, false);
  //	}
  //
  //	public ExprList<Node> copySubstitute(Mapping<Node> binding, boolean foldConstants)
  //	{
  //		ExprList<Node> x = new ExprList<Node>();
  //
  //		for (Iterator<Expr<Node>> iter = expressions.iterator(); iter.hasNext();)
  //		{
  //			Expr<Node> org.rdf4led.common.data.expr = iter.next();
  //
  //			org.rdf4led.common.data.expr = org.rdf4led.common.data.expr.copySubstitute(binding, foldConstants);
  //
  //			x.add(org.rdf4led.common.data.expr);
  //
  //		}
  //
  //		return x;
  //	}

  public void addAll(ExprList<Node> exprs) {
    expressions.addAll(exprs.getList());
  }

  public void add(Expr<Node> expr) {
    expressions.add(expr);
  }

  public List<Expr<Node>> getList() {
    return expressions;
  }

  @Override
  public Iterator<Expr<Node>> iterator() {
    return expressions.iterator();
  }

  public void prepareExprs(QueryContext<Node> queryContext) {
    ExprBuild<Node> build = new ExprBuild<Node>();

    // Give each expression the chance to set up (bind functions)
    for (Expr<Node> expr : expressions) {
      // queryContext..walk(build, org.rdf4led.common.data.expr);
      queryContext.getExprWalker().walk(build, expr);
    }
  }

  @Override
  public String toString() {
    return expressions.toString();
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof ExprList)) return false;

    ExprList<Node> exprs = (ExprList<Node>) other;

    return expressions.equals(exprs.expressions);
  }
}
