package edu.vanderbilt.cs.live6;

public interface GeoHashFactory {

    /**
     * @param lat
     * @param lon
     * @param bitsOfPrecision
     * 
     * @return
     */
    public GeoHash with(Position position, int bitsOfPrecision);

}
