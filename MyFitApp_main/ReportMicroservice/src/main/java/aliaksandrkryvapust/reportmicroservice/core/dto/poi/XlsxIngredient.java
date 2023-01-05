package aliaksandrkryvapust.reportmicroservice.core.dto.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSingleField;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XlsxIngredient {
    @XlsxSingleField(columnIndex = 9)
    private Integer weight;
    @XlsxSingleField(columnIndex = 10)
    private String productTitle;
    @XlsxSingleField(columnIndex = 11)
    private Integer productCalories;
    @XlsxSingleField(columnIndex = 12)
    private Double productProteins;
    @XlsxSingleField(columnIndex = 13)
    private Double productFats;
    @XlsxSingleField(columnIndex = 14)
    private Double productCarbohydrates;
    @XlsxSingleField(columnIndex = 15)
    private Integer productWeight;
}
