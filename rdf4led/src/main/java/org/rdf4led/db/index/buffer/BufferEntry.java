package org.rdf4led.db.index.buffer;


import org.rdf4led.db.index.PersistentLayer;

/**
 * org.rdf4led.db.index1.buffer
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  09/02/18.
 */
public interface BufferEntry {

  public int[] getKey();

  public int getRId();

  public int getWId();

  public void setRId(int rId);

  public void setWId(int wId);

  public void read(PersistentLayer persistentLayer);

  public int[] serialize();

  public void log();
}
