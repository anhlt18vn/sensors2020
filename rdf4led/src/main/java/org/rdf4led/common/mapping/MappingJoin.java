package org.rdf4led.common.mapping;

import java.util.HashMap;
import java.util.Iterator;

/**
 * org.rdf4led.utility.data
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 09/10/17.
 */
public class MappingJoin implements Mapping<Integer> {
    HashMap<Integer, Integer> varMap;
    HashMap<Integer, Integer> data;

    public MappingJoin() {
        varMap = new HashMap<>();

        data = new HashMap<>();
    }

    @Override
    public Iterator<Integer> vars() {
        return data.keySet().iterator();
    }

    @Override
    public boolean contains(Integer nodeVar) {
        return data.containsKey(nodeVar);
    }

    @Override
    public Integer getValue(Integer nodeVar) {
        return data.get(nodeVar);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public void add(Integer var, Integer value) {
        Integer count = varMap.get(var);

        if (count == null) {
            varMap.put(var, 0);
            data.put(var, value);
        } else {
            count++;
            varMap.put(var, count);
        }
    }

    @Override
    public void remove(Integer var, Integer value) {
        Integer count = varMap.get(var);
        if (count == null) {
            throw new RuntimeException(var + " : " + value + " is not in this mapping");
        } else {
            if (count == 0) {
                varMap.remove(var);
                data.remove(var);
            } else {
                count--;
                varMap.put(var, count);
            }
        }
    }

    @Override
    public Mapping<Integer> clone() {
        return null;
    }

}
