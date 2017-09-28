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

package org.rdf4led.query.sparql;

import org.rdf4led.query.expr.Expr;

/**
 * SortCondition.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class SortCondition<Node> {
  public Expr<Node> expression = null;

  public int direction = 0;

  public SortCondition(Expr<Node> expr, int dir) {
    expression = expr;

    direction = dir;
  }

  /** @return Returns the direction. */
  public int getDirection() {
    return direction;
  }

  /** @return Returns the expression. */
  public Expr<Node> getExpression() {
    return expression;
  }

  @Override
  public int hashCode() {
    int x = this.getDirection();
    if (getExpression() != null) x ^= getExpression().hashCode();
    return x;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!(other instanceof SortCondition)) return false;

    SortCondition<Node> sc = (SortCondition<Node>) other;

    if (sc.getDirection() != this.getDirection()) return false;

    if (!this.getExpression().equals(sc.getExpression())) return false;

    // if ( ! Utils.eq(this.getVariable(), sc.getVariable()) )
    // return false ;

    return true;
  }
}
