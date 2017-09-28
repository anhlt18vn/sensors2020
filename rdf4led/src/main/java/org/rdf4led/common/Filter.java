package org.rdf4led.common;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 06.02.20
 */
public interface Filter<T> {
    boolean accept(T item);
}
