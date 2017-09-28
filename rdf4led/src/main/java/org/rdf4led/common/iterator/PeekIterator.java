/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rdf4led.common.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TODO: PeekIterator
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 24 Nov 2014
 */
public class PeekIterator<T> implements Iterator<T> {

  private final Iterator<T> iter;

  private boolean finished = false;

  private T slot;

  public PeekIterator(Iterator<T> iter) {
    this.iter = iter;
    fill();
  }

  private void fill() {
    if (finished) return;

    if (iter.hasNext()) {
      slot = iter.next();

    } else {

      finished = true;

      slot = null;
    }
  }

  public T peek() {
    if (finished) {
      throw new NoSuchElementException();
    }
    return slot;
  }

  @Override
  public boolean hasNext() {

    if (finished) return false;

    return true;
  }

  public T next() {
    if (finished) throw new NoSuchElementException();

    T x = slot;

    fill();

    return x;
  }

  public void remove() {}
}
