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

public abstract class P_Path2<Node> extends PathBase<Node> {
  private Path<Node> path1;

  private Path<Node> path2;

  protected P_Path2(Path<Node> p1, Path<Node> p2) {
    this.path1 = p1;

    this.path2 = p2;
  }

  public Path<Node> getLeft() {
    return path1;
  }

  public Path<Node> getRight() {
    return path2;
  }

  public abstract int hashSeed();

  @Override
  public final int hashCode() {
    return hashSeed() ^ path1.hashCode() ^ path2.hashCode();
  }

  protected final boolean equalsIso(P_Path2<Node> other) {
    return getLeft().equalTo(other.getLeft()) && getRight().equalTo(other.getRight());
  }
}
