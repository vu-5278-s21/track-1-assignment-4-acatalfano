package edu.vanderbilt.cs.live6;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProximityDbTree<T> implements ProximityDB<T> {
    private final int resolution;
    private final GeoHashFactory geoHashFactory;
    private final PrecisionTree<Collection<GeohashEntry<T>>> geoTree;

    // @ToDo, need to support multiple entries of the same thing
    public ProximityDbTree(
        PrecisionTreeFactory<Collection<GeohashEntry<T>>> precisionTreeFactory,
        GeoHashFactory factory,
        int precision
    ) {
        resolution = precision;
        geoHashFactory = factory;
        geoTree = precisionTreeFactory.with(precision);
    }

    @Override
    public void insert(DataAndPosition<T> data) {
        geoTree
            .itemsAtLocation(treeLocationCode(data, resolution))
            .add(new GeohashEntry<>(data, geoHashFactory.with(data, resolution)));
    }

    @Override
    public Collection<DataAndPosition<T>> delete(Position pos) {
        Collection<GeohashEntry<T>> targetSet =
            geoTree.itemsAtLocation(treeLocationCode(pos, resolution));
        List<DataAndPosition<T>> deletions = targetSet
            .stream()
            .map(GeohashEntry::getDataAndPosition)
            .collect(Collectors.toList());

        targetSet.clear();
        return deletions;
    }

    @Override
    public Collection<DataAndPosition<T>> delete(Position pos, int bitsOfPrecision) {
        List<Collection<GeohashEntry<T>>> itemsToClear = geoTree
            .itemsWithinRange(treeLocationCode(pos, bitsOfPrecision), bitsOfPrecision)
            .collect(Collectors.toList());
        List<DataAndPosition<T>> deletedEntries =
            itemsToClear
                .stream()
                .flatMap(Collection::stream)
                .map(GeohashEntry::getDataAndPosition)
                .collect(Collectors.toList());

        itemsToClear.forEach(Collection::clear);
        return deletedEntries;
    }

    @Override
    public boolean contains(Position pos, int bitsOfPrecision) {
        return geoTree
            .itemsWithinRange(treeLocationCode(pos, bitsOfPrecision), bitsOfPrecision)
            .anyMatch(set -> !set.isEmpty());
    }

    @Override
    public Collection<DataAndPosition<T>> nearby(Position pos, int bitsOfPrecision) {
        return geoTree
            .itemsWithinRange(treeLocationCode(pos, bitsOfPrecision), bitsOfPrecision)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .map(GeohashEntry::getDataAndPosition)
            .collect(Collectors.toList());
    }

    private String treeLocationCode(Position pos, int precision) {
        return geoHashFactory.with(pos, precision).toString();
    }
}
