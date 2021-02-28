package edu.vanderbilt.cs.live6;

import edu.vanderbilt.cs.live6.example.Building;

import org.hamcrest.BaseMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

public class ProximityDBTest {

    private ProximityDBFactory factory = new ProximityDBFactory();

    @Test
    public void testSimpleInsert() {
        int bitsOfPrecision = 16;
        ProximityDB<Building> db = factory.create(bitsOfPrecision);
        db.insert(DataAndPosition.with(0, 0, new Building("test")));

        for(int i = 0; i < bitsOfPrecision; i++) {
            assertTrue(db.contains(Position.with(0, 0), i));
        }
    }

    @Test
    public void testSimpleDelete() {
        int bitsOfPrecision = 16;
        ProximityDB<Building> db = factory.create(bitsOfPrecision);
        db.insert(DataAndPosition.with(0, 0, new Building("test")));
        db.delete(Position.with(0, 0));

        for(int i = 0; i < bitsOfPrecision; i++) {
            assertTrue(!db.contains(Position.with(0, 0), i));
        }
    }

    @Test
    public void testZeroBits() {
        ProximityDB<Building> db = factory.create(16);
        db.insert(DataAndPosition.with(0, 0, new Building("test")));
        db.insert(DataAndPosition.with(90, 180, new Building("test")));
        db.insert(DataAndPosition.with(-90, -180, new Building("test")));
        db.insert(DataAndPosition.with(-90, 180, new Building("test")));
        db.insert(DataAndPosition.with(90, -180, new Building("test")));

        assertEquals(5, db.nearby(Position.with(0, 0), 0).size());
    }

    @Test
    public void testZeroBitsDelete() {
        ProximityDB<Building> db = factory.create(16);
        db.insert(DataAndPosition.with(0, 0, new Building("test")));
        db.insert(DataAndPosition.with(90, 180, new Building("test")));
        db.insert(DataAndPosition.with(-90, -180, new Building("test")));
        db.insert(DataAndPosition.with(-90, 180, new Building("test")));
        db.insert(DataAndPosition.with(90, -180, new Building("test")));

        db.delete(Position.with(0, 0), 0);

        assertEquals(0, db.nearby(Position.with(0, 0), 0).size());
    }

    @Test
    public void testInsertDeleteSeries() {

        ProximityDB<Building> db = factory.create(16);
        db.insert(DataAndPosition.with(0, 0, new Building("test")));
        db.insert(DataAndPosition.with(90, 180, new Building("test")));
        db.insert(DataAndPosition.with(-90, -180, new Building("test")));
        db.insert(DataAndPosition.with(-90, 180, new Building("test")));
        db.insert(DataAndPosition.with(90, -180, new Building("test")));

        assertTrue(db.contains(Position.with(0, 0), 16));
        assertTrue(db.contains(Position.with(90, 180), 16));
        assertTrue(db.contains(Position.with(-90, -180), 16));
        assertTrue(db.contains(Position.with(-90, 180), 16));
        assertTrue(db.contains(Position.with(90, -180), 16));
        assertTrue(db.contains(Position.with(90.5, -180.5), 16));
        assertTrue(!db.contains(Position.with(1, -1), 16));
        assertTrue(!db.contains(Position.with(45, -45), 16));

        db.delete(Position.with(90, -180));
        assertTrue(!db.contains(Position.with(90, -180), 16));

        db.delete(Position.with(1, 1), 1);
        assertTrue(db.contains(Position.with(-90, -180), 16));
        assertTrue(!db.contains(Position.with(90, 180), 16));
        db.insert(DataAndPosition.with(90, 180, new Building("test")));
        assertTrue(db.contains(Position.with(90, 180), 16));

    }

    @Test
    public void testInsertAtSamePosition() {
        ProximityDB<Building> db = factory.create(16);
        db.insert(DataAndPosition.with(0, 0, new Building("first building")));
        db.insert(DataAndPosition.with(0, 0, new Building("second building")));
        Collection<DataAndPosition<Building>> deletions = db.delete(Position.with(0, 0));
        assertSame(2, deletions.size());
        Iterator<DataAndPosition<Building>> iterator = deletions.iterator();
        assertSame("first building", iterator.next().getData().getName());
        assertSame("second building", iterator.next().getData().getName());
    }

}
