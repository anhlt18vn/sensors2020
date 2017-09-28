package org.rdf4led.query.engine;

import org.rdf4led.query.sparql.algebra.op.OpFilter;
import org.rdf4led.common.mapping.Mapping;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 22/01/18.
 */
public class ForwarderFilter<N> implements Forwarder<N> {

  private OpFilter<N> opFilter;
  private Forwarder<N> nextForwarder;

  public ForwarderFilter(OpFilter<N> opFilter, Forwarder<N> nextForwarder) {
    this.opFilter = opFilter;
    this.nextForwarder = nextForwarder;
  }

  @Override
  public void process(Mapping<N> mapping) {

    if (opFilter.getExprs().isSatisfied(mapping)) {
      nextForwarder.process(mapping);
    }
  }
}
