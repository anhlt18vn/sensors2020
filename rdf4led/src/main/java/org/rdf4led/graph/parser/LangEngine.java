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


import org.rdf4led.common.iterator.PeekIterator;

/**
 * TODO: LangEngine
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.
 *     <p>letuan@insight-centre.org
 *     <p>Date: 4 Jan 2015
 */
public class LangEngine {
  protected Tokenizer tokens;

  protected PeekIterator<Token> peekIter;

  protected LangEngine(Tokenizer tokens) {
    this.peekIter = new PeekIterator<>(tokens);
  }

  private Token tokenEOF = null;

  protected final boolean eof() {
    if (tokenEOF != null) {
      return true;
    }

    if (!moreTokens()) {
      tokenEOF = new Token();

      tokenEOF.setType(TokenType.EOF);

      return true;
    }

    return false;
  }

  protected final boolean moreTokens() {
    return peekIter.hasNext();
  }

  protected final Token nextToken() {
    if (eof()) {
      return tokenEOF;
    }

    Token t = peekIter.next();

    return t;
  }

  protected final Token peekToken() {
    if (eof()) return tokenEOF;

    return peekIter.peek();
  }
}
