package org.rdf4led.rdf.dictionary.vocab;


/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 1/3/20
 * PROJECT: noaa-2-rdf
 */
public class QUDT_1_1_Unit implements Vocabulary {
    public static final String prefix  = "http://qudt.org/1.1/vocab/unit#";
    public static final byte c_prefix = VocabHandler.QUDT_1_1_Unit_;
    public static Vocabulary qudt_1_1_unit = new QUDT_1_1_Unit();
    private QUDT_1_1_Unit() {}

    public static final byte DegreeCelsius = 0;
    public static final byte Pascal = 1;
    public static final byte DegreeAngle = 2;
    public static final byte MeterPerSecond = 3;
    public static final byte Meter =  4;

    @Override
    public byte getPrefix() {
        return 0;
    }

    @Override
    public byte getSuffix(String lexical) {
        return 0;
    }

    @Override
    public String toLexical(byte suffix) {
        return null;
    }
}
