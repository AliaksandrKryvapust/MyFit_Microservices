package aliaksandrkryvapust.reportmicroservice.core.dto.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSheet;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSingleField;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XlsxSheet(value = "Product")
public class XlsxIngredientProduct {
    @XlsxSingleField(columnIndex = 10)
    private String title;
    @XlsxSingleField(columnIndex = 11)
    private Integer calories;
    @XlsxSingleField(columnIndex = 12)
    private Double proteins;
    @XlsxSingleField(columnIndex = 13)
    private Double fats;
    @XlsxSingleField(columnIndex = 14)
    private Double carbohydrates;
    @XlsxSingleField(columnIndex = 15)
    private Integer weight;
}
