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

package org.rdf4led.query.expr;

import org.rdf4led.graph.parser.Chars;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.expr.function.ExprFunction1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ExprDigest.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public abstract class ExprDigest<Node> extends ExprFunction1<Node> {
  private final String digestName;

  private MessageDigest digestCache;

  public ExprDigest(Expr<Node> expr, String digestName, QueryContext<Node> queryContext) {
    super(expr, queryContext);

    this.digestName = digestName;

    try {
      digestCache = MessageDigest.getInstance(digestName);

    } catch (NoSuchAlgorithmException e) {

      throw new RuntimeException("Digest not provided in this Java system: " + digestName);
    }
  }

  private MessageDigest getDigest() {
    if (digestCache != null) {
      MessageDigest digest2 = null;

      try {

        digest2 = (MessageDigest) digestCache.clone();

        return digest2;

      } catch (CloneNotSupportedException ex) {
        // Can't clone - remove cache copy.
        digestCache = null;
      }
    }
    return createDigest();
  }

  private MessageDigest createDigest() {
    try {
      return MessageDigest.getInstance(digestName);
    } catch (Exception ex2) {
      throw new RuntimeException(ex2);
    }
  }

  Node lastSeen = null;

  Node lastCalc = null;

  @Override
  public Node eval(Node n) {
    if (lastSeen != null && lastSeen.equals(n)) {
      return lastCalc;
    }

    if (queryContext.dictionary().isLiteral(n)) // if ( ! n.isLiteral() )
    {
      throw new RuntimeException("Not a literal: " + n);
    }

    try {

      MessageDigest digest = getDigest();

      String x = queryContext.dictionary().getLexicalValue(n);

      byte b[] = x.getBytes("UTF-8");

      byte d[] = digest.digest(b);

      String y = asHexLC(d);

      Node result = queryContext.dictionary().createStringNode(y); // NodeValue.makeString(y) ;

      // Cache
      lastSeen = n;

      lastCalc = result;

      return result;

    } catch (Exception ex2) {
      throw new RuntimeException(ex2);
    }
  }

  public static String asHexLC(byte[] bytes) {
    return asHex(bytes, 0, bytes.length, Chars.hexDigitsLC);
  }

  public static String asHex(byte[] bytes, int start, int finish, char[] hexDigits) {
    StringBuilder sw = new StringBuilder();
    for (int i = start; i < finish; i++) {
      byte b = bytes[i];
      int hi = (b & 0xF0) >> 4;
      int lo = b & 0xF;
      // if ( i != start ) sw.append(' ') ;
      sw.append(hexDigits[hi]);
      sw.append(hexDigits[lo]);
    }
    return sw.toString();
  }
}
