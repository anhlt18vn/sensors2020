package org.rdf4led.db.index;

import java.util.Iterator;

/**
 * org.rdf4led.db.storage
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 16/08/17.
 */
public class RangeIterator<T> implements Iterator<T> {
  protected Cursor from;
  protected Cursor to;
  protected Index<T> index;

  public RangeIterator(Index<T> index, Cursor from, Cursor to) {
    this.index = index;
    this.from = from;
    this.to = to;
  }

  @Override
  public boolean hasNext() {
    return from.isLeft(to);
  }

  @Override
  public T next() {
    T t = index.getTuple(from);
    from = index.nextCursor(from);
    return t;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove is not supported ");
  }
}
