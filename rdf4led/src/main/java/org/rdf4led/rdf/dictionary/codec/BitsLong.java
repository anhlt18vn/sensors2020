package org.rdf4led.rdf.dictionary.codec;

public final class BitsLong {
  private BitsLong() {}

  public static int IntLen = Long.SIZE;

  public static final long unpack(long bits, int start, int finish) {
    check(start, finish);
    if (finish == 0) return 0;
    // Remove top bits by moving up. Clear bottom bits by them moving down.
    return (bits << (IntLen - finish)) >>> ((IntLen - finish) + start);
  }

  public static final long pack(long bits, long value, int start, int finish) {
    check(start, finish);
    bits = clear$(bits, start, finish);
    bits = bits | (value << start);
    return bits;
  }

  public static final int unpack(String str, int startChar, int finishChar) {
    String s = str.substring(startChar, finishChar);
    return Integer.parseInt(s, 16);
  }

  public static final long set(long bits, int bitIndex) {
    check(bitIndex);
    return set$(bits, bitIndex);
  }

  public static final long set(long bits, int start, int finish) {
    check(start, finish);
    return set$(bits, start, finish);
  }

  public static final boolean test(long bits, boolean isSet, int bitIndex) {
    check(bitIndex);
    return test$(bits, isSet, bitIndex);
  }

  public static final boolean isSet(int bits, int bitIndex) {
    check(bitIndex);
    return test$(bits, true, bitIndex);
  }

  public static final boolean test(int bits, int value, int start, int finish) {
    check(start, finish);
    return test$(bits, value, start, finish);
  }

  public static final long access(long bits, int start, int finish) {
    check(start, finish);
    return access$(bits, start, finish);
  }

  public static final long clear(long bits, int start, int finish) {
    check(start, finish);
    return clear$(bits, start, finish);
  }

  public static final long mask(int start, int finish) {
    check(start, finish);
    return mask$(start, finish);
  }

  public static final long maskZero(int start, int finish) {
    check(start, finish);
    return maskZero$(start, finish);
  }

  private static final long clear$(long bits, int start, int finish) {
    long mask = maskZero$(start, finish);
    bits = bits & mask;
    return bits;
  }

  private static final long set$(long bits, int bitIndex) {
    long mask = mask$(bitIndex);
    return bits | mask;
  }

  private static final long set$(long bits, int start, int finish) {
    long mask = mask$(start, finish);
    return bits | mask;
  }

  private static boolean test$(long bits, boolean isSet, int bitIndex) {
    return isSet == access$(bits, bitIndex);
  }

  private static boolean test$(long bits, long value, int start, int finish) {
    long v = access$(bits, start, finish);
    return v == value;
  }

  private static final boolean access$(long bits, int bitIndex) {
    int mask = mask$(bitIndex);
    return (bits & mask) != 0L;
  }

  private static final long access$(long bits, int start, int finish) {
    return ((bits << (IntLen - finish)) >>> (IntLen - finish + start)) << start;
  }

  private static final int mask$(int bitIndex) {
    return 1 << bitIndex;
  }

  private static final long mask$(int start, int finish) {

    if (finish == 0) return 0;

    int mask = -1;

    return mask << (IntLen - finish) >>> (IntLen - finish + start) << start;
  }

  private static final long maskZero$(int start, int finish) {

    return ~mask$(start, finish);
  }

  private static final void check(int bitIndex) {
    if (bitIndex < 0 || bitIndex >= IntLen)
      throw new IllegalArgumentException("Illegal bit storage: " + bitIndex);
  }

  private static final void check(int start, int finish) {
    if (start < 0 || start >= IntLen) throw new IllegalArgumentException("Illegal start: " + start);
    if (finish < 0 || finish > IntLen)
      throw new IllegalArgumentException("Illegal finish: " + finish);
    if (start > finish)
      throw new IllegalArgumentException("Illegal range: (" + start + ", " + finish + ")");
  }
}
