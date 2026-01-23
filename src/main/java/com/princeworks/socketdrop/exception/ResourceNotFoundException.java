package com.princeworks.socketdrop.exception;

public class ResourceNotFoundException extends RuntimeException {
  private final String resourceName;
  private final String identifierName;
  private final String identifierValue;

  public ResourceNotFoundException(String resourceName, String identifierName, String identifierValue) {
    super(String.format("%s not found with %s: %s", resourceName, identifierName, identifierValue));
    this.resourceName = resourceName;
    this.identifierName = identifierName;
    this.identifierValue = identifierValue;
  }

  public String getIdentifierName() {
    return identifierName;
  }

  public String getIdentifierValue() {
    return identifierValue;
  }

  public String getResourceName() {
    return resourceName;
  }
}
