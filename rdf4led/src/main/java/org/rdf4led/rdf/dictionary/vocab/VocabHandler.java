package org.rdf4led.rdf.dictionary.vocab;


import org.rdf4led.rdf.dictionary.codec.RDFNodeType;

/**
 * VocabHandler.java
 *
 * <p>TODO: Add definition
 *
 * <p>Author : Anh Le-Tuan Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 27, 2015
 */
public class VocabHandler {
    public static final byte XSD_ = 1;
    public static final byte RDF_ = 2;
    public static final byte RDFS_ = 3;
    public static final byte OWL_ = 4;
    public static final byte FOAF_ = 5;
    public static final byte GEO_ = 6;
    public static final byte QUDT_1_1_ = 7;
    public static final byte QUDT_1_1_Unit_ = 8;
    public static final byte SOSA_ = 9;
    public static final byte SSN_ = 10;
    public static final byte WGS84_ = 11;
    //public static final byte CONST_ = 6;

    public static VocabHandler vocabHandler = new VocabHandler();

    private VocabHandler() {
    }

    public byte getPrefix(String lexical) {
        if (lexical.contains(FOAF.prefix)) return FOAF.c_prefix;
        if (lexical.contains(OWL.prefix)) return OWL.c_prefix;
        if (lexical.contains(XSD.prefix)) return XSD.c_prefix;
        if (lexical.contains(RDF.prefix)) return RDF.c_prefix;
        if (lexical.contains(RDFS.prefix)) return RDFS.c_prefix;
        if (lexical.contains(GEO.prefix)) return GEO.c_prefix;
        if (lexical.contains(QUDT_1_1.prefix)) return QUDT_1_1.c_prefix;
        if (lexical.contains(QUDT_1_1_Unit.prefix)) return QUDT_1_1_Unit.c_prefix;
        if (lexical.contains(SOSA.prefix)) return SOSA.c_prefix;
        if (lexical.contains(SSN.prefix)) return SSN.c_prefix;
        if (lexical.contains(WGS84.prefix)) return WGS84.c_prefix;
        return RDFNodeType.NULL;
        // throw new IllegalArgumentException("Can not recognise URI " + lexical);
    }

    public byte getSuffix(String lexical) {
        if (lexical.contains(FOAF.prefix)) return FOAF.foaf.getSuffix(lexical);
        if (lexical.contains(OWL.prefix)) return OWL.owl.getSuffix(lexical);
        if (lexical.contains(XSD.prefix)) return XSD.xsd.getSuffix(lexical);
        if (lexical.contains(RDF.prefix)) return RDF.rdf.getSuffix(lexical);
        if (lexical.contains(RDFS.prefix)) return RDFS.rdfs.getSuffix(lexical);
        if (lexical.contains(GEO.prefix)) return GEO.geo.getSuffix(lexical);
        if (lexical.contains(QUDT_1_1.prefix)) return QUDT_1_1.qudt_1_1.getSuffix(lexical);
        if (lexical.contains(QUDT_1_1_Unit.prefix)) return QUDT_1_1_Unit.qudt_1_1_unit.getSuffix(lexical);
        if (lexical.contains(SOSA.prefix)) return SOSA.sosa.getSuffix(lexical);
        if (lexical.contains(SSN.prefix)) return SSN.ssn.getSuffix(lexical);
        if (lexical.contains(WGS84.prefix)) return WGS84.wgs84.getSuffix(lexical);

        throw new IllegalArgumentException("Can not recognise URI " + lexical);
    }

    public String getLexical(byte prefix, byte suffix) {
        switch (prefix) {
            case RDF_:
                return RDF.rdf.toLexical(suffix);
            case OWL_:
                return OWL.owl.toLexical(suffix);
            case RDFS_:
                return RDFS.rdfs.toLexical(suffix);
            case FOAF_:
                return FOAF.foaf.toLexical(suffix);
            case XSD_:
                return XSD.xsd.toLexical(suffix);
            case QUDT_1_1_:
                return QUDT_1_1.qudt_1_1.toLexical(suffix);
            case QUDT_1_1_Unit_:
                return QUDT_1_1_Unit.qudt_1_1_unit.toLexical(suffix);
            case SOSA_:
                return SOSA.sosa.toLexical(suffix);
            case SSN_:
                return SSN.ssn.toLexical(suffix);
            case WGS84_:
                return WGS84.wgs84.toLexical(suffix);
            case GEO_:
                return GEO.geo.toLexical(suffix);
            default:
                throw new IllegalArgumentException("Can not recognise prefix: " + prefix);
        }
    }
}
