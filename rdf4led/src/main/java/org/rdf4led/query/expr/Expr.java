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

import org.rdf4led.query.sparql.NodeTransform;
import org.rdf4led.query.expr.function.ExprFunction;
import org.rdf4led.query.expr.function.ExprVisitor;
import org.rdf4led.common.mapping.Mapping;

import java.util.Collection;
import java.util.Set;

/**
 * Expr.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public interface Expr<Node> {
  public boolean isSatisfied(Mapping<Node> mapping);

  public Set<Node> getVarsMentioned();

  public void varsMentioned(Collection<Node> acc);

  public Node eval(Mapping<Node> binding);

  public Expr<Node> applyNodeTransform(NodeTransform<Node> transform);

  public boolean isVariable();

  /** Variable (or null) */
  public ExprVar<Node> getExprVar();

  /** Convert to a Var variable. */
  public Node asVar();

  /**
   * Answer whether this is a constant expression - false includes "don't know" No constant folding
   * so "false" from an expression that evaluates to a constant
   */
  public boolean isConstant();

  /** NodeValue constant (returns null if not a constant) */
  public Node getConstant();

  /** Answer wether this is a function. */
  public boolean isFunction();

  /** Get the function (returns null if not a function) */
  public ExprFunction<Node> getFunction();

  public void visit(ExprVisitor<Node> visitor);
}
