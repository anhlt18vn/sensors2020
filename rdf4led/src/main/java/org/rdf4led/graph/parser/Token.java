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

package org.rdf4led.graph.parser;

/**
 * TODO: Token
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 4 Jan 2015
 */
public class Token {

  protected String tokenImage = " ";

  protected String tokenImage2 = " ";

  protected Token subToken = null;

  public int cntrlCode = 0;

  public Token() {}

  protected TokenType tokenType;

  public void setType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public TokenType getType() {
    return tokenType;
  }

  public boolean isEOF() {
    return tokenType == TokenType.EOF;
  }

  public String getImage() {
    return tokenImage;
  }

  public String getImage2() {
    return tokenImage2;
  }

  public void setImage(String tokenImage) {
    this.tokenImage = tokenImage;
  }

  public void setImage2(String tokenImage2) {
    this.tokenImage2 = tokenImage2;
  }

  public final Token setSubToken(Token subToken) {
    this.subToken = subToken;

    return this;
  }

  public final Token getSubToken() {
    return subToken;
  }

  public boolean isIRI() {
    return tokenType.equals(TokenType.IRI);
  }

  public boolean isInteger() {
    return tokenType.equals(TokenType.INTEGER);
  }

  public boolean isBNode() {
    return tokenType.equals(TokenType.BNODE);
  }

  @Override
  public String toString() {
    return tokenImage;
  }

  @Override
  public int hashCode() {
    return tokenType.hashCode() ^ tokenImage.hashCode() ^ tokenImage2.hashCode() ^ cntrlCode;
  }
}
