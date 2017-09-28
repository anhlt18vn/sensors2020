package org.rdf4led.db.index;


import org.rdf4led.common.FileUtil;
import org.rdf4led.common.Log;
import org.rdf4led.db.index.buffer.IndexT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import static org.rdf4led.common.Config.*;


/**
 * org.rdf4led.db.index1
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  07/02/18.
 */
public class PersistentLayer {

  protected FileChannel fileChannel;

  private IndexT indexT;

  String filePath;

  public PersistentLayer(String storagePath, IndexT indexT){
    this.indexT = indexT;
    this.filePath = storagePath + indexT.getDataFile();
    fileChannel = FileUtil.open(filePath);
  }

  synchronized
  public int[] readPage(int pageId){
    Log.countReadPage(indexT.getIndexName());

    ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
    try{
      fileChannel.position(pageId * PAGE_SIZE);
      fileChannel.read(byteBuffer);
    } catch (IOException e) {
      e.printStackTrace();
    }

    byteBuffer.rewind();
    int[] page = new int[PAGE_SIZE_INT];
    byteBuffer.asIntBuffer().get(page);
    return page;
  }

  synchronized
  public void writePage(int[] data, int pageId){
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(PAGE_SIZE);
    IntBuffer  intBuffer  = byteBuffer.asIntBuffer();
    intBuffer.rewind();
    intBuffer.put(data);

    byteBuffer.rewind();

    try {
      fileChannel.position(pageId*PAGE_SIZE);
      fileChannel.write(byteBuffer);
    } catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
  }


  synchronized
  public void writePage2(int[] data, int pageId){
    Log.countWritePage(indexT.getIndexName());

    int length = data.length > PAGE_SIZE_INT ? data.length * 4 : PAGE_SIZE;
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(length);
    IntBuffer  intBuffer  = byteBuffer.asIntBuffer();
    intBuffer.rewind();
    intBuffer.put(data);
    byteBuffer.rewind();
    byteBuffer.limit(PAGE_SIZE);

    try {
      fileChannel.position(pageId*PAGE_SIZE);
      fileChannel.write(byteBuffer);
    } catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
  }

  synchronized
  public void writeBlock(int[] data, int blockId){
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BLK_SIZE);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.rewind();
    intBuffer.put(data);

    byteBuffer.rewind();

    try{
      fileChannel.position(blockId*BLK_SIZE);
      fileChannel.write(byteBuffer);
    }catch (IOException e){
      throw new RuntimeException(e.toString());
    }
  }

  public void sync(){
    try {
      fileChannel.force(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PersistentLayer(String filePath)
  {
    this.filePath = filePath;
    this.fileChannel = FileUtil.open(filePath);
  }
  //==========================================================================================
  public static void main(String[] args){

    String filePath = "/home/anhlt185/fc.txt";

    PersistentLayer persistentLayer = new PersistentLayer(filePath);

    for (int i = 0; i < 10; i ++){
      persistentLayer.writeBlock(new int[]{1}, i);
    }

    long nanoTime = System.nanoTime();
    for (int step = 0; step < 100; step++){

      for (int i=0; i<NUM_PAGE_IN_BLOCK; i++){
        persistentLayer.writePage(new int[]{1}, i);
      }

    }
    long endTime = System.nanoTime();

    long delta = (endTime - nanoTime);//100/1000000;
    System.out.println("Test1 " + delta);

    nanoTime = System.nanoTime();
    for (int step = 0; step < 100; step++){


//      for (int i=0; i<NUM_PAGE_IN_BLOCK; i++){
//        persistentLayer.readPage( i);
//      }

      persistentLayer.writeBlock(new int[]{1}, 1);

    }
    endTime = System.nanoTime();

    delta = (endTime - nanoTime);//100/1000000;
    System.out.println("Test2 " + delta);

  }

}
