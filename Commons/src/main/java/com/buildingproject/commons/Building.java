package com.buildingproject.commons;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Entity
public class Building {
  /**
   * 15.02.2018
   * Id of the building
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated building ID")
  private long id;
  /**
   * 15.02.2018
   * Name of the building
   */
  @NotBlank(message = "Name of the building cannot be null or whitespace")
  @Size(max = 50, message = "Name of the building must be less than 50 characters")
  @ApiModelProperty(notes = "The building name")
  private String name;
  /**
   * 15.02.2018
   * Address of the building
   */
  @NotBlank(message = "Address of the building cannot be null or whitespace")
  @Size(max = 150, message = "Address of the building must be less than 150 characters")
  @ApiModelProperty(notes = "The building address")
  private String address;
  /**
   * 02.04.2018
   * Number of units
   */
  @PositiveOrZero(message = "Number of the units must be greater than 0 or equal 0")
  @Digits(integer = 3, fraction = 0, message = "Number of units must be number")
  @ApiModelProperty(notes = "Number of units in the building")
  @Column(name = "numberunits")
  private int numberUnits;
  /**
   * 02.04.2018
   * Number of residents
   */
  @PositiveOrZero(message = "Number of residents must be greater than 0 or equal 0")
  @Digits(integer = 4, fraction = 0, message = "Number of residents must be number")
  @ApiModelProperty(notes = "Number of residents in the building")
  @Column(name = "numberresidents")
  private int numberResidents;

  /**
   * 23.04.2018
   * Image of the building
   */
  @ApiModelProperty(notes = "Image of the building")
  @Lob
  private byte[] image;

  /**
   * 01.05.2018
   * Type of the image
   */
  @ApiModelProperty(notes = "Type of the image")
  @Column(name = "typeimage")
  private String typeImage;

  public Building() {

  }

  /**
   * Create Building
   * @param name name of the building
   * @param address address of the building
   * @param numberUnits number units
   * @param numberResidents number residents
   */
  public Building(final String name, final String address,
                  final int numberUnits, final int numberResidents) {
    this.name = name;
    this.address = address;
    this.numberUnits = numberUnits;
    this.numberResidents = numberResidents;
  }

  public Building(final String name, final String address, final int numberUnits, final int numberResidents,
                  final byte[] image){
    this(name,address,numberUnits,numberResidents);
    this.image = image;
  }

  public Building(final String name, final String address, final int numberUnits, final int numberResidents,
                  final byte[] image, final String typeImage){
    this(name,address,numberUnits,numberResidents,image);
    this.typeImage = typeImage;
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

  public int getNumberUnits() {
    return numberUnits;
  }

  public void setNumberUnits(int numberUnits) {
    this.numberUnits = numberUnits;
  }

  public int getNumberResidents() {
    return numberResidents;
  }

  public void setNumberResidents(int numberResidents) {
    this.numberResidents = numberResidents;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getTypeImage() {
    return typeImage;
  }

  public void setTypeImage(String typeImage) {
    this.typeImage = typeImage;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Building building = (Building) object;

    boolean isEqual = true;
    if (name == null && building.name == null) {
      isEqual = true;
    } else if (name == null || building.name == null) {
      isEqual = false;
    } else {
      isEqual = name.equals(building.name);
    }

    if (isEqual) {
      if (address == null && building.address == null) {
        isEqual = true;
      } else if (address == null || building.address == null) {
        isEqual = false;
      } else {
        isEqual = address.equals(building.address);
      }
    }

    if (isEqual) {
      if(image == null && building.image == null) {
        isEqual = true;
      } else if (image == null || building.image == null) {
        isEqual = false;
      } else {
        isEqual = Arrays.equals(image, building.image);
      }
    }

    if (isEqual) {
      if(typeImage == null && building.typeImage == null) {
        isEqual = true;
      } else if (typeImage == null || building.typeImage == null) {
        isEqual = false;
      } else {
        isEqual = typeImage.equals(building.typeImage);
      }
    }

    return isEqual && numberResidents == building.numberResidents
        && numberUnits == building.numberUnits;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (name == null ? 0 : name.hashCode());
    result = 31 * result + (address == null ? 0 : address.hashCode());
    result = 31 * result + numberUnits;
    result = 31 * result + numberResidents;
    result = 31 * result + (image == null ? 0 : Arrays.hashCode(image));
    result = 31 * result + (typeImage == null ? 0 : typeImage.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return String.format("Building[id = %d, name = '%s', address = '%s', numberUnits = '%d'"
        + ", numberResidents = '%d']", id, name, address, numberUnits, numberResidents);
  }
}
