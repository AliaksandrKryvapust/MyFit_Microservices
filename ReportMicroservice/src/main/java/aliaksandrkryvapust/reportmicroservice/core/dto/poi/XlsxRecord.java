package aliaksandrkryvapust.reportmicroservice.core.dto.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxCompositeField;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSheet;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSingleField;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XlsxSheet(value = "Record")
public class XlsxRecord {
    @XlsxSingleField(columnIndex = 0)
    private Integer recordWeight;
    @XlsxSingleField(columnIndex = 1)
    private String dtSupply;
    @XlsxSingleField(columnIndex = 2)
    private String productTitle;
    @XlsxSingleField(columnIndex = 3)
    private Integer productCalories;
    @XlsxSingleField(columnIndex = 4)
    private Double productProteins;
    @XlsxSingleField(columnIndex = 5)
    private Double productFats;
    @XlsxSingleField(columnIndex = 6)
    private Double productCarbohydrates;
    @XlsxSingleField(columnIndex = 7)
    private Integer productWeight;
    @XlsxSingleField(columnIndex = 8)
    private String mealTitle;
    @XlsxCompositeField(from = 9, to = 15)
    private List<XlsxIngredient> composition;
}

