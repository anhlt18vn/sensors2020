package org.rdf4led.rdf.dictionary.vocab;

import static org.rdf4led.rdf.dictionary.vocab.RDF.UnknowRDF;

public class SSN implements Vocabulary {
    public static final String prefix = "http://www.w3.org/ns/ssn/";
    public static final byte c_prefix = VocabHandler.SSN_;
    public static Vocabulary ssn = new SSN();
    private SSN() {}

    public static final byte System = 0;
    public static final byte hasSubSystem = 1;
    public static final byte Deployment = 2;
    public static final byte deployedSystem = 3;
    public static final byte Stimulus = 4;
    public static final byte isProxyFor = 5;
    public static final byte wasOriginatedBy = 6;
    public static final byte detects = 7;
    public static final byte Property = 8;
    public static final byte hasProperty = 9;
    public static final byte isPropertyOf = 10;
    public static final byte forProperty = 11;




    @Override
    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public byte getSuffix(String lexical) {
        if (lexical.contains("System")) return (System);
        if (lexical.contains("hasSubSystem")) return (hasSubSystem);
        if (lexical.contains("Deployment")) return (Deployment);
        if (lexical.contains("deployedSystem")) return (deployedSystem);
        if (lexical.contains("Stimulus")) return (Stimulus);
        if (lexical.contains("isProxyFor")) return (isProxyFor);
        if (lexical.contains("wasOriginatedBy")) return (wasOriginatedBy);
        if (lexical.contains("detects")) return (detects);
        if (lexical.contains("Property")) return (Property);
        if (lexical.contains("hasProperty")) return (hasProperty);
        if (lexical.contains("isPropertyOf")) return (isPropertyOf);
        if (lexical.contains("forProperty")) return (forProperty);
        return UnknowRDF;
    }

    @Override
    public String toLexical(byte suffix) {
        switch (suffix) {
            case System:
                return prefix + "System";
            case hasSubSystem:
                return prefix + "hasSubSystem";
            case Deployment:
                return prefix + "Deployment";
            case deployedSystem:
                return prefix + "deployedSystem";
            case Stimulus:
                return prefix + "Stimulus";
            case isProxyFor:
                return prefix + "isProxyFor";
            case wasOriginatedBy:
                return prefix + "wasOriginatedBy";
            case detects:
                return prefix + "detects";
            case Property:
                return prefix + "Property";
            case hasProperty:
                return prefix + "hasProperty";
            case isPropertyOf:
                return prefix + "isPropertyOf";
            case forProperty:
                return prefix + "forProperty";
            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }
}
