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

import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.ExprFunction2;

/**
 * E_LangMatches.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_LangMatches<Node> extends ExprFunction2<Node> {
  public E_LangMatches(Expr<Node> expr1, Expr<Node> expr2, QueryContext<Node> queryContext) {
    super(expr1, expr2, queryContext);
  }

  @Override
  public Node eval(Node lang, Node pattern) {
    return langMatches(lang, pattern);
  }

  private Node langMatches(Node nv, Node langPattern) {
    return langMatches(nv, queryContext.dictionary().getLexicalValue(langPattern));
  }

  private Node langMatches(Node node, String langPattern) {
    if (queryContext.dictionary().isLiteral(node)) {
      return null;
    }
    // ====================
    String nodeLang = queryContext.dictionary().getLexicalValue(node);

    if (langPattern.equals("*")) {
      if (nodeLang == null || nodeLang.equals("")) {
        return queryContext.getnvFalse();
        // return NodeValue.FALSE ;
      }

      return queryContext.getnvTrue();
      // return NodeValue.TRUE ;
    }

    // See RFC 3066 (it's "tag (-tag)*)"

    String[] langElts = nodeLang.split("-");

    String[] langRangeElts = langPattern.split("-");

    /*
     * Here is the logic to compare language code. There is a match if the
     * language matches the parts of the pattern - the language may be
     * longer than the pattern.
     */

    /*
     * RFC 4647 basic filtering.
     *
     * To do extended: 1. Remove any -*- (but not *-) 2. Compare primary
     * tags. 3. Is the remaining range a subsequence of the remaining
     * language tag?
     */

    // // Step one: remove "-*-" (but not "*-")
    // int j = 1 ;
    // for ( int i = 1 ; i < langRangeElts.length ; i++ )
    // {
    // String range = langRangeElts[i] ;
    // if ( range.equals("*") )
    // continue ;
    // langRangeElts[j] = range ;
    // j++ ;
    // }
    //
    // // Null fill any free space.
    // for ( int i = j ; i < langRangeElts.length ; i++ )
    // langRangeElts[i] = null ;

    // This is basic specific.

    if (langRangeElts.length > langElts.length)
    // Lang tag longer than pattern tag => can't match
    {
      return queryContext.getnvFalse();
      // return NodeValue.FALSE ;
    }

    for (int i = 0; i < langRangeElts.length; i++) {
      String range = langRangeElts[i];

      if (range == null) {
        break;
      }
      // Language longer than range
      if (i >= langElts.length) {
        break;
      }

      String lang = langElts[i];

      if (range.equals("*")) {
        continue;
      }

      if (!range.equalsIgnoreCase(lang)) {
        return queryContext.getnvFalse();
        // return NodeValue.FALSE ;
      }
    }

    // return NodeValue.TRUE ;
    return queryContext.getnvTrue();
  }

  @Override
  public Expr<Node> copy(Expr<Node> e1, Expr<Node> e2) {
    return new E_LangMatches<Node>(e1, e2, queryContext);
  }
}
