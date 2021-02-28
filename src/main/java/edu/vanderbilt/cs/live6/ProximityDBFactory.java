package edu.vanderbilt.cs.live6;

import java.util.Set;

public class ProximityDBFactory {
    /**
     * @param <T>
     * 
     * @return
     */
    public <T> ProximityDB<T> create(int bits) {
        final PrecisionTreeFactory<Set<GeohashEntry<T>>> precisionTreeFactory =
            new HashSetPrecisionTreeFactory<>();
        return new ProximityDbTree<>(
            precisionTreeFactory, new GeoHashFactoryImpl(), bits
        );
    }

}
