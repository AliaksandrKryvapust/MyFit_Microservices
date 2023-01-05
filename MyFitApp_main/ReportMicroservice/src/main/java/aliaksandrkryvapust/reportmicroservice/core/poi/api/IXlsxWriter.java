package aliaksandrkryvapust.reportmicroservice.core.poi.api;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface IXlsxWriter {
    <T> void write(List<T> data, ByteArrayOutputStream bos, String[] columnTitles, Workbook workbook);
}
