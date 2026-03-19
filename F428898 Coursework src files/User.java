/**
 * Represents a user of the appliance store system.
 * Stores user-related information such as name, contact details, and credentials.
 * May be extended or used by other classes like Customer or Admin.
 */

abstract class User {
    protected String ID;
    protected String username;
    protected String name;
    protected String role;
    protected String houseNumber;
    protected String postcode;
    protected String city;

    // Constructor
    public User(String ID, String username, String name, String houseNumber, String postcode, String city, String role) {
        this.ID = ID;
        this.username = username;
        this.name = name;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }
}

