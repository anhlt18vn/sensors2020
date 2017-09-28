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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MappingProjectBase<Node> extends MappingBase<Node> {

    private Mapping<Node> binding;

    private List<Node> actualVars = null;

    private VarChecker<Node> varChecker;

    public MappingProjectBase(Mapping<Node> bind, VarChecker<Node> varChecker) {
        this(bind, null, varChecker);
        this.varChecker = varChecker;
    }

    public MappingProjectBase(Mapping<Node> bind, Mapping<Node> parent, VarChecker<Node> varChecker) {
        super(parent);
        binding = bind;
    }

    protected boolean accept(Node nodeVar) {
        return varChecker.confirmCheck(nodeVar);
    }

    @Override
    protected boolean contains1(Node nodeVar) {
        return accept(nodeVar) && binding.contains(nodeVar);
    }

    @Override
    protected Node get1(Node nodeVar) {
        if (!accept(nodeVar)) return null;

        return binding.getValue(nodeVar);
    }

    @Override
    protected Iterator<Node> vars1() {
        return actualVars().iterator();
    }

    private List<Node> actualVars() {
        if (actualVars == null) {
            actualVars = new ArrayList<Node>();

            Iterator<Node> iter = binding.vars();

            for (; iter.hasNext(); ) {
                Node nodeVar = iter.next();

                if (accept(nodeVar)) actualVars.add(nodeVar);
            }
        }
        return actualVars;
    }

    @Override
    protected int size1() {
        return actualVars().size();
    }

    // NB is the projection and the binding don't overlap it is also empty.
    @Override
    protected boolean isEmpty1() {

        if (binding.isEmpty()) return true;

        if (size1() == 0) return true;

        return false;
    }


}
