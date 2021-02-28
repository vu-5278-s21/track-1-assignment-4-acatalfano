package edu.vanderbilt.cs.live6;

import java.util.Collection;

public class ProximityDBFactory {
    /**
     * @param <T>
     * 
     * @return
     */
    public <T> ProximityDB<T> create(int bits) {
        final PrecisionTreeFactory<Collection<GeohashEntry<T>>> precisionTreeFactory =
            new HashSetPrecisionTreeFactory<>();
        return new ProximityDbTree<>(
            precisionTreeFactory, new GeoHashFactoryImpl(), bits
        );
    }

}
