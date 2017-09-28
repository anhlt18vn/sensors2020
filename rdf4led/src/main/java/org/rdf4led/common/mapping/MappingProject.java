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

import java.util.Collection;

public class MappingProject<Node> extends MappingProjectBase<Node> {

    final Collection<Node> projectionVars;

    public MappingProject(Collection<Node> vars, Mapping<Node> bind, VarChecker<Node> varChecker) {
        this(vars, bind, null, varChecker);
    }

    public MappingProject(Collection<Node> nodeVars, Mapping<Node> bind, Mapping<Node> parent, VarChecker<Node> varChecker) {
        super(bind, parent, varChecker);
        this.projectionVars = nodeVars;
    }

    @Override
    protected boolean accept(Node nodeVar) {
        return projectionVars.contains(nodeVar);
    }
}
