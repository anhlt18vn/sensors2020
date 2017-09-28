package org.rdf4led.rdf.dictionary.codec;

// NB shifting is "mod 32" -- <<32 is a no-org.rdf4led.sparql.algebra.op (not a clear).
// http://mindprod.com/jgloss/masking.html

/** Utilities for manipulating a bit pattern which held in a 32 bit int */
public final class BitsInt {
  private BitsInt() {}

  private static int IntLen = Integer.SIZE;

  public static final int unpack(int bits, int start, int finish) {
    check(start, finish);
    if (finish == 0) return 0;
    // Remove top bits by moving up. Clear bottom bits by them moving down.
    return (bits << (IntLen - finish)) >>> ((IntLen - finish) + start);
  }

  public static final int pack(int bits, int value, int start, int finish) {
    check(start, finish);
    bits = clear$(bits, start, finish);
    bits = bits | (value << start);
    return bits;
  }

  public static final int unpack(String str, int startChar, int finishChar) {
    String s = str.substring(startChar, finishChar);
    return Integer.parseInt(s, 16);
  }

  public static final int set(int bits, int bitIndex) {
    check(bitIndex);
    return set$(bits, bitIndex);
  }

  public static final int set(int bits, int start, int finish) {
    check(start, finish);
    return set$(bits, start, finish);
  }

  public static final boolean test(int bits, boolean isSet, int bitIndex) {
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

  public static final int access(int bits, int start, int finish) {
    check(start, finish);
    return access$(bits, start, finish);
  }

  public static final int clear(int bits, int start, int finish) {
    check(start, finish);
    return clear$(bits, start, finish);
  }

  public static final int mask(int start, int finish) {
    check(start, finish);
    return mask$(start, finish);
  }

  public static final int maskZero(int start, int finish) {
    check(start, finish);
    return maskZero$(start, finish);
  }

  private static final int clear$(int bits, int start, int finish) {
    int mask = maskZero$(start, finish);
    bits = bits & mask;
    return bits;
  }

  private static final int set$(int bits, int bitIndex) {
    int mask = mask$(bitIndex);
    return bits | mask;
  }

  private static final int set$(int bits, int start, int finish) {
    int mask = mask$(start, finish);
    return bits | mask;
  }

  private static boolean test$(int bits, boolean isSet, int bitIndex) {
    return isSet == access$(bits, bitIndex);
  }

  private static boolean test$(int bits, int value, int start, int finish) {
    int v = access$(bits, start, finish);
    return v == value;
  }

  private static final boolean access$(int bits, int bitIndex) {
    int mask = mask$(bitIndex);
    return (bits & mask) != 0L;
  }

  private static final int access$(int bits, int start, int finish) {
    return ((bits << (IntLen - finish)) >>> (IntLen - finish + start)) << start;
  }

  private static final int mask$(int bitIndex) {
    return 1 << bitIndex;
  }

  private static final int mask$(int start, int finish) {

    if (finish == 0) return 0;

    int mask = -1;

    return mask << (IntLen - finish) >>> (IntLen - finish + start) << start;
  }

  private static final int maskZero$(int start, int finish) {

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

/*
 * (c) Copyright 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
