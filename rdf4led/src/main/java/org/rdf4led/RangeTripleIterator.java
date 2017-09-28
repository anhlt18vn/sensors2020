package org.rdf4led;


import org.rdf4led.db.index.Cursor;
import org.rdf4led.db.index.Index;
import org.rdf4led.graph.Triple;

import java.util.Iterator;

/**
 * org.rdf4led.graph.distributed
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 16/08/17.
 */
public class RangeTripleIterator implements Iterator<Triple<Integer>> {
  protected Cursor from;

  protected Cursor to;

  protected Index<int[]> index;

  protected Cursor _from, _to;

  TripleInt triple;

  public RangeTripleIterator(Index<int[]> index, Cursor from, Cursor to) {
    this.index = index;
    this.from = from;
    this.to = to;
    triple = new TripleInt(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  @Override
  public boolean hasNext() {
    return from.isLeft(to);
  }

  @Override
  public Triple<Integer> next() {
    int[] t = index.getTuple(from);
    from = index.nextCursor(from);
    triple.set_triple(index.getIndexName(), t);
    return triple;
  }

  public int size() {
    int from__ = from.getBlockIndex()*1000000 + from.getPageIndex()*1000 + from.getTupleIndex();
    int to__ = to.getBlockIndex()*1000000 + to.getPageIndex()*1000 + to.getTupleIndex();
    return to__ - from__;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove is not supported ");
  }
}
