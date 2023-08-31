package mes.smartmes.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IngredientsDTO {


    private int ingredientId;

    private String ingredientName;                          // 재료명

    private String productId;                               // 제품 id


}
