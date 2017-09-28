package org.rdf4led.db.index.buffer;


import org.rdf4led.common.ArrayUtil;
import org.rdf4led.db.index.PersistentLayer;

import java.util.Arrays;

import static org.rdf4led.common.Config.PAGE_SIZE_INT;

/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 01/02/18.
 */
public class PageBufferEntry implements BufferEntry {

  // Index Type
  private IndexT indexT;

  // Page Buffer Data --> cache in buffer data <----
  private PageBufferData pageBufferData;

  // Page Id = block id * number of page + order_of_page_in_block
  // private int[] pageBufferEntry;
  private boolean isRead;
  private boolean isWritten;
  private boolean isDirty;

  private int[] pageKey;

  private int rPageId;
  private int wPageId;

  private int onBufferTCount;
  private int onDiskTCount;

  private int onBufferLength;
  private int onBufferPackLength;
  private int onDiskPackLength;

  // ====================================================================================================================
  // Page Buffer Entry Constructor
  // Create empty buffer page for the first time initialized
  public static PageBufferEntry createEmptyPageBufferEntry(IndexT indexT) {
    return new PageBufferEntry(indexT);
  }

  private PageBufferEntry(IndexT indexT) {
    this.indexT = indexT;
    this.pageKey = new int[indexT.getKeyLength()];
    Arrays.fill(this.pageKey, Integer.MAX_VALUE);
    this.pageBufferData = new PageBufferData(indexT);
    this.onBufferTCount = 0;
    this.onDiskTCount = 0;
    this.onBufferLength = pageBufferData.onBufferLength();
    this.onBufferPackLength = 0;
    this.onDiskPackLength = 0;
    this.isRead = true;
    this.rPageId = 0;
    this.wPageId = 0;
    this.isDirty = false;
  }

  // ====================================================================================================================
  // for page split
  private PageBufferEntry(PageBufferData pageBufferData) {
    this.indexT = pageBufferData.getIndexT();
    this.pageKey = pageBufferData.get(0);
    this.pageBufferData = pageBufferData;
    this.onBufferTCount = pageBufferData.tupleCount();
    this.onDiskTCount = 0;
    this.onBufferLength = pageBufferData.onBufferLength();
    this.onBufferPackLength = pageBufferData.onBufferPackLength();
    this.onDiskPackLength = 0;
    this.isRead = true;
    this.isDirty = true;
    this.isWritten = false;
  }

  // ====================================================================================================================
  // From distributed
  PageBufferEntry(IndexT indexT, int[] pageBufferEntry) {
    this.indexT = indexT;
    this.pageKey = new int[indexT.getKeyLength()];
    System.arraycopy(pageBufferEntry, 0, pageKey, 0, pageKey.length);
    this.pageBufferData = new PageBufferData(indexT);
    this.onBufferTCount = 0;
    this.onDiskTCount = pageBufferEntry[indexT.onDiskTCIndex()];
    this.onBufferLength = 0;
    this.onBufferPackLength = 0;
    this.onDiskPackLength = pageBufferEntry[indexT.onDiskPackLengthIndex()];
    this.rPageId = pageBufferEntry[indexT.rPageIdIndex()];
    this.wPageId = rPageId;
    this.isDirty = false;
    this.isRead = false;
  }

  // ====================================================================================================================

  public boolean add(int[] tuple) {
    boolean b = pageBufferData.add(tuple);

    if (b) {
      if (rPageId == 0)
        if (ArrayUtil.compare(
                tuple, 0, 0, pageKey, 0, 0, indexT.getTupleLength(), indexT.getKeyLength())
            == -1) {
          this.pageKey = tuple;
        }

      this.onBufferLength = pageBufferData.onBufferLength();
      this.onBufferPackLength = pageBufferData.onBufferPackLength();
      this.onBufferTCount = pageBufferData.tupleCount();
      isDirty = true;
    }
    return b;
  }

  public boolean delete(int[] tuple) {
    return pageBufferData.delete(tuple);
  }

  public boolean isFull() {
    boolean b = this.onBufferPackLength + this.onDiskPackLength + 30 > PAGE_SIZE_INT;
    return b;
  }

  @Override
  public int[] getKey() {
    return this.pageKey;
  }

  @Override
  public int getRId() {
    return rPageId;
  }

  @Override
  public int getWId() {
    return wPageId;
  }

  @Override
  public void setRId(int rId) {
    this.rPageId = rId;
  }

  @Override
  public void setWId(int wId) {
    this.wPageId = wId;
  }

  public PageBufferEntry split() {

    if (!isRead) {
      throw new RuntimeException("Need read before splitting");
    }

    PageBufferData newPageBufferData = this.pageBufferData.split();

    PageBufferEntry splitPageBufferEntry = new PageBufferEntry(newPageBufferData);

    this.onBufferPackLength = pageBufferData.onBufferPackLength();

    this.onBufferTCount = pageBufferData.tupleCount();

    this.isDirty = true;

    return splitPageBufferEntry;
  }

  public void read(PersistentLayer persistentLayer) {
    // When a new wPageId is allocate to an not-free page. detect is this the new page --> has not
    // been written
    // Then do no read it.
    if (isRead) {
      if (rPageId != wPageId) {
        rPageId = wPageId;
        isDirty = true;
      }
      return;
    }

    int[] onDisk = persistentLayer.readPage(rPageId);

    pageBufferData.merge(onDisk);

    this.onBufferTCount = pageBufferData.tupleCount();

    // if after reading the number of tuple is not change
    if (onBufferTCount == onDiskTCount) {
      // if the data is moved from another page
      if (rPageId != wPageId) {
        // Need to rewrite
        this.isDirty = true;

      } else {
        // No need to rewrite the page
        this.isDirty = false;
      }
    }

    // when page is read ->  synchronise the read and write place
    rPageId = wPageId;
    this.isRead = true;
    this.isWritten = false;
    this.onBufferLength = pageBufferData.onBufferLength();
    this.onBufferPackLength = pageBufferData.onBufferPackLength();
    this.onDiskPackLength = 0;
    this.onDiskTCount = 0;
  }

  public void write(PersistentLayer persistentLayer) {
    if (isDirty) read(persistentLayer);

    if (isDirty) {
      //log();
      persistentLayer.writePage2(this.pageBufferData.getBuffer(), this.wPageId);
      isWritten = true;
    }

    if (isRead) {
      this.onBufferPackLength = 0;
      this.onBufferTCount = 0;
      this.onDiskPackLength = pageBufferData.onBufferPackLength();
      this.onDiskTCount = pageBufferData.tupleCount();
      this.isRead = false;
    }

    pageBufferData.reset();
  }

  @Override
  public int[] serialize() {
    int[] pageBufferEntry = new int[indexT.getPageEntryLength()];
    System.arraycopy(pageKey, 0, pageBufferEntry, 0, indexT.getKeyLength());
    if (rPageId != wPageId) {
      throw new IllegalArgumentException("is synced ??? ");
    }
    pageBufferEntry[indexT.rPageIdIndex()] = rPageId;
    pageBufferEntry[indexT.onDiskTCIndex()] = this.onDiskTCount;
    pageBufferEntry[indexT.onBufferTCIndex()] = this.onBufferTCount;
    pageBufferEntry[indexT.onDiskPackLengthIndex()] = this.onDiskPackLength;
    pageBufferEntry[indexT.onBufferPackLengthIndex()] = this.onBufferPackLength;
    pageBufferEntry[indexT.onBufferLengthIndex()] = pageBufferData.onBufferLength();

    return pageBufferEntry;
  }

  public int getOnBufferLength(){
    return onBufferLength;
  }

  public int findTupleIndex(int[] tuple) {
    if (!isRead) {
      System.out.println(" need read??");
    }

    return pageBufferData.search(tuple);
  }

  public int[] getTuple(int tupleIndex) {
    return pageBufferData.get(tupleIndex);
  }

  public int tupleCount() {
    return onBufferTCount;
  }

  @Override
  public void log() {
    System.out.print("[r:" + rPageId + "][w:" + wPageId + "]->");
    ArrayUtil.println(pageKey);
    System.out.print("[obtc: " + onBufferTCount + "]");
    System.out.print("[odtc: " + onDiskTCount + "]");
    System.out.print("[ob: " + onBufferLength + "]");
    System.out.print("[pb:" + onBufferPackLength + "]");
    System.out.print("[pd: " + onDiskPackLength + "]");
    System.out.println("[: " + isRead + "] ");
  }
}
