package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.IXlsxWriter;
import aliaksandrkryvapust.reportmicroservice.service.api.IXlsxRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
public class XlsxRecordService implements IXlsxRecordService {
    private final IXlsxWriter writer;

    public XlsxRecordService(IXlsxWriter writer) {
        this.writer = writer;
    }

    @Override
    public byte[] getRecordXlsData(List<XlsxRecord> records) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (outputStream; Workbook workbook = new XSSFWorkbook()) {
            String[] columnTitles = new String[]{"Record weight", "Record supply date", "Product title", "Product calories",
                    "Product proteins", "Product fats", "Product carbohydrates", "Product weight", "Meal title",
                    "Ingredient weight", "Ingredient product title", "Ingredient product calories",
                    "Ingredient product proteins", "Ingredient product fats", "Ingredient product carbohydrates",
                    "Ingredient product weight"};
            writer.write(records, outputStream, columnTitles, workbook);
        } catch (Exception e) {
            log.error("Generating users xls file failed", e);
        }
        return outputStream.toByteArray();
    }
}

