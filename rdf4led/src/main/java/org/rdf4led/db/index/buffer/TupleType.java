package org.rdf4led.db.index.buffer;

/**
 * org.rdf4led.db.index1.buffer
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  05/02/18.
 */
public enum TupleType {
  HASH(1,2),
  TRIPLE(3, 3);

  private final int keyLength;
  private final int tupleLength;

  private TupleType(int keyLength, int tupleLength){
    this.keyLength = keyLength;
    this.tupleLength = tupleLength;
  }

  public int getKeyLength(){
    return keyLength;
  }

  public int getTupleLength(){
    return tupleLength;
  }
}
