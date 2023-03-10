package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.IXlsxWriter;
import aliaksandrkryvapust.reportmicroservice.service.api.IXlsxRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_COLUMN_TITLES;

@Slf4j
@RequiredArgsConstructor
@Service
public class XlsxRecordService implements IXlsxRecordService {
    private final IXlsxWriter writer;

    @Override
    public byte[] getRecordXlsData(List<XlsxRecord> records) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (outputStream; Workbook workbook = new XSSFWorkbook()) {
            writer.write(records, outputStream, XLSX_COLUMN_TITLES, workbook);
        } catch (Exception e) {
            log.error("Generating users xls file failed", e);
        }
        return outputStream.toByteArray();
    }
}

