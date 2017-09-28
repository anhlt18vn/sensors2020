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

/**
 * Mappings are immutable, and are equal-by-value, but they have to be constructed somehow and this
 * interface captures that. Mappings should be created, then not changed, and returned as type
 * "Mapping".
 */
public interface MappingMap<Node> extends Mapping<Node> {

  /** Add a (var, value) pair - the value must not be null */
  public void add(Node nodeVar, Node node);

  /** Add all the (var, value) pairs from another binding */
  public void addAll(Mapping<Node> other);
}
