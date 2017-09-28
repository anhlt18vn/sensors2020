package org.rdf4led.query.sparql.algebra.transform;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.op.OpJoin;
import org.rdf4led.query.sparql.algebra.op.OpTable;

public class TransformSimplify<Node> extends TransformCopy<Node> {
  @Override
  public Op<Node> transform(OpJoin<Node> opJoin, Op<Node> left, Op<Node> right) {
    if (isJoinIdentify(left)) return right;

    if (isJoinIdentify(right)) return left;

    return super.transform(opJoin, left, right);
  }

  private boolean isJoinIdentify(Op<Node> op) {
    if (!(op instanceof OpTable)) {
      return false;
    }
    //        Table<Node> t = ((OpTable<Node>)org.rdf4led.sparql.algebra.op).getTable() ;
    //        // Safe answer.
    //        return (t instanceof TableUnit) ;
    return true;
  }
}
