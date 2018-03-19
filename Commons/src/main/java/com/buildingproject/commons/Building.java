package com.buildingproject.commons;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Building {
  /**
   * 15.02.2018
   * <p>Id of the building</p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated building ID")
  private long id;
  /**
   * 15.02.2018
   * Name of the building
   */
  @ApiModelProperty(notes = "The building name")
  private String name;
  /**
   * 15.02.2018
   * Address of the building
   */
  @ApiModelProperty(notes = "The building address")
  private String address;

  public Building() {

  }

  public Building(final String name, final String address) {
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
  public boolean equals(Object objectComparing) {
    if (this == objectComparing) {
      return true;
    }
    if (!(objectComparing instanceof Building)) {
      return false;
    }

    Building building = (Building) objectComparing;

    return id == building.id && name.equals(building.name) && address.equals(building.address);
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + name.hashCode();
    result = 31 * result + address.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("Building[id = %d, name = '%s', address = '%s']", id, name, address);
  }
}
