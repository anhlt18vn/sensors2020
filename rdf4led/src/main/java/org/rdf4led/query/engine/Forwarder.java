package org.rdf4led.query.engine;

import org.rdf4led.common.mapping.Mapping;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 05/10/17.
 */
public interface Forwarder<Node>
{
  public void process(Mapping<Node> mapping);
}
