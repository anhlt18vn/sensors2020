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

package org.rdf4led.query.sparql.algebra;

/**
 * Apply a visitor to the whole structure of Ops, recursively. Visit sub Op before the current level
 */
public class OpWalker<Node> {
  public void walk(Op<Node> op, OpVisitor<Node> visitor) {
    walk(new WalkerVisitor<Node>(visitor, null, null), op, visitor);
  }

  public void walk(
      Op<Node> op,
      OpVisitor<Node> visitor,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    walk(
        new WalkerVisitor<Node>(visitor, beforeVisitor, afterVisitor),
        op,
        visitor,
        beforeVisitor,
        afterVisitor);
  }

  public void walk(WalkerVisitor<Node> walkerVisitor, Op<Node> op, OpVisitor<Node> visitor) {
    op.visit(walkerVisitor);
  }

  public void walk(
      WalkerVisitor<Node> walkerVisitor,
      Op<Node> op,
      OpVisitor<Node> visitor,
      OpVisitor<Node> beforeVisitor,
      OpVisitor<Node> afterVisitor) {
    op.visit(walkerVisitor);
  }
}
