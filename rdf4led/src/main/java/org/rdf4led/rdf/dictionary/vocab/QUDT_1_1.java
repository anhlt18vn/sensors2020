package org.rdf4led.rdf.dictionary.vocab;


import static org.rdf4led.rdf.dictionary.vocab.RDF.UnknowRDF;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 1/3/20
 * PROJECT: noaa-2-rdf
 */
public class QUDT_1_1 implements Vocabulary {
    public static final String prefix = "http://qudt.org/1.1/schema/qudt#";
    public static final byte c_prefix = VocabHandler.QUDT_1_1_;
    public static final byte QuantityValue = 0;
    public static final byte unit = 1;
    public static final byte numericValue = 2;
    public static Vocabulary qudt_1_1 = new QUDT_1_1();

    private QUDT_1_1() {
    }

    @Override
    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public byte getSuffix(String lexical) {
        if (lexical.contains("QuantityValue")) return (QuantityValue);
        if (lexical.contains("unit")) return (unit);
        if (lexical.contains("numericValue")) return (numericValue);
        return UnknowRDF;
    }

    @Override
    public String toLexical(byte suffix) {
        switch (suffix) {
            case QuantityValue:
                return prefix + "QuantityValue";
            case unit:
                return prefix + "unit";
            case numericValue:
                return prefix + "numericValue";
            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }
}
