package org.rdf4led.rdf.dictionary.vocab;

import static org.rdf4led.rdf.dictionary.vocab.RDF.UnknowRDF;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 2/7/20
 * PROJECT: benchmark
 */
public class GEO implements Vocabulary {
    public static final String prefix  ="http://www.geonames.org/ontology#";
    public static final byte c_prefix = VocabHandler.GEO_;

    public static final byte countryCode   = 0;
    public static final byte name      = 1;
    public static final byte hasLocation  = 2;

    public static Vocabulary geo = new GEO();
    private GEO() {
    }

    @Override
    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public byte getSuffix(String lexical) {
        if (lexical.contains("countryCode")) return (countryCode);
        if (lexical.contains("name")) return (name);
        if (lexical.contains("hasLocation")) return (hasLocation);
        return UnknowRDF;
    }

    @Override
    public String toLexical(byte suffix) {
        switch (suffix) {
            case countryCode:
                return prefix + "countryCode";
            case name:
                return prefix + "name";
            case hasLocation:
                return prefix + "hasLocation";
            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }
}
