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

import org.rdf4led.query.expr.ExprList;

import java.util.List;

/** Support for a function of one argument. */
public abstract class FunctionBase2<Node> extends FunctionBase<Node> {
  @Override
  public void checkBuild(String uri, ExprList<Node> args) {
    if (args.size() != 2)
      throw new RuntimeException("Function Utils.className(this) takes two arguments");
  }

  @Override
  public final Node exec(List<Node> args) {
    if (args == null)
      // The contract on the function interface is that this should not happen.
      throw new RuntimeException("Utils.className(this) Null args list");

    if (args.size() != 2)
      throw new RuntimeException(
          " Utils.className(this)  Wrong number of arguments: Wanted 2, got " + args.size());

    Node v1 = args.get(0);

    Node v2 = args.get(1);

    return exec(v1, v2);
  }

  public abstract Node exec(Node v1, Node v2);
}
