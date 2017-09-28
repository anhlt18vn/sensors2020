package org.rdf4led.db.index.buffer;

import org.rdf4led.db.index.Cursor;

/**
 * org.rdf4led.db.index1.buffer
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  11/02/18.
 */
public class Cursor1 implements Cursor {

  private int blockIndex;
  private int pageIndex;
  private int tupleIndex;

  private BlockBufferEntry blockbufferEntry;

  public Cursor1(int blockIndex, int pageIndex, int tupleIndex){
    this.blockIndex = blockIndex;
    this.pageIndex = pageIndex;
    this.tupleIndex = tupleIndex;
  }

  public void setBufferEntry(BlockBufferEntry blockbufferEntry){
    this.blockbufferEntry = blockbufferEntry;
  }

  public void setBlockIndex(int blockIndex){
    this.blockIndex = blockIndex;
  }

  public void setPageIndex(int pageIndex){
    this.pageIndex = pageIndex;
  }

  public void setTupleIndex(int tupleIndex){
    this.tupleIndex = tupleIndex;
  }

  public int getBlockIndex(){
    return blockIndex;
  }

  public int getPageIndex(){
    return pageIndex;
  }

  public int getTupleIndex(){
    return tupleIndex;
  }

  @Override
  public void setBlockEntry(BlockBufferEntry blockbufferEntry) {
    this.blockbufferEntry = blockbufferEntry;
  }

  @Override
  public BlockBufferEntry getBlockEntry() {
    return this.blockbufferEntry;
  }


  public void nextTupleIndex(){
    this.tupleIndex++;
  }

  public void nextPageIndex(){
    this.pageIndex++;
  }

  public void nextBlockIndex(){
    this.blockIndex++;
  }

  public void resetTupleIndex(){
    this.tupleIndex = 0;
  }

  public void resetPageIndex(){
    this.pageIndex = 0;
  }

  @Override
  public boolean hasNext() {
    if (tupleIndex< blockbufferEntry.findPageBufferEntry(pageIndex).tupleCount() - 1){
      return true;
    }
    else {
      if (pageIndex < blockbufferEntry.pageCount() - 1){
        return true;
      }else{
        return false;
      }
    }
  }

  @Override
  public void next() {
    if (tupleIndex < blockbufferEntry.findPageBufferEntry(pageIndex).tupleCount() - 1) {
      this.nextTupleIndex();
    } else {
      if (pageIndex < blockbufferEntry.pageCount() - 1) {
        this.nextPageIndex();
        this.resetTupleIndex();
      } else {
        this.nextBlockIndex();
        this.resetPageIndex();
        this.resetTupleIndex();
      }
    }
  }

  @Override
  public void setCursor() {

  }

  public boolean isLeft(Cursor cursor){
    Cursor1 cursor1 = (Cursor1) cursor;

    return this.blockIndex < cursor1.getBlockIndex() ? true :
            this.blockIndex != cursor1.getBlockIndex() ? false :
              this.pageIndex < cursor1.getPageIndex() ? true :
                this.pageIndex != cursor1.getPageIndex() ? false :
                  this.tupleIndex < cursor1.getTupleIndex();
  }
}
