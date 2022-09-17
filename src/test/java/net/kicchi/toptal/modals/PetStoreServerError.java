package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PetStoreServerError{
  @JsonProperty("code")
  private int code;
  @JsonProperty("type")
  private String type;
  @JsonProperty("message")
  private String message;

  @Override
  public String toString() {
    return "PetStoreServerError{" +
        "code=" + code +
        ", type='" + type + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
