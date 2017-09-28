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

import java.io.IOException;
import java.io.Reader;

/**
 * Buffering reader without the (hidden) sync overhead in BufferedReader
 *
 * @see java.io.BufferedReader
 */
public final class CharStreamBuffered extends CharStreamReader {
  // ARQ to v2.8.7
  // /*package*/ static final int CB_SIZE = 16 * 1024 ;
  /* package */ static final int CB_SIZE = 128 * 1024;

  private final char[] chars; // CharBuffer?
  private int buffLen = 0;
  private int idx = 0;

  private final Source source;

  public CharStreamBuffered(Reader r) {
    this(r, CB_SIZE);
  }

  public CharStreamBuffered(Reader r, int buffSize) {
    super();
    source = new SourceReader(r);
    chars = new char[buffSize];
  }

  // Local adapter/encapsulation
  private interface Source {
    int fill(char[] array);

    void close();
  }

  static final class SourceReader implements Source {
    final Reader reader;

    SourceReader(Reader r) {
      reader = r;
    }

    @Override
    public void close() {
      try {
        reader.close();
      } catch (IOException ex) {
        IO.exception(ex);
      }
    }

    @Override
    public int fill(char[] array) {
      try {
        return reader.read(array);
      } catch (IOException ex) {
        System.out.println(ex);
        IO.exception(ex);
        return -1;
      }
    }
  }

  // /** Faster?? f

  @Override
  public final int advance() {
    if (idx >= buffLen)
      // Points outside the array. Refill it
      fillArray();

    // Advance one character.
    if (buffLen >= 0) {
      char ch = chars[idx];
      // Advance the lookahead character
      idx++;
      return ch;
    } else
      // Buffer empty, end of stream.
      return IO.EOF;
  }

  private int fillArray() {
    int x = source.fill(chars);
    idx = 0;
    buffLen = x; // Maybe -1
    return x;
  }

  @Override
  public void closeStream() {
    source.close();
  }
}
