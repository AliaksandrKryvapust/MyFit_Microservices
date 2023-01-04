package aliaksandrkryvapust.reportmicroservice.core.dto.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxCompositeField;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSheet;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSingleField;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XlsxSheet(value = "Ingredient")
public class XlsxIngredient {
    @XlsxSingleField(columnIndex = 9)
    private Integer weight;
    @XlsxCompositeField(from = 10, to = 15)
    private XlsxIngredientProduct product;
}
