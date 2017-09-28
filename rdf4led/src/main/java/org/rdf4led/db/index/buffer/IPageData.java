package org.rdf4led.db.index.buffer;

/**
 * org.rdf4led.db.index1.buffer
 *
 * <p>TODO: Add class description
 *
 * <p>Author: Anh Le_Tuan
 *
 * <p>Email: anh.letuan@insight-centre.org
 *
 * <p>Date: 02/02/18.
 */
public interface IPageData<T> {

  // Add a tuple to page
  public boolean add(T tuple);

//  //Add an array of tuple
//  public boolean add(T[] tuple);

  // Delete a tuple from page
  public boolean delete(T tuple);

  // Return a tuple in a give position
  public T get(int position);

  // Return a position of a Tuple in page
  // If page contains this tuple -> return it's position
  // If page does not contain this tuple -> return the position of the fisrt tuple that
  // is lesser than the given tuple.
  public int search(T tuple);

  //check if is there free space left in page
  //public boolean isFull();

  //Split a page. Copy haft of a page into a new page.
  //public IPageData<T> split();

  //public ByteBuffer toByteBuffer();
}
