package org.rdf4led.rdf.dictionary.vocab;

/**
 * TODO: XSDDataType
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 9 Dec 2014
 */
public class XSD implements Vocabulary {

  public static final byte c_prefix = VocabHandler.XSD_;

  public static final byte XSDfloat = 1;
  public static final byte XSDdouble = 2;
  public static final byte XSDint = 3;
  public static final byte XSDlong = 4;
  public static final byte XSDshort = 5;
  public static final byte XSDbyte = 6;
  public static final byte XSDunsignedByte = 7;
  public static final byte XSDunsignedShort = 8;
  public static final byte XSDunsignedInt = 9;
  public static final byte XSDunsignedLong = 10;
  public static final byte XSDdecimal = 11;
  public static final byte XSDinteger = 12;
  public static final byte XSDnonPositiveInteger = 13;
  public static final byte XSDnonNegativeInteger = 14;
  public static final byte XSDpositiveInteger = 15;
  public static final byte XSDnegativeInteger = 16;
  public static final byte XSDboolean = 17;
  public static final byte XSDstring = 18;
  public static final byte XSDnormalizedString = 19;
  public static final byte XSDanyURI = 20;
  public static final byte XSDtoken = 21;
  public static final byte XSDName = 22;
  public static final byte XSDQName = 23;
  public static final byte XSDlanguage = 24;
  public static final byte XSDNMTOKEN = 25;
  public static final byte XSDENTITY = 26;
  public static final byte XSDID = 27;
  public static final byte XSDNCName = 28;
  public static final byte XSDIDREF = 29;
  public static final byte XSDNOTATION = 30;
  public static final byte XSDhexBinary = 31;
  public static final byte XSDbase64Binary = 32;
  public static final byte XSDdate = 33;
  public static final byte XSDtime = 34;
  public static final byte XSDdateTime = 35;
  public static final byte XSDduration = 36;
  public static final byte XSDgDay = 37;
  public static final byte XSDgMonth = 38;
  public static final byte XSDgYear = 39;
  public static final byte XSDgYearMonth = 40;
  public static final byte XSDgMonthDay = 41;
  public static final byte XMLLiteral = 42;

  public static final String prefix = "http://www.w3.org/2001/XMLSchema#";

  public static XSD xsd = new XSD();

  private XSD() {}

  public byte getPrefix() {
    return c_prefix;
  }

  @Override
  public String toLexical(byte suffix) {
    switch (suffix) {
      case XSDfloat:
        return prefix + "float";
      case XSDdouble:
        return prefix + "double";
      case XSDint:
        return prefix + "int";
      case XSDlong:
        return prefix + "long";
      case XSDshort:
        return prefix + "short";
      case XSDbyte:
        return prefix + "byte";
      case XSDunsignedByte:
        return prefix + "unsignedByte";
      case XSDunsignedShort:
        return prefix + "unsignedShort";
      case XSDunsignedInt:
        return prefix + "unsignedInt";
      case XSDunsignedLong:
        return prefix + "unsignedLong";
      case XSDdecimal:
        return prefix + "decimal";
      case XSDinteger:
        return prefix + "integer";
      case XSDnonPositiveInteger:
        return prefix + "nonPositiveInteger";
      case XSDnonNegativeInteger:
        return prefix + "nonNegativeInteger";
      case XSDpositiveInteger:
        return prefix + "positiveInteger";
      case XSDnegativeInteger:
        return prefix + "negativeInteger";
      case XSDboolean:
        return prefix + "boolean";
      case XSDstring:
        return prefix + "string";
      case XSDnormalizedString:
        return prefix + "normalizedString";
      case XSDanyURI:
        return prefix + "anyURI";
      case XSDtoken:
        return prefix + "token";
      case XSDName:
        return prefix + "Name";
      case XSDQName:
        return prefix + "QName";
      case XSDlanguage:
        return prefix + "language";
      case XSDNMTOKEN:
        return prefix + "NMTOKEN";
      case XSDENTITY:
        return prefix + "ENTITY";
      case XSDID:
        return prefix + "ID";
      case XSDNCName:
        return prefix + "NCName";
      case XSDIDREF:
        return prefix + "IDREF";
      case XSDNOTATION:
        return prefix + "NOTATION";
      case XSDhexBinary:
        return prefix + "hexBinary";
      case XSDbase64Binary:
        return prefix + "base64Binary";
      case XSDdate:
        return prefix + "date";
      case XSDtime:
        return prefix + "time";
      case XSDdateTime:
        return prefix + "dateTime";
      case XSDduration:
        return prefix + "duration";
      case XSDgDay:
        return prefix + "gDay";
      case XSDgMonth:
        return prefix + "gMonth";
      case XSDgYear:
        return prefix + "gYear";
      case XSDgYearMonth:
        return prefix + "gYearMonth";
      case XSDgMonthDay:
        return prefix + "gMonthDay";
      case XMLLiteral:
        return "http://www.w3.org/2000/01/rdf-schema#XMLLiteral";
      case error:
        return prefix + "error";
      default:
        return prefix + "error";
        // throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
    }
  }

  public byte getSuffix(String lexical) {

    if (lexical.contains("unsignedByte")) return XSDunsignedByte;
    if (lexical.contains("unsignedShort")) return XSDunsignedShort;
    if (lexical.contains("unsignedInt")) return XSDunsignedInt;
    if (lexical.contains("unsignedLong")) return XSDunsignedLong;
    if (lexical.contains("nonPositiveInteger")) return XSDnonPositiveInteger;
    if (lexical.contains("nonNegativeInteger")) return XSDnonNegativeInteger;
    if (lexical.contains("positiveInteger")) return XSDpositiveInteger;
    if (lexical.contains("negativeInteger")) return XSDnegativeInteger;
    if (lexical.contains("boolean")) return XSDboolean;
    if (lexical.contains("string")) return XSDstring;
    if (lexical.contains("normalizedString")) return XSDnormalizedString;
    if (lexical.contains("anyURI")) return XSDanyURI;
    if (lexical.contains("token")) return XSDtoken;
    if (lexical.contains("Name")) return XSDName;
    if (lexical.contains("QName")) return XSDQName;
    if (lexical.contains("language")) return XSDlanguage;
    if (lexical.contains("NMTOKEN")) return XSDNMTOKEN;
    if (lexical.contains("ENTITY")) return XSDENTITY;
    if (lexical.contains("#ID")) return XSDID;
    if (lexical.contains("NCName")) return XSDNCName;
    if (lexical.contains("IDREF")) return XSDIDREF;
    if (lexical.contains("NOTATION")) return XSDNOTATION;
    if (lexical.contains("hexBinary")) return XSDhexBinary;
    if (lexical.contains("base64Binary")) return XSDbase64Binary;
    if (lexical.contains("dateTime")) return XSDdateTime;
    if (lexical.contains("datetime")) return XSDdateTime;
    if (lexical.contains("duration")) return XSDduration;
    if (lexical.contains("gDay")) return XSDgDay;
    if (lexical.contains("gMonth")) return XSDgMonth;
    if (lexical.contains("gYearMonth")) return XSDgYearMonth;
    if (lexical.contains("gMonthDay")) return XSDgMonthDay;
    if (lexical.contains("XMLLiteral")) return XMLLiteral;
    if (lexical.contains("gYear")) return XSDgYear;
    if (lexical.contains("float")) return XSDfloat;
    if (lexical.contains("double")) return XSDdouble;
    if (lexical.contains("long")) return XSDlong;
    if (lexical.contains("short")) return XSDshort;
    if (lexical.contains("byte")) return XSDbyte;
    if (lexical.contains("decimal")) return XSDdecimal;
    if (lexical.contains("integer")) return XSDinteger;
    if (lexical.contains("date")) return XSDdate;
    if (lexical.contains("time")) return XSDtime;
    if (lexical.contains("int")) return XSDint;

    // System.out.println("XSD error: " + lexical);
    // return RDFNodeType.NULL;
    return (byte) lexical.hashCode();
  }
}
