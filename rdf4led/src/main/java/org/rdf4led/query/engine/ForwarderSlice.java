package org.rdf4led.query.engine;

import org.rdf4led.query.sparql.algebra.op.OpSlice;
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
public class ForwarderSlice<N> implements Forwarder<N> {
  private long offset;
  private long current;
  private long limit;
  private Forwarder<N> forwarder;

  public ForwarderSlice(OpSlice<N> opSlice, Forwarder<N> forwarder) {
    this.offset = opSlice.getStart();
    this.limit = opSlice.getLength();
    this.current = 0;
    this.forwarder = forwarder;
  }

  @Override
  public void process(Mapping<N> mapping) {
      current++;

      if (current>=offset && current <=limit){
          forwarder.process(mapping);
      }
  }
}
