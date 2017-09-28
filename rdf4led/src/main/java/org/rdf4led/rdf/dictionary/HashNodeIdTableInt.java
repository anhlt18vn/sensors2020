package org.rdf4led.rdf.dictionary;

import org.rdf4led.db.index.Cursor;
import org.rdf4led.db.index.LayerIndex;
import org.rdf4led.db.index.buffer.IndexT;

/**
 * org.rdf4led.org.rdf4led.rdf.dictionary
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  18/02/18.
 */
public class HashNodeIdTableInt implements HashNodeIdTable<Integer> {

    private LayerIndex index;

    public HashNodeIdTableInt(String storagePath) {
        index = new LayerIndex(storagePath, IndexT.Hash);
    }

    @Override
    public boolean put(Integer hash, Integer integer) {
        return index.add(new int[]{hash, integer});
    }

    @Override
    public Integer get(Integer hash) {
        Cursor cursor = index.searchCursor(new int[]{hash});
        int[] tuple = index.getTuple(cursor);
        return hash == tuple[0] ? tuple[1] : null;
    }

    public void sync() {
        index.sync();
    }
}
