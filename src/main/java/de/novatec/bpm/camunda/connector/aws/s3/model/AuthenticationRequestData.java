package de.novatec.bpm.camunda.connector.aws.s3.model;

import io.camunda.connector.api.annotation.Secret;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class AuthenticationRequestData {
  @NotEmpty
  private String accessKey;
  @NotEmpty
  private String secretKey;

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(final String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(final String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AuthenticationRequestData that = (AuthenticationRequestData) o;
    return Objects.equals(accessKey, that.accessKey) && Objects.equals(secretKey, that.secretKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKey, secretKey);
  }

  @Override
  public String toString() {
    return "AuthenticationRequestData{" +
            "accessKey=[REDACTED], " +
            "secretKey=[REDACTED]" +
            "}";
  }
}
