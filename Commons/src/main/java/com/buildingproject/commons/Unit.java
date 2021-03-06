package com.buildingproject.commons;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Data
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
public class Unit {
  /**
   * 17.05.2018
   * Id of the unit
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(notes = "The database generated unit ID")
  private long id;

  /**
   * 17.05.2018
   * Number of the unit
   */
  @ApiModelProperty(notes = "Number of the unit")
  @Positive(message = "Number of the unit must be greater than 0")
  private int number;

  /**
   * 17.05.2018
   * Number of bedrooms in the unit
   */
  @ApiModelProperty(notes = "Number of bedrooms in the unit")
  @Positive(message = "Number of the bedrooms must be greater than 0")
  @Column(name = "numberbedrooms")
  private int numberBedrooms;

  /**
   * 17.05.2018
   * Number of bedrooms in the unit
   */
  @ApiModelProperty(notes = "Area apartment of the unit")
  @Positive(message = "Area apartment must be greater than 0")
  @Column(name = "areaapartment")
  private int areaApartment;

  /**
   * 17.05.2018
   * Available of furniture in the unit
   */
  @ApiModelProperty(notes = "Availability of furniture in the unit")
  @Column(name = "furnitureavailable")
  private boolean furnitureAvailable;

  /**
   * 17.05.2018
   * allow animal in the unit
   */
  @ApiModelProperty(notes = "Can contain animals in the unit")
  @Column(name = "animalsallowed")
  private boolean animalsAllowed;

  /**
   * 17.05.2018
   * Building, where unit is located
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="idbuilding", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ApiModelProperty(notes = "Building, where locate unit")
  private Building building;

  public Unit(int number, int numberBedrooms, int areaApartment,
              boolean furnitureAvailable, boolean animalsAllowed, Building building) {
    this.number = number;
    this.numberBedrooms = numberBedrooms;
    this.areaApartment = areaApartment;
    this.furnitureAvailable = furnitureAvailable;
    this.animalsAllowed = animalsAllowed;
    this.building = building;
  }
}
