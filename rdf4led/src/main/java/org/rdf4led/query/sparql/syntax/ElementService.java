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

package org.rdf4led.query.sparql.syntax;

/** A SERVICE pattern - access a remote SPARQL service. */
public class ElementService<Node> extends Element<Node> {
  private final Node serviceNode;

  private final Element<Node> element;

  private final boolean silent;

  public ElementService(Node serviceURI, Element<Node> el) {
    this((serviceURI), el, false);
  }

  // Variable?
  public ElementService(Node n, Element<Node> el, boolean silent) {
    //      if ( ! exeContext.isURI(n) && ! exeContext.isVar(n) )
    //      {
    //        	Log.fatal(this, "Must be a URI (or variable which will be bound) for a service
    // endpoint") ;
    //      }

    this.serviceNode = n;

    this.element = el;

    this.silent = silent;
  }

  public Element<Node> getElement() {
    return element;
  }

  public Node getServiceNode() {
    return serviceNode;
  }

  public Node getServiceURI() {
    return serviceNode;
  }

  public boolean getSilent() {
    return silent;
  }

  @Override
  public int hashCode() {
    return serviceNode.hashCode() ^ element.hashCode();
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    if (!(el2 instanceof ElementService)) {
      return false;
    }

    ElementService<Node> service = (ElementService<Node>) el2;

    if (!serviceNode.equals(service.serviceNode)) {
      return false;
    }

    if (service.getSilent() != getSilent()) {
      return false;
    }

    if (!this.getElement().equalTo(service.getElement())) {
      return false;
    }

    return true;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }
}
