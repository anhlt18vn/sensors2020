package org.rdf4led.db.index;

/** Created by anhlt185 on 30/04/17. */
public interface Index<T> {

  public boolean add(T tuple);

  public boolean delete(T tuple);

  public T getTuple(Cursor cursor);

  public Cursor searchCursor(T tuple);

  public Cursor nextCursor(Cursor cursor);

  public Cursor prevCursor(Cursor cursor);

  public String getIndexName();

  public void sync();
}
