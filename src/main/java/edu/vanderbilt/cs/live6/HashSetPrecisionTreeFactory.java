package edu.vanderbilt.cs.live6;

import java.util.HashSet;
import java.util.Set;

public class HashSetPrecisionTreeFactory<T> implements PrecisionTreeFactory<Set<T>> {
    @Override
    public PrecisionTree<Set<T>> with(int resolution) {
        return new ArrayPrecisionTree<>(resolution, HashSet::new);
    }
}
