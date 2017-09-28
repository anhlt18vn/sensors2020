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

import org.rdf4led.common.iterator.NullIterator;

import java.util.Iterator;

/**
 * Special purpose binding for nothing. Surprisingly useful.
 */
public class Mapping0<Node> extends MappingBase<Node> {
    public Mapping0() {
        super(null);
    }

    public Mapping0(Mapping0<Node> parent) {
        super(parent);
    }

    /**
     * Iterate over all the names of variables.
     */
    @Override
    public Iterator<Node> vars1() {
        return new NullIterator<>();
    }

    @Override
    protected int size1() {
        return 0;
    }

    @Override
    protected boolean isEmpty1() {
        return true;
    }

    @Override
    public boolean contains1(Node nodeVar) {
        return false;
    }

    @Override
    public Node get1(Node nodeVar) {
        return null;
    }
}
