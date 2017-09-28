package org.rdf4led.query.engine;

import org.rdf4led.RangeTripleIterator;
import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;
import org.rdf4led.common.mapping.Mapping;
import org.rdf4led.common.iterator.NullIterator;
import org.rdf4led.common.iterator.SingletonIterator;

import java.util.Iterator;
import java.util.List;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 21/09/17.
 */
public class RankTP {
  private Graph<Integer> graphStore;

  public RankTP(Graph<Integer> graphStore) {
    this.graphStore = graphStore;
  }

  public QueryTP findMin(List<QueryTP> queryTpList, Mapping<Integer> mapping) {

    QueryTP minQueryTP = queryTpList.get(0);

    int minRank = rankCalculate(mapping, minQueryTP);

    for (QueryTP queryTP : queryTpList) {

      int rank = rankCalculate(mapping, queryTP);

      if (rank == 0) {
        return queryTP;
      }

      if (minRank > rank) {
        minRank = rank;
        minQueryTP = queryTP;
      }

    }

    return minQueryTP;
  }

  private boolean isShare(Mapping<Integer> mapping, QueryTP queryTP) {

    return mapping.contains(queryTP.getTriplePattern().getSubject())
        || mapping.contains(queryTP.getTriplePattern().getPredicate())
        || mapping.contains(queryTP.getTriplePattern().getObject());
  }

  public int rankCalculate(Mapping<Integer> mapping, QueryTP queryTP) {

    if (!isShare(mapping, queryTP)) return  Integer.MAX_VALUE;

    Iterator<Triple<Integer>> iterator = queryTP.find(mapping);

    if (iterator instanceof NullIterator) return 0;

    if (iterator instanceof SingletonIterator) return 1;

    if (iterator instanceof RangeTripleIterator) {
      return ((RangeTripleIterator) iterator).size();
    }

    return Integer.MAX_VALUE;
  }


  QueryTP minQueryTP;

  Iterator<Triple<Integer>>  minIter;

  public void findShortestIter(List<QueryTP> queryPatterns, Mapping<Integer> inputMapping) {

    int minRank;

    minQueryTP = queryPatterns.get(0);
    minIter    = minQueryTP.find(inputMapping);
    minRank    = calculateRank(minIter);

    if ((minRank == 0)||(minRank==1)) return;

    Iterator<Triple<Integer>> iterator = null;

    int rank = 0;

    for (int index=0; index<queryPatterns.size(); index++) {

      QueryTP queryTP = queryPatterns.get(index);
      iterator = queryTP.find(inputMapping);
      rank = calculateRank(iterator);

      if (minRank > rank){
        minIter = iterator;
        minQueryTP = queryTP;
        minRank = rank;
      }

      if ((minRank == 0) || (minRank ==1)) return;
    }
  }

  public int calculateRank(Iterator<Triple<Integer>> iterator){

    if (iterator instanceof NullIterator) return 0;

    if (iterator instanceof SingletonIterator) return 1;

    if (iterator instanceof RangeTripleIterator) {
      return ((RangeTripleIterator) iterator).size();
    }

    throw new RuntimeException("Unknown iterator");
  }

  public Iterator<Triple<Integer>> getMinIter() {
    return minIter;
  }

  public QueryTP getMinQueryTP() {
    return minQueryTP;
  }
}
