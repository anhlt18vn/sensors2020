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

/** The org.rdf4led.sparql.algebra.syntax element for "Not Exists" in a pattern. */
public class ElementNotExists<Node> extends Element1<Node> {
  public ElementNotExists(Element<Node> el) {
    super(el);
  }

  @Override
  public int hashCode() {
    return getElement().hashCode() ^ Element.HashNotExists;
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (el2 == null) return false;

    if (!(el2 instanceof ElementNotExists)) {
      return false;
    }

    ElementNotExists<Node> unsaid2 = (ElementNotExists<Node>) el2;

    if (!this.getElement().equalTo(unsaid2.getElement())) {
      return false;
    }

    return true;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
