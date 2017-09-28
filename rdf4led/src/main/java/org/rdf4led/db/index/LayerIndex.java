package org.rdf4led.db.index;


import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.Config;
import org.rdf4led.common.MemoryManager;
import org.rdf4led.db.cache.LRUQueue;
import org.rdf4led.db.index.buffer.BlockBufferEntry;
import org.rdf4led.db.index.buffer.Cursor1;
import org.rdf4led.db.index.buffer.IndexT;
import org.rdf4led.db.index.buffer.PageBufferEntry;

import java.util.Iterator;

/**
 * org.rdf4led.db.index1
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 01/02/18.
 */
public class LayerIndex implements Index<int[]> {

  // Buffer Layer
  private BufferLayer bufferLayer;
  private PersistentLayer persistentLayer;

  private IndexT indexT;

  public IndexT getIndexT() {
    return indexT;
  }

  LRUQueue<BlockBufferEntry> queueLRU;

  public LayerIndex(String storagePath, IndexT indexT) {
    persistentLayer = new PersistentLayer(storagePath, indexT);
    bufferLayer = new BufferLayer(storagePath, indexT, persistentLayer);
    queueLRU = new LRUQueue<>();
    this.indexT = indexT;
  }

  static int numberOfWrite = 0;

  @Override
  public boolean add(int[] tuple) {

    BlockBufferEntry blockBufferEntry = bufferLayer.findBlockEntry(tuple);

    PageBufferEntry pageBufferEntry = blockBufferEntry.findPageBufferEntry(tuple);

    boolean b = pageBufferEntry.add(tuple);

    if (!b) return false;

    if (pageBufferEntry.isFull()) {

      pageBufferEntry.read(persistentLayer);

      // if still full?
      if (!pageBufferEntry.isFull()) {
        return true;
      }

      if (blockBufferEntry.needSplit()) {
        int newBlkId = bufferLayer.allocateBlockId();
        BlockBufferEntry splitBlockBufferEntry = blockBufferEntry.getSplit(newBlkId);
        bufferLayer.addBlockEntry(splitBlockBufferEntry);
      } else {
        PageBufferEntry splitPageBuffer = pageBufferEntry.split();
        blockBufferEntry.addPageBufferEntry(splitPageBuffer);
      }
    }

    queueLRU.add(blockBufferEntry);
    checkBuffer();

    return true;
  }

  @Override
  public boolean delete(int[] tuple) {
    return true;
  }

  @Override
  public int[] getTuple(Cursor cursor) {
    checkBuffer();

    int pageIndex = cursor.getPageIndex();
    int tupleIndex = cursor.getTupleIndex();

    BlockBufferEntry blockBufferEntry = cursor.getBlockEntry();
    PageBufferEntry pageBufferEntry = blockBufferEntry.findPageBufferEntry(pageIndex);
    pageBufferEntry.read(persistentLayer);

    return pageBufferEntry.getTuple(tupleIndex);
  }

  @Override
  public Cursor searchCursor(int[] tuple) {

    int blockIndex = bufferLayer.findBlockIndex(tuple);

    BlockBufferEntry blockBufferEntry = bufferLayer.findBlockBufferEntry(blockIndex);

    queueLRU.add(blockBufferEntry);
    checkBuffer();

    int pageIndex = blockBufferEntry.findPageBufferEntryIndex(tuple);
    PageBufferEntry pageBufferEntry = blockBufferEntry.findPageBufferEntry(pageIndex);
    pageBufferEntry.read(persistentLayer);

    int tupleIndex = pageBufferEntry.findTupleIndex(tuple);
    Cursor cursor = new Cursor1(blockIndex, pageIndex, tupleIndex);
    cursor.setBlockEntry(blockBufferEntry);

    return cursor;
  }

  @Override
  public Cursor nextCursor(Cursor cursor) {

    if (cursor.hasNext()) {
      cursor.next();
    } else {
      cursor.next();
      BlockBufferEntry blockBufferEntry = bufferLayer.findBlockBufferEntry(cursor.getBlockIndex());
      cursor.setBlockEntry(blockBufferEntry);
      queueLRU.add(blockBufferEntry);
      checkBuffer();
    }

    return cursor;
  }

  @Override
  public Cursor prevCursor(Cursor cursor) {
    return null;
  }

  @Override
  public String getIndexName() {
    return this.indexT.getIndexName();
  }

  @Override
  public void sync() {
    bufferLayer.sync();
  }

  private void checkBuffer() {
    if (Config.R) {
      while (queueLRU.size() > 300) {
        BlockBufferEntry towrite = queueLRU.poll();
        if (towrite != null) {
          towrite.write();
        } else {
          break;
        }
      }
    } else {
      while (MemoryManager.usedMemory() >= Config.maxMem) {
        BlockBufferEntry toWrite = queueLRU.poll();
        if (toWrite != null) {
          toWrite.write();
        } else {
          break;
        }
      }
    }
  }

  //  private void checkBuffer(){
  //
  //    while (queueLRU.size() > 2) {
  //
  //      BlockBufferEntry towrite = queueLRU.poll();
  //      if (towrite != null) {
  //        towrite.write();
  //      } else {
  //        break;
  //      }
  //    }
  //  }

  // ====================================================================================================================
  public static void main(String[] args) {
    String storagePath = "/home/anhlt185/rdfstore/";
    LayerIndex layerIndex = new LayerIndex(storagePath, IndexT.SPO);

    int j = 1;
    while (j > 0) {
      j--;

      System.out.println(
          "################################################################################");
      layerIndex = new LayerIndex(storagePath, IndexT.Hash);
      //      System.out.println("TEST1");
      //      //      System.out.println("===>> Before");
      //      //      layerIndex.bufferLayer.log();
      //       testInput1(layerIndex);
      //      //      System.out.println("===>>After");
      //                  layerIndex.bufferLayer.log();
      //       layerIndex.sync();
      //      //  System.out.println("===>>After Sync");
      //      testReadAll(layerIndex);
      //      layerIndex.sync();
      //      testReadAll(layerIndex);
      //      //layerIndex.bufferLayer.log();
      //      MemoryManager.log1();
      //
      //      System.out.println("#####################");
      //      System.out.println("TEST2");
      //
      //      System.out.println("===>> Before");
      //      layerIndex.bufferLayer.log();
      //      testInput2(layerIndex);
      //      testReadAll(layerIndex);
      //      testInput1(layerIndex);
      //      testReadAll(layerIndex);
      //     System.out.println("===>>After");
      //      layerIndex.bufferLayer.log();
      // layerIndex.sync();
      //        System.out.println("===>>After Sync");
      //        layerIndex.bufferLayer.log();
      //
      //
      //      System.out.println("########################");
      //      System.out.println("TEST3");
      //    System.out.println("===>> Before");
      //          layerIndex.bufferLayer.log();

      //            testInput3(layerIndex);
      //    System.out.println("===>>After");
      //    layerIndex.bufferLayer.log();
      //      layerIndex.sync();
      //    System.out.println("===>>After Sync");
      //    layerIndex.bufferLayer.log();
      //
      //    layerIndex = new LayerIndex(storagePath, IndexT.SPO);
      //    layerIndex.bufferLayer.log();
      //
      //            testInput4(layerIndex);
      //            layerIndex.sync();
      //    layerIndex.bufferLayer.log();

      //    testReadOneTuple(layerIndex);

      //      System.out.println("Test All
      // =============================================================");
      //      testReadAll(layerIndex);
      //    System.out.println("Test Read 1
      // =============================================================");
      //    testRead1(layerIndex);
      //      System.out.println("Test Read 2 ============================");
      // testRead2(layerIndex);
      //    System.out.println("Test Read 3
      // =============================================================");
      //    testRead3(layerIndex);

      testHash(layerIndex);
      layerIndex.sync();
      readHash(layerIndex);
    }
  }

  private static void testHash(LayerIndex layerIndex) {
    for (int i = 1; i <= 100; i++) {
      for (int j = 1; j <= 10000; j++)
        layerIndex.add(new int[] {i * 10000 + j, i * 1000 + j + 1000});

      System.out.println(i + " =================================================================");
      System.out.println("Number of Write: " + numberOfWrite);
      numberOfWrite = 0;
      MemoryManager.log1();
    }
  }

  private static void readHash(LayerIndex index) {
    Cursor1 from = (Cursor1) index.searchCursor(new int[] {Integer.MIN_VALUE});
    Cursor1 to = (Cursor1) index.searchCursor(new int[] {Integer.MAX_VALUE});

    Iterator<int[]> iterator = new TupleRangeIter(index, from, to);

    int i = 0;
    while (iterator.hasNext()) {
      int[] t = iterator.next();
      ArrayUtil.println(t);
      i++;
    }

    System.out.println(i);
  }

  private static void testReadOneTuple(LayerIndex layerIndex) {

    //    Cursor1 cursor1 = new Cursor1(0, 0, 0);
    //    int i = 0;
    //    while (true) {
    //      int[] tuple = layerIndex.getTuple(cursor1);
    //      layerIndex.nextCursor(cursor1);
    //      // ArrayUtil.println(tuple);
    //      // System.out.println();
    //      i++;
    //    }
  }

  private static void testInput1(LayerIndex layerIndex) {
    for (int i = 1; i <= 200; i++) {
      for (int j = 1; j <= 50; j++) {
        for (int k = 1; k <= 1000; k++) {
          //          System.out.println(k + " " + j  + " " + i);
          layerIndex.add(new int[] {k, j, i});
        }
        //        MemoryManager.log1();
      }
      System.out.println(i + " =================================================================");
      System.out.println("Number of Write: " + numberOfWrite);
      numberOfWrite = 0;
      MemoryManager.log1();
      //       layerIndex.sync();
      //       layerIndex.bufferLayer.log();
      //
      // System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      // if (i * 50000 != testReadAll(layerIndex)) {
      //  System.out.println("DEBUG");
      //  System.exit(0);
      // }
    }
  }

  private static void testInput2(LayerIndex layerIndex) {
    for (int i = 201; i > 101; i--)
      for (int j = 201; j > 101; j--)
        for (int k = 201; k > 101; k--) {
          layerIndex.add(new int[] {i, j, k});
        }
  }

  private static void testInput3(LayerIndex layerIndex) {
    for (int i = 2; i < 3; i++) {
      for (int j = 11; j < 99; j++) {
        // for (int k = 1; k < 10; k++)
        layerIndex.add(new int[] {0, i, j});
        // layerIndex.bufferLayer.log();
      }
    }
  }

  private static void testInput4(LayerIndex index) {
    for (int s = 1; s < 10; s++) {
      for (int p = 1; p < 10; p++) {
        for (int o = 1; o < 10; o++) {
          //          storage.bufferLayer.log();
          index.add(new int[] {s, p, o});
        }
      }
    }
  }

  private static int testReadAll(LayerIndex index) {
    Cursor1 from =
        (Cursor1)
            index.searchCursor(new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE});
    Cursor1 to =
        (Cursor1)
            index.searchCursor(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE});

    Iterator<int[]> iterator = new TupleRangeIter(index, from, to);

    int i = 0;
    while (iterator.hasNext()) {
      try {
        int[] t = iterator.next();
        i++;
      } catch (Exception e) {
        //        e.printStackTrace();
      }
    }

    System.out.println(i);

    return i;
  }

  private static void testRead1(LayerIndex index) {
    Cursor1 from = (Cursor1) index.searchCursor(new int[] {8, 8, Integer.MIN_VALUE});
    Cursor1 to = (Cursor1) index.searchCursor(new int[] {8, 8, Integer.MAX_VALUE});

    Iterator<int[]> iterator = new TupleRangeIter(index, from, to);

    while (iterator.hasNext()) {
      ArrayUtil.println(iterator.next());
      System.out.println();
    }
  }

  private static void testRead2(LayerIndex index) {
    Cursor1 from =
        (Cursor1) index.searchCursor(new int[] {0, Integer.MIN_VALUE, Integer.MIN_VALUE});
    Cursor1 to = (Cursor1) index.searchCursor(new int[] {0, Integer.MAX_VALUE, Integer.MAX_VALUE});

    Iterator<int[]> iterator = new TupleRangeIter(index, from, to);

    while (iterator.hasNext()) {
      ArrayUtil.println(iterator.next());
      System.out.println();
    }
  }

  private static void testRead3(LayerIndex index) {
    Cursor1 from = (Cursor1) index.searchCursor(new int[] {8, 8, 8});
    Cursor1 to = (Cursor1) index.searchCursor(new int[] {8, 8, 8});

    Iterator<int[]> iterator = new TupleRangeIter(index, from, to);

    while (iterator.hasNext()) {
      ArrayUtil.println(iterator.next());
      System.out.println();
    }
  }

  private static class TupleRangeIter extends RangeIterator<int[]> {

    public TupleRangeIter(Index<int[]> index, Cursor from, Cursor to) {
      super(index, from, to);
    }
  }

  //  private static void testHash(){
  //    String storagePath = "/home/anhlt185/rdfstore/";
  //
  //    LayerIndex layerIndex = new LayerIndex(storagePath, IndexT.Hash);
  //
  //    for (int key = 0; key<1000; key++){
  //      layerIndex.add(new int[]{key, key});
  //    }
  //
  //    layerIndex.bufferLayer.log();
  //    layerIndex.sync();
  //
  // System.out.println("==========================================================================================");
  //    System.out.println("After Sync");
  //    layerIndex.bufferLayer.log();
  //  }

}
