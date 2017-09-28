/// *
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
package org.rdf4led.query.sparql.algebra;

import org.rdf4led.query.sparql.algebra.op.*;

public class OpVisitorByTypeBase<Node> extends OpVisitorByType<Node> {
  @Override
  protected void visitN(OpN<Node> op) {}

  @Override
  protected void visit2(Op2<Node> op) {}

  @Override
  protected void visit1(Op1<Node> op) {}

  @Override
  protected void visit0(Op0<Node> op) {}

  //    @Override
  //    protected void visitExt(OpExt<Node> org.rdf4led.sparql.algebra.op) {}

  @Override
  protected void visitModifer(OpModifier<Node> opMod) {
    visit1(opMod);
  }
}
