package org.rdf4led.db.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * org.rdf4led.db.cache
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  15/02/18.
 */
public class LRUQueue<T> implements Queue<T>{

  private LinkedList<T> queue;

  public LRUQueue(){
    queue = new LinkedList<T>();
  }

  @Override
  public int size() {
    return queue.size();
  }

  @Override
  public boolean isEmpty() {
    return queue.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return queue.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return queue.iterator();
  }

  @Override
  public Object[] toArray() {
    return queue.toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] t1s) {
    return queue.toArray(t1s);
  }

  @Override
  public boolean add(T t) {
    if (queue.contains(t)){
      queue.remove(t);
    }
    return queue.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return queue.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    return queue.containsAll(collection);
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    return queue.addAll(collection);
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    return queue.removeAll(collection);
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    return queue.retainAll(collection);
  }

  @Override
  public void clear() {
    queue.clear();
  }

  @Override
  public boolean offer(T t) {
    return false;
  }

  @Override
  public T remove() {
    return queue.remove();
  }

  @Override
  public T poll() {
    return queue.poll();
  }

  @Override
  public T element() {
    return queue.element();
  }

  @Override
  public T peek() {
    return queue.poll();
  }




  public static void main(String[] args){
    Queue<SimpleObject> queue = new LinkedList<>();

    SimpleObject o1 = new SimpleObject(1);
    SimpleObject o2 = new SimpleObject(2);
    SimpleObject o3 = new SimpleObject(3);
    SimpleObject o4 = new SimpleObject(4);
    SimpleObject o5 = new SimpleObject(5);


    queue.add(o1);
    queue.add(o2);
    queue.add(o3);
    queue.add(o4);


    queue.remove(o1);
    queue.add(o1);
    queue.remove(o5);
    queue.add(o5);

    SimpleObject first = queue.poll();
    while (first != null){
      System.out.println(first.getI());
      first = queue.poll();
    }
  }

  private static class SimpleObject{
    int i;

    public SimpleObject(int i){
      this.i = i;
    }

    private int getI(){
      return i;
    }
  }
}
