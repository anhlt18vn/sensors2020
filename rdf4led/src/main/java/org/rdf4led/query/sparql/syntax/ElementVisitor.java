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

package org.rdf4led.query.sparql.syntax;

public interface ElementVisitor<Node> {
  public void visit(ElementTriplesBlock<Node> el);

  public void visit(ElementPathBlock<Node> el);

  public void visit(ElementFilter<Node> el);

  public void visit(ElementAssign<Node> el);

  public void visit(ElementBind<Node> el);

  public void visit(ElementData<Node> el);

  public void visit(ElementUnion<Node> el);

  public void visit(ElementOptional<Node> el);

  public void visit(ElementGroup<Node> el);
  //    public void visit(ElementDataset el) ;

  public void visit(ElementNamedGraph<Node> el);

  public void visit(ElementStream<Node> el);

  public void visit(ElementExists<Node> el);

  public void visit(ElementNotExists<Node> el);

  public void visit(ElementMinus<Node> el);

  public void visit(ElementService<Node> el);

  public void visit(ElementFetch<Node> el);

  public void visit(ElementSubQuery<Node> el);
}
