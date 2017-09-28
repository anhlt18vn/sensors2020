package org.rdf4led.db.index.buffer;


import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.BitUtil;
import org.rdf4led.common.Log;
import org.rdf4led.db.index.PersistentLayer;

import java.util.Arrays;

import static org.rdf4led.common.Config.NUM_PAGE_IN_BLOCK;


/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le-Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 05/02/18.
 */
public class BlockBufferEntry implements BufferEntry {

  private int blockId;

  private final IndexT indexT;

  private PageBufferEntry[] pageEntries;

  private long wPageIdBitSet;

  private int pageEntryCount;

  private PersistentLayer persistentLayer;

  private int inBufferPack;

  // ===================================================================================================================
  // Constructors
  // Create an empty Block Buffer Entry
  public static BlockBufferEntry createEmptyBlockBufferEntry(
      IndexT indexT, PersistentLayer persistentLayer) {
    BlockBufferEntry blockBufferEntry = new BlockBufferEntry(indexT);
    blockBufferEntry.pageEntries[0] = PageBufferEntry.createEmptyPageBufferEntry(indexT);
    blockBufferEntry.pageEntryCount = 1;
    blockBufferEntry.blockId = 0;
    blockBufferEntry.persistentLayer = persistentLayer;
    return blockBufferEntry;
  }

  private BlockBufferEntry(IndexT indexT) {
    this.indexT = indexT;
    pageEntries = new PageBufferEntry[NUM_PAGE_IN_BLOCK];
    this.wPageIdBitSet = Long.MAX_VALUE - 1;
  }

  // ====================================================================================================================
  // Create
  public static BlockBufferEntry injectBlockBufferEntry(
      int[] metaIn, int offset, IndexT indexT, PersistentLayer persistentLayer) {
    BlockBufferEntry blockBufferEntry = new BlockBufferEntry(indexT);
    blockBufferEntry.injectPageBufferEntry(metaIn, offset);
    blockBufferEntry.persistentLayer = persistentLayer;
    return blockBufferEntry;
  }

  // Constructor .....

  private BlockBufferEntry(
      PageBufferEntry[] pageEntries,
      int pageEntryCount,
      IndexT indexT,
      PersistentLayer persistentLayer,
      int blockId) {
    this.pageEntries = pageEntries;
    this.pageEntryCount = pageEntryCount;
    this.indexT = indexT;
    this.persistentLayer = persistentLayer;
    this.wPageIdBitSet = Long.MAX_VALUE;
    this.blockId = blockId;
  }

  private void injectPageBufferEntry(int[] metaInt, int offset) {
    this.blockId = metaInt[offset];
    this.pageEntryCount = metaInt[offset + 1];

    for (int i = 0; i < pageEntryCount; i++) {
      int[] pageEntryInt =
          ArrayUtil.getTupleAtPosition(metaInt, i, offset + 2, this.indexT.getPageEntryLength());

      this.pageEntries[i] = new PageBufferEntry(indexT, pageEntryInt);
      this.wPageIdBitSet =
          BitUtil.unSetL(wPageIdBitSet, pageEntries[i].getWId() % NUM_PAGE_IN_BLOCK);
    }
  }

  // ===================================================================================================================
  public boolean add(int[] tuple) {
    PageBufferEntry pageBufferEntry = findPageBufferEntry(tuple);
    return pageBufferEntry.add(tuple);
  }

  public PageBufferEntry findPageBufferEntry(int[] tuple) {
    int index = findPageBufferEntryIndex(tuple);
    return findPageBufferEntry(index);
  }

  public PageBufferEntry findPageBufferEntry(int pageIndex) {
    return pageEntries[pageIndex];
  }

  public int findPageBufferEntryIndex(int[] tuple) {
    int index =
        Arrays.binarySearch(
            pageEntries,
            0,
            pageEntryCount,
            KeyBufferEntry.createKeyBufferEntry(tuple),
            BufferEntryComparator.comparator);

    index = ArrayUtil.indexToOrder(index);

    return index;
  }

  private int allocateWPageId() {
    int wPageId__ = BitUtil.firstBitSetL(this.wPageIdBitSet);
    this.wPageIdBitSet = BitUtil.unSetL(this.wPageIdBitSet, wPageId__);
    int newRWPageId = blockId * NUM_PAGE_IN_BLOCK + wPageId__;
    return newRWPageId;
  }

  public void addPageBufferEntry(PageBufferEntry pageBufferEntry) {
    int newRWPageId = allocateWPageId();

    pageBufferEntry.setRId(newRWPageId);
    pageBufferEntry.setWId(newRWPageId);

    int index =
        Arrays.binarySearch(
            pageEntries, 0, pageEntryCount, pageBufferEntry, BufferEntryComparator.comparator);

    index = ArrayUtil.indexToInsertPos(index);

    System.arraycopy(pageEntries, index, pageEntries, index + 1, pageEntryCount - index);

    pageEntries[index] = pageBufferEntry;
    pageEntryCount++;
  }

  // Still need to be read from this block
  private boolean isNotRead(int rPage) {
    rPage = rPage - (blockId * NUM_PAGE_IN_BLOCK);
    return (rPage >= 0) && (rPage < NUM_PAGE_IN_BLOCK);
  }

  public boolean needSplit() {
    return pageEntryCount == pageEntries.length;
  }

  public BlockBufferEntry getSplit(int spBlockId) {

    PageBufferEntry[] newPageEntries = new PageBufferEntry[NUM_PAGE_IN_BLOCK];

    System.arraycopy(pageEntries, NUM_PAGE_IN_BLOCK / 2, newPageEntries, 0, NUM_PAGE_IN_BLOCK / 2);

    BlockBufferEntry splitBlockBufferEntry =
        new BlockBufferEntry(
            newPageEntries, NUM_PAGE_IN_BLOCK / 2, this.indexT, this.persistentLayer, spBlockId);

    this.pageEntryCount = NUM_PAGE_IN_BLOCK / 2;

    for (int i = 0; i < NUM_PAGE_IN_BLOCK / 2; i++) {
      this.wPageIdBitSet = BitUtil.setL(this.wPageIdBitSet, newPageEntries[i].getWId());
      newPageEntries[i].setWId(splitBlockBufferEntry.allocateWPageId());
      newPageEntries[i].read(persistentLayer);
    }

    return splitBlockBufferEntry;
  }

  @Override
  public int[] getKey() {
    return pageEntries[0].getKey();
  }

  @Override
  public int getRId() {
    return this.blockId;
  }

  @Override
  public int getWId() {
    throw new IllegalArgumentException();
  }

  @Override
  public void setRId(int rId) {
    this.blockId = rId;
  }

  @Override
  public void setWId(int wId) {
    throw new IllegalArgumentException();
  }

  @Override
  public void read(PersistentLayer persistentLayer) {}

  @Override
  public int[] serialize() {
    int[] meta = new int[indexT.getBlockEntryLength()];
    meta[0] = blockId;
    meta[1] = pageEntryCount;

    for (int i = 0; i < pageEntryCount; i++) {
      int[] pageEntryBuffer = pageEntries[i].serialize();

      ArrayUtil.copyTuple(
          pageEntryBuffer,
          0,
          0,
          meta,
          i,
          indexT.getBlockEntryOffset(),
          indexT.getPageEntryLength());
    }

    return meta;
  }

  public void write() {
    Log.countWriteBlock(indexT.getIndexName());
    for (int i = 0; i < pageEntryCount; i++) {
      pageEntries[i].write(persistentLayer);
      pageEntries[i].setRId(pageEntries[i].getWId());
    }
    //persistentLayer.sync();
  }

  public int pageCount() {
    return pageEntryCount;
  }

  public int onBufferLength(){
    int onBufferLength = 0;
    for (int i=0; i<pageEntryCount; i++){
      onBufferLength = pageEntries[i].getOnBufferLength() + onBufferLength;
    }
    return onBufferLength;
  }

  @Override
  public void log() {
    System.out.println("-------------------------------------");
    System.out.println("[BLKID]     : " + blockId);
    System.out.print("[BLK KEY]   : ");
    ArrayUtil.println(getKey());
    System.out.println();
    System.out.println("page count : " + this.pageEntryCount);
    for (PageBufferEntry bufferEntry : pageEntries) {
      if (bufferEntry != null) {
        bufferEntry.log();
      }
    }
    System.out.println();
  }
}
