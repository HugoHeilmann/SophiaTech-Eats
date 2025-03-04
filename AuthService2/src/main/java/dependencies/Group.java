package dependencies;


import dependencies.customer.Customer;
import dependencies.databse.CustomerDatabase;
import dependencies.databse.GroupDatabase;
import dependencies.location.Location;
import exceptions.CustomerNotFoundException;
import exceptions.GroupNotFoundException;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Group {

    private static int ID;

    static {
        try {
            ID = GroupDatabase.getInstance().getGroups().size();
        } catch (CustomerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetId() {
        ID = 0;
    }

    /* Attributes */

    private int id;
    private LocalTime delivery;
    private Location location;
    private List<Integer> memberIds; // Liste des IDs des membres

    /* Getter */

    public int getId() {
        return id;
    }

    public LocalTime getDelivery() {
        return delivery;
    }

    public Location getLocation() {
        return location;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    /* Construction */

    // Default constructor
    public Group() {
    }

    public Group(LocalTime delivery, Location location, Customer creator) {
        this.id = ID++;
        this.delivery = delivery;
        this.location = location;
        this.memberIds = new ArrayList<>();
        this.memberIds.add(creator.getId()); // Ajouter l'ID du créateur
    }

    public static Group create(LocalTime delivery, Location location, Customer creator) throws CustomerNotFoundException {
        Group g = new Group(delivery, location, creator);
        GroupDatabase.getInstance().addGroup(g);
        return g;
    }

    public void addMember(Customer customer) throws CustomerNotFoundException, GroupNotFoundException {
        GroupDatabase.getInstance().addGroupMember(this.id, customer.getId());
    }

    public static boolean close(int groupId, int customerId, LocalTime deliveryTime) {
        try {
            Group group = GroupDatabase.getInstance().getGroupById(groupId);
            if (!group.getMemberIds().contains(customerId)) {
                return false;
            }
            if (group.getDelivery() == null) {
                group.delivery = deliveryTime;
            }
            for (int memberId : group.getMemberIds()) {
                Customer member = CustomerDatabase.getInstance().getCustomerById(memberId);
                Order.Builder orderBuilder = member.getBasket();
                if (orderBuilder != null) {
                    orderBuilder.delivery(group.getDelivery());
                }
            }
            GroupDatabase.getInstance().removeGroupById(groupId);
        } catch (GroupNotFoundException | CustomerNotFoundException e) {
            return false;
        }
        return true;
    }

    /* Data : used by the Facade design pattern */

    public record Data(int id, LocalTime delivery, Location location, List<Integer> memberIds) {

        public Group toGroup() throws GroupNotFoundException, CustomerNotFoundException {
            return GroupDatabase.getInstance().getGroupById(this.id);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(" {");

        // Utilisation de Reflection pour obtenir les champs
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Permet d'accéder aux champs privés
            try {
                sb.append("\n  ").append(field.getName()).append(": ").append(field.get(this));
            } catch (IllegalAccessException e) {
                sb.append("\n  ").append(field.getName()).append(": [inaccessible]");
            }
        }
        sb.append("\n}");
        return sb.toString();
    }

    public Data toData() {
        return new Data(this.id, this.delivery, this.location, this.memberIds);
    }
}