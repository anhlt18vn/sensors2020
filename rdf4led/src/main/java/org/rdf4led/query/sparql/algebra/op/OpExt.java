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
// import graphofthings.share.jena.sparql.algebra.Op;
// import graphofthings.share.jena.sparql.algebra.OpVisitor;
//
//
/// **
// */
// public abstract class OpExt<Node> extends OpBase<Node>
// {
//
//	public OpExt() {}
//
//	/**
//	 * Return an org.rdf4led.sparql.algebra.op that will used by query processing algorithms such as
//	 * optimization. This method returns a non-extension Op expression that is
//	 * the equivalent SPARQL expression. For example, this is the Op replaced by
//	 * this extension node.
//	 */
//	public abstract Op<Node> effectiveOp();
//
//	/**
//	 * Evaluate the org.rdf4led.sparql.algebra.op, given a stream of bindings as input Throw
//	 * UnsupportedOperationException if this OpExt is not executeable.
//	 */
//	public abstract QueryIterator<Node> eval(QueryIterator<Node> input, ARQqueryContext<Node>
// execCxt);
//
//	 @Override
//	public final byte getName()
//	{
//		return Tags.tagExt;
//	}
//
//	@Override
//	public final void visit(OpVisitor<Node> opVisitor)
//	{
//		opVisitor.visit(this);
//	}
//
// }
