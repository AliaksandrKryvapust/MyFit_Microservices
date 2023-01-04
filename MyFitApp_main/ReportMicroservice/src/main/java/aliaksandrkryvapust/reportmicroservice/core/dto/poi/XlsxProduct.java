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
public class XlsxProduct {
    @XlsxSingleField(columnIndex = 2)
    private String title;
    @XlsxSingleField(columnIndex = 3)
    private Integer calories;
    @XlsxSingleField(columnIndex = 4)
    private Double proteins;
    @XlsxSingleField(columnIndex = 5)
    private Double fats;
    @XlsxSingleField(columnIndex = 6)
    private Double carbohydrates;
    @XlsxSingleField(columnIndex = 7)
    private Integer weight;
}
