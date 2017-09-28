package org.rdf4led.query.sparql.algebra.op;
/// *
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package graphofthings.share.jena.sparql.algebra.org.rdf4led.sparql.algebra.op;
//
// import graphofthings.share.jena.sparql.algebra.OpVisitor;
// import graphofthings.share.jena.sparql.algebra.Transform;
//
//
/// **
// * Property functions (or any OpBGP replacement) Execution will be per-engine
// * specific
// */
// public class OpPropFunc<Node> extends Op1<Node>
// {
//	// c.f. OpProcedure which is similar except for the handling of arguments.
//	// Safer to have two (Ops are mainly abstract org.rdf4led.sparql.algebra.syntax, not executional).
//	private Node uri;
//
//	private PropFuncArg<Node> subjectArgs;
//
//	private PropFuncArg<Node> objectArgs2;
//
//	public OpPropFunc(Node uri, PropFuncArg<Node> args1, PropFuncArg<Node> args2, Op<Node> org.rdf4led.sparql.algebra.op)
//	{
//		super(org.rdf4led.sparql.algebra.op);
//		this.uri = uri;
//		this.subjectArgs = args1;
//		this.objectArgs2 = args2;
//	}
//
//	public PropFuncArg<Node> getSubjectArgs()
//	{
//		return subjectArgs;
//	}
//
//	public PropFuncArg<Node> getObjectArgs()
//	{
//		return objectArgs2;
//	}
//
//	@Override
//	public Op<Node> apply(Transform<Node> org.rdf4led.sparql.transform, Op<Node> subOp)
//	{
//		return org.rdf4led.sparql.transform.org.rdf4led.sparql.transform(this, subOp);
//	}
//
//	@Override
//	public void visit(OpVisitor<Node> opVisitor)
//	{
//		opVisitor.visit(this);
//	}
//
//	public Node getProperty()
//	{
//		return uri;
//	}
//
//	@Override
//	public Op<Node> copy(Op<Node> org.rdf4led.sparql.algebra.op)
//	{
//		return new OpPropFunc<Node>(uri, subjectArgs, objectArgs2, org.rdf4led.sparql.algebra.op);
//	}
//
//	@Override
//	public int hashCode()
//	{
//		return uri.hashCode() ^ getSubOp().hashCode();
//	}
//
//	@Override
//	public boolean equalTo(Op<Node> other)
//	{
//		if (!(other instanceof OpPropFunc))
//			return false;
//		OpPropFunc<Node> procFunc = (OpPropFunc<Node>) other;
//
//		return getSubOp().equalTo(procFunc.getSubOp());
//	}
//
//	@Override
//	public byte getName()
//	{
//		return Tags.tagPropFunc;
//	}
// }
