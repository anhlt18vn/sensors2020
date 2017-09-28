package org.rdf4led.rdf.dictionary;


/**
 * Created by anhlt185 on 30/04/17.
 */
public interface HashNodeIdTable<TNode> {

    public boolean put(TNode hash, TNode node);

    public TNode get(TNode hash);

    public void sync();
}
