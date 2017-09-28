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

/**
 * OpTable.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>1 Oct 2015
 */
public class OpTable<Node> extends Op0<Node> {
  public OpTable() {}

  @Override
  public Op<Node> apply(Transform<Node> transform) {
    return transform.transform(this);
  }

  @Override
  public Op<Node> copy() {
    return new OpTable<Node>();
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    throw new UnsupportedOperationException();
  }
  //	private Table<Node> table;
  //
  //	public OpTable(Table<Node> table)
  //	{
  //		this.table = table;
  //	}
  //
  //	public boolean isJoinIdentity()
  //	{
  //		return (table instanceof TableUnit);
  //	}
  //
  //	// One row of no bindings.
  //
  //	public Table<Node> getTable()
  //	{
  //		return table;
  //	}
  //
  //	@Override
  //	public void visit(OpVisitor<Node> opVisitor)
  //	{
  //		opVisitor.visit(this);
  //	}
  //
  //	@Override
  //	public Op<Node> apply(Transform<Node> org.rdf4led.sparql.transform)
  //	{
  //		return org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(this);
  //	}
  //
  //	@Override
  //	public Op<Node> copy()
  //	{
  //		return new OpTable<Node>(table);
  //	}
  //
  //	@Override
  //	public int hashCode()
  //	{
  //		return table.hashCode();
  //	}
  //
  //	@Override
  //	public boolean equalTo(Op<Node> other)
  //	{
  //		if (!(other instanceof OpTable))
  //			return false;
  //
  //		OpTable<Node> opTable = (OpTable<Node>) other;
  //
  //		return table.equals(opTable.table);
  //	}

}
