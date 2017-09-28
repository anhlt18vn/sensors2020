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

package org.rdf4led.query;


import org.rdf4led.graph.Quad;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A class whose purpose is to give a name to a collection of quads
 */
public class QuadPattern<Node> implements Iterable<Quad<Node>> {
    private List<Quad<Node>> quads = new ArrayList<Quad<Node>>();

    public QuadPattern() {
    }

    public QuadPattern(QuadPattern<Node> other) {
        quads.addAll(other.quads);
    }

    public void add(Quad<Node> q) {
        quads.add(q);
    }

    public void addAll(QuadPattern<Node> other) {
        quads.addAll(other.quads);
    }

    public void add(int i, Quad<Node> q) {
        quads.add(i, q);
    }

    public Quad<Node> get(int i) {
        return quads.get(i);
    }

    @Override
    public ListIterator<Quad<Node>> iterator() {
        return quads.listIterator();
    }

    public int size() {
        return quads.size();
    }

    public boolean isEmpty() {
        return quads.isEmpty();
    }

    public List<Quad<Node>> getList() {
        return quads;
    }

    @Override
    public int hashCode() {
        return quads.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof QuadPattern)) {
            return false;
        }

        QuadPattern<Node> bp = (QuadPattern<Node>) other;

        return quads.equals(bp.quads);
    }

}
