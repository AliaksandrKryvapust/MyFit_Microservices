package aliaksandrkryvapust.reportmicroservice.core.poi;

import aliaksandrkryvapust.reportmicroservice.core.poi.api.IXlsxWriter;
import aliaksandrkryvapust.reportmicroservice.core.poi.api.XlsxSheet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static aliaksandrkryvapust.reportmicroservice.core.poi.XlsxWriterHelper.getFieldNamesForClass;

@Slf4j
@RequiredArgsConstructor
@Component
public class XlsxWriter implements IXlsxWriter {
    private final XlsxWriterHelper fileWriterService;

    @Override
    public <TYPE> void write(List<TYPE> data, ByteArrayOutputStream outputStream, String[] columnTitles, Workbook workbook) {
        if (emptyDataCheck(data)) return;
        long start = System.currentTimeMillis();
//        setting up the basic styles for the workbook
        Font boldFont = fileWriterService.getBoldFont(workbook);
        Font genericFont = fileWriterService.getGenericFont(workbook);
        CellStyle headerStyle = fileWriterService.getLeftAlignedCellStyle(workbook, boldFont);
        CellStyle currencyStyle = fileWriterService.setCurrencyCellStyle(workbook);
        CellStyle centerAlignedStyle = fileWriterService.getCenterAlignedCellStyle(workbook);
        CellStyle genericStyle = fileWriterService.getLeftAlignedCellStyle(workbook, genericFont);
        try {
            Sheet sheet = setSheetName(data, workbook);
//            get the metadata for each field of the POJO class into a list
            List<XlsxField> xlsColumnFields = getFieldNamesForClass(data.get(0).getClass());
            int tempRowNo = 0;
            int recordBeginRowNo;
            int recordEndRowNo = 0;
//            set spreadsheet titles
            Row mainRow = sheet.createRow(tempRowNo);
            Cell columnTitleCell;
            for (int i = 0; i < columnTitles.length; i++) {
                columnTitleCell = mainRow.createCell(i);
                columnTitleCell.setCellStyle(headerStyle);
                columnTitleCell.setCellValue(columnTitles[i]);
            }
            recordEndRowNo++;
//            get class of the passed dataset
            Class<?> clazz = data.get(0).getClass();
//            looping the past dataset
            for (TYPE record : data) {
                tempRowNo = recordEndRowNo;
                recordBeginRowNo = tempRowNo;
                mainRow = sheet.createRow(tempRowNo++);
                boolean isFirstValue;
                boolean isFirstRow;
                boolean isRowNoToDecrease = false;
                Method xlsMethod;
                Object xlsObjValue;
                ArrayList<Object> objValueList;
//                get max size of the record if its multiple row
                int maxListSize = fileWriterService.getMaxListSize(record, xlsColumnFields, clazz);
//                looping through the fields of the current record
                for (XlsxField xlsColumnField : xlsColumnFields) {
//                    writing a single field
                    if (!xlsColumnField.isAnArray() && !xlsColumnField.isComposite()) {
                        fileWriterService.writeSingleFieldRow(mainRow, xlsColumnField, clazz, currencyStyle, centerAlignedStyle, genericStyle,
                                record, workbook);
//                        overlooking the next field and adjusting the starting row
                        if (fileWriterService.isNextColumnAnArray(xlsColumnFields, xlsColumnField, clazz, record)) {
                            isRowNoToDecrease = true;
                            tempRowNo = recordBeginRowNo + 1;
                        }
//                        writing a single array field
                    } else if (xlsColumnField.isAnArray() && !xlsColumnField.isComposite()) {
                        xlsMethod = fileWriterService.getMethod(clazz, xlsColumnField);
                        xlsObjValue = xlsMethod.invoke(record, (Object[]) null);
                        objValueList = (ArrayList<Object>) xlsObjValue;
                        isFirstValue = true;
//                        looping through the items of the single array
                        for (Object objectValue : objValueList) {
                            Row childRow;
                            if (isFirstValue) {
                                childRow = mainRow;
                                fileWriterService.writeArrayFieldRow(childRow, xlsColumnField, objectValue,
                                        currencyStyle, centerAlignedStyle, genericStyle, workbook);
                                isFirstValue = false;
                            } else if (isRowNoToDecrease) {
                                childRow = fileWriterService.getOrCreateNextRow(sheet, tempRowNo++);
                                fileWriterService.writeArrayFieldRow(childRow, xlsColumnField, objectValue,
                                        currencyStyle, centerAlignedStyle, genericStyle, workbook);
                                isRowNoToDecrease = false;
                            } else {
                                childRow = fileWriterService.getOrCreateNextRow(sheet, tempRowNo++);
                                fileWriterService.writeArrayFieldRow(childRow, xlsColumnField, objectValue,
                                        currencyStyle, centerAlignedStyle, genericStyle, workbook);
                            }
                        }
                        //                        overlooking the next field and adjusting the starting row
                        if (fileWriterService.isNextColumnAnArray(xlsColumnFields, xlsColumnField, clazz, record)) {
                            isRowNoToDecrease = true;
                            tempRowNo = recordBeginRowNo + 1;
                        }
//                        writing a composite array field
                    } else if (xlsColumnField.isAnArray() && xlsColumnField.isComposite()) {
                        xlsMethod = fileWriterService.getMethod(clazz, xlsColumnField);
                        xlsObjValue = xlsMethod.invoke(record, (Object[]) null);
                        objValueList = (ArrayList<Object>) xlsObjValue;
                        isFirstRow = true;
//                        looping through the items of the composite array
                        for (Object objectValue : objValueList) {
                            Row childRow;
                            List<XlsxField> xlsCompositeColumnFields = getFieldNamesForClass(objectValue.getClass());
                            if (isFirstRow) {
                                childRow = mainRow;
                                for (XlsxField xlsCompositeColumnField : xlsCompositeColumnFields) {
                                    fileWriterService.writeCompositeFieldRow(objectValue, xlsCompositeColumnField,
                                            childRow, currencyStyle, centerAlignedStyle, genericStyle, workbook);
                                }
                                isFirstRow = false;
                            } else if (isRowNoToDecrease) {
                                childRow = fileWriterService.getOrCreateNextRow(sheet, tempRowNo++);
                                for (XlsxField xlsCompositeColumnField : xlsCompositeColumnFields) {
                                    fileWriterService.writeCompositeFieldRow(objectValue, xlsCompositeColumnField,
                                            childRow, currencyStyle, centerAlignedStyle, genericStyle, workbook);
                                }
                                isRowNoToDecrease = false;
                            } else {
                                childRow = fileWriterService.getOrCreateNextRow(sheet, tempRowNo++);
                                for (XlsxField xlsCompositeColumnField : xlsCompositeColumnFields) {
                                    fileWriterService.writeCompositeFieldRow(objectValue, xlsCompositeColumnField,
                                            childRow, currencyStyle, centerAlignedStyle, genericStyle, workbook);
                                }
                            }
                        }
//                        overlooking the next field and adjusting the starting row
                        if (fileWriterService.isNextColumnAnArray(xlsColumnFields, xlsColumnField, clazz, record)) {
                            isRowNoToDecrease = true;
                            tempRowNo = recordBeginRowNo + 1;
                        }
                    }
                }
//                adjusting the ending row number for the current record
                recordEndRowNo = maxListSize + recordBeginRowNo;
            }
//            auto sizing the columns of the whole sheet
            fileWriterService.autoSizeColumns(sheet, xlsColumnFields.size());
            workbook.write(outputStream);
            log.info("Xls file generated in [{}] seconds", fileWriterService.processTime(start));
        } catch (Exception e) {
            log.info("Xls file write failed", e);
        }
    }

    private <TYPE> boolean emptyDataCheck(List<TYPE> data) {
        if (data.isEmpty()) {
            log.error("No data received to write Xls file..");
            return true;
        }
        return false;
    }

    private <TYPE> Sheet setSheetName(List<TYPE> data, Workbook workbook) {
        XlsxSheet xlsxSheet = data.get(0).getClass().getAnnotation(XlsxSheet.class);
        String sheetName = xlsxSheet.value();
        String dateTime = LocalDateTime.now().toString().replaceAll(":", "_");
        return workbook.createSheet(sheetName + dateTime);
    }
}
