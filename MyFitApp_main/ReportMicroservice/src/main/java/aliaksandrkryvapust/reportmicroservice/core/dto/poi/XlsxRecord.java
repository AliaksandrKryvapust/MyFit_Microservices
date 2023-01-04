package aliaksandrkryvapust.reportmicroservice.core.dto.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxCompositeField;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSheet;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSingleField;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XlsxSheet(value = "Record")
public class XlsxRecord {
    @XlsxSingleField(columnIndex = 0)
    private Integer weight;
    @XlsxSingleField(columnIndex = 1)
    private Instant dtSupply;
    @XlsxCompositeField(from = 2, to = 7)
    private XlsxProduct product;
    @XlsxCompositeField(from = 8, to = 15)
    private XlsxMeal recipe;
}
