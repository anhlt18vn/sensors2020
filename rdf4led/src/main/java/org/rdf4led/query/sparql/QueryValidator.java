package org.rdf4led.query.sparql;

import org.rdf4led.graph.Triple;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.Expr;
import org.rdf4led.query.expr.VarExprList;
import org.rdf4led.query.sparql.syntax.*;

import java.util.*;

/**
 * QueryValidator.java
 *
 * <p>Modified from Jena
 *
 * <p>TODO
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>Date : 4 Nov 2015
 */
public class QueryValidator<Node> {
  QueryContext<Node> queryContext;

  public QueryValidator(QueryContext<Node> queryContext) {
    this.queryContext = queryContext;
  }

  /* SPARQL 1.1 "in scope" rules
    These define the variables from a pattern that are in-scope
    These are not the usage rules.

  Syntax Form                                     In-scope variables

  Basic Graph Pattern (BGP)                       v occurs in the BGP
  Path                                            v occurs in the path
  Group { P1 P2 ... }                             v is in-scope if in-scope in one or more of P1, P2, ...
  GRAPH term { P }                                v is term or v is in-scope in P
  { P1 } UNION { P2 }                             v is in-scope in P1 or in-scope in P2
  OPTIONAL {P}                                    v is in-scope in P
  SERVICE term {P}                                v is term or v is in-scope in P
  (org.rdf4led.common.data.expr AS v) for BIND, SELECT and GROUP BY       v is in-scope
  SELECT ..v .. { P }                             v is in-scope if v is mentioned as a project variable
  SELECT * { P }                                  v is in-scope in P
  VALUES var     (values)                         v is in-scope if v is in varlist
  VALUES varlist (values)                         v is in-scope if v is in varlist
    */

  // 	Weakness : EXISTS inside FILTERs?
  public void check(Query<Node> query) {
    if (query.getQueryPattern() == null)
      // DESCRIBE may not have a pattern
      return;

    checkSubQuery(query.getQueryPattern());

    checkBind(query);

    // Check this level.
    checkQueryScope(query);

    // Other checks.
    Collection<Node> vars = varsOfQuery(query);

    check(query, vars);
  }

  // Check BIND by accumulating variables and making sure BIND does not attempt to reuse one
  private void checkBind(Query<Node> query) {
    ScopeChecker v = new ScopeChecker();
    new ElementWalker<Node>().walk(query.getQueryPattern(), v);
  }

  // Check subquery by finding subquries and recurisively checking.
  // Includes appling all checks to nested subqueries.
  private void checkSubQuery(Element<Node> el) {
    ElementVisitor<Node> v = new SubQueryScopeChecker();

    new ElementWalker<Node>().walk(el, v);
  }

  // 	Check one level of query - SELECT expressions
  private void checkQueryScope(Query<Node> query) {
    Collection<Node> vars = varsOfQuery(query);

    checkExprListAssignment(vars, query.getProject());
  }

  // get all vars of a query
  private Collection<Node> varsOfQuery(Query<Node> query) {
    Collection<Node> vars = new LinkedHashSet<Node>();

    // 		PatternVars.vars(query.getQueryPattern()) ;
    //
    new ElementWalker<Node>().walk(query.getQueryPattern(), new PatternVars(vars));

    if (query.hasValues()) {
      vars.addAll(query.getValuesVariables());
    }

    return vars;
  }

  // Other check (not scoping at this level) of a query
  private void check(Query<Node> query, Collection<Node> vars) {
    // Check any expressions are assigned to fresh variables.
    checkExprListAssignment(vars, query.getProject());

    // Check for SELECT * GROUP BY
    // Legal in ARQ, not in SPARQL 1.1
    // 		if ( ! Syntax.syntaxARQ.equals(query.getSyntax()) )
    {
      if (query.isQueryResultStar() && query.hasGroupBy())
        throw new QueryParseException("SELECT * not legal with GROUP BY", -1, -1);
    }

    // Check any variable in an expression is in scope (if GROUP BY)
    checkExprVarUse(query);

    // Check GROUP BY AS
    // ENABLE
  }

  private void checkExprListAssignment(Collection<Node> vars, VarExprList<Node> exprList) {
    Set<Node> vars2 = new LinkedHashSet<Node>(vars);

    for (Iterator<Node> iter = exprList.getVars().iterator(); iter.hasNext(); ) {
      // In scope?
      Node v = iter.next();

      Expr<Node> e = exprList.getExpr(v);

      checkAssignment(vars2, e, v);

      vars2.add(v);
    }
  }

  private void checkExprVarUse(Query<Node> query) {
    if (query.hasGroupBy()) {
      VarExprList<Node> groupKey = query.getGroupBy();

      List<Node> groupVars = groupKey.getVars();

      VarExprList<Node> exprList = query.getProject();

      for (Iterator<Node> iter = exprList.getVars().iterator(); iter.hasNext(); ) {
        // In scope?
        Node v = iter.next();

        Expr<Node> e = exprList.getExpr(v);

        if (e == null) {
          if (!groupVars.contains(v))
            throw new QueryParseException(
                "Non-group key variable in SELECT: " + queryContext.getVarName(v), -1, -1);
        } else {
          Set<Node> eVars = e.getVarsMentioned();

          for (Node v2 : eVars) {
            if (!groupVars.contains(v2))
              throw new QueryParseException(
                  "Non-group key variable in SELECT: "
                      + queryContext.getVarName(v2)
                      + " in expression "
                      + e,
                  -1,
                  -1);
          }
        }
      }
    }
  }

  private void checkAssignment(Collection<Node> scope, Expr<Node> expr, Node var) {
    // Project SELECT ?x
    if (expr == null) return;

    // org.rdf4led.common.data.expr not null
    if (scope.contains(var))
      throw new QueryParseException("Variable used when already in-scope: " + var + " in ", -1, -1);

    // test for impossible variables - bound() is a bit odd.
    // 		if ( false )
    // 		{
    //        	Set<Var> vars = org.rdf4led.common.data.expr.getVarsMentioned() ;
    //        	for ( Var v : vars )
    //        	{
    //             if ( !scope.contains(v) )
    //                 throw new QueryParseException("Variable used in expression is not in-scope:
    // "+v+" in "+org.rdf4led.common.data.expr, -1 , -1) ;
    //        	}
    // 		}
  }

  // Modifed walked for variables.

  /** Visitor for subqueries scope rules . */
  private class SubQueryScopeChecker extends ElementVisitorBase<Node> {
    @Override
    public void visit(ElementSubQuery<Node> el) {
      Query<Node> query = el.getQuery();

      checkQueryScope(query);

      // Recursively check sub-queries in sub-queries.
      check(el.getQuery());
    }
  }

  // Applies scope rules at each point it matters.
  // Does some recalculation in nested structures.

  private class ScopeChecker extends ElementVisitorBase<Node> {
    private Collection<Node> accScope = new HashSet<Node>();

    public ScopeChecker() {}

    @Override
    public void visit(ElementGroup<Node> el) {
      // There are two kinds of elements: ones that accumulate variables
      // across the group and ones that isolate a subexpression to be joined
      // Scoped: UNION, Group, (MINUS), SERVICE, SubSELECT
      // Accumulating: BGPs, paths, BIND, LET, OPTIONAL.
      // FILTER: may involve an EXISTS (not checked currently)

      // Accumulate:
      // 	This BGP - used by EleemntBind.
      //	All elements of this group - total aggregation.

      accScope.clear();

      for (Element<Node> e : el.getElements()) {
        // Tests.
        if (e instanceof ElementBind) {
          check(accScope, (ElementBind<Node>) e);
        } else {
          if (e instanceof ElementService) {
            check(accScope, (ElementService<Node>) e);
          }
        }

        // if joined in, the scope protects
        if (!joinedInGroup(e)) {
          // PatternVars.vars(accScope, e) ;
        }
      }
    }

    private boolean joinedInGroup(Element<Node> e) {
      return e instanceof ElementGroup
          || e instanceof ElementUnion
          ||
          // e instanceof ElementOptional ||
          e instanceof ElementService
          || e instanceof ElementSubQuery;
    }

    // Inside filters.

    private void check(Collection<Node> scope, ElementBind<Node> el) {
      Node var = el.getVar();

      if (scope.contains(var))
        throw new QueryParseException(
            "BIND: Variable used when already in-scope: " + var + " in " + el, -1, -1);
      checkAssignment(scope, el.getExpr(), var);
    }

    private void check(Collection<Node> scope, ElementService<Node> el) {
      //         if ( ARQ.isStrictMode() && el.getServiceNode().isVariable() )
      //         {
      //             Var var = Var.alloc(el.getServiceNode()) ;
      //             if ( ! scope.contains(var) )
      //                 throw new QueryParseException("SERVICE: Variable not already in-scope:
      // "+var+" in "+el, -1 , -1) ;
      //         }
    }
  }

  private class PatternVars extends ElementVisitorBase<Node> {
    public Collection<Node> acc;

    public PatternVars(Collection<Node> s) {
      acc = s;
    }

    @Override
    public void visit(ElementTriplesBlock<Node> el) {
      for (Iterator<Triple<Node>> iter = el.patternElts(); iter.hasNext(); ) {
        Triple<Node> t = iter.next();

        // 	            Node s = t.getSubject();
        //
        // 				Node p = t.getPredicate();
        //
        // 				Node o = t.getObject();
        //
        // 				if (queryContext.isVarNode(s)) org.rdf4led.common.data.incremental.acc.add(s);
        //
        // 				if (queryContext.isVarNode(p)) org.rdf4led.common.data.incremental.acc.add(p);
        //
        // 				if (queryContext.isVarNode(o)) org.rdf4led.common.data.incremental.acc.add(o);

        addVar(acc, t.getSubject());

        addVar(acc, t.getPredicate());

        addVar(acc, t.getObject());
      }
    }

    private void addVar(Collection<Node> acc, Node node) {
      if (queryContext.isVarNode(node)) {
        acc.add(node);
      }
    }

    // 	    @Override
    // 	    public void visit(ElementPathBlock el)
    // 	    {
    // 	        for (Iterator<TriplePath> iter = el.patternElts() ; iter.hasNext() ; )
    // 	        {
    // 	            TriplePath tp = iter.next() ;
    // 	            // If it's triple-izable, then use the triple.
    // 	            if ( tp.isTriple() )
    // 	                VarUtils.addVarsFromTriple(org.rdf4led.common.data.incremental.acc, tp.asTriple()) ;
    // 	            else
    // 	                VarUtils.addVarsFromTriplePath(org.rdf4led.common.data.incremental.acc, tp) ;
    // 	        }
    // 	    }

    // Variables here are non-binding.
    // 	    @Override public void visit(ElementExists el)       { }
    // 	    @Override public void visit(ElementNotExists el)    { }
    // 	    @Override public void visit(ElementMinus el)        { }
    // 	    @Override public void visit(ElementFilter el)       { }

    @Override
    public void visit(ElementNamedGraph<Node> el) {
      addVar(acc, el.getGraphNameNode());
    }

    @Override
    public void visit(ElementSubQuery<Node> el) {
      el.getQuery().setResultVars();

      VarExprList<Node> x = el.getQuery().getProject();

      acc.addAll(x.getVars());
    }

    @Override
    public void visit(ElementAssign<Node> el) {
      acc.add(el.getVar());
    }

    @Override
    public void visit(ElementBind<Node> el) {
      acc.add(el.getVar());
    }

    @Override
    public void visit(ElementData<Node> el) {
      acc.addAll(el.getVars());
    }
  }
}
