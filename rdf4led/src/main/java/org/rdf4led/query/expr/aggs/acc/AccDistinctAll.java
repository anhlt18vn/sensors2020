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
package org.rdf4led.query.expr.aggs.acc;

import org.rdf4led.common.mapping.Mapping;

import java.util.HashMap;

/**
 * AccDistinctAll.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>26 Oct 2015
 */
public abstract class AccDistinctAll<Node> implements Acc<Node> {
  private final HashMap<Mapping<Node>, Count> values;

  protected AccDistinctAll() {
    values = new HashMap<Mapping<Node>, Count>();
  }

  @Override
  public final synchronized boolean updateArrival(Mapping<Node> mapping) {
    if (values.containsKey(mapping)) {
      values.get(mapping).inc();

      return false;
    } else {
      values.put(mapping, new Count());
    }

    updateArrivalDistinct(mapping);

    return isUpdate();
  }

  @Override
  public final synchronized boolean updateExpiry(Mapping<Node> mapping) {
    if (values.containsKey(mapping)) {
      Count count = values.get(mapping);

      count.dec();

      if (count.getCount() == 0) {
        updateExpiryDistinct(mapping);

        return isUpdate();
      } else {
        return false;
      }
    } else {
      throw new RuntimeException("Expire non-arrival mapping  ???");
    }
  }

  protected abstract boolean isUpdate();

  protected abstract void updateArrivalDistinct(Mapping<Node> mapping);

  protected abstract void updateExpiryDistinct(Mapping<Node> mapping);

  public class Count {
    int count;

    public Count() {
      count = 1;
    }

    public int getCount() {
      return count;
    }

    public void inc() {
      count++;
    }

    public void dec() {
      count--;
    }
  }
}
