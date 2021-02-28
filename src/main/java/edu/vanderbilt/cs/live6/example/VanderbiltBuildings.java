package edu.vanderbilt.cs.live6.example;

import edu.vanderbilt.cs.live6.DataAndPosition;
import edu.vanderbilt.cs.live6.Position;
import edu.vanderbilt.cs.live6.ProximityDB;
import edu.vanderbilt.cs.live6.ProximityDBFactory;

import java.util.Collection;

public class VanderbiltBuildings {

    public static void main(String[] args) {

        // The idea behind the ProximityDB is that we can store arbitrary
        // objects in it at a location. For example, the database could store
        // the locations of buildings on Vanderbilt's campus. The type of
        // object should be up to the user and is thus a type parameter.
        // You could just as easily store RealEstateListing objects, Maps, etc.

        // It is important to note that the DataAndPosition objects that the database
        // holds are containers for assocating a position with the type of data that
        // you want to store. Although the ProximityDB<T> is parameterized by T, internally
        // it stores objects of type DataAndPosition<T>.

        Building kirklandHall = new Building("Kirkland Hall");
        Building fgh = new Building("Featheringill Hall");
        Building esb = new Building("Engineering Sciences Building");

        ProximityDBFactory factory = new ProximityDBFactory();

        ProximityDB<Building> db = factory.create(50);

        db.insert(DataAndPosition.with(36.145050, 86.803365, fgh));
        db.insert(DataAndPosition.with(36.148345, 86.802909, kirklandHall));
        db.insert(DataAndPosition.with(36.143171, 86.805772, esb));

        // Find all of the other buildings near a location
        Collection<DataAndPosition<Building>> buildingsNearFgh =
            db.nearby(Position.with(36.145050, 86.803365), 28);

        for(DataAndPosition<Building> buildingAndPos : buildingsNearFgh) {
            Building building = buildingAndPos.getData();
            System.out
                .println(
                    building.getName() + " is located at " +
                        buildingAndPos.getLatitude() + "," + buildingAndPos.getLongitude()
                );
        }
    }


}
