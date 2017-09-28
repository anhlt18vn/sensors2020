package org.rdf4led.query.engine;

import org.rdf4led.query.QueryContext;
import org.rdf4led.common.mapping.Mapping;

import java.util.Iterator;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 05/10/17.
 */

public class ForwarderPrint<N> implements Forwarder<N> {
  private QueryContext<N> context;

  public static int number;

  public ForwarderPrint(QueryContext<N> context) {
    this.context = context;
    number = 0;
  }

  private void print(Mapping<N> mapping) {
    System.out.println("----------------------------------------------------");
    Iterator<N> vars = mapping.vars();

    while (vars.hasNext()) {
      N var = vars.next();

      N value = mapping.getValue(var);

      System.out.println(
          context.dictionary().getLexicalForm(var)
              + " : "
              + context.dictionary().getLexicalForm(value));
    }

    System.out.println("----------------------------------------------------");
  }

  @Override
  public void process(Mapping<N> mapping) {
    print(mapping);
  }
}
