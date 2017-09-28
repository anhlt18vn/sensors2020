package org.rdf4led.rdf.dictionary;

/**
 * Created by anhlt185 on 30/04/17.
 */
public interface NodeIdLexTable<Lexical, LexId> {
    public Lexical get(LexId lexId);

    public LexId put(Lexical lexical);

    public void sync();
}
