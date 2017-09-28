package org.rdf4led.rdf.dictionary.vocab;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 2/7/20
 * PROJECT: benchmark
 */
public class SOSA implements Vocabulary{
    public static Vocabulary sosa = new SOSA();
    private SOSA() {
    }
    public static final String prefix ="http://www.w3.org/ns/sosa/";
    public static final byte c_prefix = VocabHandler.SOSA_;

    public static final byte Platform = 0;
    public static final byte Observation = 1;
    public static final byte FeatureOfInterest = 2;
    public static final byte Sensor = 3;
    public static final byte Result = 4;
    public static final byte hosts = 5;
    public static final byte isHostedBy = 6;
    public static final byte isObservedBy = 7;
    public static final byte hasFeatureOfInterest = 8;
    public static final byte isFeatureOfInterestOf = 9;
    public static final byte madeBySensor = 10;
    public static final byte madeObservation = 11;
    public static final byte resultTime = 12;
    public static final byte hasResult = 13;
    public static final byte hasSimpleResult = 14;
    public static final byte observes = 15;
    public static final byte ObservableProperty = 16;

    @Override
    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public byte getSuffix(String lexical) {
        if (lexical.contains("Platform")) return (Platform);
        if (lexical.contains("Observation")) return (Observation);
        if (lexical.contains("FeatureOfInterest")) return (FeatureOfInterest);
        if (lexical.contains("Sensor")) return (Sensor);
        if (lexical.contains("Result")) return (Result);
        if (lexical.contains("hosts")) return (hosts);
        if (lexical.contains("isHostedBy")) return (isHostedBy);
        if (lexical.contains("isObservedBy")) return (isObservedBy);
        if (lexical.contains("hasFeatureOfInterest")) return (hasFeatureOfInterest);
        if (lexical.contains("isFeatureOfInterestOf")) return (isFeatureOfInterestOf);
        if (lexical.contains("madeBySensor")) return (madeBySensor);
        if (lexical.contains("madeObservation")) return (madeObservation);
        if (lexical.contains("resultTime")) return (resultTime);
        if (lexical.contains("hasResult")) return (hasResult);
        if (lexical.contains("hasSimpleResult")) return (hasSimpleResult);
        if (lexical.contains("observes")) return observes;
        if (lexical.contains("ObservableProperty")) return ObservableProperty;
        return error;
    }

    @Override
    public String toLexical(byte suffix) {
        switch (suffix) {
            case Platform:
                return prefix + "Platform";
            case Observation:
                return prefix + "Observation";
            case FeatureOfInterest:
                return prefix + "FeatureOfInterest";
            case Sensor:
                return prefix + "Sensor";
            case Result:
                return prefix + "Result";
            case hosts:
                return prefix + "hosts";
            case isHostedBy:
                return prefix + "isHostedBy";
            case isObservedBy:
                return prefix + "isObservedBy";
            case hasFeatureOfInterest:
                return prefix + "hasFeatureOfInterest";
            case isFeatureOfInterestOf:
                return prefix + "isFeatureOfInterestOf";
            case madeBySensor:
                return prefix + "madeBySensor";
            case madeObservation:
                return prefix + "madeObservation";
            case resultTime:
                return prefix + "resultTime";
            case hasSimpleResult:
                return prefix + "hasSimpleResult";
            case observes:
                return prefix + "observes";
            case ObservableProperty:
                return prefix + "ObservableProperty";
            case error:
                return prefix + "error";
            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }
}
