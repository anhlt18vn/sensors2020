package org.rdf4led.db.block;

import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.Config;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * org.rdf4led.db.block
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 11/09/17.
 */
public class PageCompressedTriple implements Page {

  private static int tupleLength = 3;

  private static int headLength = 1;

  private static int pageSize = Config.PAGE_SIZE / 4;

  private int PUNC = -8;

  private int DOT = -9;

  private int deltaInt = 0;

  public int intCount = 3;

  private boolean isDirty;

  public int[] pageData;

  private PageCompressedTriple(int[] pageData) {
    this.pageData = pageData;

    intCount = computeIntCount(this.pageData);
  }

  public PageCompressedTriple() {
    pageData = new int[2048];
  }

  @Override
  public boolean addTuple(int[] tuple) {
    if (isFull()) {
      ArrayUtil.println(tuple);

      String message = "Page is full " + pageData[0] + " : " + intCount;

      throw new RuntimeException(message);
    }

    if (needExpand()) {
      pageData = expand(pageData);
    }

    int slot = findSlot(pageData, tuple, pageData[0]);

    if (slot >= 0) {
      return false;
    } else {
      slot = -(slot + 1);

      ArrayUtil.shiftRight(pageData, pageData[0], slot, tupleLength, headLength);

      ArrayUtil.set(pageData, tuple, slot, headLength);

      pageData[0]++;

      isDirty = true;

      intCount = intCount + deltaInt;

      deltaInt = 0;

      return true;
    }
  }

  private int[] expand(int[] old) {
    return ArrayUtil.expandArray(old, 2048);
  }

  @Override
  public boolean deleteTuple(int[] tuple) {
    if (pageData[0] == 0) return false;

    int slot = ArrayUtil.findSlot(pageData, pageData[0], tuple, tupleLength, headLength);

    if (slot < 0) {
      return false;
    } else {
      ArrayUtil.shiftLeft(pageData, pageData[0], slot, tupleLength, headLength);

      pageData[0]--;

      intCount = intCount - deltaInt;

      deltaInt = 0;

      isDirty = true;
    }

    return false;
  }

  @Override
  public int searchTuple(int[] tuple) {
    int order = ArrayUtil.findSlot(pageData, pageData[0], tuple, tupleLength, headLength);

    return order >= 0 ? order : -(order + 1) == 0 ? 0 : -(order + 1);
  }

  @Override
  public int[] getTuple(int position) {
    return ArrayUtil.get(pageData, position, tupleLength, headLength);
  }

  @Override
  public int[] getPageIndex() {
    return getTuple(0);
  }

  @Override
  public int[] getPageData() {
    return compress(pageData);
  }

  @Override
  public int getTupleCount() {
    return pageData[0];
  }

  @Override
  public void setPage(ByteBuffer byteBuffer) {
    byteBuffer.rewind();

    if (byteBuffer.getInt() == 0) {
      this.pageData = new int[2048];
    } else {
      byteBuffer.rewind();

      this.pageData = new int[pageSize];

      IntBuffer intBuffer = byteBuffer.asIntBuffer();

      try {
        intBuffer.get(this.pageData);
      } catch (Exception e) {
        throw new RuntimeException(intBuffer.capacity() + " " + pageData.length);
      }

      this.pageData = decode(this.pageData);
    }
  }

  @Override
  public boolean isFull() {
    return intCount >= pageSize - 15;
  }

  @Override
  public void setDirty() {
    this.isDirty = true;
  }

  @Override
  public boolean isDirty() {
    return isDirty;
  }

  private boolean needExpand() {
    return tupleLength * pageData[0] > pageData.length - 30;
  }

  @Override
  public Page split() {
    int[] newPageData = new int[pageData.length];

    int newTupleCount = pageData[0] / 2;

    int oldTupleCount = pageData[0] - newTupleCount;

    System.arraycopy(
        pageData,
        headLength + tupleLength * oldTupleCount,
        newPageData,
        headLength,
        newTupleCount * tupleLength);

    pageData[0] = oldTupleCount;

    newPageData[0] = newTupleCount;

    Page page = new PageCompressedTriple(newPageData);

    intCount = computeIntCount(this.pageData);

    return page;
  }

  @Override
  public ByteBuffer toByteBuffer() {
    throw new UnsupportedOperationException();
  }

  private int[] compress(int[] data) {
    int[] output = new int[pageSize];

    int[] t1 = ArrayUtil.get(data, 0, 3, 1);
    int c_s = t1[0];
    int c_p = t1[1];
    int c_o = t1[2];

    output[0] = data[0];
    output[1] = c_s;
    output[2] = c_p;
    output[3] = c_o;

    int c_id = 3;

    for (int i = 1; i < data[0]; i++) {
      int[] t = ArrayUtil.get(data, i, 3, 1);

      if (t[0] == c_s) {
        if (t[1] == c_p) {
          c_id++;

          output[c_id] = t[2];
        } else {
          c_p = t[1];

          c_id++;
          output[c_id] = PUNC;

          c_id++;
          output[c_id] = c_p;

          c_id++;
          output[c_id] = t[2];
        }
      } else {
        c_s = t[0];
        c_p = t[1];
        c_o = t[2];

        c_id++;
        output[c_id] = DOT;

        c_id++;
        output[c_id] = c_s;

        c_id++;
        output[c_id] = c_p;

        c_id++;
        output[c_id] = c_o;
      }
    }

    return output;
  }

  private int[] decode(int[] data) {
    int bufferSize;
    // Calculate buffer Size
    bufferSize = 1024 * ((data[0] * 3) / 1024 + 1);

    int[] output = new int[bufferSize];

    output[0] = data[0];
    output[1] = data[1];
    output[2] = data[2];
    output[3] = data[3];

    int c_id = 4;

    int[] new_t = new int[] {data[1], data[2], data[3]};

    int tripleCount = 1;

    while (tripleCount < data[0]) {
      if (data[c_id] == DOT) {
        c_id++;
        new_t[0] = data[c_id];

        c_id++;
        new_t[1] = data[c_id];

        c_id++;
        new_t[2] = data[c_id];

        c_id++;
      } else {
        if (data[c_id] == PUNC) {
          c_id++;

          new_t[1] = data[c_id];
          c_id++;

          new_t[2] = data[c_id];

          c_id++;
        } else {
          new_t[2] = data[c_id];
          c_id++;
        }
      }

      ArrayUtil.set(output, new_t, tripleCount, 1);

      tripleCount++;
    }

    intCount = c_id;

    return output;
  }

  private int findSlot(int[] data, int[] key, int tupleCount) {
    int low = 0;
    int high = tupleCount - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;

      int x = compareKey(data, key, mid);

      if (x > 0) {
        low = mid + 1;
      } else {
        if (x < 0) {
          high = mid - 1;
        } else {
          return mid;
        }
      }

      deltaInt = Math.abs(x);

      deltaInt = deltaInt == 1 ? deltaInt : deltaInt + 1;
    }

    return -(low + 1);
  }

  private int compareKey(int[] data, int[] key, int position) {
    int pos = headLength + tupleLength * position;

    for (int i = 0; i < 3; i++) {

      int keyInt = key[i];

      int posInt = data[pos + i];

      if (keyInt != posInt) {

        return (keyInt < posInt) ? (i - 3) : (3 - i);

      }
    }

    return 0;
  }

  private int computeIntCount(int[] data) {
    int count = 0;

    int numTuple = data[0];

    int c_s = 0;
    int c_p = 0;

    for (int i = 0; i < numTuple; i++) {
      if (data[i * 3 + headLength] == c_s) {
        if (data[i * 3 + 1 + headLength] == c_p) {
          count += 1;

        } else {
          c_p = data[i * 3 + 1 + headLength];

          count += 3;
          // continue;
        }
      } else {
        c_s = data[i * 3 + headLength];

        c_p = data[i * 3 + 1 + headLength];

        count += 4;
        // continue;
      }
    }

    return count;
  }

  // End of class
  // ============================================================================================================//
  public static void main(String[] args) {
    PageCompressedTriple page = new PageCompressedTriple();

    int[] data =
        new int[] {
          9, 1, 2, 3, 1, 2, 4, 1, 2, 5, 1, 3, 1, 1, 3, 2, 2, 1, 1, 2, 1, 2, 2, 1, 3, 3, 1, 2
        };

    System.out.println(page.computeIntCount(data));

    page.addTuple(new int[] {1, 2, 3});
    page.addTuple(new int[] {1, 2, 4});
    page.addTuple(new int[] {1, 2, 5});
    page.addTuple(new int[] {1, 3, 1});
    page.addTuple(new int[] {1, 3, 2});
    page.addTuple(new int[] {2, 1, 1});
    page.addTuple(new int[] {2, 1, 2});
    page.addTuple(new int[] {2, 1, 3});
    page.addTuple(new int[] {3, 1, 2});

    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Config.PAGE_SIZE);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(page.getPageData());
    ArrayUtil.println(page.getPageData());
    page.setPage(byteBuffer);
    System.out.println();
    ArrayUtil.println(page.pageData);
    System.out.println();
    System.out.println(1024 * (2044 / 1024 + 1));
  }
}
