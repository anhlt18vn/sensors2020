package org.rdf4led.db.index.buffer;


import org.rdf4led.common.ArrayUtil;

import java.util.Arrays;

import static org.rdf4led.common.Config.EX_CHUNK_INT;
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
 * <p>Date: 02/02/18.
 */
public class PageBufferData implements IPageData<int[]> {

  private int[] buffer;

  private static final int offset = 2;

  private IndexT indexT;

  // private static int exChunk = 32; //256 int ~ 1kb

  public PageBufferData(IndexT indexT) {
    this.buffer = new int[offset];
    this.indexT = indexT;
  }

  private PageBufferData(IndexT indexT, int[] buffer) {
    this.buffer = buffer;
    this.indexT = indexT;
  }

  @Override
  public boolean add(int[] tuple) {
    if (this.needExpand()) {
      buffer = ArrayUtil.expandArray(buffer, EX_CHUNK_INT); // allocate 1k more for buffer
    }

    if (indexT != IndexT.Hash) {
      return ArrayUtil.insert2(buffer, tuple, offset, indexT.getTupleLength(), indexT.getKeyLength());
    } else{
      return ArrayUtil.insert(buffer, tuple, offset, indexT.getTupleLength(), indexT.getKeyLength());
    }
  }

  @Override
  public boolean delete(int[] tuple) {
    return ArrayUtil.remove2(buffer, tuple, offset, indexT.getTupleLength(), indexT.getKeyLength());
  }

  @Override
  public int[] get(int position) {

    if (position >= buffer[0]) {
      int[] result = new int[indexT.getTupleLength()];
      Arrays.fill(result, Integer.MAX_VALUE);
      return result;
    }

    return ArrayUtil.getTupleAtPosition(buffer, position, offset, indexT.getTupleLength());
  }

  @Override
  public int search(int[] tuple) {

    return ArrayUtil.searchReadPosition(
        buffer, tuple, offset, indexT.getTupleLength(), indexT.getKeyLength(), 0, buffer[0]);
  }

  // Merge with the part that is read from disk
  public void merge(int[] onDisk) {
    if (indexT != IndexT.Hash) {
      onDisk = ArrayUtil.unpackTriple(onDisk, offset, indexT.getTupleLength());
      buffer =
          ArrayUtil.mergeTP(onDisk, buffer, offset, indexT.getTupleLength(), indexT.getKeyLength());
    } else {
      buffer =
          ArrayUtil.mergeH(onDisk, buffer, offset, indexT.getTupleLength(), indexT.getKeyLength());
    }
  }

  private boolean needExpand() {
    return (buffer[0] + 2) * indexT.getTupleLength() > buffer.length;
  }

  public PageBufferData split() {
    int nTupleCount = this.buffer[0] >>> 1;
    int oTupleCount = this.buffer[0] - nTupleCount;

    int nBufferLength = (offset + nTupleCount * indexT.getTupleLength()) / EX_CHUNK_INT + 1;
    int[] nBuffer = new int[offset + nBufferLength * EX_CHUNK_INT];

    // do copy to new
    int cpFrom = offset + oTupleCount * indexT.getTupleLength();
    int cpLength = nTupleCount * indexT.getTupleLength();
    System.arraycopy(this.buffer, cpFrom, nBuffer, offset, cpLength);

    // do copy to old
    int oBufferLength = (offset + oTupleCount * indexT.getTupleLength()) / EX_CHUNK_INT + 1;
    int[] oBuffer = new int[offset + oBufferLength * EX_CHUNK_INT];
    cpLength = oTupleCount * indexT.getTupleLength();
    System.arraycopy(this.buffer, offset, oBuffer, offset, cpLength);

    this.buffer = oBuffer;
    this.buffer[0] = oTupleCount;
    if (indexT != IndexT.Hash) {
      ArrayUtil.computePackedInt(this.buffer, offset);
    } else {
      this.buffer[1] = buffer[0] * indexT.getTupleLength() + offset;
    }

    nBuffer[0] = nTupleCount;
    if (indexT != IndexT.Hash) {
      ArrayUtil.computePackedInt(nBuffer, offset);
    } else {
      nBuffer[1] = nBuffer[0] * indexT.getTupleLength() + offset;
    }

    return new PageBufferData(this.indexT, nBuffer);
  }

  public int[] getBuffer() {
    if (indexT != IndexT.Hash) {
      return ArrayUtil.packTriple(buffer, this.offset, PAGE_SIZE_INT);
    } else {
      return buffer;
    }
  }

  public int onBufferLength() {
    return this.buffer.length;
  }

  public int onBufferPackLength() {
    return this.buffer[1];
  }

  public int tupleCount() {
    return this.buffer[0];
  }

  public void reset() {
    this.buffer = new int[offset];
  }

  public IndexT getIndexT() {
    return indexT;
  }
  // ===================================================================================================================
  public static void main(String[] args) {
    PageBufferData bufferPageData = new PageBufferData(IndexT.SPO);
    bufferPageData.add(new int[] {1, 1, 1});
    bufferPageData.add(new int[] {1, 2, 2});
    bufferPageData.add(new int[] {1, 3, 3});
    bufferPageData.add(new int[] {1, 4, 4});
    bufferPageData.add(new int[] {1, 5, 5});
    bufferPageData.add(new int[] {1, 6, 6});
    bufferPageData.add(new int[] {1, 7, 7});
    bufferPageData.add(new int[] {1, 8, 8});
    bufferPageData.add(new int[] {1, 9, 9});
    bufferPageData.dumb();
    PageBufferData nBufferPageData = bufferPageData.split();
    bufferPageData.dumb();
    nBufferPageData.dumb();
  }

  private void dumb() {
    ArrayUtil.println(
        buffer, offset, indexT.getKeyLength(), indexT.getTupleLength() - indexT.getKeyLength());
  }
}
