package net.kicchi.toptal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDo {

  private int id;
  private String title;
  private boolean completed;
  private int userId;

  public ToDo(String title, int userId) {
    this.title = title;
    this.completed = completed;
    this.userId = userId;
  }

    /* json object
    {
        "userId": 1,
        "id": 10,
        "title": "illo est ratione doloremque quia maiores aut",
        "completed": true
      }
     */
}
