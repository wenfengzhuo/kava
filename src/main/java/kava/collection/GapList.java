package kava.collection;

import java.util.*;

/**
 * A Java implementation of <a href="https://en.wikipedia.org/wiki/Gap_buffer">Gap Buffer</a><br/>
 * <br/>
 * The implementation extends the AbstractList and hence implements the operations in java.util.List.
 *
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class GapList<T> extends AbstractList<T> {

  private static final int DEFAULT_SIZE = 10;

  private Object[] elementData;

  private int size;

  private int gapStart;

  private int gapEnd;

  public GapList() {
    this(DEFAULT_SIZE);
  }

  public GapList(int capacity) {
    elementData = new Object[capacity];
    gapStart = 0;
    gapEnd = elementData.length - 1;
  }

  /**
   * Moving gap [gapStart, gapEnd] to index i means moving gap to left until the gapStart reaches i
   *
   * Current state:
   *       gapStart        gapEnd
   *          |              |
   *  +--+--+--+--+--+--+--+--+--+
   *  | 0| 1|  |  |  |  |  |  | 2|
   *  +--+--+--+--+--+--+--+--+--+
   *
   * The number 0, 1, 2 means the public index of the elements in the list, which is not the real index
   * in the array.
   *
   * After moving:
   *   gapStart        gapEnd
   *      |              |
   *  +--+--+--+--+--+--+--+--+--+
   *  | 0|  |  |  |  |  |  | 1| 2|
   *  +--+--+--+--+--+--+--+--+--+
   *
   * @param index
   * @return
   */
  private int move(int index) {
    if (index < gapStart) {
      int count = gapStart - index;
      System.arraycopy(elementData, index, elementData, gapEnd - count + 1, count);
      gapStart = index;
      gapEnd = gapEnd - count;
      // It is okay to not set null for these elements, but for GC perspective, it
      // is recommended to remove these invalid elements.
      Arrays.fill(elementData, gapStart, gapEnd, null);
    } else if (index > gapStart) {
      int count = index - gapStart;
      System.arraycopy(elementData, gapEnd + 1, elementData, gapStart, count);
      gapStart = index;
      gapEnd = gapEnd + count;
      Arrays.fill(elementData, gapStart, gapEnd, null);
    }
    return gapStart;
  }

  private void ensureCapacity(int needed) {
    int left = elementData.length - size();
    if (left <= needed) {
      Object[] newArray = new Object[elementData.length * 2 + needed];
      move(size());
      System.arraycopy(elementData, 0, newArray, 0, elementData.length);
      elementData = newArray;
      gapStart = gapEnd;
      gapEnd = newArray.length - 1;
    }
  }

  @Override
  public boolean add(T t) {
    add(size(), t);
    return true;
  }

  @Override
  public T set(int index, T element) {
    T pre = null;
    if (index < gapStart) {
      pre = (T) elementData[index];
      elementData[index] = element;
    } else {
      int id = gapEnd + index - gapStart + 1;
      pre = (T) elementData[id];
      elementData[id] = element;
    }
    return pre;
  }

  @Override
  public void add(int index, T element) {
    ensureCapacity(1);
    int id = move(index);
    elementData[id] = element;
    gapStart ++;
    size ++;
  }

  @Override
  public T remove(int index) {
    move(index);
    T rmObj = (T) elementData[++gapEnd];
    elementData[gapEnd] = null;
    size --;
    return rmObj;
  }

  /**
   * New API in GapList which supports removing a range of elements in the list
   * @param fromIndex
   * @param toIndex
   */
  public void removeAll(int fromIndex, int toIndex) {
    removeRange(fromIndex, toIndex);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    ensureCapacity(c.size());
    int id = move(index);
    for (T t : c) {
      elementData[id ++] = t;
    }
    gapStart = id;
    size += c.size();
    return true;
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    move(fromIndex);
    Arrays.fill(elementData, gapEnd+1, gapEnd + toIndex - fromIndex + 1, null);
    gapEnd = gapEnd + toIndex - fromIndex;
    size -= toIndex - fromIndex;
  }

  @Override
  public T get(int index) {
    if (index < gapStart) {
      return (T) elementData[index];
    } else {
      int id = gapEnd + index - gapStart + 1;
      return (T) elementData[id];
    }
  }

  @Override
  public int size() {
    return size;
  }


}
