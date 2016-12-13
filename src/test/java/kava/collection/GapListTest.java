package kava.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Wenfeng Zhuo (wz2366@columbia.edu)
 */
public class GapListTest {

  @Test
  public void testGapList() {
    int baseSize = 100000;
    int testSetSize = 10;
    int modifySize = 100;
    // GapList
    List<Integer> gapList = new GapList<>();

    // Java built-in array list to verify the correctness of GapList
    List<Integer> arrList = new ArrayList<>();

    Random r = new Random();

    // Add 10000 random elements in two lists
    for (int i = 0; i < baseSize; i ++) {
      int n = r.nextInt();
      gapList.add(n);
      arrList.add(n);
    }
    verify("Add 10000 random elements in two lists", arrList, gapList);

    // Randomly choose some positions and then remove 100 elements from each position
    for (int i = 0; i < testSetSize; i ++) {
      int p = r.nextInt(arrList.size() - modifySize);
      for (int j = 0; j < modifySize; j++) {
        gapList.remove(p);
        arrList.remove(p);
      }
    }
    verify("Randomly choose some positions and then remove 100 elements from each position", arrList, gapList);

    // Randomly choose some positions and then add 100 elements started from each position
    for (int i = 0; i < testSetSize; i ++) {
      int p = r.nextInt(arrList.size() - modifySize);
      for (int j = 0; j < modifySize; j ++) {
        int n = r.nextInt();
        gapList.add(p + j, n);
        arrList.add(p + j, n);
      }
    }
    verify("Randomly choose some positions and then add 100 elements started from each position", arrList, gapList);

    // Randomly choose some positions and then set a random number for each position
    for (int i = 0; i < testSetSize; i ++) {
      int p = r.nextInt(arrList.size() - modifySize);
      for (int j = 0; j < modifySize; j ++) {
        int n = r.nextInt();
        gapList.set(p + j, n);
        arrList.set(p + j, n);
      }
    }
    verify("Randomly choose some positions and then set a random number for each position", arrList, gapList);

  }

  @Test
  public void testRemoveAll() {
    int baseSize = 10000;
    int testSetSize = 10;
    int modifySize = 100;

    GapList<Integer> gapList = new GapList<>();
    List<Integer> arrList = new ArrayList<>();

    Random r = new Random();

    for (int i = 0; i < baseSize; i ++) {
      int n = r.nextInt();
      gapList.add(n);
      arrList.add(n);
    }

    for (int i = 0; i < testSetSize; i ++) {
      int p = r.nextInt(gapList.size() - modifySize);
      gapList.removeAll(p, p + modifySize);
      for (int j = 0; j < modifySize; j ++) {
        arrList.remove(p);
      }
    }

    verify("Randomly choose some positins and then remove some elements started from that position", arrList, gapList);
  }

  private void verify(String msg, List<Integer> expected, List<Integer> actual) {
    String failedMsg = "Failed: " + msg;
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i ++) {
      assertEquals(failedMsg, expected.get(i), actual.get(i));
    }
  }

}
