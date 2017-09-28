package org.rdf4led.db.block;

import org.rdf4led.common.ArrayUtil;
import org.rdf4led.common.Config;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * org.rdf4led.db.block
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 17/08/17.
 */
public abstract class PageAbstract implements Page {

    static int pageSize = Config.PAGE_SIZE; // set page size = 4k byte

    protected int[] pageData;

    protected boolean isDirty;

    protected boolean addTuple(int[] tuple, int tupleSize, int headSize) {
        if (isFull()) {
            throw new RuntimeException("Page is full");
        }

        int slot = ArrayUtil.findSlot(pageData, pageData[0], tuple, tupleSize, headSize);

        if (slot >= 0) {
            return false;
        } else {
            slot = -(slot + 1);

            ArrayUtil.shiftRight(pageData, pageData[0], slot, tupleSize, headSize);

            ArrayUtil.set(pageData, tuple, slot, headSize);

            pageData[0]++;

            isDirty = true;

            return true;
        }
    }

    protected boolean deleteTuple(int[] tuple, int tupleSize, int headSize) {
        if (pageData[0] == 0) return false;

        int slot = ArrayUtil.findSlot(pageData, pageData[0], tuple, tupleSize, headSize);

        if (slot < 0) {
            return false;
        } else {
            ArrayUtil.shiftLeft(pageData, pageData[0], slot, tupleSize, headSize);

            pageData[0]--;

            isDirty = true;
        }

        return false;
    }

    protected int searchTuple(int tuple[], int tupleSize, int headSize) {
        int order = ArrayUtil.findSlot(pageData, pageData[0], tuple, tupleSize, headSize);
        return order >= 0 ? order : -(order + 1) == 0 ? 0 : -(order + 1);
    }

    public int[] getTuple(int position, int tupleSize, int headSize) {
        return ArrayUtil.get(pageData, position, tupleSize, headSize);
    }

    @Override
    public int getTupleCount() {
        return pageData[0];
    }

    @Override
    public int[] getPageData() {
        return pageData;
    }

    @Override
    public void setPage(ByteBuffer byteBuffer) {
        this.pageData = new int[pageSize / 4];

        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        intBuffer.get(this.pageData);
    }

    protected Page split(int tupleSize, int headSize) {
        int[] newPageData = new int[pageSize / 4];

        int newTupleCount = pageData[0] / 2;

        int oldTupleCount = pageData[0] - newTupleCount;

        System.arraycopy(
                pageData,
                headSize + tupleSize * oldTupleCount,
                newPageData,
                headSize,
                newTupleCount * tupleSize);

        System.arraycopy(
                newPageData,
                headSize + tupleSize * newTupleCount,
                pageData,
                headSize + tupleSize * oldTupleCount,
                oldTupleCount * tupleSize);

        pageData[0] = oldTupleCount;

        newPageData[0] = newTupleCount;

        return createPage(newPageData);
    }

    public void setDirty() {
        this.isDirty = true;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    protected abstract Page createPage(int[] dataPage);
}
