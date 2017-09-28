package org.rdf4led.rdf.dictionary.vocab;


import static org.rdf4led.rdf.dictionary.vocab.RDF.UnknowRDF;

/**
 * insight.dev.noaa2rdf.vocabulary
 * <p>
 * TODO: Add class description
 * <p>
 * Author:  Anh Le-Tuan
 * <p>
 * Email:   anh.letuan@insight-centre.org
 * <p>
 * Date:  27/10/18.
 */
public class WGS84 implements Vocabulary {
    public static final String prefix =  "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static final byte c_prefix = VocabHandler.WGS84_;
    public static Vocabulary wgs84 = new WGS84();
    private WGS84() {}

    public static final byte Point  = 0;
    public static final byte lat    = 1;
    public static final byte lon    = 2;

    @Override
    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public byte getSuffix(String lexical) {
        if (lexical.contains("Point")) return (Point);
        if (lexical.contains("lat")) return (lat);
        if (lexical.contains("lon")) return (lon);
        return UnknowRDF;
    }

    @Override
    public String toLexical(byte suffix) {
        switch (suffix) {
            case Point:
                return prefix + "Point";
            case lat:
                return prefix + "lat";
            case lon:
                return prefix + "lon";
            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }
}
