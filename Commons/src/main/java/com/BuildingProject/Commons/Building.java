package com.BuildingProject.Commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Building {
    /***
     * Id of the building
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /***
     * Name of the building
     */
    private String name;
    /***
     * Address of the building
     */
    private String address;

    protected Building() {

    }

    public Building(String name, String address){
        this.name = name;
        this.address = address;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        return id != building.id && name.equals(building.name) && address.equals(building.address);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return String.format("Building[id = %d, name = '%s', address = '%s']", id, name, address);
    }
}
