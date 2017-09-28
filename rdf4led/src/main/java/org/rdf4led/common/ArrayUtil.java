package org.rdf4led.common;

import static org.rdf4led.common.Config.EX_CHUNK_INT;

/**
 * org.rdf4led.db
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 18/08/17.
 */
public class ArrayUtil {

  // =====================================================================================================
  // INT ARRAY
  // =====================================================================================================
  public static int indexToOrder(final int index) {
    // return i   >= 0 ? i     : - (i     + 1) == 0 ? 0 : -(i     + 1) - 1;
    return index >= 0 ? index : -(index + 1) == 0 ? 0 : -(index + 1) - 1;
  }

  public static int indexToInsertPos(final int index) {
    if (index >= 0) {
      throw new IllegalArgumentException("storage > 0 can not insert");
    }

    return -(index + 1);
  }

  public static int indexToSearchPosition(final int index) {
    if (index >= 0) return index;
    else return -(index + 1);
  }

  public static int searchReadPosition(
      final int[] array,
      final int[] tuple,
      final int arrOff,
      final int tupleLength,
      final int keyLength,
      final int lowPos,
      final int highPos) {
    int index =
        ArrayUtil.searchPosition(array, tuple, arrOff, tupleLength, keyLength, lowPos, highPos);
    return ArrayUtil.indexToSearchPosition(index);
  }

  public static int searchPosition(
      final int[] array,
      final int[] tuple,
      final int arrOff,
      final int tupleLength,
      final int keyLength,
      final int lowPos,
      final int highPos) {

    int low = lowPos;
    int high = highPos - 1;

    while (low <= high) {

      int mid = (low + high) >>> 1;

      int x = compare(array, mid, arrOff, tuple, 0, 0, tupleLength, keyLength);

      if (x == 1) {
        // if mid tuple > input tuple move to left
        high = mid - 1;
      } else {
        // if mid tuple < input tuple move to right
        if (x == -1) {
          low = mid + 1;
        } else {
          return mid;
        }
      }
    }

    return -(low + 1);
  }

  // Compare a given tuple to a tuple inside an array in a given position
  public static int compare(
      int[] arr1,
      int arr1Pos,
      int arr1Off,
      int[] arr2,
      int arr2Pos,
      int arr2Off,
      int tupleLength,
      int keyLength) {

    for (int i = 0; i < keyLength; i++) {
      if (arr1[arr1Off + arr1Pos * tupleLength + i] != arr2[arr2Off + arr2Pos * tupleLength + i]) {
        return arr1[arr1Off + arr1Pos * tupleLength + i] > arr2[arr2Off + arr2Pos * tupleLength + i]
            ? 1
            : -1;
      }
    }

    return 0;
  }

  // inject a tuple from an array in a given position
  public static int[] getTupleAtPosition(
      final int[] array, final int pos, final int arrOffset, final int tupleLength) {

    int[] output = new int[tupleLength];
    int offset = arrOffset + pos * tupleLength;

    System.arraycopy(array, offset, output, 0, tupleLength);

    return output;
  }

  // insert tuple into array
  public static boolean insert(
      int[] array, int[] tuple, int arrayOffset, int tupleLength, int keyLength) {

    int tupleCount = array[0];

    int insertPos =
        ArrayUtil.searchPosition(array, tuple, arrayOffset, tupleLength, keyLength, 0, tupleCount);

    if (insertPos >= 0) {

      return false;

    } else {

      insertPos = -(insertPos + 1);

      ArrayUtil.shiftRight_(array, insertPos, arrayOffset, tupleLength, 1, tupleCount);

      ArrayUtil.copyTuple(tuple, 0, 0, array, insertPos, arrayOffset, tupleLength);

      array[0]++;

      array[1] = array[0] * tupleLength + arrayOffset;

      return true;
    }
  }

  // insert and update compact
  public static boolean insert2(
      int[] array, int[] tuple, int arrOffset, int tupleLength, int keyLength) {

    int tupleCount = array[0];

    int insertPos =
        ArrayUtil.searchPosition(array, tuple, arrOffset, tupleLength, keyLength, 0, tupleCount);

    if (insertPos >= 0) {

      return false;

    } else {

      insertPos = -(insertPos + 1);

      int delta = computeDeltaInt(tuple, array, insertPos, arrOffset, tupleLength);

      if (insertPos != 0) {

        int deltaL = computeDeltaInt(tuple, array, insertPos - 1, arrOffset, tupleLength);

        delta = delta >= deltaL ? deltaL : delta;
      }

      ArrayUtil.shiftRight_(array, insertPos, arrOffset, tupleLength, 1, tupleCount);

      ArrayUtil.copyTuple(tuple, 0, 0, array, insertPos, arrOffset, tupleLength);

      array[0]++;

      array[1] += delta;

      return true;
    }
  }

  // compute delta ints
  private static int computeDeltaInt(
      int[] tuple, int[] array, int pos, int arrOffset, int tupleLength) {

    int offset = arrOffset + pos * tupleLength;

    int i = 0;

    for (; i < tupleLength; i++) {
      if (tuple[i] != array[offset + i]) break;
    }

    int delta = tupleLength - i;

    return delta == 1 ? delta : delta + 1;
  }

  // remove a tuple from array
  public static boolean remove(
      int[] array, int[] tuple, int arrOffset, int tupleLength, int keyLength) {

    int tupleCount = array[0];

    if (tupleCount == 0) return false;

    int removePos =
        ArrayUtil.searchPosition(array, tuple, arrOffset, tupleLength, keyLength, 0, tupleCount);

    if (removePos >= 0) {

      shiftLeft_(array, removePos, arrOffset, tupleLength, 1, tupleCount);

      array[0]--;

      return true;

    } else {

      return false;
    }
  }

  // remove
  public static boolean remove2(
      int[] array, int[] tuple, int arrOffset, int tupleLength, int keyLength) {

    int tupleCount = array[0];

    if (tupleCount == 0) return false;

    int removePos =
        ArrayUtil.searchPosition(array, tuple, arrOffset, tupleLength, keyLength, 0, tupleCount);

    if (removePos >= 0) {

      int deltaInt = computeDeltaInt(tuple, array, removePos + 1, arrOffset, tupleLength);

      if (removePos != 0) {

        int deltaL = computeDeltaInt(tuple, array, removePos - 1, arrOffset, tupleLength);

        deltaInt = deltaInt >= deltaL ? deltaL : deltaInt;
      }

      shiftLeft_(array, removePos, arrOffset, tupleLength, 1, tupleCount);

      array[0]--;

      array[1] -= deltaInt;

      return true;

    } else {
      return false;
    }
  }

  // Move an array to the Right
  public static void shiftRight_(
      int[] array, int pos, int arrOffset, int tupleLength, int shiftLength, int tupleCount) {

    int fromOffset = arrOffset + pos * tupleLength;
    int toOffset = arrOffset + (pos + shiftLength) * tupleLength;
    int length = (tupleCount - pos) * tupleLength;

    System.arraycopy(array, fromOffset, array, toOffset, length);
  }

  // Move an array to the Left
  public static void shiftLeft_(
      int[] array, int pos, int arrOffset, int tupleLength, int shiftLength, int tupleCount) {
    int fromOffset = arrOffset + (pos + shiftLength) * tupleLength;
    int toOffset = arrOffset + pos * tupleLength;
    int length = (tupleCount - pos) * tupleLength;

    System.arraycopy(array, fromOffset, array, toOffset, length);
  }

  // Copy tuple from an array from a given position to another array
  public static void copyTuple(
      int[] from, int fromPos, int fromOff, int[] to, int toPos, int toOff, int tupleLength) {

    int fromOffset = fromOff + fromPos * tupleLength;

    int toOffset = toOff + toPos * tupleLength;

    System.arraycopy(from, fromOffset, to, toOffset, tupleLength);
  }

  // ===========================================================================================================//
  // pack and un pack triples
  private static int PUNC = -8;

  private static int DOT = -9;

  public static int[] unpackTriple(int[] array, int offset, int tupleLength) {

    int bufferSize = offset + EX_CHUNK_INT * ((array[0] * 3) / EX_CHUNK_INT + 1);

    int[] output = new int[bufferSize];

    output[0] = array[0];
    output[1] = array[1];

    int[] new_t = new int[3];
    new_t[0] = output[offset] = array[offset];
    new_t[1] = output[offset + 1] = array[offset + 1];
    new_t[2] = output[offset + 2] = array[offset + 2];

    int tupleCount = 1;

    int c_id = offset + 3;

    while (tupleCount < array[0]) {

      if (array[c_id] == DOT) {

        c_id++;
        new_t[0] = array[c_id];

        c_id++;
        new_t[1] = array[c_id];

        c_id++;
        new_t[2] = array[c_id];

        c_id++;
      } else {

        if (array[c_id] == PUNC) {

          c_id++;
          new_t[1] = array[c_id];

          c_id++;
          new_t[2] = array[c_id];

          c_id++;
        } else {

          new_t[2] = array[c_id];
          c_id++;
        }
      }

      ArrayUtil.copyTuple(new_t, 0, 0, output, tupleCount, offset, tupleLength);
      tupleCount++;
    }

    return output;
  }

  public static int[] packTriple(
      int[] array,
      int offset, /*number of int to store page's metadata such as number of triple,
                    number of int consumed [num of Triple][num of ints]*/
      int pageLength) {

    int[] output = new int[pageLength];

    output[0] = array[0];

    int c_s = output[offset] = array[offset];
    int c_p = output[offset + 1] = array[offset + 1];
    output[offset + 2] = array[offset + 2];

    int c_id = 3 + (offset - 1);

    // array[0] contains the number of triple in the array
    for (int i = 1; i < array[0]; i++) {
      int[] t = ArrayUtil.getTupleAtPosition(array, i, offset, 3);

      if (t[0] == c_s) {
        if (t[1] == c_p) {
          // if the next triple has the same subject and ojbect -> store only object in the next
          // cell
          c_id++;
          output[c_id] = t[2];
        } else {
          // if the next triple has the same subject only -> update p and o
          // c_p = t[1];
          c_id++;
          output[c_id] = PUNC; // [s1 p1 o1][s1 p2 o2] -> [s1 p1 o1 ; p2 o2]

          c_id++;
          c_p = output[c_id] = t[1];

          c_id++;
          output[c_id] = t[2];
        }
      } else {
        // [s1 p1 o1] [s1 p1 o2] [s2 p1 o1] -> [s1 p1 o1 o2 . s2 p1 o1]
        c_id++;
        output[c_id] = DOT;

        c_id++;
        c_s = output[c_id] = t[0];

        c_id++;
        c_p = output[c_id] = t[1];

        c_id++;
        output[c_id] = t[2];
      }
    }

    output[1] = c_id;

    return output;
  }

  public static int computePackedInt(int[] array, int offset) {
    int count = 0;
    int numTuple = array[0];
    int c_s = 0;
    int c_p = 0;

    for (int i = 0; i < numTuple; i++) {
      if (array[i * 3 + offset] == c_s) {
        if (array[i * 3 + 1 + offset] == c_p) {
          count += 1;
        } else {
          c_p = array[i * 3 + 1 + offset];
          count += 3;
        }
      } else {
        c_s = array[i * 3 + offset];
        c_p = array[i * 3 + 1 + offset];

        count += 4;
      }
    }

    array[1] = count;
    return count;
  }
  // ===========================================================================================================//
  // Merge function
  //  public static int[] merge(
  //      int[] onDisk,
  //      int[] onBuffer,
  //      int offset,
  //      int tupleLength,
  //      int keyLength
  //  ){
  //    //total number of tuple after merge
  //    int mergedCount = onDisk[0] + onBuffer[0];
  //
  //    int onDiskCount   = onDisk[0];
  //    int onBufferCount = onBuffer[0];
  //
  //    //move all tuple onDisk to give a free space
  //    int move_offset = offset + onBufferCount * tupleLength;
  //    System.arraycopy(onDisk, offset, onDisk, move_offset, onDiskCount * tupleLength);
  //
  //    int onDiskPos = onBufferCount;
  //    int onBufferPos = 0;
  //    int onMergePos = 0;
  //
  //    int numDuplication = 0;
  //
  //    while (onBufferPos < onBufferCount && onDiskPos < mergedCount){
  //      int x = ArrayUtil.compare(onDisk, onDiskPos, offset, onBuffer, onBufferPos, offset,
  // tupleLength, keyLength);
  //
  //      //on Disk > on Buffer
  //      if (x == 1){
  //        ArrayUtil.copyTuple(onBuffer, onBufferPos, offset, onDisk, onMergePos, offset,
  // tupleLength );
  //        onBufferPos++;
  //        onMergePos++;
  //      } else {
  //        ArrayUtil.copyTuple(onDisk, onDiskPos, offset, onDisk, onMergePos, offset, tupleLength);
  //        onDiskPos++;
  //        onMergePos++;
  //
  //        if (x == 0){
  //          onBufferPos ++;
  //          numDuplication++;
  //        }
  //      }
  //    }
  //
  //    //move back
  //    int moveBackFrom = offset + onDiskPos * tupleLength;
  //    int moveBackTo = offset + (onDiskPos - numDuplication)*tupleLength;
  //    int length =  onDisk.length - moveBackFrom;  //(mergedCount - onBufferPos) * tupleLength;
  //
  //    System.arraycopy(onDisk, moveBackFrom, onDisk, moveBackTo, length);
  //
  //    if (onBufferPos < onBufferCount){
  //      int onBufferRemainFrom = offset + onBufferPos * tupleLength;
  //      int onDiskTo = offset + (mergedCount - onBufferCount + onBufferPos) * tupleLength;
  //      length = (onBufferCount - onBufferPos) * tupleLength;
  //      System.arraycopy(onBuffer, onBufferRemainFrom, onDisk, onDiskTo, length);
  //    }
  //
  //    onDisk[0] = mergedCount - numDuplication;
  //
  //    return onDisk;
  //  }

  private static int[] merge(
      int[] onDisk, int[] onBuffer, int offset, int tupleLength, int keyLength) {
    // total number of tuple after merge
    int mergedCount = onDisk[0] + onBuffer[0];

    int onDiskCount = onDisk[0];
    int onBufferCount = onBuffer[0];
    int[] merged = new int[mergedCount * tupleLength + offset];

    int onDiskPos = 0;
    int onBufferPos = 0;
    int onMergePos = 0;

    int numDuplication = 0;

    while (onBufferPos < onBufferCount && onDiskPos < onDiskCount) {
      int x =
          ArrayUtil.compare(
              onDisk, onDiskPos, offset, onBuffer, onBufferPos, offset, tupleLength, keyLength);

      // on Disk > on merged
      if (x == 1) {
        ArrayUtil.copyTuple(onBuffer, onBufferPos, offset, merged, onMergePos, offset, tupleLength);
        onBufferPos++;
        onMergePos++;
      } else {
        ArrayUtil.copyTuple(onDisk, onDiskPos, offset, merged, onMergePos, offset, tupleLength);
        onDiskPos++;
        onMergePos++;

        if (x == 0) {
          onBufferPos++;
          numDuplication++;
        }
      }
    }

    if (onBufferPos < onBufferCount) {
      int onBufferFrom = offset + onBufferPos * tupleLength;
      int onMergedTo = offset + onMergePos * tupleLength;
      int length = (onBufferCount - onBufferPos) * tupleLength;
      System.arraycopy(onBuffer, onBufferFrom, merged, onMergedTo, length);
    } else {
      if (onDiskPos < onDiskCount) {
        int onDiskFrom = offset + onDiskPos * tupleLength;
        int onMergedTo = offset + onMergePos * tupleLength;
        int length = (onDiskCount - onDiskPos) * tupleLength;
        System.arraycopy(onDisk, onDiskFrom, merged, onMergedTo, length);
      }
    }

    merged[0] = mergedCount - numDuplication;
    //merged[1] = computePackedInt(merged, offset);
    return merged;
  }

  public static int[] mergeTP(
      int[] onDisk, int[] onBuffer, int offset, int tupleLength, int keyLength){
    int[] merged = merge(onDisk, onBuffer, offset, tupleLength, keyLength);
          merged[1] = computePackedInt(merged, offset);
    return merged;
  }

  public static int[] mergeH(
      int[] onDisk, int[] onBuffer, int offset, int tupleLength, int keyLength){
    int[] merged = merge(onDisk, onBuffer, offset, tupleLength, keyLength);
    merged[1] = merged[0]*tupleLength + offset;
    return merged;
  }

  // -------------------------------------------------------------------------------------------------------------------
  public static void shiftLeft(
      int[] array, int recordCount, int position, int recordSize, int headSize) {
    int from = headSize + (position + 1) * recordSize;
    int to = headSize + position * recordSize;
    int size = (recordCount - position) * recordSize;

    System.arraycopy(array, from, array, to, size);
  }

  public static void shiftRight(
      int[] array, int recordCount, int position, int recordSize, int headSize) {
    int from = headSize + position * recordSize;
    int to = headSize + position * recordSize + recordSize;
    int size = (recordCount - position) * recordSize;

    System.arraycopy(array, from, array, to, size);
  }

  public static int[] get(int[] array, int recordCount, int[] key, int recordSize, int headSize) {
    int slot = findSlot(array, recordCount, key, recordSize, headSize);

    if (slot < 0) {
      return null;
    } else {
      return get(array, slot, recordSize, headSize);
    }
  }

  public static boolean insert(int[] array, int recordCount, int[] key, int[] value, int headSize) {
    int slot = findSlot(array, recordCount, key, key.length + value.length, headSize);

    if (slot > 0) {
      return false;
    } else {
      slot = -(slot + 1);

      shiftRight(array, recordCount, slot, key.length + value.length, headSize);

      int[] record = new int[key.length + value.length];

      System.arraycopy(key, 0, record, 0, key.length);

      System.arraycopy(value, 0, record, key.length, value.length);

      set(array, record, slot, headSize);

      return true;
    }
  }

  public static boolean remove(int[] array, int recordCount, int[] key, int[] value, int headSize) {
    int slot = findSlot(array, recordCount, key, key.length + value.length, headSize);

    if (slot < 0) {
      // the record is not inside block
      return false;
    } else {
      // do deleting the record
      shiftLeft(array, recordCount, slot, key.length + value.length, headSize);

      return true;
    }
  }

  public static int findSlot(
      int[] array, int recordCount, int[] key, int recordSize, int headSize) {
    int low = 0;
    int high = recordCount - 1;

    while (low <= high) {

      int mid = (low + high) >>> 1;

      int x = compareKey(array, key, mid, recordSize, headSize);

      if (x == 2) {
        low = mid + 1;
      } else {
        if (x == 1) {
          high = mid - 1;
        } else {
          return mid;
        }
      }
    }

    return -(low + 1);
  }

  public static int compareKey(int[] array, int[] key, int position, int recordSize, int headSize) {
    int pos = headSize + recordSize * position;

    for (int i = 0; i < key.length; i++) {
      int keyInt = key[i];

      int posInt = array[pos + i];

      if (keyInt == posInt) continue;
      else {
        return (keyInt < posInt) ? 1 : 2;
      }
    }

    return 0;
  }

  public static void set(int[] array, int[] record, int position, int headSize) {
    int to = position * record.length + headSize;

    System.arraycopy(record, 0, array, to, record.length);
  }

  public static int[] get(final int[] array, int offset, int tupleSize) {

    int[] out = new int[tupleSize];

    System.arraycopy(array, offset, out, 0, tupleSize);

    return out;
  }

  public static int[] get(int[] array, int position, int recordSize, int headSize) {
    int[] result = new int[recordSize];

    int from = headSize + recordSize * position;

    System.arraycopy(array, from, result, 0, recordSize);

    return result;
  }

  public static void println(int[] array, int headSize, int keySize, int valueSize) {
    // Print headSize
    int[] head = new int[headSize];

    System.arraycopy(array, 0, head, 0, headSize);

    System.out.println("HEAD: ");

    println(head);

    System.out.println("");

    for (int i = headSize; (i + keySize + valueSize) <= array.length; i = i + keySize + valueSize) {
      System.out.print("{");

      int[] key = new int[keySize];

      System.arraycopy(array, i, key, 0, keySize);

      int[] value = new int[valueSize];

      System.arraycopy(array, i + keySize, value, 0, valueSize);

      println(key);
      System.out.print(":");
      println(value);

      System.out.println("}");
    }

    System.out.println("");
  }

  public static void println1(int[] array, int headSize, int keySize, int valueSize) {
    // Print headSize
    int[] head = new int[headSize];

    System.arraycopy(array, 0, head, 0, headSize);

    System.out.println("HEAD: ");

    println(head);

    System.out.println("");

    int num = head[0];

    for (int i = headSize; (i + keySize + valueSize) < array.length; i = i + keySize + valueSize) {

      System.out.print("{");

      int[] key = new int[keySize];

      System.arraycopy(array, i, key, 0, keySize);

      int[] value = new int[valueSize];

      System.arraycopy(array, i + keySize, value, 0, valueSize);

      println(key);
      System.out.print(":");
      println(value);

      System.out.println("}");

      num--;

      if (num == 0) break;
    }

    System.out.println("");
  }

  public static void println(int[] array) {
    System.out.print("[");

    for (int i : array) {
      System.out.print(i + " ");
    }

    System.out.print("]");
  }

  public static void println(byte[] array) {
    System.out.print("[");

    for (byte i : array) {
      System.out.print(i + " ");
    }

    System.out.print("]");
  }

  public static int[] expandArray(int[] old, int expandedSize) {

    int[] newBuffer = new int[old.length + expandedSize];

    System.arraycopy(old, 0, newBuffer, 0, old.length);

    return newBuffer;
  }

  public static void main(String[] args) {

    int[] onBuffer = new int[] {5, 5, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 0, 0, 0};

    int[] onDisk =
        new int[] {8, 0, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 3, 3, 1, 1, 3, 1, 2, 4, 1, 1, 4, 1, 2};

    onDisk = packTriple(onDisk, 2, 10 * 3 + 2);

    ArrayUtil.println(onDisk);
    System.out.println();

    onDisk = unpackTriple(onDisk, 2, 3);

    //    onDisk = ArrayUtil.merge(onDisk, onBuffer, 2, 3,3);
    ArrayUtil.println(onDisk, 2, 3, 0);

    //    int[] ints = new int[]{1,1,0};
    //
    //    shiftRight_(ints,0, 0, 1,1,2);
    //    ArrayUtil.println(ints);

    //    int[] packed = ArrayUtil.packTriple(onDisk, 2, 512);
    //    ArrayUtil.println(packed);

    //    int[] unpack = ArrayUtil.unpackTriple(packed, 2, 3);
    //    ArrayUtil.println(unpack, 2, 3, 0);

    //    int[] source =
    //        new int[] {
    //          0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0,
    //             0, 0, 0
    //        };
    //
    //    int[] t1 = new int[]{0 , 0 , 0};
    //    ArrayUtil.insert2(onDisk, t1, 2,3,3);
    //    int[] t2 = new int[]{1 , 1 , 2};
    //    int[] t3 = new int[]{1 , 2 , 1};
    //    int[] t4 = new int[]{2 , 1 , 3};
    //    int[] t5 = new int[]{2 , 2 , 2};
    //    int[] t6 = new int[]{3 , 4 , 1};
    //    int[] t7 = new int[]{2 , 2 , 9};
    //    int[] t8 = new int[]{2 , 3 , 2};
    //    int[] t9 = new int[]{3 , 3 , 2};
    //    int[] t10 = new int[]{2 , 4 , 2};
    //
    //    ArrayUtil.insert2(source, t9, 2, 3, 3);
    //    ArrayUtil.insert2(source, t10, 2, 3, 3);
    //    ArrayUtil.insert2(source, t1, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t8, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t1, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t7, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t4, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t5, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t6, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t2, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //    ArrayUtil.insert2(source, t3, 2, 3, 3);
    ////    ArrayUtil.println(source, 2, 3, 0);
    //
    ////    ArrayUtil.remove2(source, t1, 2, 3,3);
    ////    ArrayUtil.remove2(source, t3, 2, 3,3);
    ////    ArrayUtil.remove2(source, t8, 2, 3,3);
    //    ArrayUtil.println(source, 2, 3, 0);
    //
    //
    //
  }
}
