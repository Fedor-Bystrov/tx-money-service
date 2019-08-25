package com.bank.pojo;

public class ErrorResponse {
  private String error;

  public ErrorResponse(RuntimeException exception) {
    this.error = exception.getMessage();
  }

  public String getError() {
    return error;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
      "error='" + error + '\'' +
      '}';
  }
}
