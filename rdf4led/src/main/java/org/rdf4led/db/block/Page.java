package org.rdf4led.db.block;

import java.nio.ByteBuffer;

/**
 * org.rdf4led.db.block
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 19/08/17.
 */
public interface Page {
    public boolean addTuple(int[] tuple);

    public boolean deleteTuple(int[] tuple);

    public int searchTuple(int[] tuple);

    public int[] getTuple(int position);

    public int[] getPageIndex();

    public int[] getPageData();

    public int getTupleCount();

    public void setPage(ByteBuffer byteBuffer);

    public boolean isFull();

    public void setDirty();

    public boolean isDirty();

    public Page split();

    public ByteBuffer toByteBuffer();
}
