package org.rdf4led.query.engine;

import org.rdf4led.graph.Graph;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.op.*;

/**
 * org.rdf4led.query.sparql.algebra
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 05/10/17.
 */
public class QueryExecutor {
  QueryContext<Integer> context;

  Graph<Integer> graph;

  public QueryExecutor(QueryContext<Integer> context, Graph<Integer> graph) {
    this.context = context;

    this.graph = graph;
  }

  public Forwarder<Integer> compile(Op<Integer> op, Forwarder<Integer> nextForwader) {

    if (op instanceof OpDistinct) {
      return createForwarderDistinct((OpDistinct<Integer>) op, nextForwader);
    }

    if (op instanceof OpBGP) {
      return createOpForwardBGP((OpBGP<Integer>) op, nextForwader);
    }

    if (op instanceof OpProject) {
      return createOpForwardProject((OpProject<Integer>) op, nextForwader);
    }

    if (op instanceof OpSlice) {
      return createForwarderSlice((OpSlice<Integer>) op, nextForwader);
    }

    if (op instanceof OpFilter) {
      return createForwarderFilter((OpFilter<Integer>) op, nextForwader);
    }

    return null;
  }

  private Forwarder<Integer> createOpForwardBGP(
      OpBGP<Integer> opBGP, Forwarder<Integer> nextOpForward) {
    return new OpForwardBGP(opBGP, graph, context, nextOpForward);
  }

  private Forwarder<Integer> createForwarderDistinct(
      OpDistinct<Integer> opDistinct, Forwarder forwarder) {

    Op<Integer> subOp = opDistinct.getSubOp();

    Forwarder<Integer> nextForward = new ForwarderDistinct<>(opDistinct, forwarder);

    return compile(subOp, nextForward);
  }

  private Forwarder<Integer> createOpForwardProject(
      OpProject<Integer> opProject, Forwarder forwarder) {

    Op<Integer> subOp = opProject.getSubOp();

    Forwarder<Integer> nextForwarder = new ForwarderProjection<>(opProject, forwarder);

    return compile(subOp, nextForwarder);
  }

  private Forwarder<Integer> createForwarderSlice(
      OpSlice<Integer> opSlice, Forwarder<Integer> forwarder) {
    Op<Integer> subOp = opSlice.getSubOp();

    Forwarder<Integer> nextForward = new ForwarderSlice<>(opSlice, forwarder);

    return compile(subOp, nextForward);
  }

  private Forwarder<Integer> createForwarderFilter(
      OpFilter<Integer> opFilter, Forwarder<Integer> forwarder) {

    Op<Integer> subOp = opFilter.getSubOp();

    Forwarder<Integer> nextForwarder = new ForwarderFilter<>(opFilter, forwarder);

    return compile(subOp, nextForwarder);
  }
}
