package org.rdf4led.db.cache;

import java.util.Iterator;

/**
 * org.rdf4led.db.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 01/02/18.
 */
public interface Cache<K, V> {

  public V get(K key);

  public V put(K key, V value);

  public Iterator<K> keys();

  public Iterator<V> values();

  public long size();
}
