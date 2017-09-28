package org.rdf4led.query.sparql.algebra.op;

import org.rdf4led.query.sparql.algebra.Op;
import org.rdf4led.query.sparql.algebra.OpVisitor;
import org.rdf4led.query.sparql.algebra.transform.Transform;

public class OpStream<Node> extends Op1<Node> {
  byte windowType;

  int windowSize;

  long range; // time

  Node streamNode;

  public OpStream(Op<Node> subOp, Node streamNode, byte windowType, int windowSize, long range) {
    super(subOp);

    this.windowType = windowType;

    this.windowSize = windowSize;

    this.range = range;

    this.streamNode = streamNode;
  }

  public Node getStreamNode() {
    return streamNode;
  }

  public byte getWindowType() {
    return windowType;
  }

  public int getWindowSize() {
    return windowSize;
  }

  public long getRange() {
    return range;
  }

  @Override
  public void visit(OpVisitor<Node> opVisitor) {
    opVisitor.visit(this);
  }

  @Override
  public Op<Node> apply(Transform<Node> transform, Op<Node> subOp) {
    return transform.transform(this, subOp);
  }

  @Override
  public Op<Node> copy(Op<Node> subOp) {
    return new OpStream<Node>(subOp, streamNode, windowType, windowSize, range);
  }

  @Override
  public int hashCode() {
    return streamNode.hashCode() ^ getSubOp().hashCode();
  }

  @Override
  public boolean equalTo(Op<Node> other) {
    // TODO Auto-generated method stub
    return false;
  }
}
