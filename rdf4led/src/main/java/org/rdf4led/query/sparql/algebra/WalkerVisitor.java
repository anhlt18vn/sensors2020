package org.rdf4led.query.sparql.algebra;

import org.rdf4led.query.sparql.algebra.op.Op0;
import org.rdf4led.query.sparql.algebra.op.Op1;
import org.rdf4led.query.sparql.algebra.op.Op2;
import org.rdf4led.query.sparql.algebra.op.OpN;

import java.util.Iterator;

public class WalkerVisitor<Node> extends OpVisitorByType<Node> {
  private final OpVisitor<Node> beforeVisitor;

  private final OpVisitor<Node> afterVisitor;

  protected final OpVisitor<Node> visitor;

  public WalkerVisitor(
      OpVisitor<Node> visitor, OpVisitor<Node> beforeVisitor, OpVisitor<Node> afterVisitor) {
    this.visitor = visitor;

    this.beforeVisitor = beforeVisitor;

    this.afterVisitor = afterVisitor;
  }

  public WalkerVisitor(OpVisitor<Node> visitor) {
    this(visitor, null, null);
  }

  protected final void before(Op<Node> op) {
    if (beforeVisitor != null) {
      op.visit(beforeVisitor);
    }
  }

  protected final void after(Op<Node> op) {
    if (afterVisitor != null) {
      op.visit(afterVisitor);
    }
  }

  @Override
  protected void visit0(Op0<Node> op) {
    before(op);

    if (visitor != null) op.visit(visitor);

    after(op);
  }

  @Override
  protected void visit1(Op1<Node> op) {
    before(op);

    if (op.getSubOp() != null) op.getSubOp().visit(this);

    if (visitor != null) op.visit(visitor);

    after(op);
  }

  @Override
  protected void visit2(Op2<Node> op) {
    before(op);

    if (op.getLeft() != null) op.getLeft().visit(this);

    if (op.getRight() != null) op.getRight().visit(this);

    if (visitor != null) op.visit(visitor);

    after(op);
  }

  @Override
  protected void visitN(OpN<Node> op) {
    before(op);

    for (Iterator<Op<Node>> iter = op.iterator(); iter.hasNext(); ) {
      Op<Node> sub = iter.next();

      sub.visit(this);
    }

    if (visitor != null) op.visit(visitor);

    after(op);
  }

  //    @Override
  //    protected void visitExt(OpExt<Node> org.rdf4led.sparql.algebra.op)
  //    {
  //        before(org.rdf4led.sparql.algebra.op) ;
  //
  //        if ( visitor != null ) org.rdf4led.sparql.algebra.op.visit(visitor) ;
  //
  //        after(org.rdf4led.sparql.algebra.op) ;
  //    }
}
