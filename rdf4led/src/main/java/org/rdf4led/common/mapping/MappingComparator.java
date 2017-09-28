///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.rdf4led.common.mapping;
//
//
//import java.util.*;
//
//public class MappingComparator<Node> implements Comparator<Mapping<Node>> {
//  private Comparator<Node> varComparator =
//      new Comparator<Node>() {
//        @Override
//        public int compare(Node o1, Node o2) {
//          return o1.toString().compareTo(o2.toString());
//        }
//      };
//
//  private List<SortCondition<Node>> conditions;
//
//  private QueryContext<Node> queryContext;
//
//  public MappingComparator(List<SortCondition<Node>> conditions, QueryContext<Node> queryContext) {
//    this.conditions = conditions;
//    this.queryContext = queryContext;
//  }
//
//  public MappingComparator(List<SortCondition<Node>> _conditions) {
//    conditions = _conditions;
//  }
//
//  public List<SortCondition<Node>> getConditions() {
//    return Collections.unmodifiableList(conditions);
//  }
//
//  // Compare bindings by iterating.
//  // Node comparsion is:
//  //  Compare by
//
//  @Override
//  public int compare(Mapping<Node> bind1, Mapping<Node> bind2) {
//    for (Iterator<SortCondition<Node>> iter = conditions.iterator(); iter.hasNext(); ) {
//      SortCondition<Node> sc = iter.next();
//
//      if (sc.expression == null) {
//        throw new RuntimeException("Broken sort condition");
//        // throw new QueryExecException("Broken sort condition") ;
//      }
//
//      // NodeValue nv1 = null ;
//      // NodeValue nv2 = null ;
//
//      Node nodeValue1 = null;
//      Node nodeValue2 = null;
//
//      // try {
//      nodeValue1 = sc.expression.eval(bind1);
//      // }
//
//      // catch (VariableNotBoundException ex) {}
//
//      // catch (ExprEvalException ex)
//      // { }
//
//      // try
//      {
//        nodeValue2 = sc.expression.eval(bind2);
//      }
//      // catch (VariableNotBoundException ex) {}
//      // catch (ExprEvalException ex)
//      // {  }
//
//      // NodeValue.toNode(nv1);
//
//      // NodeValue.toNode(nv2);
//
//      int x = compareNodes(nodeValue1, nodeValue2, sc.direction);
//
//      if (x != QueryContext.CMP_EQUAL) return x;
//    }
//    // Same by the SortConditions - now do any extra tests to make sure they are unique.
//    return compareMappingsSyntactic(bind1, bind2);
//    // return 0 ;
//  }
//
//  private int compareNodes(Node nv1, Node nv2, int direction) {
//    int x = compareNodesRaw(nv1, nv2);
//
//    if (direction == Query.ORDER_DESCENDING) x = -x;
//    return x;
//  }
//
//  public int compareNodesRaw(Node nv1, Node nv2) {
//    // Absent nodes sort to the start
//    if (nv1 == null) {
//      return nv2 == null ? QueryContext.CMP_EQUAL : QueryContext.CMP_LESS;
//    }
//
//    if (nv2 == null) {
//      return QueryContext.CMP_GREATER;
//    }
//
//    // Compare - always getting a result.
//    return queryContext.copmpareNodeasValue(nv1, nv2);
//  }
//
//  public int compareMappingsSyntactic(Mapping<Node> bind1, Mapping<Node> bind2) {
//    int x = 0;
//
//    // The variables in bindings are unordered.  If we want a good comparison, we need an order.
//    // We'll choose lexicographically by name.  Unfortunately this means a sort on every comparison.
//    List<Node> varList = new ArrayList<Node>();
//
//    for (Iterator<Node> iter = bind1.vars(); iter.hasNext(); ) {
//      varList.add(iter.next());
//    }
//    for (Iterator<Node> iter = bind2.vars(); iter.hasNext(); ) {
//      varList.add(iter.next());
//    }
//
//    // Lets try to make it a tiny bit faster by using Arrays.sort() instead of Collections.sort().
//    Node[] nodeVars =
//        (Node[]) new Object[varList.size()]; // new queryContext.getNodeArray(varList.size());
//    // new Var[varList.size()];
//
//    nodeVars = varList.toArray(nodeVars);
//
//    Arrays.sort(nodeVars, varComparator);
//
//    for (Node nodeVar : nodeVars) {
//      Node n1 = bind1.getValue(nodeVar);
//
//      Node n2 = bind2.getValue(nodeVar);
//
//      x = queryContext.compareRDFTerms(n1, n2);
//
//      if (x != 0) {
//        return x;
//      }
//    }
//
//    return x;
//  }
//}
