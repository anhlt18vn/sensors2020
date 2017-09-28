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

import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.ExprList;
import org.rdf4led.common.mapping.Mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Interface to value-testing extensions to the expression evaluator. */
public abstract class FunctionBase<Node> implements Function<Node> {
  String uri = null;

  protected ExprList<Node> arguments = null;

  @Override
  public final void build(String uri, ExprList<Node> args) {
    this.uri = uri;

    arguments = args;

    checkBuild(uri, args);
  }

  @Override
  public Node exec(Mapping<Node> mapping, ExprList<Node> args, String uri) {
    // This is merely to allow functions to be
    // It duplicates code in E_Function/ExprFunctionN.

    if (args == null) throw new RuntimeException("FunctionBase: Null args list");

    List<Node> evalArgs = new ArrayList<Node>();

    for (Iterator<Expr<Node>> iter = args.iterator(); iter.hasNext(); ) {
      Expr<Node> e = iter.next();

      Node x = e.eval(mapping);

      evalArgs.add(x);
    }

    Node nv = exec(evalArgs);

    arguments = null;

    return nv;
  }

  /** Function call to a list of evaluated argument values */
  public abstract Node exec(List<Node> args);

  public abstract void checkBuild(String uri, ExprList<Node> args);
}
