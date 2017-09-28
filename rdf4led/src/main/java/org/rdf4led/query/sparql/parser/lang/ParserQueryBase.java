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

package org.rdf4led.query.sparql.parser.lang;

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.Query;
import org.rdf4led.query.sparql.QueryParseException;
import org.rdf4led.common.mapping.Mapping;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Class that has all the parse event operations and other query/update specific things */
public class ParserQueryBase<Node> extends ParserBase<Node> {
  public ParserQueryBase(QueryContext<Node> queryContext) {
    super(queryContext);
  }

  private Deque<Query<Node>> stack = new ArrayDeque<Query<Node>>();

  protected Query<Node> query;

  public void setQuery(Query<Node> q) {
    query = q;
  }

  public Query<Node> getQuery() {
    return query;
  }

  // Move down to SPARQL 1.1 or rename as ParserBase
  protected void startQuery() {}

  protected void finishQuery() {}

  protected void startUpdateOperation() {}

  protected void finishUpdateOperation() {}

  protected void startUpdateRequest() {}

  protected void finishUpdateRequest() {}

  private boolean oldBNodesAreVariables;

  private boolean oldBNodesAreAllowed;

  protected void startSubSelect(int line, int col) {
    if (query == null) {}

    stack.push(query);

    Query<Node> subQuery = new Query<>(queryContext);

    query = subQuery;
  }

  protected Query<Node> endSubSelect(int line, int column) {
    Query<Node> subQuery = query;

    if (!subQuery.isSelectType()) throwParseException("Subquery not a SELECT query", line, column);

    query = stack.pop();

    return subQuery;
  }

  private List<Node> variables = null;

  private int currentColumn = -1;

  protected void startValuesClause(int line, int col) {
    variables = new ArrayList<Node>();
  }

  protected void finishValuesClause(int line, int col) {}

  protected void startInlineData(List<Node> vars, List<Mapping<Node>> rows, int line, int col) {
    variables = vars;
  }

  protected void finishInlineData(int line, int col) {}

  protected void emitDataBlockVariable(Node nodeVar) {
    variables.add(nodeVar);
  }

  protected void startDataBlockValueRow(int line, int col) {
    currentColumn = -1;
  }

  protected void emitDataBlockValue(Node node, int line, int col) {
    currentColumn++;

    if (currentColumn >= variables.size()) return;

    Node nodeVar = variables.get(currentColumn);

    if (node != null) {}
  }

  protected void finishDataBlockValueRow(int line, int col) {
    if (currentColumn + 1 != variables.size()) {
      String msg =
          String.format(
              "Mismatch: %d variables but %d values", variables.size(), currentColumn + 1);

      msg = QueryParseException.formatMessage(msg, line, col);

      throw new QueryParseException(msg, line, col);
    }
  }
}
