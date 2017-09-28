package org.rdf4led.db.cache;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class CacheImpl<K, V> extends LinkedHashMap<K, V> {

  private final DropHandler<K, V> dropHandler;

  private int maxEntries;


  public CacheImpl(int maxEntries){

    dropHandler = new DropHandler<>();

    this.maxEntries = maxEntries;
  }

  public CacheImpl<K, V> adjustCacheSize(int maxEntries){
    this.maxEntries = maxEntries;
    return this;
  }

  public V drop(){
    return dropHandler.getValue();
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest){

    boolean b = size() >= maxEntries;

    if (b) {
       //System.out.println(" detect from cache: " + MemoryManager.usedMemory());
       dropHandler.apply(eldest.getKey(), eldest.getValue());
     }

     return b;
  }

  private class DropHandler<K, V>{
    private K key;
    private V value;

    DropHandler(){
      value = null;
    }

    public K getKey(){
      return key;
    }

    public V getValue(){
      return value;
    }

    public void apply(K key, V value){
      this.key = key;
      this.value = value;
    }
  }


  //=====================================================================================================
  public static void main(String[] args){
    CacheImpl<Integer, Integer> cache = new CacheImpl<>(1000);

    for (int i = 0; i < 1000; i ++){
      cache.put(i,i);
    }

    for (int i = 1; i<100; i++){
      System.out.println(cache.drop());
    }
  }
}
