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

package org.rdf4led.common.mapping;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A muatable mapping from a name to a value such that we can create a tree of levels with higher
 * (earlier levels) being shared. Looking up a name is done by looking in the current level, then
 * trying the parent is not found.
 */
public class MappingHashMap<Node> implements MappingMap<Node> {
    HashMap<Node, Node> hashMap;

    public MappingHashMap() {
        hashMap = new HashMap<>();
    }

    private MappingHashMap(HashMap<Node, Node> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public Iterator<Node> vars() {
        return hashMap.keySet().iterator();
    }

    @Override
    public boolean contains(Node nodeVar) {
        return hashMap.containsKey(nodeVar);
    }

    @Override
    public Node getValue(Node nodeVar) {
        return hashMap.get(nodeVar);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public void add(Node nodeVar, Node node) {
        hashMap.put(nodeVar, node);
    }

    @Override
    public void remove(Node var, Node value) {
        hashMap.remove(var);
    }

    @Override
    public Mapping<Node> clone() {
        return new MappingHashMap<>((HashMap<Node, Node>) hashMap.clone());
    }

    @Override
    public void addAll(Mapping<Node> other) {
        throw new RuntimeException("add All is not supported ");
    }
}
