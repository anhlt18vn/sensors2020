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

import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.Config;
import org.rdf4led.common.FileUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

/**
 * TODO: NodeIdLexTableInt
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 8 Dec 2014
 */
public class NodeIdLexTableIntBuffer
    implements NodeIdLexTable<String, Integer>, Closeable{
  private FileChannel metaChannel, dataChannel;
  private int size, len;

  private ByteBuffer data_buffer;

  private int lastDataBlkId;

  private String storagePath;

  private int[] meta;

  public NodeIdLexTableIntBuffer(String storagePath) {
    this.storagePath = storagePath;

    metaChannel = FileUtil.open(this.storagePath, "lex_meta.idx", "rw");

    dataChannel = FileUtil.open(this.storagePath, "lex_data.dat", "rw");

    data_buffer = ByteBuffer.allocate(Config.BLK_SIZE);

    try {
      if (metaChannel.size() != 0) {
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) metaChannel.size());

        metaChannel.read(byteBuffer);

        byteBuffer.rewind();

        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        meta = new int[intBuffer.capacity()];

        intBuffer.get(meta);

        size = meta[0];

        len = meta[1];

        lastDataBlkId = meta[2];

        dataChannel.read(data_buffer, lastDataBlkId * Config.BLK_SIZE);

        data_buffer.position(len % Config.BLK_SIZE);

      } else {
        meta = new int[1024];

        lastDataBlkId = 0;
      }
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  public synchronized String get(Integer lexicalId) {
    if (lexicalId > size) {
      return "OUT_OF_INDEX";
    }

    int pos;

    int length;

    try {
      pos = meta[lexicalId * 2 + 3];

      length = meta[lexicalId * 2 + 4];

      ByteBuffer byteBuffer = ByteBuffer.allocate(length);

      openFileChannelIfClosed();

      dataChannel.read(byteBuffer, pos);

      byteBuffer.rewind();

      String s = new String(byteBuffer.array());

      return s;

    } catch (IOException e) {

      throw new RuntimeException(e.toString());
    }
  }

  public synchronized Integer put(String lexicalForm) {
    if (lexicalForm.length() == 0) return null;

    try {

      byte[] lexicalInByteArray = lexicalForm.getBytes();

      if (data_buffer.remaining() > lexicalInByteArray.length) {
        data_buffer.put(lexicalInByteArray);
      } else {
        data_buffer = addCommitPush(data_buffer, lexicalInByteArray, lastDataBlkId);

        lastDataBlkId++;
      }

      // write meta
      if (meta.length <= size * 2 + 10) meta = ArrayUtil.expandArray(meta, 1024);

      meta[size * 2 + 3] = len;

      meta[size * 2 + 4] = lexicalInByteArray.length;

      len = len + lexicalInByteArray.length;

      size++;

      meta[0] = size;

      meta[1] = len;

      meta[2] = lastDataBlkId;
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }

    return size - 1;
  }

  private ByteBuffer addCommitPush(ByteBuffer byteBuffer, byte[] data, int blkId) {
    int c = 0;

    while (byteBuffer.remaining() != 0) {
      byteBuffer.put(data[c]);

      c++;
    }

    resetBuffer(byteBuffer, blkId);

    while (c < data.length) {
      byteBuffer.put(data[c]);

      c++;
    }

    return byteBuffer;
  }

  private void resetBuffer(ByteBuffer byteBuffer, int id) {
    openFileChannelIfClosed();

    try {
      byteBuffer.rewind();

      dataChannel.write(byteBuffer, id * Config.BLK_SIZE);

//      dataChannel.force(false);

//      dataChannel.close();

      byteBuffer.clear();
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  private void openFileChannelIfClosed() {
    if (!metaChannel.isOpen()) {
      metaChannel = FileUtil.open(this.storagePath, "lex_meta.idx", "rw");
    }

    if (!dataChannel.isOpen()) {
      dataChannel = FileUtil.open(this.storagePath, "lex_data.dat", "rw");
    }
  }

  public void sync() {
    try {
      openFileChannelIfClosed();

      data_buffer.rewind();

      dataChannel.write(data_buffer, lastDataBlkId * Config.BLK_SIZE);

      dataChannel.force(false);

      dataChannel.close();

      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(meta.length * 4);

      IntBuffer intBuffer = byteBuffer.asIntBuffer();

      intBuffer.put(meta);

      byteBuffer.rewind();

      metaChannel.write(byteBuffer, 0);

      metaChannel.force(false);

      metaChannel.close();

    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  @Override
  public void close() throws IOException {
    sync();
  }

  private void print(byte[] byteArray) {
    for (byte b : byteArray) {
      System.out.print(b + " ");
    }
    System.out.println();
  }

  public static void main(String[] args) {

    NodeIdLexTableIntBuffer table = new NodeIdLexTableIntBuffer("/home/anhlt185/");

    File file = new File("/home/anhlt185/experiment/data/1.nt");

    int i = 0;
    try {
      FileReader reader = new FileReader(file);

      BufferedReader bufReader = new BufferedReader(reader);

      String line = bufReader.readLine();

      while (line != null) {
        // System.out.println(table.put(line) + "  " + line);

        table.put(line);

        line = bufReader.readLine();

        i++;

        if (i == 6000) break;
      }

      table.sync();

      System.out.println(
          "=====================================================================================");

      table = new NodeIdLexTableIntBuffer("/home/anhlt185/");

      for (i = 0; i < 10; i++) {
        System.out.println(i + "" + table.get(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
