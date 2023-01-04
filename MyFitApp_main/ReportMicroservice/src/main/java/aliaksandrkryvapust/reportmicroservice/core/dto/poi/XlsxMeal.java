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
@XlsxSheet(value = "Meal")
public class XlsxMeal {
    @XlsxSingleField(columnIndex = 8)
    private String title;
    @XlsxCompositeField(from = 9, to = 15)
    private List<XlsxIngredient> composition;
}
