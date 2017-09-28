package org.rdf4led.query.engine;

import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;
import org.rdf4led.query.BasicPattern;
import org.rdf4led.query.QueryContext;
import org.rdf4led.query.sparql.algebra.op.OpBGP;
import org.rdf4led.common.mapping.Mapping;
import org.rdf4led.common.mapping.MappingJoin;
import org.rdf4led.common.iterator.NullIterator;

import java.util.*;

/**
 * org.rdf4led.query.engine
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 20/09/17.
 */
public class OpForwardBGP implements Forwarder<Integer> {
    BasicPattern<Integer> bgp;

    private Graph<Integer> graphStore;

    List<QueryTP> queryPatterns;

    private int mLevel;

    static QueryContext<Integer> context;

    private RankTP rankTP;

    private Forwarder<Integer> forwarder;

    public OpForwardBGP(
            OpBGP<Integer> opBGP,
            Graph<Integer> graphStore,
            QueryContext<Integer> context,
            Forwarder<Integer> forwarder) {

        this.bgp = opBGP.getPattern();

        this.graphStore = graphStore;

        mLevel = this.bgp.size();

        //trackPattern = new boolean[mLevel];

        //Arrays.fill(trackPattern, true);

        this.context = context;

        queryPatterns = new ArrayList<>();

        rankTP = new RankTP(this.graphStore);

        this.forwarder = forwarder;

        init();
    }

    private void init() {
        int id = 0;

        for (Triple<Integer> triple : bgp.getList()) {
            queryPatterns.add(new QueryTP(graphStore, triple, id, context));
            id++;
        }
    }

    public static void printTriple(Triple<Integer> t) {
        System.out.print(context.dictionary().getLexicalForm(t.getSubject()) + " -- ");
        System.out.print(context.dictionary().getLexicalForm(t.getPredicate()) + " -- ");
        System.out.println(context.dictionary().getLexicalForm(t.getObject()));
    }

    public void printTripleNumber(Triple<Integer> t) {
        System.out.print((t.getSubject()) + " -- ");
        System.out.print((t.getPredicate()) + " -- ");
        System.out.println((t.getObject()));
    }

    int level = 0;

    static int i = 0;

    private void joinPropagation(Mapping<Integer> inputMapping) {
        i++;

        if (level < mLevel) {

            level++;

            QueryTP queryTP = findNextQueryTP(inputMapping);

            queryPatterns.remove(queryTP);

            Iterator<Triple<Integer>> iterator = queryTP.find(inputMapping);

            while (iterator.hasNext()) {

                Triple<Integer> nextTriple = iterator.next();

                putSubstitute(inputMapping, nextTriple, queryTP.getTriplePattern());

                joinPropagation(inputMapping);

                removeSubtitute(inputMapping, nextTriple, queryTP.getTriplePattern());
            }

            queryPatterns.add(queryTP);

            level--;

        } else {
            forwarder.process(inputMapping);
        }
    }

    private void joinPropagation1(Mapping<Integer> inputMapping) {
        i++;

        if (level < mLevel) {

            rankTP.findShortestIter(queryPatterns, inputMapping);

            Iterator<Triple<Integer>> iterator = rankTP.getMinIter();

            QueryTP queryTP = rankTP.getMinQueryTP();

            level++;

            queryPatterns.remove(queryTP);

            if (!(iterator instanceof NullIterator)) {

                while (iterator.hasNext()) {

                    Triple<Integer> nextTriple = iterator.next();

                    putSubstitute(inputMapping, nextTriple, queryTP.getTriplePattern());

                    joinPropagation1(inputMapping);

                    removeSubtitute(inputMapping, nextTriple, queryTP.getTriplePattern());
                }
            }

            queryPatterns.add(queryTP);

            level--;

        } else {
            forwarder.process(inputMapping);
        }
    }

    private QueryTP findNextQueryTP(Mapping<Integer> inputMapping) {
        return queryPatterns.isEmpty() ? null : rankTP.findMin(queryPatterns, inputMapping);
    }

    private void putSubstitute(Mapping<Integer> mapping,
                               Triple<Integer> triple,
                               Triple<Integer> pattern) {

        Integer pS = pattern.getSubject();
        Integer pP = pattern.getPredicate();
        Integer pO = pattern.getObject();

        if (context.isVarNode(pS)) {
            mapping.add(pS, triple.getSubject());
        }

        if (context.isVarNode(pP)) {
            mapping.add(pP, triple.getPredicate());
        }

        if (context.isVarNode(pO)) {
            mapping.add(pO, triple.getObject());
        }
    }

    private void removeSubtitute(Mapping<Integer> mapping,
                                 Triple<Integer> triple,
                                 Triple<Integer> pattern) {

        Integer pS = pattern.getSubject();
        Integer pP = pattern.getPredicate();
        Integer pO = pattern.getObject();

        if (context.isVarNode(pS)) {
            mapping.remove(pS, triple.getSubject());
        }

        if (context.isVarNode(pP)) {
            mapping.remove(pP, triple.getPredicate());
        }

        if (context.isVarNode(pO)) {
            mapping.remove(pO, triple.getObject());
        }
    }

    @Override
    public void process(Mapping<Integer> mapping) {

//    joinPropagation(new MappingJoin());
//    System.out.println("--> " + i);
//    System.out.println(ForwarderPrint.number);

//    i=0;
//    ForwarderPrint.number=0;

//    queryPatterns.clear();
//    init();
        try {
            joinPropagation1(new MappingJoin());
        } catch (OutOfMemoryError error) {
            return;
        }

        System.out.println("number propagations: " + i);
        i = 0;
        System.out.println(ForwarderPrint.number);

//    i=0;
//    ForwarderPrint.number=0;
//    queryPatterns.clear();
//    init();
//    joinPropagation1(new MappingJoin());
//    System.out.println("--> " + i);
//    System.out.println(ForwarderPrint.number);
//
//    i=0;
//    ForwarderPrint.number=0;
//    queryPatterns.clear();
//    init();
//    joinPropagation1(new MappingJoin());
//    System.out.println("--> " + i);
//    System.out.println(ForwarderPrint.number);
    }
}
