package net.kicchi.toptal.modals;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.swing.Spring;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Order {
  @JsonProperty("id")
  private long id;
  @JsonProperty("petId")
  private long petId;
  @JsonProperty("quantity")
  private int quantity;
  @JsonProperty("shipDate")
  private String shipDate;
  @JsonProperty("status")
  private String status;
  @JsonProperty("complete")
  private boolean complete;

  public static Order getSampleOrder(){
    //sample order id: 1767623249526899811
    return Order.builder().petId(9223372036854234168L).quantity(2).shipDate("2022-11-20T19:11:44.103Z").status("available").complete(false).build();
  }
}
