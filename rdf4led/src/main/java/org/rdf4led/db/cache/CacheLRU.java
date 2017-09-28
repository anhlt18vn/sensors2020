package org.rdf4led.db.cache;

import java.util.Iterator;

/**
 * org.rdf4led.db.cache
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le_Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  01/02/18.
 */
public class CacheLRU<K, V> implements Cache<K, V> {

  private int maxSize;

  private CacheImpl<K, V> cache;

  public CacheLRU(int maxSize)
  {
    this.maxSize = maxSize;

    cache = new CacheImpl<>(maxSize);
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  @Override
  public V put(K key, V value) {
    cache.put(key, value);

    return cache.drop();
  }

  @Override
  public Iterator<K> keys() {
    return cache.keySet().iterator();
  }

  @Override
  public Iterator<V> values() {
    return cache.values().iterator();
  }

  @Override
  public long size() {
    return cache.size();
  }
}
