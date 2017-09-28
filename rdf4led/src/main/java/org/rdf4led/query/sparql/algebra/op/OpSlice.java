package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

/**
 * OpSlice.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class OpSlice<N> extends OpModifier<N> {
  private long start;
  private long length;

  public OpSlice(Op<N> subOp, long start, long length) {
    super(subOp);
    this.start = start;
    this.length = length;
  }

  public long getLength() {
    return length;
  }

  public long getStart() {
    return start;
  }

  @Override
  public void visit(OpVisitor<N> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<N> copy(Op<N> subOp) {
    return new OpSlice<N>(subOp, start, length);
  }

  @Override
  public Op<N> apply(Transform<N> transform, Op<N> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public int hashCode() {
    return getSubOp().hashCode() ^ (int) (start & 0xFFFFFFFF) ^ (int) (length & 0xFFFFFFFF);
  }

  @Override
  public boolean equalTo(Op<N> other) {
    if (!(other instanceof OpSlice)) {
      return false;
    }

    OpSlice<N> opSlice = (OpSlice<N>) other;

    if (opSlice.start != start || opSlice.length != length) {
      return false;
    }

    return getSubOp().equalTo(opSlice.getSubOp());
  }
}
