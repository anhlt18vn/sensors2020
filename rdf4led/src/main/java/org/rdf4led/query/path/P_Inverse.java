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

package org.rdf4led.query.path;

public class P_Inverse<Node> extends P_Path1<Node> {
  public P_Inverse(Path<Node> p) {
    super(p);
  }

  @Override
  public void visit(PathVisitor<Node> visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equalTo(Path<Node> path2) {
    if (!(path2 instanceof P_Inverse)) {
      return false;
    }

    P_Inverse<Node> other = (P_Inverse<Node>) path2;

    return getSubPath().equalTo(other.getSubPath());
  }

  @Override
  public int hashCode() {
    return getSubPath().hashCode() ^ hashInverse;
  }
}
