package edu.vanderbilt.cs.live6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProximityDbTree<T> implements ProximityDB<T> {
    private final int resolution;
    private final GeoHashFactory geoHashFactory;
    private final List<Set<GeohashEntry<T>>> geoTree;

    private ProximityDbTree(
        GeoHashFactory factory,
        int precision
    ) {
        resolution = precision;
        geoHashFactory = factory;
        final int capacity = (int)Math.pow(2, resolution);
        geoTree = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++) {
            geoTree.add(new HashSet<>());
        }
    }

    public static <T> ProximityDbTree<T> with(GeoHashFactory factory, int precision) {
        return new ProximityDbTree<>(factory, precision);
    }

    @Override
    public void insert(DataAndPosition<T> data) {
        int index = index(data);
        geoTree
            .get(index)
            .add(new GeohashEntry<>(data, geoHashFactory.with(data, resolution)));

    }

    @Override
    public Collection<DataAndPosition<T>> delete(Position pos) {
        int index = index(pos);
        Set<GeohashEntry<T>> targetSet = geoTree.get(index);
        List<DataAndPosition<T>> deletions = targetSet
            .stream()
            .map(GeohashEntry::getDataAndPosition)
            .collect(Collectors.toList());

        targetSet.clear();
        return deletions;
    }

    @Override
    public Collection<DataAndPosition<T>> delete(Position pos, int bitsOfPrecision) {
        int rangeStartIndex = rangeStartIndex(pos, bitsOfPrecision);
        int rangeEndIndex = rangeEndIndex(rangeStartIndex, bitsOfPrecision);
        List<DataAndPosition<T>> deletedEntries = geoTree
            .subList(rangeStartIndex, rangeEndIndex)
            .stream()
            .filter(Objects::nonNull)
            .flatMap(Set::stream)
            .map(GeohashEntry::getDataAndPosition)
            .collect(Collectors.toList());

        for(int i = rangeStartIndex; i < rangeEndIndex; i++) {
            geoTree.get(i).clear();
        }
        return deletedEntries;
    }

    @Override
    public boolean contains(Position pos, int bitsOfPrecision) {
        int rangeStartIndex = rangeStartIndex(pos, bitsOfPrecision);
        int rangeEndIndex = rangeEndIndex(rangeStartIndex, bitsOfPrecision);
        return geoTree
            .subList(rangeStartIndex, rangeEndIndex)
            .stream()
            .anyMatch(set -> !set.isEmpty());
    }

    @Override
    public Collection<DataAndPosition<T>> nearby(Position pos, int bitsOfPrecision) {
        int rangeStartIndex = rangeStartIndex(pos, bitsOfPrecision);
        int rangeEndIndex = rangeEndIndex(rangeStartIndex, bitsOfPrecision);
        return geoTree
            .subList(rangeStartIndex, rangeEndIndex)
            .stream()
            .filter(Objects::nonNull)
            .flatMap(Set::stream)
            .map(GeohashEntry::getDataAndPosition)
            .collect(Collectors.toList());
    }

    /**
     * @Return index in the physical list where the provided position should be stored (as
     *             per underlying tree semantics)
     */
    private int index(Position pos) {
        return rangeStartIndex(pos, resolution);
    }

    /**
     * @Return list index where the range starts for all stored positions that match the
     *             provided position up to the provided precision
     * 
     * @Assume precision <= resolution
     */
    private int rangeStartIndex(
        Position pos,
        int precision
    ) {
        String geohashString =
            zeroPaddedGeohashString(pos, precision);
        return Integer.parseInt(geohashString, 2);
    }

    /**
     * @Return list index where the range ends for all stored positions that match the
     *             provided start index up to the provided precision
     * 
     * @Assume precision <= resolution
     */
    private int rangeEndIndex(
        int startIndex,
        int precision
    ) {
        return (int)Math.pow(2, (resolution - precision)) + startIndex;
    }

    /**
     * @Return geohash at specified position and precision, as a string representing a
     *             binary number. The string is padded with 0's (at the end of the string)
     *             until it matches the DB's resolution
     * 
     * @Assume precision <= resolution
     */
    private String zeroPaddedGeohashString(
        Position pos,
        int precision
    ) {
        String unpaddedGeohash =
            geoHashFactory.with(pos, precision).toString();
        StringBuilder paddedGeohashBuilder = new StringBuilder(unpaddedGeohash);
        char[] padding = new char[resolution - precision];
        Arrays.fill(padding, '0');
        paddedGeohashBuilder.append(padding);
        return paddedGeohashBuilder.toString();
    }
}
