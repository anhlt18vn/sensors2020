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

package org.rdf4led.query.expr.function;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprNode;

import java.util.ArrayList;
import java.util.List;

/**
 * ExprFunction.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 Oct 2015
 */
public abstract class ExprFunction<Node> extends ExprNode<Node> {
  private List<Expr<Node>> argList = null;

  protected ExprFunction(QueryContext<Node> queryContext) {
    super(queryContext);
  }

  public abstract Expr<Node> getArg(int i);

  public abstract int numArgs();

  public List<Expr<Node>> getArgs() {
    if (argList != null) {
      return argList;
    }

    argList = new ArrayList<Expr<Node>>();

    for (int i = 1; i <= numArgs(); i++) {
      argList.add(this.getArg(i));
    }

    return argList;
  }

  @Override
  public boolean isFunction() {
    return true;
  }

  @Override
  public ExprFunction<Node> getFunction() {
    return this;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;

    if (!other.getClass().equals(this.getClass())) {
      return false;
    }

    ExprFunction<Node> ex = (ExprFunction<Node>) other;

    if (numArgs() != ex.numArgs()) {
      return false;
    }

    // Arguments are 1, 2, 3, ...
    for (int i = 1; i <= numArgs(); i++) {
      Expr<Node> a1 = this.getArg(i);

      Expr<Node> a2 = ex.getArg(i);

      if (!a1.equals(a2)) {
        return false;
      }
    }
    return true;
  }
}
