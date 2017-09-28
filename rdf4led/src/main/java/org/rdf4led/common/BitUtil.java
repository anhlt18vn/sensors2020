package org.rdf4led.common;

/**
 * org.rdf4led.utility
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le-Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 13/02/18.
 */
public class BitUtil {

  public static boolean isSet(int int_, int index) {
    return (int_ & (1 << index)) != 0;
  }

  public static int set(int int_, int index) {
    return int_ |= (1 << index);
  }

  public static int unSet(int int_, int index) {
    return int_ ^ (1 << index);
  }

  public static int firstBitSet(int int_) {
    for (int i = 0; i < 32; i++) {
      if (isSet(int_, i)) {
        return i;
      }
    }
    return 32;
  }


  public static boolean isSetL(long long_, int index) {
    return (long_ & (1 << index)) != 0;
  }

  public static long setL(long long_, int index) {
    return long_ |= (1 << index);
  }

  public static long unSetL(long long_, int index) {
    return long_ ^ (1 << index);
  }

  public static int firstBitSetL(long long_) {
    for (int i = 0; i < 32; i++) {
      if (isSetL(long_, i)) {
        return i;
      }
    }
    return 32;
  }

  private static void testIsSet() {
    int n = Integer.parseInt("11100", 2);
    System.out.println(isSet(n, 1));
  }

  private static void unSetTest() {
    int n = Integer.parseInt("11111", 2);
    n = unSet(n, 0);
    printBit(n);
  }

  private static void testSetBit() {
    int n = Integer.parseInt("10100", 2);
    n = set(n, 3);
    printBit(n);
  }

  public static void printBit(int in) {
    System.out.println((Integer.toBinaryString(in)));
  }

  public static void main(String[] args) {
    long long_ = Long.MAX_VALUE;


    for (int i=0; i<45; i++){
      long_ = unSetL(long_, i);
      System.out.println(firstBitSetL(long_));
      printBitL(long_);
    }

//    testIsSet();
//    testSetBit();
//    unSetTest();
//    testfirstBitUnset();
//    printBit(Integer.MAX_VALUE);
//    printBit(Integer.MIN_VALUE);
//    System.out.println(firstBitSet(Integer.MIN_VALUE));
  }

  public static void printBitL(long long_) {
    String s = Long.toBinaryString(long_);
    System.out.println(s);
  }

  private static void testfirstBitUnset() {
    int n = Integer.parseInt("00010000000", 2);
    System.out.println(firstBitSet(n));
  }
}
