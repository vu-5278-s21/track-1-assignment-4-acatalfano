package edu.vanderbilt.cs.live6;

import java.util.stream.Stream;

public interface PrecisionTree<T> {
    Stream<T> itemsWithinRange(String locationCodePrefix, int precision);
    T itemsAtLocation(String locationCode);
}
