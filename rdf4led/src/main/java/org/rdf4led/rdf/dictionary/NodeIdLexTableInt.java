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

package org.rdf4led.rdf.dictionary;

import org.rdf4led.common.FileUtil;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * TODO: NodeIdLexTableInt
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 8 Dec 2014
 */
public class NodeIdLexTableInt implements NodeIdLexTable<String, Integer>, Closeable {
  private FileChannel meta, data;
  private int size, len;

  public NodeIdLexTableInt(String storagePath) {

    meta = FileUtil.open(storagePath, "lex_meta.idx", "rw");

    data = FileUtil.open(storagePath, "lex_data.dat", "rw");

    ByteBuffer bb = ByteBuffer.allocate(8);

    try {

      meta.read(bb, 0);

      size = bb.getInt(0);

      len = bb.getInt(4);

    } catch (IOException e) {

      size = 0;

      len = 0;
    }
  }

  public synchronized String get(Integer lexicalId) {
    if (lexicalId > size) {
      return "OUT_OF_INDEX";
    }

    int pos;

    int length;

    try {
      ByteBuffer buff = ByteBuffer.allocate(8);

      meta.read(buff, lexicalId * 8 + 8);

      pos = buff.getInt(0);

      length = buff.getInt(4);

      ByteBuffer buff__ = ByteBuffer.allocate(length);

      data.read(buff__, pos);

      buff__.rewind();

      String s = new String(buff__.array());

      return s;

    } catch (IOException e) {

      throw new RuntimeException(e.toString());
    }
  }

  public synchronized Integer put(String lexicalForm) {

    ByteBuffer buff = ByteBuffer.wrap(lexicalForm.getBytes());

    try {
      data.position(len);

      data.write(buff);

      ByteBuffer indexBuff = ByteBuffer.allocate(8);

      size++;

      indexBuff.putInt(0, len);

      indexBuff.putInt(4, buff.capacity());

      meta.write(indexBuff, (size) * 8);

      len = len + buff.capacity();

      ByteBuffer indexBuff__ = ByteBuffer.allocate(8);

      indexBuff__.putInt(0, size);

      indexBuff__.putInt(4, len);

      meta.write(indexBuff__, 0);

    } catch (IOException e) {
      throw new RuntimeException(e.toString());
    }

    return size - 1;
  }

  public void close() {
    sync();
    try {
      meta.close();
      data.close();
    } catch (IOException e) {
      // TODO
    }
  }

  public void sync() {
    try {
      data.force(false);
      meta.force(false);
    } catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
  }
}
