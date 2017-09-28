package org.rdf4led.db.index.buffer;

import org.rdf4led.common.ArrayUtil;

import java.util.Comparator;

/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le-Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 08/02/18.
 */
public class BufferEntryComparator implements Comparator<BufferEntry> {

  public static BufferEntryComparator comparator = new BufferEntryComparator();

  @Override
  public int compare(BufferEntry b0, BufferEntry b1) {
//    System.out.print("b0"); ArrayUtil.println(b0.getKey());
//    System.out.println();
//    System.out.print("b1");ArrayUtil.println(b1.getKey());
//    System.out.println();
    return ArrayUtil.compare(b0.getKey(), 0, 0, b1.getKey(), 0, 0, 0, b1.getKey().length);
  }
}