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

import org.rdf4led.common.FileUtil;
import org.rdf4led.query.sparql.Query;
import org.rdf4led.query.sparql.QueryParseException;

import java.io.File;

/**
 * This class provides the root of lower level access to all the parsers. Each subclass hides the
 * details of the per-language exception handlers and other javacc details to provide a methods that
 * deal with setting up Query objects and using QueryException exceptions for problems.
 */
public abstract class Parser<Node> {
  public final Query<Node> parse(Query<Node> query, String queryString) throws QueryParseException {
    // Sort out BOM
    if (queryString.startsWith("\uFEFF")) queryString = queryString.substring(1);
    return parse$(query, queryString);
  }

  public final Query<Node> parsefromFileName(Query<Node> query, String fileName)
      throws QueryParseException {
    // Sort out BOM
    String queryString = FileUtil.readStringFromFile(fileName);

    return parse$(query, queryString);
  }

  public final Query<Node> parse(Query<Node> query, File fileName) throws QueryParseException {
    // Sort out BOM
    String queryString = FileUtil.readStringFromFile(fileName);

    return parse$(query, queryString);
  }

  protected abstract Query<Node> parse$(Query<Node> query, String queryString)
      throws QueryParseException;

  // Do any testing of queries after the construction of the parse tree.
  protected void validateParsedQuery(Query<Node> query) {
    //		 SyntaxVarScope.check(query) ;
  }
}
