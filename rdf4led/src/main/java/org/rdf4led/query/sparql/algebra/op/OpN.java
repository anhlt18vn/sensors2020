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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OpN.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class OpN<Node> extends OpBase<Node> {
  private List<Op<Node>> elements = new ArrayList<Op<Node>>();

  protected OpN() {
    elements = new ArrayList<Op<Node>>();
  }

  protected OpN(List<Op<Node>> x) {
    elements = x;
  }

  public void add(Op<Node> op) {
    elements.add(op);
  }

  public Op<Node> get(int idx) {
    return elements.get(idx);
  }

  public abstract Op<Node> apply(Transform<Node> transform, List<Op<Node>> elts);

  public abstract Op<Node> copy(List<Op<Node>> elts);

  // Tests the sub-elements for equalTo.
  protected boolean equalsSubOps(OpN<Node> op) {
    Iterator<Op<Node>> iter1 = elements.listIterator();

    Iterator<Op<Node>> iter2 = op.elements.listIterator();

    for (; iter1.hasNext(); ) {
      Op<Node> op1 = iter1.next();
      Op<Node> op2 = iter2.next();

      if (!op1.equalTo(op2)) return false;
    }
    return true;
  }

  public int size() {
    return elements.size();
  }

  @Override
  public int hashCode() {
    return elements.hashCode();
  }

  public List<Op<Node>> getElements() {
    return elements;
  }

  public Iterator<Op<Node>> iterator() {
    return elements.iterator();
  }
}
