package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/** Super class for operators that do not combine other operators */
public abstract class Op0<Node> extends OpBase<Node> {
  public abstract Op<Node> apply(Transform<Node> transform);

  public abstract Op<Node> copy();
}
