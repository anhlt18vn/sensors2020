package org.rdf4led.query.engine;

import org.rdf4led.query.sparql.algebra.op.OpDistinct;
import org.rdf4led.common.mapping.Mapping;

import java.util.HashSet;
import java.util.Set;

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
public class ForwarderDistinct<N> implements Forwarder<N> {

    private Forwarder<N> nextForwarder;
    private Set<Mapping<N>> seen;

    public ForwarderDistinct(OpDistinct<N> opDistinct, Forwarder<N> nextForwarder){
        this.nextForwarder = nextForwarder;
        this.seen = new HashSet<>();
    }

    @Override
    public void process(Mapping<N> mapping) {
        if (!seen.contains(mapping))
        {
            seen.add(mapping);
            nextForwarder.process(mapping);
        }
    }
}
