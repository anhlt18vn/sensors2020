package org.rdf4led.common.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Anh Le Tuan
 * @email anh.letuan@tu-berlin.de
 *        anh.letuan@insight-centre.org
 */
public class SingletonIterator<T> implements Iterator<T> {

    private T thing = null;

    private boolean yielded = false;


    public static <T> SingletonIterator<T> create(T thing) {
        return new SingletonIterator<T>(thing);
    }


    public SingletonIterator(T thing) {
        this.thing = thing;
    }


    @Override
    public boolean hasNext() {
        return !yielded;
    }


    @Override
    public T next() {
        if (yielded) {
            throw new NoSuchElementException("SingletonIterator.next");
        }

        yielded = true;

        return thing;
    }


    @Override
    public void remove() {
        throw new NoSuchElementException("SingletonIterator.remove");
    }


    public Iterator<T> iterator() {
        return this;
    }
}
