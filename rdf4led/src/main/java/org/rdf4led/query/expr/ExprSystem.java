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
import org.rdf4led.query.expr.function.ExprFunction0;

/**
 * ExprSystem.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprSystem<Node> extends ExprFunction0<Node> {
  protected ExprSystem(QueryContext<Node> queryContext) // , Symbol
        // systemSymbol)
      {
    super(queryContext);
  }

  @Override
  public Node eval() {
    // Object obj = env.getContext().get(systemSymbol) ;
    //
    // if ( obj == null )
    // throw new ExprEvalException("null for system symbol: "+systemSymbol)
    // ;
    // if ( ! ( obj instanceof Node ) )
    // throw new ExprEvalException("Not a Node: "+Utils.className(obj)) ;
    //
    // Node n = (Node)obj ;
    // // if ( n == null )
    // // throw new
    // ExprEvalException("No value for system variable: "+systemSymbol) ;
    // // NodeValue.makeNode could have a cache.
    // NodeValue nv = NodeValue.makeNode(n) ;
    // return nv ;

    throw new RuntimeException("Node for System operation here !!!");
  }
}
