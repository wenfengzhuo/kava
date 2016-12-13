# kava
Aiming to implement some drop-in replacements for Java built-in API

### GapList
GapList is a Java implementation for [Gap Buffer](https://en.wikipedia.org/wiki/Gap_buffer). It allows efficient 
insertion and deletion operations clustered near the same location. As a result, *Gap Buffer* is commoly used in
text editor.

=> **Usage**

As the implementation implements *java.util.List* interface, it could be a drop-in replacement of Java built-in 
java.util.ArrayList.

```java
List<Integer> gapList = new GapList<Integer>();
gapList.add(1);
gapList.remove(0);
gapList.addAll(Arrays.asList(0, 1, 2, 3));
gapList.add(0, 100); // achieve high performance if operations are clustered in the same location

// A new API is added. removeAll(int fromIndex, int toIndex). It allows client to remove elements 
// in the list from "fromIndex" to "toIndex"(exclusive).
((GapList<Integer>)gapList).removeAll(0, 3);
```

=> **Improve**

1. Multiple APIs should be tested
2. Should add range check for safe operation on list
3. A benchmark test is needed to reveal the performance difference between ArrayList and GapList


