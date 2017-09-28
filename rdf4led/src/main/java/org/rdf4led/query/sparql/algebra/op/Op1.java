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

package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/** Super class for operators that operate on a single sub-operation (i.e. a table or sequence)) */

/**
 * Op1.java
 *
 * <p>Modified from Jena
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class Op1<Node> extends OpBase<Node> {
  private Op<Node> sub;

  public Op1(Op<Node> subOp) {
    this.sub = subOp;
  }

  public Op<Node> getSubOp() {
    return sub;
  }

  public abstract Op<Node> apply(Transform<Node> transform, Op<Node> subOp);

  public abstract Op<Node> copy(Op<Node> subOp);
}
