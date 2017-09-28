package org.rdf4led.graph.parser;


import org.rdf4led.graph.Triple;
import org.rdf4led.rdf.dictionary.RDFDictionary;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;
import org.rdf4led.rdf.dictionary.vocab.LangTag;
import org.rdf4led.rdf.dictionary.vocab.XSD;

import java.util.Iterator;

/** Created by anhlt185 on 30/04/17. */
public class LangNTriples<Node> extends LangEngine implements Iterator<Triple<Node>> {
  RDFDictionary<Node, String, Byte, Byte> dic;

  Node graphNode;

  public LangNTriples(Tokenizer tokenizer, RDFDictionary<Node, String, Byte, Byte> dic) {
    super(tokenizer);

    this.dic = dic;
  }

  public Triple<Node> createTriple(Token sToken, Token pToken, Token oToken, String graphName) {
    Node s = create(sToken, graphName);
    Node p = create(pToken, graphName);
    Node o = create(oToken, graphName);

    return new Triple<>(s, p, o);
  }

  @Override
  public boolean hasNext() {
    return super.moreTokens();
  }

  @Override
  public Triple next() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  protected final void checkIRI(Token token) {
    if (token.getType() == TokenType.IRI) {
      return;
    }

    throw new IllegalArgumentException("Predicate must be IRI");
  }

  protected final void checkIRIOrBNode(Token token) {
    if (token.getType() == TokenType.IRI) {
      return;
    }

    if (token.getType() == TokenType.BNODE) {
      return;
    }

    throw new IllegalArgumentException("Predicate must be IRI + " + token.getImage());
  }

  protected final void checkRDFTerm(Token token) {
    switch (token.getType()) {
      case IRI:
      case BNODE:
        return;
      case STRING:
        //                checkString(token);
        return;
      case STRING2:
        return;

      case LITERAL_LANG:
      case LITERAL_DT:
        //                checkString(token.getSubToken1());
        return;
      default:
        throw new IllegalArgumentException("Predicate must be IRI " + token.getImage());
    }
  }

  public Triple parseOne(String graphName) {
    Token sToken = (Token) nextToken();
    if (sToken.isEOF()) {
      /* //TODO */
    }

    Token pToken = (Token) nextToken();
    if (pToken.isEOF()) {
      /* //TODO */
    }

    Token oToken = (Token) nextToken();
    if (oToken.isEOF()) {
      /* //TODO */
    }

    // Validate RDF_Triple
    checkIRIOrBNode(sToken);

    checkIRI(pToken);

    checkRDFTerm(oToken);

    Token DOT = nextToken();

    while (DOT.getType() != TokenType.DOT) {
      DOT = nextToken();
    }
    //        if (DOT.getType() != TokenType.DOT)
    //        {
    //            System.out.println(sToken.tokenImage + " " + pToken.tokenImage + " " +
    // oToken.tokenImage + " " + DOT.tokenImage);
    //
    //            throw new IllegalArgumentException("Triple is not terminated by DOT: ");
    //        }

    return createTriple(sToken, pToken, oToken, graphName);
  }

  protected Node create(Token token, String graphNode) {
    String str = token.getImage();

    switch (token.getType()) {
      case BNODE:
        {
          return dic.createBNode(graphNode, str);
        }

      case IRI:
        return dic.createURI(str);

      case PREFIXED_NAME:

      case DECIMAL:
        return dic.createTypedLiteral(str, XSD.XSDdecimal);

      case DOUBLE:
        return dic.createTypedLiteral(str, XSD.XSDdouble);

      case INTEGER:
        return dic.createTypedLiteral(str, XSD.XSDint);

      case LITERAL_DT:
        {
          Token tokenDT = token.getSubToken();

          String uriStr;

          switch (tokenDT.getType()) {
            case IRI:
              uriStr = tokenDT.getImage();

              break;

            case PREFIXED_NAME:
              {
                String prefix = tokenDT.getImage();
                String suffix = tokenDT.getImage2();

                throw new UnsupportedOperationException(
                    "N-turtles with prefix??? " + prefix + " : " + suffix);
              }

            default:
              uriStr = "";
              //						TODO : throw an Exception
          }

          if (XSD.xsd.getSuffix(uriStr) != RDFNodeType.NULL) {
            return dic.createTypedLiteral(str, XSD.xsd.getSuffix(uriStr));
          } else {
            return dic.createPlainLiteral(str + "^^" + uriStr);
          }
        }

      case LITERAL_LANG:
        {
          String lang = token.getImage2();

          byte langTag = LangTag.langtag.getTag(lang);

          return dic.createLangLiteral(str, langTag);
        }

      case STRING:

      case STRING1:

      case STRING2:

      case LONG_STRING1:

      case LONG_STRING2:
        return dic.createPlainLiteral(str);

      default:
        {
          return null;
        }
    }
  }
}
