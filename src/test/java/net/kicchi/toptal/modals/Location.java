package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class Location {


  @JsonProperty("country")
  private String country;

  @JsonProperty("country abbreviation")
  private String countryAbbreviation;

  @JsonProperty("places")
  private List<Place> places;
  @JsonProperty("place name")
  private String placeName;
  @JsonProperty("state")
  private String state;
  @JsonProperty("state abbreviation")
  private String stateAbbreviation;
}

class Place{
  @JsonProperty("place name")
  private String placeName;

  @JsonProperty("longitude")
  private String longitude;


  @JsonProperty("latitude")
  private String latitude;
  @JsonProperty("post code")
  private int postCode;
}

/*
{
  "post code": "90210",
  "country": "United States",
  "country abbreviation": "US",
    "places": [
      {
        "place name": "Beverly Hills",
        "longitude": "-118.4065",
        "state": "California",
        "state abbreviation": "CA",
        "latitude": "34.0901"
      }
    ]
}
 */