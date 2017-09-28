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


import org.rdf4led.common.iterator.IteratorConcat;

import java.util.Iterator;

/**
 * Machinary encapsulating a mapping from a name to a value. The "parent" is a shared, immutable,
 * common set of bindings. An association of var/node must not override a setting in the parent.
 */
public abstract class MappingBase<Node> implements Mapping<Node> {
    protected Mapping<Node> parent;

    protected MappingBase(Mapping<Node> _parent) {
        parent = _parent;
    }


    public Mapping<Node> getParent() {
        return parent;
    }


    /**
     * Iterate over all the names of variables.
     */
    @Override
    public final Iterator<Node> vars() {
        Iterator<Node> iter = vars1();
        if (parent != null) iter = IteratorConcat.concat(parent.vars(), iter);
        return iter;
    }


    protected abstract Iterator<Node> vars1();


    @Override
    public final int size() {
        int x = size1();

        if (parent != null) x = x + parent.size();

        return x;
    }


    protected abstract int size1();


    @Override
    public boolean isEmpty() {
        if (!isEmpty1()) return false;

        if (parent == null) return true;

        return parent.isEmpty();
    }

    // { return size() == 0 ; }


    protected abstract boolean isEmpty1();


    /**
     * Test whether a name is bound to some object
     */
    @Override
    public boolean contains(Node var) {
        if (contains1(var)) return true;

        if (parent == null) return false;

        return parent.contains(var);
    }


    protected abstract boolean contains1(Node var);


    /**
     * Return the object bound to a name, or null
     */
    @Override
    public final Node getValue(Node var) {
        Node node = get1(var);

        if (node != null) return node;

        if (parent == null) return null;

        return parent.getValue(var);
    }


    protected abstract Node get1(Node var);


    @Override
    public int hashCode() {
        return hashCode(this);
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof Mapping)) return false;

        Mapping<Node> binding = (Mapping<Node>) other;

        if (this == binding) return true;

        if (this.size() != binding.size()) return false;

        for (Iterator<Node> iter1 = this.vars(); iter1.hasNext(); ) {
            Node var = iter1.next();

            Node node1 = this.getValue(var);

            Node node2 = binding.getValue(var);

            if (!(node1.equals(node2))) return false;
        }
        return true;
    }


    // Not everything derives from MappingBase.
    public int hashCode(Mapping<Node> bind) {
        int hash = 0xC0;

        for (Iterator<Node> iter = bind.vars(); iter.hasNext(); ) {

            Node var = iter.next();

            Node node = bind.getValue(var);

            hash ^= var.hashCode();

            hash ^= node.hashCode();
        }
        return hash;
    }


    @Override
    public void add(Node var, Node value) {
    }


    @Override
    public void remove(Node var, Node value) {
    }


    @Override
    public Mapping<Node> clone() {
        return null;
    }
}
