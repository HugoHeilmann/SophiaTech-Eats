package backend;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Capacity {

    // Temps de préparation de menu qu'un membre du personnel peut prendre en charge
    private final int PREPARATION_TIME_FOR_STAFF_MEMBER = 300;
    private static final Map<LocalTime, Integer> SLOT_NUMBER_FOR_TIME = new HashMap<>();

    static {
        LocalTime[] times = new LocalTime[48];
        int index = 0;
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                times[index] = LocalTime.of(hour, minute);
                index++;
            }
        }
        for (int slot = 0; slot < times.length; slot++) {
            SLOT_NUMBER_FOR_TIME.put(times[slot], slot);
        }
    }

    private int[][] capacity;

    public Capacity(int[][] staff) {
        capacity = staff;
    }

    /**
     * Set capacity for all week with a random value
     */
    public Capacity() {
        int[][] staff = new int[7][48];
        // Temps de préparation de commande qu'un membre du perosnnel peut prendre en charge

        for (int day = 0; day < 7; day++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                staff[day][halfHour] = 5 * PREPARATION_TIME_FOR_STAFF_MEMBER;
            }
        }
        capacity = staff;
    }

    public int[][] getCapacity() {
        return capacity;
    }

    /**
     * Get the capacity for the current time and day
     *
     * @return the capacity for the actual day and time
     */
    public int getCapacityForNow() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        int dayNumber = dayOfWeek.getValue();
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (time.getMinute() < 30) {
            time = time.withMinute(0);
        } else {
            time = time.withMinute(30);
        }
        return getCapacityForSlot(dayNumber - 1, SLOT_NUMBER_FOR_TIME.get(time));
    }

    /**
     * Get the capacity for a specified day
     *
     * @param dayNumber the day for which we want to retrieve the capacity
     * @return a table of the capacity for the specified day
     */
    public int[] getCapacityForDay(int dayNumber) {
        return capacity[dayNumber];
    }

    /**
     * Get the capacity for a specified day and a specified half-hour
     *
     * @param dayNumber  the day for which we want to retrieve the capacity
     * @param slotNumber the half-hour number for which we want to retrieve the capacity
     * @return the capacity for the specified day and half-hour
     */
    public int getCapacityForSlot(int dayNumber, int slotNumber) {
        return capacity[dayNumber][slotNumber];
    }


    /**
     * Set the capacity for a single slot
     *
     * @param dayNumber   the day for which we want to update the capacity
     * @param slotNumber  the half-hour number for which we want to update the capacity
     * @param newCapacity the new value for the capacity
     */
    public void setCapacityForSlot(int dayNumber, int slotNumber, int newCapacity) {
        capacity[dayNumber][slotNumber] = newCapacity * PREPARATION_TIME_FOR_STAFF_MEMBER;
    }

    /**
     * Set to 0 the number of staff member available when the restaurant is closed
     *
     * @param weekSchedule represent the schedule of the restaurant
     */

    public void setClosedMoments(Map<Integer, LocalTime[]> weekSchedule) {
        for (Map.Entry<Integer, LocalTime[]> entry : weekSchedule.entrySet()) {
            int day = entry.getKey();
            LocalTime[] schedule = entry.getValue();
            setClosedMomentsForDay(day, schedule);
        }
    }

    /**
     * Set to 0 the number of staff member available when the restaurant is closed
     *
     * @param dayNumber represents the day when the closed moment is set
     * @param schedule  represents the schedule of the modified day
     */
    public void setClosedMomentsForDay(int dayNumber, LocalTime[] schedule) {
        int openingIndex = SLOT_NUMBER_FOR_TIME.get(schedule[0]);
        int closingIndex = SLOT_NUMBER_FOR_TIME.get(schedule[1]);

        // Mettre à 0 toutes les demi-heures avant l'ouverture et après la fermeture
        // Fermé avant l'ouverture
        for (int i = 0; i < openingIndex; i++) {
            capacity[dayNumber][i] = 0;
        }

        // Fermé après la fermeture
        for (int i = closingIndex; i < 48; i++) {
            capacity[dayNumber][i] = 0;
        }
    }


    /**
     * update a restaurant's capacity whenever the schedule is modified for a specified day
     *
     * @param day              day for which the schedule has been modified
     * @param previousSchedule schedule before modification
     * @param newSchedule      schedule after modification
     */
    public void update(int day, LocalTime[] previousSchedule, LocalTime[] newSchedule) {
        int oldOpeningIndex = SLOT_NUMBER_FOR_TIME.get(previousSchedule[0]);
        int oldClosingIndex = SLOT_NUMBER_FOR_TIME.get(previousSchedule[1]);

        int newOpeningIndex = SLOT_NUMBER_FOR_TIME.get(newSchedule[0]);
        int newClosingIndex = SLOT_NUMBER_FOR_TIME.get(newSchedule[1]);

        int firstOpenIndex = -1;
        int lastOpenIndex = -1;

        for (int i = 0; i < 48; i++) {
            if (capacity[day][i] != 0) {
                firstOpenIndex = i;
                break;
            }
        }

        for (int i = 47; i >= 0; i--) {
            if (capacity[day][i] != 0) {
                lastOpenIndex = i;
                break;
            }
        }

        // Si le nouvel horaire ouvre plus tard que l'ancien, on ferme les demi-heures avant le nouvel horaire
        for (int i = oldOpeningIndex; i < newOpeningIndex; i++) {
            capacity[day][i] = 0;  // Le restaurant est désormais fermé dans ces slots
        }

        // Si le nouvel horaire ferme plus tôt que l'ancien, on ferme les demi-heures après le nouvel horaire
        for (int i = newClosingIndex; i < oldClosingIndex; i++) {
            capacity[day][i] = 0;  // Fermé dans ces slots
        }

        // Si le nouvel horaire ouvre plus tôt que l'ancien, on ouvre les demi-heures correspondantes
        for (int i = newOpeningIndex; i < oldOpeningIndex; i++) {
            capacity[day][i] = capacity[day][firstOpenIndex];  // Le restaurant est ouvert dans ces slots
        }

        // Si le nouvel horaire ferme plus tard que l'ancien, on ouvre les demi-heures après l'ancien horaire de fermeture
        for (int i = oldClosingIndex; i < newClosingIndex; i++) {
            capacity[day][i] = capacity[day][lastOpenIndex];  // Ouvert dans ces slots
        }
    }
}