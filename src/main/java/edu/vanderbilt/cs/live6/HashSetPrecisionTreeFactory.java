package edu.vanderbilt.cs.live6;

import java.util.Collection;
import java.util.HashSet;

public class HashSetPrecisionTreeFactory<T> implements
    PrecisionTreeFactory<Collection<T>> {
    @Override
    public PrecisionTree<Collection<T>> with(int resolution) {
        return new ArrayPrecisionTree<>(resolution, HashSet::new);
    }
}
