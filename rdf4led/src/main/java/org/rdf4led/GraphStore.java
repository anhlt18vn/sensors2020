package org.rdf4led;

import org.rdf4led.db.index.buffer.IndexT;
import org.rdf4led.graph.Graph;
import org.rdf4led.graph.Triple;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;

import java.io.IOException;
import java.util.Iterator;

/**
 * org.rdf4led.distributed
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 16/08/17.
 */
public class GraphStore implements Graph<Integer> {
  public TripleIndex SPO, POS, OSP;

  public GraphStore(String pathToStorage) {
    SPO = new TripleIndex(pathToStorage, IndexT.SPO);
    POS = new TripleIndex(pathToStorage,  IndexT.POS);
    OSP = new TripleIndex(pathToStorage,  IndexT.OSP);
  }

  public void add(TripleInt triple) {
    SPO.add(triple.toSPO());
    POS.add(triple.toPOS());
    OSP.add(triple.toOSP());
  }

  @Override
  public void add(Triple<Integer> triple) {
    SPO.add(triple.getSubject(), triple.getPredicate(), triple.getObject());
    POS.add(triple.getPredicate(), triple.getObject(), triple.getSubject());
    OSP.add(triple.getObject(), triple.getSubject(), triple.getPredicate());
  }

  @Override
  public void delete(Triple<Integer> triple) {
    SPO.delete(triple.getSubject(), triple.getPredicate(), triple.getObject());
    POS.delete(triple.getPredicate(), triple.getObject(), triple.getSubject());
    OSP.delete(triple.getObject(), triple.getSubject(), triple.getPredicate());
  }

  @Override
  public Iterator<Triple<Integer>> find(Triple<Integer> triple) {
    return find(triple.getSubject(), triple.getPredicate(), triple.getObject());
  }

  @Override
  public Iterator<Triple<Integer>> find(Integer s, Integer p, Integer o) {
    if (s == RDFNodeType.ANY) {
      if (p == RDFNodeType.ANY) {
        if (o == RDFNodeType.ANY) {
          // (? ? ?)
          return SPO.find(s, p, o);
        } else {
          // (? ? O)
          return OSP.find(o, s, p);
        }
      } else {
        // (? P O) and (? P ?)
        return POS.find(p, o, s);
      }
    } else {
      if (p == RDFNodeType.ANY) {
        if (o == RDFNodeType.ANY) {
          // (S ? ?)
          return SPO.find(s, p, o);
        } else {
          // S ? O)TripleJ2SE
          return OSP.find(o, s, p);
        }
      } else {
        // (S P ?) and (S P O)
        if (o == RDFNodeType.ANY) {
          return SPO.find(s, p, o);
        } else {
          return SPO.findMatch(s, p, o);
        }
      }
    }
  }

  @Override
  public boolean contains(Triple<Integer> triple) {
    return false;
  }

  @Override
  public void sync() {
    SPO.sync();
    POS.sync();
    OSP.sync();
  }

  @Override
  public void close() throws IOException {}

  public int size() {
    Iterator<Triple<Integer>> iter = find(RDFNodeType.ANY, RDFNodeType.ANY, RDFNodeType.ANY);

    int count = 0;

    while (iter.hasNext()) {
      Triple<Integer> t = iter.next();

      count++;
    }

    return count;
  }

  public void dumd() {
    // POS.bufferLayer.bufferLayerDump(3);
  }
}
