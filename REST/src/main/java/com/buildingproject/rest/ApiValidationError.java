package com.buildingproject.rest;

import lombok.Data;

@Data
public class ApiValidationError {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationError() {

  }

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }

  ApiValidationError(String object, String field, Object rejectedValue, String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }
}
