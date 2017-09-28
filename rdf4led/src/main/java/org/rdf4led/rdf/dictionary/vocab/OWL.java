package org.rdf4led.rdf.dictionary.vocab;

/**
 * OWL.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 27, 2015
 */
public class OWL implements Vocabulary {

  static final byte c_prefix = VocabHandler.OWL_;

  public static final byte versionInfo = 0;
  public static final byte equivalentClass = 1;
  public static final byte distinctMembers = 2;
  public static final byte oneOf = 3;
  public static final byte sameAs = 4;
  public static final byte incompatibleWith = 5;
  public static final byte minCardinality = 6;
  public static final byte complementOf = 7;
  public static final byte onProperty = 8;
  public static final byte equivalentProperty = 9;
  public static final byte inverseOf = 10;
  public static final byte backwardCompatibleWith = 11;
  public static final byte differentFrom = 12;
  public static final byte priorVersion = 13;
  public static final byte imports = 14;
  public static final byte allValuesFrom = 15;
  public static final byte unionOf = 16;
  public static final byte hasValue = 17;
  public static final byte someValuesFrom = 18;
  public static final byte disjointWith = 19;
  public static final byte cardinality = 20;
  public static final byte intersectionOf = 21;
  public static final byte Thing = 22;
  public static final byte DataRange = 23;
  public static final byte Ontology = 24;
  public static final byte DeprecatedClass = 25;
  public static final byte AllDifferent = 26;
  public static final byte DatatypeProperty = 27;
  public static final byte SymmetricProperty = 28;
  public static final byte TransitiveProperty = 29;
  public static final byte DeprecatedProperty = 30;
  public static final byte AnnotationProperty = 31;
  public static final byte Restriction = 32;
  public static final byte Class = 33;
  public static final byte OntologyProperty = 34;
  public static final byte ObjectProperty = 35;
  public static final byte FunctionalProperty = 36;
  public static final byte InverseFunctionalProperty = 37;
  public static final byte Nothing = 38;
  public static final byte maxCardinality = 39;

  public static final String prefix = "http://www.w3.org/2002/07/owl#";

  public static Vocabulary owl = new OWL();

  private OWL() {}

  @Override
  public byte getPrefix() {
    return c_prefix;
  }

  @Override
  public String toLexical(byte suffix) {
    switch (suffix) {
      case versionInfo:
        return prefix + "versionInfo";
      case equivalentClass:
        return prefix + "equivalentClass";
      case distinctMembers:
        return prefix + "distinctMembers";
      case oneOf:
        return prefix + "oneOf";
      case sameAs:
        return prefix + "sameAs";
      case incompatibleWith:
        return prefix + "incompatibleWith";
      case minCardinality:
        return prefix + "minCardinality";
      case maxCardinality:
        return prefix + "maxCardinality";
      case complementOf:
        return prefix + "complementOf";
      case onProperty:
        return prefix + "onProperty";
      case equivalentProperty:
        return prefix + "equivalentProperty";
      case inverseOf:
        return prefix + "inverseOf";
      case backwardCompatibleWith:
        return prefix + "backwardCompatibleWith";
      case differentFrom:
        return prefix + "differentFrom";
      case priorVersion:
        return prefix + "priorVersion";
      case imports:
        return prefix + "imports";
      case allValuesFrom:
        return prefix + "allValuesFrom";
      case unionOf:
        return prefix + "unionOf";
      case hasValue:
        return prefix + "hasValue";
      case someValuesFrom:
        return prefix + "someValuesFrom";
      case disjointWith:
        return prefix + "disjointWith";
      case cardinality:
        return prefix + "cardinality";
      case intersectionOf:
        return prefix + "intersectionOf";
      case Thing:
        return prefix + "Thing";
      case DataRange:
        return prefix + "DataRange";
      case Ontology:
        return prefix + "Ontology";
      case DeprecatedClass:
        return prefix + "DeprecatedClass";
      case AllDifferent:
        return prefix + "AllDifferent";
      case DatatypeProperty:
        return prefix + "DatatypeProperty";
      case SymmetricProperty:
        return prefix + "SymmetricProperty";
      case TransitiveProperty:
        return prefix + "TransitiveProperty";
      case DeprecatedProperty:
        return prefix + "DeprecatedProperty";
      case AnnotationProperty:
        return prefix + "AnnotationProperty";
      case Restriction:
        return prefix + "Restriction";
      case Class:
        return prefix + "Class";
      case OntologyProperty:
        return prefix + "OntologyProperty";
      case ObjectProperty:
        return prefix + "ObjectProperty";
      case FunctionalProperty:
        return prefix + "FunctionalProperty";
      case InverseFunctionalProperty:
        return prefix + "InverseFunctionalProperty";
      case Nothing:
        return prefix + "Nothing";
      case error:
        return prefix + "error";

      default:
        throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * graphofthings.interfaces.rdf.org.rdf4led.rdf.dictionary.vocabulary.OWL#toNode(
   * java.org.rdf4led.rdf.org.rdf4led.sparql.parser.lang.Object)
   */
  public byte getSuffix(String lexical) {
    if (lexical.contains("backwardCompatibleWith")) return backwardCompatibleWith;
    if (lexical.contains("equivalentProperty")) return equivalentProperty;
    if (lexical.contains("versionInfo")) return versionInfo;
    if (lexical.contains("equivalentClass")) return equivalentClass;
    if (lexical.contains("distinctMembers")) return distinctMembers;
    if (lexical.contains("oneOf")) return oneOf;
    if (lexical.contains("sameAs")) return sameAs;
    if (lexical.contains("incompatibleWith")) return incompatibleWith;
    if (lexical.contains("minCardinality")) return minCardinality;
    if (lexical.contains("maxCardinality")) return maxCardinality;
    if (lexical.contains("complementOf")) return complementOf;
    if (lexical.contains("inverseOf")) return inverseOf;
    if (lexical.contains("differentFrom")) return differentFrom;
    if (lexical.contains("priorVersion")) return priorVersion;
    if (lexical.contains("imports")) return imports;
    if (lexical.contains("allValuesFrom")) return allValuesFrom;
    if (lexical.contains("unionOf")) return unionOf;
    if (lexical.contains("hasValue")) return hasValue;
    if (lexical.contains("someValuesFrom")) return someValuesFrom;
    if (lexical.contains("disjointWith")) return disjointWith;
    if (lexical.contains("cardinality")) return cardinality;
    if (lexical.contains("intersectionOf")) return intersectionOf;
    if (lexical.contains("Thing")) return Thing;
    if (lexical.contains("DataRange")) return DataRange;
    if (lexical.contains("DeprecatedClass")) return DeprecatedClass;
    if (lexical.contains("AllDifferent")) return AllDifferent;
    if (lexical.contains("DatatypeProperty")) return DatatypeProperty;
    if (lexical.contains("SymmetricProperty")) return SymmetricProperty;
    if (lexical.contains("TransitiveProperty")) return TransitiveProperty;
    if (lexical.contains("DeprecatedProperty")) return DeprecatedProperty;
    if (lexical.contains("AnnotationProperty")) return AnnotationProperty;
    if (lexical.contains("Restriction")) return Restriction;
    if (lexical.contains("OntologyProperty")) return OntologyProperty;
    if (lexical.contains("ObjectProperty")) return ObjectProperty;
    if (lexical.contains("InverseFunctionalProperty")) return InverseFunctionalProperty;
    if (lexical.contains("FunctionalProperty")) return FunctionalProperty;
    if (lexical.contains("Nothing")) return Nothing;
    if (lexical.contains("Class")) return Class;
    if (lexical.contains("onProperty")) return onProperty;
    if (lexical.contains("Ontology")) return Ontology;

    return error;
    // throw new IllegalArgumentException("Can not recognise URI " + lexical);
  }
}
