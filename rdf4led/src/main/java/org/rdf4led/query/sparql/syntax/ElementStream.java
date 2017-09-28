package org.rdf4led.query.sparql.syntax;

public class ElementStream<Node> extends Element<Node> {
  Node streamUri;

  byte windowType;

  int maxCount;

  long timeRange;

  private Element<Node> element;

  public ElementStream(
      Element<Node> element, Node streamUri, byte windowType, int maxCount, long timeRange) {
    this.streamUri = streamUri;

    this.element = element;

    this.windowType = windowType;

    this.maxCount = maxCount;

    this.timeRange = timeRange;
  }

  public Node getStreamUri() {
    return streamUri;
  }

  public byte getWindowType() {
    return windowType;
  }

  public int getMaxCount() {
    return maxCount;
  }

  public long getTimeRange() {
    return timeRange;
  }

  public Element<Node> getElement() {
    return element;
  }

  @Override
  public void visit(ElementVisitor<Node> v) {
    v.visit(this);
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean equalTo(Element<Node> el2) {
    // TODO Auto-generated method stub
    return false;
  }
}
