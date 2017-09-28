package org.rdf4led;

import org.rdf4led.common.iterator.NullIterator;
import org.rdf4led.common.iterator.SingletonIterator;
import org.rdf4led.db.index.Cursor;
import org.rdf4led.db.index.Index;
import org.rdf4led.db.index.LayerIndex;
import org.rdf4led.db.index.buffer.IndexT;
import org.rdf4led.graph.Triple;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;

import java.util.Iterator;

public class TripleIndex {

    private Index<int[]> index;

    public TripleIndex(String pathToStorage, IndexT indexT) {
        index = new LayerIndex(pathToStorage, indexT);
    }

    public boolean add(int first, int second, int third) {
        // ---> to tuple
        index.add(new int[]{first, second, third});

        return false;
    }

    public boolean add(int[] triples) {
        return index.add(triples);
    }

    public boolean delete(int[] triples) {
        return index.add(triples);
    }

    public boolean delete(int first, int second, int third) {
        return delete(new int[]{first, second, third});
    }

    Index<int[]> getIndex() {
        return index;
    }

    public Iterator<Triple<Integer>> findMatch(int first, int second, int third) {
        Cursor cursor = index.searchCursor(new int[]{first, second, third});

        int[] result = index.getTuple(cursor);

        if ((first == result[0]) && (second == result[1]) && (third == result[2])) {
            return new SingletonIterator<Triple<Integer>>(new TripleInt(first, second, third));
        } else {
            return new NullIterator<>();
        }
    }

    public Iterator<Triple<Integer>> find(int first, int second, int third) {
        // search lower bound
        int[] lowerBound = new int[]{lowerBound(first), lowerBound(second), lowerBound(third)};

        Cursor from = index.searchCursor(lowerBound);

        // search upper bound
        int[] upperBound = new int[]{upperBound(first), upperBound(second), upperBound(third)};

        Cursor to = index.searchCursor(upperBound);

        return new RangeTripleIterator(this.index, from, to);
    }

    private int lowerBound(int node) {
        return node == RDFNodeType.ANY ? Integer.MIN_VALUE : node;
    }

    private int upperBound(int node) {
        return node == RDFNodeType.ANY ? Integer.MAX_VALUE : node;
    }

    public void sync() {
        index.sync();
    }
}
