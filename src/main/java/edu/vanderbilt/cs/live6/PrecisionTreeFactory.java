package edu.vanderbilt.cs.live6;

public interface PrecisionTreeFactory<T> {
    PrecisionTree<T> with(int resolution);
}
