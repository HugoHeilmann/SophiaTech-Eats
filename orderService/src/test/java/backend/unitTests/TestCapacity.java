package backend.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.Capacity;
import backend.Restaurant;
import backend.strategy.StudentStrategy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestCapacity {
    Capacity capacity;
    Restaurant restaurant = new Restaurant("test", "test", "test", "test", new ArrayList<>(), new Capacity(), new StudentStrategy(), "", "");
    int[][] staff = new int[7][48];

    @BeforeEach
    void setUp() {
        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                if (halfHour > 17 && halfHour < 46) {// Entre 9h et 23h inclus
                    staff[day][halfHour] = 100 * (day + 1);
                } else {
                    staff[day][halfHour] = 0;
                }
            }
        }
        // Chaque jour : 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 x00 x00 x00 x00 x00 x00
        //              x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 x00 0 0
        capacity = new Capacity(staff);
        restaurant.getCapacity().setCapacity(staff);
    }

    @Test
    void test_get_capacity_for_day() {
        int[] monday = capacity.getCapacityForDay(0);
        for (int i = 0; i < 48; i++) {
            if (i > 17 && i < 46) {
                assertEquals(100, monday[i]);
            } else {
                assertEquals(0, monday[i]);
            }
        }
    }

    @Test
    void test_get_capacity_for_slot() {
        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                if (halfHour > 17 && halfHour < 46) {
                    assertEquals(100 * (day + 1), capacity.getCapacityForSlot(day, halfHour));
                } else {
                    assertEquals(0, capacity.getCapacityForSlot(day, halfHour));
                }
            }
        }
    }

    @Test
    void test_set_closed_moments() {
        Map<Integer, LocalTime[]> schedule = new HashMap<>();
        LocalTime[] daySchedule = new LocalTime[2];
        daySchedule[0] = LocalTime.of(11, 0);
        daySchedule[1] = LocalTime.of(12, 0);
        for (int day = 0; day < 7; day++) {
            schedule.put(day, daySchedule);
        }

        capacity.setClosedMoments(schedule);
        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                if (halfHour > 21 && halfHour < 24) {
                    assertEquals(100 * (day + 1), capacity.getCapacityForSlot(day, halfHour));
                } else {
                    assertEquals(0, capacity.getCapacityForSlot(day, halfHour));
                }
            }
        }
    }

    @Test
    void test_update_for_all_days_smaller() {
        Map<Integer, LocalTime[]> previous = new HashMap<>();
        Map<Integer, LocalTime[]> futur = new HashMap<>();

        LocalTime previousOpen = LocalTime.of(9, 0);
        LocalTime previousClose = LocalTime.of(23, 0);
        LocalTime[] previousSchedule = new LocalTime[2];
        previousSchedule[0] = previousOpen;
        previousSchedule[1] = previousClose;

        LocalTime futurOpen = LocalTime.of(10, 0);
        LocalTime futurClose = LocalTime.of(22, 0);
        LocalTime[] futurSchedule = new LocalTime[2];
        futurSchedule[0] = futurOpen;
        futurSchedule[1] = futurClose;

        for (int day = 0; day < 7; day++) {
            previous.put(day, previousSchedule);
            futur.put(day, futurSchedule);
        }

        capacity.updateForAllDays(previous, futur);
        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                if (halfHour > 19 && halfHour < 44) {
                    assertEquals(100 * (day + 1), capacity.getCapacityForSlot(day, halfHour));
                } else {
                    assertEquals(0, capacity.getCapacityForSlot(day, halfHour));
                }
            }
        }
    }

    @Test
    void test_update_for_all_days_larger() {
        Map<Integer, LocalTime[]> previous = new HashMap<>();
        Map<Integer, LocalTime[]> futur = new HashMap<>();

        LocalTime previousOpen = LocalTime.of(9, 0);
        LocalTime previousClose = LocalTime.of(23, 0);
        LocalTime[] previousSchedule = new LocalTime[2];
        previousSchedule[0] = previousOpen;
        previousSchedule[1] = previousClose;

        LocalTime futurOpen = LocalTime.of(8, 0);
        LocalTime futurClose = LocalTime.of(23, 30);
        LocalTime[] futurSchedule = new LocalTime[2];
        futurSchedule[0] = futurOpen;
        futurSchedule[1] = futurClose;

        for (int day = 0; day < 7; day++) {
            previous.put(day, previousSchedule);
            futur.put(day, futurSchedule);
        }
        capacity.updateForAllDays(previous, futur);

        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                if (halfHour > 15 && halfHour < 47) {
                    assertEquals(100 * (day + 1), capacity.getCapacityForSlot(day, halfHour));
                } else {
                    assertEquals(0, capacity.getCapacityForSlot(day, halfHour));
                }
            }
        }
    }
}
