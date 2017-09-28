package org.rdf4led.db.index.buffer;

import org.rdf4led.common.Config;

/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le-Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 08/02/18.
 */
public enum IndexT {
  Hash("HASH", 2, 1, Config.HASH_INDEX_MEM),
  SPO("SPO", 3, 3, Config.SPO_MEM),
  POS("POS", 3, 3, Config.POS_MEM),
  OSP("OSP", 3, 3, Config.OSP_MEM);

  private final String indexName;

  private final int tupleLength;
  private final int keyLength;
  private final int memory; //in MB

  private IndexT(String indexName, int tupleLength, int keyLength, int memory) {
    this.indexName = indexName;
    this.tupleLength = tupleLength;
    this.keyLength = keyLength;
    this.memory = memory;
  }

  public String getIndexName() {
    return indexName;
  }

  public String getBufferFile() {
    return this.indexName + ".idx";
  }

  public int getBlockEntryOffset() {
    return 2;
  }

  // [blkId][pageCount]
  public int getBlockEntryLength() {
    return this.getBlockEntryOffset() + this.getPageEntryLength() * Config.NUM_PAGE_IN_BLOCK;
  }

  public String getDataFile() {
    return indexName + ".dat";
  }

  // *[key][pageId][onBufferTC][onDiskTC][onBufferLength][packOnBufferLength][packOnDiskLength]
  public int getPageEntryLength() {
    return this.keyLength + 1 + 1 + 1 + 1 + 1 + 1;
  }

  public int rPageIdIndex() {
    return this.keyLength;
  }

  public int onBufferTCIndex() {
    return this.rPageIdIndex() + 1;
  }

  public int onDiskTCIndex() {
    return onBufferTCIndex() + 1;
  }

  public int onBufferLengthIndex() {
    return this.onDiskTCIndex() + 1;
  }

  public int onBufferPackLengthIndex() {
    return this.onBufferLengthIndex() + 1;
  }

  public int onDiskPackLengthIndex() {
    return onBufferPackLengthIndex() + 1;
  }

  public int getTupleLength() {
    return this.tupleLength;
  }

  public int getKeyLength() {
    return this.keyLength;
  }

  public int getMemory() {return this.memory/Config.BLK_SIZE;}
}
