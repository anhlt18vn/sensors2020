package org.rdf4led.common.mapping;

/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 06.02.20
 */
public interface VarChecker<Node> {
    public boolean confirmCheck(Node var);
}
