package org.rdf4led.common;

/**
 * org.rdf4led.db
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 21/08/17.
 */
public class Config {

  public static boolean R = false;

  public static int KB = 1024;

  public static int MB = 1024 * KB;

  public static int PAGE_SIZE = 4 * KB; // 512;        //;4 * KB; // number of bytes

  public static int EX_CHUNK = 512;// KB;

  public static int EX_CHUNK_INT = EX_CHUNK/4;

  public static int PAGE_SIZE_INT = PAGE_SIZE/4;

  public static int BLK_SIZE =  32 * PAGE_SIZE; // number of bytes

  public static int NUM_PAGE_IN_BLOCK = BLK_SIZE / PAGE_SIZE;

  public static int CH_FACTOR = 30;

  public static int TRIPLE_INDEX_MEM = 45 * MB;

  public static int SPO_MEM = 30 * MB;

  public static int POS_MEM = 50 * MB;

  public static int OSP_MEM = 80 * MB;

  public static int SPO_MEM_1 = 14 * MB ; //* MB;

  public static int POS_MEM_1 = 22 * MB ; //* MB;

  public static int OSP_MEM_1 = 30 * MB ;

  public static int HASH_INDEX_MEM = 20 * MB;

  public static int HASH_INDEX_MEM_1 = 10 * MB;

  public static int tCacheSize = TRIPLE_INDEX_MEM / 3 / PAGE_SIZE;

  public static int hCashSize = HASH_INDEX_MEM_1 / PAGE_SIZE;

  public static int numReleaseBlock = 1; // 5 % of cache size

  public static int numPageRelease = BLK_SIZE / PAGE_SIZE;

  public static int maxSizeCache = (140*MB)/3/BLK_SIZE;

  public static void setMaxMemory(int maxMemory){
    Config.maxSizeCache = maxMemory*MB/3/BLK_SIZE;
    Config.maxMem = maxMemory;
  }

  public static int maxMem = 80;

}
