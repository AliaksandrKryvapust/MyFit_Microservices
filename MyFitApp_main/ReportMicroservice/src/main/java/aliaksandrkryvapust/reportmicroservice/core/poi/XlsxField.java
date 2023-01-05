package aliaksandrkryvapust.reportmicroservice.core.poi;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XlsxField {
    private String fieldName;
    private int cellIndex;
    private int cellIndexFrom;
    private int cellIndexTo;
    private boolean isAnArray;
    private boolean isComposite;
}
