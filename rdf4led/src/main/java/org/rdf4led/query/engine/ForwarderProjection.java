package org.rdf4led.query.engine;

import org.rdf4led.query.sparql.algebra.op.OpProject;
import org.rdf4led.common.mapping.Mapping;

/**
 * org.rdf4led.query.engine
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  22/01/18.
 */
public class ForwarderProjection<N> implements Forwarder<N> {

    private Forwarder<N> nextForwader;
    private OpProject<N> op;

    public ForwarderProjection(OpProject<N> op, Forwarder<N> nextForwarder)
    {
        this.op = op;
        this.nextForwader = nextForwarder;
    }

    @Override
    public void process(Mapping<N> mapping) {
        nextForwader.process(mapping);
    }
}
