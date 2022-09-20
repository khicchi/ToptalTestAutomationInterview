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
@NoArgsConstructor
@Getter
@Builder
public class Pet {
  @JsonProperty("id")
  @Setter
  private long id;
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

  public static Pet getSamplePet(){
    return Pet.builder().name("baba").photoUrls(Arrays.asList("ada")).category(new Category(0, "string")).status("available").tags(Arrays.asList(new Tag(0, "tag"))
        ).build();
  }
}

@AllArgsConstructor
@Getter
class Category{
  @JsonProperty("id")
  private long id;
  @JsonProperty("name")
  private String name;
}

@AllArgsConstructor
@Getter
class Tag{
  @JsonProperty("id")
  private long id;
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