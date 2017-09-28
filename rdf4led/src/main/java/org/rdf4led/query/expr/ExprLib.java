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
import org.rdf4led.query.sparql.algebra.transform.ExprTransform;
import org.rdf4led.query.sparql.algebra.transform.ExprTransformCopy;
import org.rdf4led.query.expr.aggs.ExprAggregator;
import org.rdf4led.query.expr.function.ExprFunction2;

public class ExprLib<Node> {
  QueryContext<Node> queryContext;

  public ExprLib(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;
  }

  /**
   * org.rdf4led.sparql.transform an expression that may involve aggregates into one that just uses the variable for
   * the aggregate
   */
  public Expr<Node> replaceAggregateByVariable(Expr<Node> expr) {
    // return ExprTransformer.org.rdf4led.sparql.transform(replaceAgg, org.rdf4led.common.data.expr) ;
    throw new RuntimeException("Under construction");
  }

  /**
   * org.rdf4led.sparql.transform expressions that may involve aggregates into one that just uses the variable for the
   * aggregate
   */
  public ExprList<Node> replaceAggregateByVariable(ExprList<Node> exprs) {
    // return ExprTransformer.org.rdf4led.sparql.transform(replaceAgg, exprs) ;
    throw new RuntimeException("Under construction");
  }

  private ExprTransform<Node> replaceAgg =
      new ExprTransformCopy<Node>(queryContext) {
        @Override
        public Expr<Node> transform(ExprAggregator<Node> eAgg) {
          return eAgg.getAggVar();
        }
      };

  /**
   * Decide whether an expression is safe for using a a graph substitution. Need to be careful about
   * value-like tests when the graph is not matched in a value fashion.
   */
  public boolean isAssignmentSafeEquality(Expr<Node> expr) {
    return isAssignmentSafeEquality(expr, false, false);
  }

  /**
   * @param graphHasStringEquality True if the graph triple matching equates xsd:string and plain
   *     literal
   * @param graphHasNumercialValueEquality True if the graph triple matching equates numeric values
   */
  public boolean isAssignmentSafeEquality(
      Expr<Node> expr, boolean graphHasStringEquality, boolean graphHasNumercialValueEquality) {
    if (!(expr instanceof E_Equals) && !(expr instanceof E_SameTerm)) return false;

    // Corner case: sameTerm is false for string/plain literal,
    // but true in the graph.

    ExprFunction2<Node> eq = (ExprFunction2<Node>) expr;

    Expr<Node> left = eq.getArg1();

    Expr<Node> right = eq.getArg2();

    Node var = null;

    Node constant = null;

    if (left.isVariable() && right.isConstant()) {
      var = left.asVar();
      constant = right.getConstant();
    } else if (right.isVariable() && left.isConstant()) {
      var = right.asVar();
      constant = left.getConstant();
    }

    // Not between a variable and a constant
    if (var == null || constant == null) return false;

    if (!queryContext.dictionary().isLiteral(constant))
      // URIs, bNodes. Any bNode will have come from a substitution - not
      // legal org.rdf4led.sparql.algebra.syntax in filters
      return true;

    if (expr instanceof E_SameTerm) {
      if (graphHasStringEquality && queryContext.dictionary().isPlainLiteral(constant))
        // Graph is not same term
        return false;
      if (graphHasNumercialValueEquality && queryContext.dictionary().isNumber(constant))
        return false;
      return true;
    }

    // Final check for "=" where a FILTER = can do value matching when the
    // graph does not.
    if (expr instanceof E_Equals) {
      if (!graphHasStringEquality && queryContext.dictionary().isPlainLiteral(constant))
        return false;
      if (!graphHasNumercialValueEquality && queryContext.dictionary().isNumber(constant))
        return false;
      return true;
    }
    // Unreachable.
    throw new RuntimeException();
  }
}
