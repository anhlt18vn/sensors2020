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

package org.rdf4led.query.expr;

import org.rdf4led.rdf.dictionary.vocab.XSD;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.ExprFunction1;

/**
 * E_StrEncodeForURI.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_StrEncodeForURI<Node> extends ExprFunction1<Node> {
  public E_StrEncodeForURI(Expr<Node> expr, QueryContext<Node> queryContext) {
    super(expr, queryContext);
  }

  @Override
  public Node eval(Node n) {
    if (queryContext.dictionary().isLiteral(n)) {
      throw new RuntimeException("Not a literal");
    }

    if (queryContext.dictionary().getNodeType(n) != XSD.XSDstring) {
      throw new RuntimeException("Not a string Literal");
    }

    return n;
  }

  @Override
  public Expr<Node> copy(Expr<Node> expr) {
    return new E_StrEncodeForURI<Node>(expr, queryContext);
  }
}
