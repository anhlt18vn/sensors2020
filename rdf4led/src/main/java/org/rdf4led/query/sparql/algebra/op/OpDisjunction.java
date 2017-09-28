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
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

import java.util.List;

/** N-way disjunction. OpUnion remains as the strict SPARQL algebra operator. */

/**
 * OpDisjunction.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpDisjunction<Node> extends OpN<Node> {
  // public static OpDisjunction create()
  // {
  // return new OpDisjunction() ;
  // }

  // public static Op create(Op left, Op right)
  // {
  // // Avoid stages of nothing
  // if ( left == null && right == null )
  // return null ;
  // // Avoid stages of one.
  // if ( left == null )
  // return right ;
  // if ( right == null )
  // return left ;
  //
  // if ( left instanceof OpDisjunction )
  // {
  // OpDisjunction opDisjunction = (OpDisjunction)left ;
  // opDisjunction.add(right) ;
  // return opDisjunction ;
  // }
  //
  // // if ( right instanceof OpDisjunction )
  // // {
  // // OpDisjunction opSequence = (OpDisjunction)right ;
  // // // Add front.
  // // opDisjunction.getElements().add(0, left) ;
  // // return opDisjunction ;
  // // }
  //
  // OpDisjunction stage = new OpDisjunction() ;
  // stage.add(left) ;
  // stage.add(right) ;
  // return stage ;
  // }

  private OpDisjunction(List<Op<Node>> elts) {
    super(elts);
  }

  private OpDisjunction() {
    super();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  // { throw new ARQNotImplemented("OpDisjunction.visit") ; }

  @Override
  public boolean equalTo(Op<Node> op) {
    if (!(op instanceof OpDisjunction)) return false;

    OpDisjunction<Node> other = (OpDisjunction<Node>) op;

    return super.equalsSubOps(other);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, List<Op<Node>> elts) {
    return transform.transform(this, elts);
  }

  // { throw new ARQNotImplemented("OpDisjunction.apply") ; }

  @Override
  public Op<Node> copy(List<Op<Node>> elts) {
    return new OpDisjunction<Node>(elts);
  }
}
