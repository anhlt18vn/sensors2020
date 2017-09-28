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
import org.rdf4led.query.sparql.parser.lang.sparql_11.ParseException;
import org.rdf4led.query.sparql.parser.lang.sparql_11.SPARQLParser11;
import org.rdf4led.query.sparql.parser.lang.sparql_11.TokenMgrError;
import org.rdf4led.query.sparql.syntax.Element;
import org.rdf4led.query.sparql.syntax.Template;

import java.io.Reader;
import java.io.StringReader;

public class ParserSPARQL11<Node> extends Parser<Node> {

  private QueryContext<Node> queryContext;

  public ParserSPARQL11(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;
  }

  @Override
  protected Query<Node> parse$(final Query<Node> query, String queryString) {
    perform(query, queryString);

    validateParsedQuery(query);

    return query;
  }

  public Element<Node> parseElement(String string) {
    final Query<Node> query = new Query<>(queryContext);

    perform(query, string);

    return query.getQueryPattern();
  }

  public Template<Node> parseTemplate(String string) {
    final Query<Node> query = new Query<>(queryContext);

    perform(query, string);

    return query.getConstructTemplate();
  }

  private void perform(Query<Node> query, String string) {

    Reader in = new StringReader(string);

    SPARQLParser11<Node> parser = new SPARQLParser11<>(in, queryContext);

    try {
      parser.setQuery(query);

      parser.QueryUnit();
    } catch (ParseException ex) {

      throw new QueryParseException(
          ex.getMessage(), ex.currentToken.beginLine, ex.currentToken.beginColumn);
    } catch (TokenMgrError tErr) {
      // Last valid token : not the same as token error message - but this should not happen

      int col = parser.token.endColumn;

      int line = parser.token.endLine;

      throw new QueryParseException(tErr.getMessage(), line, col);
    }
  }
}
