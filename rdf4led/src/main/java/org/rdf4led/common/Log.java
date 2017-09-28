package org.rdf4led.common;

/**
 * org.rdf4led.utility
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 12/01/18.
 */
public class Log {
  static final int cacheHit_page_SPO = 1;
  static final int cacheHit_page_POS = 2;
  static final int cacheHit_page_OSP = 3;
  static final int cacheHit_page_H = 4;

  static final int cacheMiss_page_SPO = 5;
  static final int cacheMiss_page_POS = 6;
  static final int cacheMiss_page_OSP = 7;
  static final int cacheMiss_page_H = 8;

  static final int cacheHit_blk_SPO = 9;
  static final int cacheHit_blk_POS = 10;
  static final int cacheHit_blk_OSP = 11;
  static final int cacheHit_blk_H = 12;

  static final int cacheMiss_blk_SPO = 13;
  static final int cacheMiss_blk_POS = 14;
  static final int cacheMiss_blk_OSP = 15;
  static final int cacheMiss_blk_H = 16;

  static final int pageReadCount_SPO = 17;
  static final int pageReadCount_POS = 18;
  static final int pageReadCount_OSP = 19;
  static final int pageReadCount_H = 20;

  static final int pageWriteCount_SPO = 21;
  static final int pageWriteCount_POS = 22;
  static final int pageWriteCount_OSP = 23;
  static final int pageWriteCount_H = 24;

  static final int blkWriteCount_SPO = 25;
  static final int blkWriteCount_POS = 26;
  static final int blkWriteCount_OSP = 27;
  static final int blkWriteCount_H = 28;

  public static int[] log = new int[30];

  public static void countCacheMissPage(String index) {
    if (index == null) return;

    if (index.contains("SPO")) {
      log[cacheMiss_page_SPO]++;
    }
    if (index.contains("POS")) {
      log[cacheMiss_page_POS]++;
    }
    if (index.contains("OSP")) {
      log[cacheMiss_page_OSP]++;
    }
    if (index.contains("HASH")) {
      log[cacheMiss_page_H]++;
    }
  }

  public static void countCacheHitPage(String index) {
    if (index == null) return;

    if (index.contains("SPO")) {
      log[cacheHit_page_SPO]++;
    }
    if (index.contains("POS")) {
      log[cacheHit_page_POS]++;
    }
    if (index.contains("OSP")) {
      log[cacheHit_page_OSP]++;
    }
    if (index.contains("HASH")) {
      log[cacheHit_page_H]++;
    }
  }

  public static void countCacheMissBlk(String index) {
    if (index == null) return;
    if (index.contains("SPO")) {
      log[cacheMiss_blk_SPO]++;
    }
    if (index.contains("POS")) {
      log[cacheMiss_blk_POS]++;
    }
    if (index.contains("OSP")) {
      log[cacheMiss_blk_OSP]++;
    }
    if (index.contains("HASH")) {
      log[cacheMiss_blk_H]++;
    }
  }

  public static void countCacheHitBlk(String index) {
    if (index == null) return;
    if (index.contains("SPO")) {
      log[cacheHit_blk_SPO]++;
    }
    if (index.contains("POS")) {
      log[cacheHit_blk_POS]++;
    }
    if (index.contains("OSP")) {
      log[cacheHit_blk_OSP]++;
    }
    if (index.contains("HASH")) {
      log[cacheHit_blk_H]++;
    }
  }

  public static void countWriteBlock(String index) {
    if (index == null) return;

    if (index.contains("SPO")) {
      log[blkWriteCount_SPO]++;
    }
    if (index.contains("POS")) {
      log[blkWriteCount_POS]++;
    }
    if (index.contains("OSP")) {
      log[blkWriteCount_OSP]++;
    }
    if (index.contains("HASH")) {
      log[blkWriteCount_H]++;
    }
  }

  public static void countWritePage(String index) {
    if (index == null) return;

    if (index.contains("SPO")) {
      log[pageWriteCount_SPO]++;
    }
    if (index.contains("POS")) {
      log[pageWriteCount_POS]++;
    }
    if (index.contains("OSP")) {
      log[pageWriteCount_OSP]++;
    }
    if (index.contains("HASH")) {
      log[pageWriteCount_H]++;
    }
  }

  public static void countReadPage(String index) {
    if (index == null) return;

    if (index.contains("SPO")) {
      log[pageReadCount_SPO]++;
    }
    if (index.contains("POS")) {
      log[pageReadCount_POS]++;
    }
    if (index.contains("OSP")) {
      log[pageReadCount_OSP]++;
    }
    if (index.contains("HASH")) {
      log[pageReadCount_H]++;
    }
  }

  public static void print() {
    System.out.println("Cache page miss SPO: " + log[cacheMiss_page_SPO]);
    System.out.println("Cache page miss POS: " + log[cacheMiss_page_POS]);
    System.out.println("Cache page miss OSP: " + log[cacheMiss_page_OSP]);
    System.out.println("Cache page miss H  : " + log[cacheMiss_page_H]);

    System.out.println("Cache page hit SPO : " + log[cacheHit_page_SPO]);
    System.out.println("Cache page hit POS : " + log[cacheHit_page_POS]);
    System.out.println("Cache page hit OSP : " + log[cacheHit_page_OSP]);
    System.out.println("Cache page hit H   : " + log[cacheHit_page_H]);

    System.out.println("Cache block miss SPO: " + log[cacheMiss_blk_SPO]);
    System.out.println("Cache block miss POS: " + log[cacheMiss_blk_POS]);
    System.out.println("Cache block miss OSP: " + log[cacheMiss_blk_OSP]);
    System.out.println("Cache block miss H  : " + log[cacheMiss_blk_H]);

    System.out.println("Cache block hit SPO : " + log[cacheHit_blk_SPO]);
    System.out.println("Cache block hit POS : " + log[cacheHit_blk_POS]);
    System.out.println("Cache block hit OSP : " + log[cacheHit_blk_OSP]);
    System.out.println("Cache block hit H   : " + log[cacheHit_blk_H]);

    System.out.println("Cache page write SPO : " + log[pageWriteCount_SPO]);
    System.out.println("Cache page write POS : " + log[pageWriteCount_POS]);
    System.out.println("Cache page write OSP : " + log[pageWriteCount_OSP]);
    System.out.println("Cache page write H   : " + log[pageWriteCount_H]);

    System.out.println("Cache block write SPO : " + log[blkWriteCount_SPO]);
    System.out.println("Cache block write POS : " + log[blkWriteCount_POS]);
    System.out.println("Cache block write OSP : " + log[blkWriteCount_OSP]);
    System.out.println("Cache block write H   : " + log[blkWriteCount_H]);

  }

  public static String cacheHistoryToString() {
    String s = "";
    for (int i = 0; i < 29; i++) {
      s = s + " " + log[i];
    }

    s = s + "\n";

    return s;
  }

  public static void println(String index, int... array) {
    if (!index.contains("OPS")) return;

    System.out.print("[");

    for (int i : array) {
      System.out.print(i + " ");
    }

    System.out.print("]");
    System.out.println();
  }

  public static void reset()
  {
    log = new int[30];
  }

  public static class TimeLap {
    private long currentTime;

    public TimeLap() {
      currentTime = System.currentTimeMillis();
    }

    public long lap() {
      System.out.println("lap : " + (System.currentTimeMillis() - currentTime));

      return (System.currentTimeMillis() - currentTime);
    }
  }

  private static class Count {
    int i;

    Count() {
      i = 1;
    }

    public void count() {
      i++;
    }

    public int getCount() {
      return i;
    }
  }
}
