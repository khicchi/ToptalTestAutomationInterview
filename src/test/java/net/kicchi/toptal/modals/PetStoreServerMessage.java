package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetStoreServerMessage {
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

  public String extractSessionId(){
    return message.split(":")[1];
  }
}
