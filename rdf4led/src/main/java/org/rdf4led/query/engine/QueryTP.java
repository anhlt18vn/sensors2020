package org.rdf4led.query.engine;

import org.rdf4led.TripleInt;
import org.rdf4led.graph.Graph;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.QueryContext;
import org.rdf4led.common.mapping.Mapping;

import java.util.Iterator;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 21/09/17.
 */
public class QueryTP {
  private Graph<Integer> graphStore;

  public Triple<Integer> triplePattern;

  private int id;

  QueryContext<Integer> context;

  public QueryTP(
      Graph<Integer>  graphStore,
      Triple<Integer> triplePattern,
      int patternId,
      QueryContext<Integer> context) {
    this.graphStore = graphStore;

    this.triplePattern = triplePattern;

    this.id = patternId;

    this.context = context;
  }

  public int getId() {
    return id;
  }

  public Triple<Integer> getTriplePattern() {
    return triplePattern;
  }

  public Iterator<Triple<Integer>> find(Mapping<Integer> mapping) {

    Triple<Integer> tt = substitute(mapping, this.triplePattern);

    return graphStore.find(tt);
  }

  public Triple<Integer> substitute(Mapping<Integer> mapping, Triple<Integer> triple) {
    int ss = substitute(mapping, triple.getSubject());
    int sp = substitute(mapping, triple.getPredicate());
    int so = substitute(mapping, triple.getObject());

    ss = replaceVarNode(ss);
    sp = replaceVarNode(sp);
    so = replaceVarNode(so);

    return new TripleInt(ss, sp, so);
  }

  public Integer replaceVarNode(Integer node) {
    return context.isVarNode(node) ? RDFNodeType.ANY : node;
  }

  public Integer substitute(Mapping<Integer> mapping, Integer inNode) {

    Integer outNode = mapping.getValue(inNode);

    return outNode == null ? inNode : outNode;
  }
}
