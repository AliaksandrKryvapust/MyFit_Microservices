package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;

import java.io.IOException;
import java.util.List;

public interface IXlsxRecordService {
    byte[] getRecordXlsData(List<XlsxRecord> records) throws IOException;
}

