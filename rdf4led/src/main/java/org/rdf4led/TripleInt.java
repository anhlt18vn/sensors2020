package org.rdf4led;


import org.rdf4led.graph.Triple;

/**
 * TODO: TripleInt
 *
 * @author Anh Le Tuan
 * @email anh.le@deri.org anh.letuan@insight-centre.org
 *     <p>Date: 5 Jan 2015
 */
public class TripleInt extends Triple<Integer> {

  public TripleInt(Integer s, Integer p, Integer o) {
    super(s, p, o);
  }



  public TripleInt(String order, int[] nodes) {
    super(0, 0, 0);

    if (order.equals("SPO")) {
      set_spo((byte) 1, nodes);
    } else {
      if (order.equals("POS")) {
        set_spo((byte) 2, nodes);
      } else {
        set_spo((byte) 3, nodes);
      }
    }
  }

  public TripleInt(byte order, byte[] nodes) {
    super(0, 0, 0);

    set_spo(order, nodes);

    throw new RuntimeException("Unsupported object creator");
  }

  public void set_triple(String order, int ... nodes){
    if (order.equals("SPO")) {
      set_spo((byte) 1, nodes);
    } else {
      if (order.equals("POS")) {
        set_spo((byte) 2, nodes);
      } else {
        set_spo((byte) 3, nodes);
      }
    }
  }

  public void set_spo(int order, int... nodes) {
    if (order == 1) {
      s = nodes[0];
      p = nodes[1];
      o = nodes[2];
    }

    if (order == 2) {
      p = nodes[0];
      o = nodes[1];
      s = nodes[2];
    }

    if (order == 3) {
      o = nodes[0];
      s = nodes[1];
      p = nodes[2];
    }
  }

  public void set_spo(byte order, byte[] nodes) {
    int int_1, int_2, int_3;

    int_1 = nodes[0] << 24 | (nodes[1] & 0xFF) << 16 | (nodes[2] & 0xFF) << 8 | (nodes[3] & 0xFF);

    int_2 = nodes[4] << 24 | (nodes[5] & 0xFF) << 16 | (nodes[6] & 0xFF) << 8 | (nodes[7] & 0xFF);

    int_3 = nodes[8] << 24 | (nodes[9] & 0xFF) << 16 | (nodes[10] & 0xFF) << 8 | (nodes[11] & 0xFF);

    set_spo(order, int_1, int_2, int_3);
  }

  public Triple<Integer> clone() {
    return new TripleInt(this.s, this.o, this.p);
  }

  public boolean sameAs(Object entry) {
    if (entry instanceof TripleInt) {
      TripleInt triple = (TripleInt) entry;

      if (triple.getSubject() != s) return false;

      if (triple.getPredicate() != p) return false;

      if (triple.getObject() != o) return false;

      return true;
    }

    return false;
  }

  @Override
  public Integer getSubject() {
    return s;
  }

  @Override
  public Integer getPredicate() {
    return p;
  }

  @Override
  public Integer getObject() {
    return o;
  }

  public int[] toSPO() {
    return new int[] {s, p, o};
  }

  public int[] toPOS() {
    return new int[] {p, o, s};
  }

  public int[] toOSP() {
    return new int[] {o, s, p};
  }
}
