package org.rdf4led.rdf.dictionary;


import org.rdf4led.rdf.dictionary.codec.EncoderInt;
import org.rdf4led.rdf.dictionary.codec.HashNodeImpl;
import org.rdf4led.rdf.dictionary.codec.RDFNodeType;

/** Created by anhlt185 on 30/04/17. */
public class RDFDictionaryInt extends RDFDictionaryAbstract<Integer> {
  public RDFDictionaryInt(
          HashNodeIdTable<Integer> hashTable, NodeIdLexTable<String, Integer> lexTable) {
    super(HashNodeImpl.MD5_Hash_Int, hashTable, lexTable, EncoderInt.encodeInt);
  }

  @Override
  protected Integer doEncode(Byte nodeType, Byte prefix, Integer suffix) {
    return encoder.encode(nodeType, prefix, suffix);
  }

  @Override
  protected Integer from_byte_to_TNode(byte suffix) {
    return Integer.valueOf(suffix);
  }

  @Override
  protected byte from_TNode_to_byte(Integer suffix) {
    return (byte) (suffix.byteValue());
  }

  public void sync() {
    hashTable.sync();
    lexTable.sync();
  }

  @Override
  public Integer getNodeAny() {
    return RDFNodeType.ANY;
  }
}
