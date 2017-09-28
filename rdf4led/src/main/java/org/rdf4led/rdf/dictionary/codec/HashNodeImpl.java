package org.rdf4led.rdf.dictionary.codec;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * NodeHashJ2SE16.java
 *
 * <p>TODO
 *
 * <p>Author : Le Tuan Anh Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>27 May 2015
 */
public abstract class HashNodeImpl<TNode> implements HashNode<TNode, String, Byte, Byte> {
  public static HashNodeImpl MD5_Hash_Int =
      new HashNodeImpl<Integer>() {
        @Override
        protected Integer getHash(String message) {
          byte[] hash = new byte[16];

          MessageDigest digest;

          try {

            digest = MessageDigest.getInstance("MD5");

            digest.update(message.getBytes("UTF-8"));

            digest.digest(hash, 0, 16);

          } catch (NoSuchAlgorithmException e) {

          } catch (UnsupportedEncodingException e) {

          } catch (DigestException e) {
          }

          return Arrays.hashCode(hash);
        }
      };

  public static HashNodeImpl MURMUR_INT =
      new HashNodeImpl<Integer>() {
        @Override
        protected Integer getHash(String message) {
          throw new UnsupportedOperationException("Unimplemented yet");
        }
      };

  @Override
  public TNode getHash(String lexical, Byte langTag, Byte nodeType) {
    return getHash(lexical, Byte.toString(langTag), Byte.toString(nodeType));
  }

  private TNode getHash(String lexical, String langTag, String dataType) {
    String toHash = lexical + "|" + langTag + "|" + dataType;

    return getHash(toHash);
  }

  protected abstract TNode getHash(String message);
}
