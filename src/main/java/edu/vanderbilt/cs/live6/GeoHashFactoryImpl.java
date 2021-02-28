package edu.vanderbilt.cs.live6;

public class GeoHashFactoryImpl implements GeoHashFactory {

    @Override
    public GeoHash with(
        Position pos,
        int bitsOfPrecision
    ) {
        return new GeoHashImpl(pos.getLatitude(), pos.getLongitude(), bitsOfPrecision);
    }

}
