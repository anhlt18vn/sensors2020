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
public abstract class FunctionBase1<Node> extends FunctionBase<Node> {
  @Override
  public void checkBuild(String uri, ExprList<Node> args) {
    if (args.size() != 1)
      throw new RuntimeException("Function Utils.className(this) takes one argument");
  }

  @Override
  public final Node exec(List<Node> args) {
    if (args == null)
      // The contract on the function interface is that this should not happen.
      throw new RuntimeException("FunctionBase1: Null args list");

    if (args.size() != 1)
      throw new RuntimeException(
          "FunctionBase1: Wrong number of arguments: Wanted 1, got " + args.size());

    Node v1 = args.get(0);

    return exec(v1);
  }

  public abstract Node exec(Node v);
}
