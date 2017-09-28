package org.rdf4led.db.index;


import org.rdf4led.db.index.buffer.BlockBufferEntry;

/**
 * org.rdf4led.db.cursor
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 15/08/17.
 */
public interface Cursor {

   public void setCursor();

   public boolean isLeft(Cursor cursor);

   public int getBlockIndex();

   public int getPageIndex();

   public int getTupleIndex();

   public void setBlockEntry(BlockBufferEntry blockEntry);

   public BlockBufferEntry getBlockEntry();

   public void nextTupleIndex();

   public void nextPageIndex();

   public void resetTupleIndex();

   public void nextBlockIndex();

   public void resetPageIndex();

   public boolean hasNext();

   public void next();


//  private int pageOrder;
//
//  private int tupleOrder;
//
//  private PageEntry pageEntry;
//
//  public Cursor(int pageOrder, int tupleOrder) {
//    this.pageOrder = pageOrder;
//
//    this.tupleOrder = tupleOrder;
//  }
//
//  public void setCursor(int pageOrder, int tupleOrder) {
//    this.pageOrder = pageOrder;
//
//    this.tupleOrder = tupleOrder;
//  }
//
//  public int getPageOrder() {
//    return pageOrder;
//  }
//
//  public int getTupleOrder() {
//    return tupleOrder;
//  }
//
//  public Cursor setPageEntry(PageEntry pageEntry) {
//    this.pageEntry = pageEntry;
//
//    return this;
//  }
//
//  public PageEntry getPageEntry() {
//    return pageEntry;
//  }
//
//  public boolean isLeft(Cursor cursor) {
//    return pageOrder < cursor.pageOrder
//        ? true
//        : pageOrder != cursor.pageOrder ? false : tupleOrder < cursor.tupleOrder ? true : false;
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    return !(o instanceof Cursor)
//        ? false
//        : ((((Cursor) o).pageOrder == pageOrder) && (((Cursor) o).tupleOrder == tupleOrder));
//  }
//
//  public void log() {
//    System.out.println("page Order: " + pageOrder + " tuple Order " + tupleOrder);
//  }
}
