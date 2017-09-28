//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.rdf4led.query.path;

import org.rdf4led.graph.Triple;

public abstract class TriplePath<Node> {
  private final Node subject;
  private final Node predicate;
  private final Path path;
  private final Node object;
  private Triple triple = null;
  private int hash = -1;

  public TriplePath(Node s, Path<Node> path, Node o) {
    this.subject = s;

    this.object = o;

    if (path instanceof P_Link) {
      this.predicate = ((P_Link<Node>) path).getNode();

      this.triple = new Triple(this.subject, this.predicate, o);
    } else {
      this.predicate = null;
    }

    this.path = path;
  }

  public TriplePath(Triple<Node> triple) {
    this.subject = triple.getSubject();

    Node p = triple.getPredicate();

    if (isURI(p)) {
      this.path = new P_Link(triple.getPredicate());

      this.predicate = p;
    } else {
      this.path = null;

      this.predicate = triple.getPredicate();
    }

    this.object = triple.getObject();

    this.triple = triple;
  }

  public abstract boolean isURI(Node p);

  public Node getSubject() {
    return this.subject;
  }

  public Path getPath() {
    return this.path;
  }

  public Node getPredicate() {
    return this.predicate;
  }

  public Node getObject() {
    return this.object;
  }

  public boolean isTriple() {
    return this.triple != null || this.predicate != null;
  }

  public Triple<Node> asTriple() {
    if (this.triple != null) {
      return this.triple;
    } else {
      if (this.path instanceof P_Link) {
        this.triple =
            new Triple<Node>(this.subject, ((P_Link<Node>) this.path).getNode(), this.object);
      }

      return this.triple;
    }
  }

  public int hashCode() {
    if (this.hash == -1) {
      if (this.isTriple()) {
        this.hash = this.asTriple().hashCode();
      } else {
        this.hash =
            this.subject.hashCode() << 2 ^ this.path.hashCode() ^ this.object.hashCode() << 1;
      }
    }

    return this.hash;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof TriplePath)) {
      return false;
    } else {
      TriplePath<Node> tp = (TriplePath<Node>) other;
      return tp.isTriple() ^ this.isTriple()
          ? false
          : (this.isTriple()
              ? this.asTriple().equals(tp.asTriple())
              : this.subject.equals(tp.subject)
                  && this.object.equals(tp.object)
                  && this.path.equals(tp.path));
    }
  }

  public String toString() {
    return this.path != null
        ? this.subject + " (" + this.path + ") " + this.object
        : this.subject + " " + this.predicate + " " + this.object;
  }
}
