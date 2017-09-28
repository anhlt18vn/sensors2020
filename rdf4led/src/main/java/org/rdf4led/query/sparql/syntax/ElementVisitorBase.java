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

/**
 * A ElementVisitor that does nothing. It saves writing lots of empty visits when only interested in
 * a few element types.
 */
public class ElementVisitorBase<Node> implements ElementVisitor<Node> {
  @Override
  public void visit(ElementTriplesBlock<Node> el) {}

  @Override
  public void visit(ElementFilter<Node> el) {}

  @Override
  public void visit(ElementAssign<Node> el) {}

  @Override
  public void visit(ElementBind<Node> el) {}

  @Override
  public void visit(ElementData<Node> el) {}

  @Override
  public void visit(ElementUnion<Node> el) {}

  //    @Override
  //    public void visit(ElementDataset el)        { }

  @Override
  public void visit(ElementOptional<Node> el) {}

  @Override
  public void visit(ElementGroup<Node> el) {}

  @Override
  public void visit(ElementNamedGraph<Node> el) {}

  @Override
  public void visit(ElementExists<Node> el) {}

  @Override
  public void visit(ElementNotExists<Node> el) {}

  @Override
  public void visit(ElementMinus<Node> el) {}

  @Override
  public void visit(ElementService<Node> el) {}

  @Override
  public void visit(ElementFetch<Node> el) {}

  @Override
  public void visit(ElementSubQuery<Node> el) {}

  @Override
  public void visit(ElementPathBlock<Node> el) {}

  @Override
  public void visit(ElementStream<Node> el) {}
}
