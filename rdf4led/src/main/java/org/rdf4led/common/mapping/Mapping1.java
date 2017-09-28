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

import org.rdf4led.common.iterator.SingletonIterator;

import java.util.Iterator;

/**
 * Special purpose binding for adding just one name/value slot.
 */
public class Mapping1<Node> extends MappingBase<Node> {
    private final Node nodeVar;

    private final Node value;


    public Mapping1(Mapping<Node> parent, Node _varNode, Node _node) {
        super(parent);

        nodeVar = _varNode;

        value = _node;
    }


    @Override
    protected int size1() {
        return 1;
    }


    @Override
    protected boolean isEmpty1() {
        return false;
    }


    /**
     * Iterate over all the names of variables.
     */
    @Override
    public Iterator<Node> vars1() {
        return new SingletonIterator<>(nodeVar);
    }


    @Override
    public boolean contains1(Node var) {
        return nodeVar.equals(var);
    }


    @Override
    public Node get1(Node var) {
        if (var.equals(nodeVar)) return value;

        return null;
    }
}
