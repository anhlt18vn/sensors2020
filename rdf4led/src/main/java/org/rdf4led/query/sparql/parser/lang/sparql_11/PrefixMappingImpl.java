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

package org.rdf4led.query.sparql.parser.lang.sparql_11;

import org.rdf4led.rdf.dictionary.PrefixMapping;

import java.util.HashMap;

/**
 * TODO: PrefixMap
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 31 Jan 2015
 */
public class PrefixMappingImpl implements PrefixMapping<String, String> {

  private HashMap<String, String> prefixMap;

  public PrefixMappingImpl() {
    prefixMap = new HashMap<String, String>();
  }

  @Override
  /** */
  public PrefixMapping<String, String> addPrefix(String prefix, String iri) {

    prefixMap.put(prefix, iri);

    return this;
  }

  @Override
  /** */
  public String getURI(String prefix) {
    String s = prefixMap.get(prefix);

    return s;
  }

  @Override
  /** */
  public PrefixMapping<String, String> removePrefix(String prefix) {
    return null;
  }

  @Override
  /** */
  public String getPrefix(String uri) {
    // TODO Auto-generated method stub
    return null;
  }
}
