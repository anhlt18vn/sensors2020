package org.rdf4led.db.block;

import java.nio.ByteBuffer;

/**
 * org.rdf4led.db.block
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 20/08/17.
 */
public class PageSimpleHash extends PageAbstract {
  static int tupleLength = 2; // tuple size    = 2 int;
  static int headLength = 1; // head size     = 1 int;
  static int tupleCountMax = (pageSize - headLength * 4) / (tupleLength * 4) - 1;

  public PageSimpleHash(int[] pageData) {
    this.pageData = pageData;
  }

  public PageSimpleHash() {
    this.pageData = new int[pageSize / 4];
  }

  @Override
  public boolean addTuple(int[] tuple) {
    return super.addTuple(tuple, tupleLength, headLength);
  }

  @Override
  public boolean deleteTuple(int[] tuple) {
    return super.deleteTuple(tuple, tupleLength, headLength);
  }

  @Override
  public int searchTuple(int[] tuple) {
    return super.searchTuple(tuple, tupleLength, headLength);
  }

  @Override
  public int[] getTuple(int position) {
    return super.getTuple(position, tupleLength, headLength);
  }

  @Override
  public int[] getPageIndex() {
    // get fist element of hash tuple
    return new int[] {getTuple(0)[0]};
  }

  @Override
  public boolean isFull() {
    return pageData[0] == tupleCountMax;
  }

  @Override
  public Page split() {
    return split(tupleLength, headLength);
  }

  @Override
  public ByteBuffer toByteBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Page createPage(int[] dataPage) {
    Page page = new PageSimpleHash(dataPage);

    page.setDirty();

    return page;
  }
}
