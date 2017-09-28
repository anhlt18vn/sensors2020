package org.rdf4led.db.index.buffer;


import org.rdf4led.db.index.PersistentLayer;

/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le-Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 09/02/18.
 */
public class KeyBufferEntry implements BufferEntry {

  private static KeyBufferEntry keyBufferEntry;

  public static BufferEntry createKeyBufferEntry(int[] key) {
    if (keyBufferEntry == null) {
      keyBufferEntry = new KeyBufferEntry(key);
    } else {
      keyBufferEntry.setKeyBufferEntry(key);
    }

    return keyBufferEntry;
  }

  // ==================================================================
  private int[] key;

  private KeyBufferEntry(int[] key) {
    this.key = key;
  }

  private void setKeyBufferEntry(int[] key) {
    this.key = key;
  }

  @Override
  public int[] getKey() {
    return key;
  }

  @Override
  public int getRId() {
    throw new UnsupportedOperationException(this.getClass().toString());
  }

  @Override
  public int getWId() {
    throw new UnsupportedOperationException(this.getClass().toString());
  }

  @Override
  public void setRId(int rId) {
    throw new UnsupportedOperationException(this.getClass().toString());
  }

  @Override
  public void setWId(int wId) {
    throw new UnsupportedOperationException(this.getClass().toString());
  }

  @Override
  public void read(PersistentLayer persistentLayer) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] serialize() {
    throw new UnsupportedOperationException(this.getClass().toString());
  }

  @Override
  public void log() {
    throw new UnsupportedOperationException(this.getClass().toString());
  }
}
