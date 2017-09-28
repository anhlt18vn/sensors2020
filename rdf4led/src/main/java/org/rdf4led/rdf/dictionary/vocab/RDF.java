package org.rdf4led.rdf.dictionary.vocab;

/**
 * RDF.java
 *
 */
public class RDF implements Vocabulary {

    public static final byte c_prefix = VocabHandler.RDF_;

    public static final byte Alt = 0;
    public static final byte Bag = 1;
    public static final byte Property = 2;
    public static final byte Seq = 3;
    public static final byte Statement = 4;
    public static final byte List = 5;
    public static final byte nil = 6;
    public static final byte first = 7;
    public static final byte rest = 8;
    public static final byte subject = 9;
    public static final byte predicate = 10;
    public static final byte object = 11;
    public static final byte type = 12;
    public static final byte value = 13;
    public static final byte langString = 14;
    public static final byte XMLLiteral = 15;
    public static final byte UnknowRDF = 16;

    public static final String prefix = "http://www.w3.org/1999/02/22-rdf-org.rdf4led.sparql.algebra.syntax-ns#";

    public static Vocabulary rdf = new RDF();

    private RDF() {
    }

    public byte getPrefix() {
        return c_prefix;
    }

    @Override
    public String toLexical(byte suffix) {

        switch (suffix) {
            case Alt:
                return prefix + "Alt";
            case Bag:
                return prefix + "Bag";
            case Property:
                return prefix + "Property";
            case Seq:
                return prefix + "Seq";
            case Statement:
                return prefix + "Statement";
            case List:
                return prefix + "List";
            case nil:
                return prefix + "nil";
            case first:
                return prefix + "first";
            case rest:
                return prefix + "rest";
            case subject:
                return prefix + "subject";
            case predicate:
                return prefix + "predicate";
            case object:
                return prefix + "object";
            case type:
                return prefix + "type";
            case value:
                return prefix + "value";
            case langString:
                return prefix + "langString";
            case XMLLiteral:
                return prefix + "XMLLiteral";
            case UnknowRDF:
                return prefix + "error";

            default:
                throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
        }
    }

    public byte getSuffix(String lexical) {
        if (lexical.contains("Alt")) return (Alt);
        if (lexical.contains("Bag")) return (Bag);
        if (lexical.contains("Property")) return (Property);
        if (lexical.contains("Seq")) return (Seq);
        if (lexical.contains("Statement")) return (Statement);
        if (lexical.contains("List")) return (List);
        if (lexical.contains("nil")) return (nil);
        if (lexical.contains("first")) return (first);
        if (lexical.contains("rest")) return (rest);
        if (lexical.contains("subject")) return (subject);
        if (lexical.contains("predicate")) return (predicate);
        if (lexical.contains("object")) return (object);
        if (lexical.contains("type")) return (type);
        if (lexical.contains("value")) return (value);
        if (lexical.contains("langString")) return langString;
        if (lexical.contains("XMLLiteral")) return XMLLiteral;

        return UnknowRDF;
        // throw new IllegalArgumentException("Can not recognise URI " + lexical);
    }
}
