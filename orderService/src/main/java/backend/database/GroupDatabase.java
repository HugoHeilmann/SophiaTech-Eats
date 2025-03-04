package backend.database;

import com.fasterxml.jackson.core.type.TypeReference;
import backend.*;
import backend.customer.Customer;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.GroupNotFoundException;
import backend.utils.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GroupDatabase {

    /* Singleton design pattern */
    Logger logger = Logger.getLogger(CustomerDatabase.class.getName());
    private static String DATABASE_FILE = "";
    static {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.") ||
                    element.getClassName().startsWith("org.testng.")) {
                DATABASE_FILE = "src/test/resources/data/groups.json";
            }
        }
        if (DATABASE_FILE == "") {
            DATABASE_FILE = "../database/groups.json";
        }
    }
    private static GroupDatabase instance;

    public static GroupDatabase getInstance() throws CustomerNotFoundException {
        if (instance == null) {
            instance = new GroupDatabase();
            instance.loadGroups();
        }
        return instance;
    }

    private GroupDatabase() {
    }

    private List<Group> groups = new ArrayList<>();

    public List<Group> getGroups() {
        return groups;
    }

    public void initializeGroups() throws CustomerNotFoundException {
        Group.resetId();
        groups = new ArrayList<>();
        saveGroups();
    }

    private void loadGroups() throws CustomerNotFoundException {
        try {
            File file = new File(DATABASE_FILE);
            groups = JsonUtil.readFromFile(file, new TypeReference<List<Group>>() {
            });
        } catch (IOException e) {
            logger.info("Error loading groups: " + e.getMessage());
            initializeGroups();
        }
    }

    public void saveGroups() {
        try {
            JsonUtil.writeToFile(new File(DATABASE_FILE), groups);
        } catch (IOException e) {
            logger.info("Error saving groups: " + e.getMessage());
        }
    }


    public void addGroup(Group group) {
        groups.add(group);
        saveGroups();
    }


    /**
     * @param id a group's id
     * @throws GroupNotFoundException if the group is not found
     */
    public void removeGroupById(int id) throws GroupNotFoundException {
        groups.remove(getGroupById(id));
        saveGroups();
    }


    /**
     * @param id a group's id
     * @return the group which has the id
     * @throws GroupNotFoundException if the group is not found
     */
    public Group getGroupById(int id) throws GroupNotFoundException {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        throw new GroupNotFoundException("Group with id " + id + " not found");
    }

    /**
     * @param GroupId a group's id
     * @param CustomerId a customer's id
     * @return nothing
     * @throws GroupNotFoundException if the group is not found
     * @throws CustomerNotFoundException if the group is not found
     */
    public void addGroupMember(int GroupId, int CustomerId) throws GroupNotFoundException, CustomerNotFoundException {
        Group group = null;
        for (Group g : groups) {
            if (g.getId() == GroupId) {
                group = g;
                break;
            }
        }
        if (group == null) {
            throw new GroupNotFoundException("Group with id " + GroupId + " not found");
        }

        Customer customer = null;
        for (Customer c : CustomerDatabase.getInstance().getCustomers()) {
            if (c.getId() == CustomerId) {
                customer = c;
                break;
            }
        }
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with id " + CustomerId + " not found");
        }
        if (!group.getMemberIds().contains(CustomerId)) {
            group.getMemberIds().add(CustomerId);
        }

        try {
            JsonUtil.writeToFile(new File(DATABASE_FILE), groups);
        } catch (IOException e) {
            logger.info("Error saving groups: " + e.getMessage());
        }
    }
}
