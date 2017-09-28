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
import org.rdf4led.query.expr.ExprList;

/**
 * General procedure in algebra evaluation (a stored procedure facility) Syntax (ARQ extension):
 * CALL <iri>(?x, ?y+3)
 *
 * <p>See also the similary algebra form for property functions. The difference is in argument
 * handling. A property function has a URI and two argment lists, one for subject, one for objects.
 * A procedure is a URI and a list of arguments.
 */

/**
 * OpProcedure.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpProcedure<Node> extends Op1<Node> {
  private Node procId;

  private ExprList<Node> args = null;

  public OpProcedure(Node procId, ExprList<Node> args, Op<Node> op) {
    super(op);

    this.args = args;

    this.procId = procId;
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    if (other == this) return true;

    if (!(other instanceof OpProcedure)) return false;

    OpProcedure<Node> proc = (OpProcedure<Node>) other;

    if (!procId.equals(proc.procId)) return false;

    if (!args.equals(proc.args)) return false;

    return getSubOp().equalTo(proc.getSubOp());
  }

  @Override
  public int hashCode() {
    int x = procId.hashCode();

    x ^= args.hashCode();

    x ^= getSubOp().hashCode();

    return x;
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpProcedure<Node>(procId, args, getSubOp());
  }

  public Node getProcId() {
    return procId;
  }

  //    public String getURI()
  //    {
  //        return procId.getURI() ;
  //    }

  public ExprList<Node> getArgs() {
    return args;
  }
}
