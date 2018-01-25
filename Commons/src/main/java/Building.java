public class Building {
    /***
     * Id of the building
     */
    private long id;
    /***
     * Name of the building
     */
    private String name;
    /***
     * Address of the building
     */
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
