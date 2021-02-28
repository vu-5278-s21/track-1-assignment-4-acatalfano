package edu.vanderbilt.cs.live6;


public class ProximityDBFactory {

    /**
     * @ToDo:
     *
     *            Fill this in to create one of your implementations
     *
     * @param <T>
     * 
     * @return
     */
    public <T> ProximityDB<T> create(int bits) {
        return ProximityDbTree.with(new GeoHashFactoryImpl(), bits);
    }

}
