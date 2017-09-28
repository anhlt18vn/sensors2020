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

/**
 * Op2.java
 *
 * <p>Modified from Jena
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */

/** Super class for operators that combine two sub-operators */
public abstract class Op2<Node> extends OpBase<Node> {
  private Op<Node> left;
  private Op<Node> right;

  public Op2(Op<Node> left, Op<Node> right) {
    this.left = left;
    this.right = right;
  }

  public Op<Node> getLeft() {
    return left;
  }

  public Op<Node> getRight() {
    return right;
  }

  public void setLeft(Op<Node> op) {
    left = op;
  }

  public void setRight(Op<Node> op) {
    right = op;
  }

  public abstract Op<Node> apply(Transform<Node> transform, Op<Node> left, Op<Node> right);

  public abstract Op<Node> copy(Op<Node> left, Op<Node> right);

  @Override
  public int hashCode() {
    //        return left.hashCode()<<1 ^ right.hashCode() ^ getName().hashCode() ;
    return left.hashCode() << 1 ^ right.hashCode();
  }

  // equalsTo worker
  protected final boolean sameArgumentsAs(Op2<Node> op2) {
    return left.equalTo(op2.left) && right.equalTo(op2.right);
  }
}
