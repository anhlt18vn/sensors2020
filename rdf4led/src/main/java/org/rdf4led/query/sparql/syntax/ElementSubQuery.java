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

import org.rdf4led.query.sparql.Query;

public class ElementSubQuery<Node> extends Element<Node> {
  Query<Node> query;

  public ElementSubQuery(Query<Node> query) {
    this.query = query;
  }

  public Query<Node> getQuery() {
    return query;
  }

  @Override
  public boolean equalTo(Element<Node> other) {
    if (!(other instanceof ElementSubQuery)) {
      return false;
    }

    ElementSubQuery<Node> el = (ElementSubQuery<Node>) other;

    return query.equals(el.query);
  }

  @Override
  public int hashCode() {
    return query.hashCode();
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
