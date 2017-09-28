// COPYRIGHT_BEGIN
//
// COPYRIGHT_END

package org.rdf4led.rdf.dictionary.vocab;

/**
 * RDFS.java
 *
 * <p>TODO Modified from Jena
 *
 * <p>Author : Anh Le-Tuan Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 27, 2015
 */
public class RDFS implements Vocabulary {

  public static final byte c_prefix = VocabHandler.RDFS_;

  public static final byte Class = 0;
  public static final byte Datatype = 1;
  public static final byte Container = 2;
  public static final byte ContainerMembershipProperty = 3;
  public static final byte Literal = 4;
  public static final byte Resource = 5;
  public static final byte comment = 6;
  public static final byte domain = 7;
  public static final byte label = 8;
  public static final byte seeAlso = 9;
  public static final byte range = 10;
  public static final byte subPropertyOf = 11;
  public static final byte subClassOf = 12;
  public static final byte member = 13;
  public static final byte isDefinedBy = 14;

  public static final String prefix = "http://www.w3.org/2000/01/rdf-schema#";

  public static Vocabulary rdfs = new RDFS();

  private RDFS() {}

  @Override
  public byte getPrefix() {
    return c_prefix;
  }

  @Override
  public String toLexical(byte suffix) {
    switch (suffix) {
      case Class:
        return prefix + "Class";
      case Datatype:
        return prefix + "Datatype";
      case Container:
        return prefix + "Container";
      case ContainerMembershipProperty:
        return prefix + "ContainerMembershipProperty";
      case Literal:
        return prefix + "Literal";
      case Resource:
        return prefix + "Resource";
      case comment:
        return prefix + "comment";
      case domain:
        return prefix + "domain";
      case label:
        return prefix + "label";
      case seeAlso:
        return prefix + "seeAlso";
      case range:
        return prefix + "range";
      case subPropertyOf:
        return prefix + "subPropertyOf";
      case subClassOf:
        return prefix + "subClassOf";
      case member:
        return prefix + "member";
      case isDefinedBy:
        return prefix + "isDefinedBy";
      case error:
        return prefix + "error";
      default:
        throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
    }
  }

  @Override
  public byte getSuffix(String lexical) {
    if (lexical.contains("#subClassOf")) return (subClassOf);
    if (lexical.contains("#Class")) return (Class);
    if (lexical.contains("Datatype")) return (Datatype);
    if (lexical.contains("ContainerMembershipProperty")) return (ContainerMembershipProperty);
    if (lexical.contains("Container")) return (Container);
    if (lexical.contains("Literal")) return (Literal);
    if (lexical.contains("#Resource")) return (Resource);
    if (lexical.contains("#comment")) return (comment);
    if (lexical.contains("#domain")) return (domain);
    if (lexical.contains("#label")) return (label);
    if (lexical.contains("#seeAlso")) return (seeAlso);
    if (lexical.contains("#range")) return (range);
    if (lexical.contains("#subPropertyOf")) return (subPropertyOf);
    if (lexical.contains("#subclassOf")) return (subClassOf);
    if (lexical.contains("member")) return (member);
    if (lexical.contains("isDefinedBy")) return isDefinedBy;
    return error;
    // throw new IllegalArgumentException("Can not recognise URI " + lexical);
  }
}
