package org.rdf4led.db.index;

import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.FileUtil;
import org.rdf4led.db.index.buffer.BlockBufferEntry;
import org.rdf4led.db.index.buffer.BufferEntryComparator;
import org.rdf4led.db.index.buffer.IndexT;
import org.rdf4led.db.index.buffer.KeyBufferEntry;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * org.rdf4led.db.buffer
 *
 * <p>Buffer Layer holds the information of blockEntry and pageEntry - add new pageEntry - find a
 * pageID with a given tuple
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 15/08/17.
 */
public class BufferLayer {

  private int blockCount;

  private final int blockEntryLength;

  private IndexT indexT;

  private String filePath;

  private BlockBufferEntry[] blockEntries;

  private int exChunk = 16;

  private final int offset = 1;

  private PersistentLayer persistentLayer;

  BufferLayer(String storagePath, IndexT indexT, PersistentLayer persistentLayer) {

    this.indexT = indexT;

    this.filePath = storagePath + indexT.getBufferFile();

    this.persistentLayer = persistentLayer;

    this.blockEntryLength = indexT.getBlockEntryLength();

    FileChannel fileChannel = FileUtil.open(filePath);

    try {

      if (fileChannel.size() == 0) {
        initBufferLayer(new int[this.blockEntryLength]);
      } else {
        ByteBuffer bb = ByteBuffer.allocateDirect((int) fileChannel.size());
        IntBuffer intBuffer = bb.asIntBuffer();
        fileChannel.read(bb);
        int[] buffer = new int[intBuffer.capacity()];
        intBuffer.rewind();
        intBuffer.get(buffer);
        initBufferLayer(buffer);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    try {

      fileChannel.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int allocateBlockId() {
    return blockCount;
  }

  private void initBufferLayer(int[] buffer) {

    this.blockCount = buffer[0];

    this.blockEntries = new BlockBufferEntry[(blockCount / exChunk + 1) * exChunk];

    if (blockCount == 0) {

      blockEntries[0] = BlockBufferEntry.createEmptyBlockBufferEntry(indexT, persistentLayer);

      blockCount = 1;

    } else {

      for (int i = 0; i < this.blockCount; i++) {

        int offset__ = offset + i*this.blockEntryLength;

        blockEntries[i] =
            BlockBufferEntry.injectBlockBufferEntry(buffer, offset__, indexT, persistentLayer);
      }
    }
  }

  public BlockBufferEntry findBlockEntry(int[] tuple) {
    int index = findBlockIndex(tuple);
    return blockEntries[index];
  }

  public BlockBufferEntry findBlockBufferEntry(int index) {
    return blockEntries[index];
  }

  public int findBlockIndex(int[] tuple) {
    int index =
        Arrays.binarySearch(
            blockEntries,
            0,
            blockCount,
            KeyBufferEntry.createKeyBufferEntry(tuple),
            BufferEntryComparator.comparator);
    index = ArrayUtil.indexToOrder(index);

    return index;
  }

  public void addBlockEntry(BlockBufferEntry bufferBlockEntry) {
    if (needExpand()) {
      BlockBufferEntry[] nBlockBufferEntries = new BlockBufferEntry[blockCount + exChunk];
      System.arraycopy(blockEntries, 0, nBlockBufferEntries, 0, blockCount);
      this.blockEntries = nBlockBufferEntries;
    }

    int index =
        Arrays.binarySearch(
            blockEntries, 0, blockCount, bufferBlockEntry, BufferEntryComparator.comparator);

    index = ArrayUtil.indexToInsertPos(index);

    System.arraycopy(blockEntries, index, blockEntries, index + 1, blockCount - index);

    blockEntries[index] = bufferBlockEntry;

    blockCount++;
  }

  private boolean needExpand() {
    return blockCount == blockEntries.length;
  }

  public int getBlockCount(){
    return blockCount;
  }

  public void sync() {
    ByteBuffer byteBuffer =
        ByteBuffer.allocateDirect((offset + blockCount * this.blockEntryLength) * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.rewind();
    intBuffer.put(blockCount);

    for (int i = 0; i < blockCount; i++) {
      blockEntries[i].write();
      int[] blkEntry = blockEntries[i].serialize();
      intBuffer.position(offset + i * this.blockEntryLength);
      intBuffer.put(blkEntry);
    }
    byteBuffer.rewind();

    FileChannel fileChannel = FileUtil.open(this.filePath);
    try {
      fileChannel.position(0);
      fileChannel.write(byteBuffer);
      fileChannel.force(false);
      fileChannel.close();
    } catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
  }

  private void log(int[] meta) {
    int blkCount = meta[0];
    System.out.println();
    System.out.println("BlK count " + blkCount);

    for (int i = 0; i < blkCount; i++) {
      System.out.print("[");
      System.out.print(meta[1 + i * blockEntryLength]);
      System.out.print("]");
      System.out.print("[");
      System.out.print(meta[1 + 1 + i * blockEntryLength]);
      System.out.print("]");
      System.out.print("->");
      System.out.print("[");
      System.out.print(meta[1 + 1 + 1 + i * blockEntryLength] + " ");

      for (int j = 1; j < (blockEntryLength - 2); j++) {
        // if begin of page
        if (j % indexT.getPageEntryLength() == 0) System.out.print("] [");
        System.out.print(meta[3 + i * blockEntryLength + j] + " ");
      }

      System.out.print("]");

      System.out.println();
    }
  }

  // ====================================================================================================================
  public static void main(String[] args) {
    System.out.println("Buffer Layer");
  }

  public void log() {
    System.out.println("-----------------------------------------");
    System.out.println("[ block count: " + this.blockCount + "]");

    for (int i = 0; i < blockCount; i++) {
      BlockBufferEntry blkEntry = blockEntries[i];
      blkEntry.log();
    }
  }
}
