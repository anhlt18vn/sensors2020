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
import org.rdf4led.query.sparql.RegexEngine;
import org.rdf4led.query.expr.function.ExprFunctionN;

import java.util.List;

/** Indirect to the choosen regular expression implementation */

/**
 * E_Regex.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class E_Regex<Node> extends ExprFunctionN<Node> {
  private RegexEngine regexEngine;

  public E_Regex(
      Expr<Node> expr, Expr<Node> pattern, Expr<Node> flags, QueryContext<Node> queryContext) {
    super(queryContext, expr, pattern, flags);

    init(pattern, flags);
  }

  // Not used by org.rdf4led.sparql.parser
  public E_Regex(Expr<Node> expr, String pattern, String flags, QueryContext<Node> queryContext) {

    super(
        queryContext,
        expr,
        queryContext.nodeToExpr(queryContext.dictionary().createStringNode(pattern)),
        queryContext.nodeToExpr(queryContext.dictionary().createStringNode(flags)));

    init(getArg(2), getArg(3));
  }

  private void init(Expr<Node> pattern, Expr<Node> flags) {
    // if ( pattern.isConstant() && pattern.getConstant().isString() && (
    // flags==null || flags.isConstant() ) )
    // regexEngine = makeRegexEngine(pattern.getConstant(),
    // (flags==null)?null:flags.getConstant()) ;

    // Node pa = pattern.getConstant();

    // Node fl = flags.getConstant();

    regexEngine = queryContext.makeRegexEngine(pattern, flags);
  }

  @Override
  public Node eval(List<Node> args) {
    Node arg = args.get(0);

    Node vPattern = args.get(1);

    Node vFlags = (args.size() == 2 ? null : args.get(2));

    RegexEngine regex = regexEngine;

    if (regex == null) {
      regex = queryContext.makeRegexEngine(vPattern, vFlags);
    }

    boolean b = regex.match(queryContext.dictionary().getLexicalValue(arg));

    return b ? queryContext.getnvTrue() : queryContext.getnvFalse();
  }

  // public static RegexEngine makeRegexEngine(NodeValue vPattern, NodeValue
  // vFlags)
  // {
  // if ( ! vPattern.isString() )
  // throw new ExprException("REGEX: Pattern is not a string: "+vPattern) ;
  // if ( vFlags != null && ! vFlags.isString() )
  // throw new ExprException("REGEX: Pattern flags are not a string: "+vFlags)
  // ;
  // String s = (vFlags==null)?null:vFlags.getString() ;
  //
  // return makeRegexEngine(vPattern.getString(), s) ;
  // }
  //
  // public static RegexEngine makeRegexEngine(String pattern, String flags)
  // {
  // if ( regexImpl.equals(ARQ.xercesRegex))
  // return new RegexXerces(pattern, flags) ;
  // return new RegexJava(pattern, flags) ;
  // }

  // /** @return Returns the org.rdf4led.common.data.expr of the regex */
  // public final Expr getRegexExpr() { return expr1 ; }
  //
  // /** @return Returns the pattern. */
  // public final Expr getPattern() { return expr2 ; }
  //
  // /** @return Returns the flags. */
  // public final Expr getFlags() { return expr3 ; }

  @Override
  protected Expr<Node> copy(ExprList<Node> newArgs) {
    if (newArgs.size() == 2) {
      return new E_Regex<Node>(newArgs.get(0), newArgs.get(1), null, queryContext);
    }

    return new E_Regex<Node>(newArgs.get(0), newArgs.get(1), newArgs.get(2), queryContext);
  }
}
