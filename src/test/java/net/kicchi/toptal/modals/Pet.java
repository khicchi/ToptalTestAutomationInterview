package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Pet {
  @JsonProperty("id")
  private int id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("photoUrls")
  private List<String> photoUrls;
  @JsonProperty("tags")
  private List<Tag> tags;
  @JsonProperty("status")
  private String status;
  @JsonProperty("category")
  private Category category;
}

class Category{
  @JsonProperty("id")
  private int id;
  @JsonProperty("name")
  private String name;
}

@Getter
class Tag{
  @JsonProperty("id")
  private int id;
  @JsonProperty("name")
  private String name;
}

/*
{
  "id": 1,
  "category": {
    "id": 1,
    "name": "string"
  },
  "name": "doggie",
  "photoUrls": [
    "string"
  ],
  "tags": [
    {
      "id": 1,
      "name": "string"
    }
  ],
  "status": "available"
}
 */