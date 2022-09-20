package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class User {
  @JsonProperty("id")
  private long id;
  @JsonProperty("username")
  private String username;
  @JsonProperty("firstName")
  private String firstName;
  @JsonProperty("lastName")
  private String lastName;
  @JsonProperty("email")
  private String email;
  @JsonProperty("password")
  private String password;
  @JsonProperty("phone")
  @Setter
  private String phone;
  @JsonProperty("userStatus")
  private int userStatus;

  public static User getSampleUser(){
    //kicchi id: 9222968140497199506
    //current session id : 1663501096923
    return User.builder().username("kicchi").firstName("ki").lastName("cchi").email("kicchi@mail.com").password("abc123.").phone("6354635").userStatus(0).build();
  }

  public static User getSampleUser2(){
    return User.builder().username("kocchi").firstName("ko").lastName("cchi").email("kocchi@mail.com").password("abc123.").phone("6354635").userStatus(0).build();
  }

  public static List<User> getSampleUserList(){
    return Arrays.asList(getSampleUser(), getSampleUser2());
  }

  public static User[] getSampleUserArray(){
    return new User[]{
      getSampleUser(), getSampleUser2()
    };
  }
}
