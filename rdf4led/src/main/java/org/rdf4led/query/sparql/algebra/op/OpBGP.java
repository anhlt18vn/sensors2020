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

import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/**
 * OpBGP.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>22 Oct 2015
 */
public class OpBGP<Node> extends Op0<Node> {
  private BasicPattern<Node> pattern;

  public OpBGP() {
    this(new BasicPattern<Node>());
  }

  public OpBGP(BasicPattern<Node> pattern) {
    this.pattern = pattern;
  }

  public BasicPattern<Node> getPattern() {
    return pattern;
  }

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpBGP<Node>(pattern);
  }

  @Override
  public int hashCode() {
    int calcHashCode = OpBase.HashBasicGraphPattern;

    calcHashCode ^= pattern.hashCode();

    return calcHashCode;
  }

  @Override
  public boolean equalTo(Op<Node> op2) {
    if (!(op2 instanceof OpBGP)) return false;

    OpBGP<Node> bgp2 = (OpBGP<Node>) op2;

    return pattern.equiv(bgp2.pattern);
  }
}
