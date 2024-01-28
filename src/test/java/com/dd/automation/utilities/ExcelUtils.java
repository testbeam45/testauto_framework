package com.dd.automation.utilities;

import static com.dd.automation.utilities.CommonUtils.LOGGER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;

public class ExcelUtils {

    private File file;
    private Workbook workbook;
    private Sheet worksheet;
    private int labelCol;
    private int valueCol;
    private int startRow;

    public ExcelUtils(File file) {
        this.file = file;
        this.workbook = getExcelFile();
        this.labelCol = 0;
        this.valueCol = 1;
        this.startRow = 0;
    }

    public ExcelUtils(File file, String labelCol, String valueCol, int startRow) {
        this.file = file;
        this.workbook = getExcelFile();
        this.labelCol = getColIndex(labelCol);
        this.valueCol = getColIndex(valueCol);
        this.startRow = startRow;
    }

    public Sheet getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(String sheetName) {
        this.worksheet = workbook.getSheet(sheetName);
    }

    public String getCellData(String cellAddress) {
        return focusCellAndDo(Action.GET, cellAddress);
    }

    public void setCellData(String cellAddress, String value) {
        focusCellAndDo(Action.SET, cellAddress, value);
    }

    public String getLabelData(String label) {
        return iterateAndDo(Action.GET, label);
    }

    public void setLabelData(String label, String value) {
        iterateAndDo(Action.SET, label, value);
    }

    public int getColIndex(String colString) {
        return CellReference.convertColStringToIndex(colString);
    }

    public Cell getCell(int rowNum, int colNum) {
        Row row = worksheet.getRow(rowNum);
        if (!Optional.ofNullable(row).isPresent()) {
            row = worksheet.createRow(rowNum);
        }

        Cell cell = row.getCell(colNum);
        if (!Optional.ofNullable(cell).isPresent()) {
            cell = row.createCell(colNum);
        }

        return cell;
    }

    public List<Map<String, String>> getRowsData(String scenarioName) {
        return iterateAndGetMultipleRowData(scenarioName);
    }

    public Map<String, String> getRowData(String scenarioName) {
        return iterateAndGetRowData(scenarioName);
    }

    private Workbook getExcelFile() {
        XSSFWorkbook excelWorkbook = null;

        String parentDir = file.getParent();
        String toUpper = this.file.getName().toUpperCase(Locale.ENGLISH);

        try {
            for (File temp : FileUtils.getAllFiles(parentDir)) {
                if (file.getName().equals(temp.getName())) {
                    try (FileInputStream excelFile = new FileInputStream(file)) {

                        if (toUpper.endsWith(Extension.XLSX.value) || toUpper.endsWith(Extension.XLSM.value)) {
                            excelWorkbook = new XSSFWorkbook(excelFile);
                            XSSFFormulaEvaluator.evaluateAllFormulaCells(excelWorkbook);
                        } else {
                            LOGGER.warn(String.format("%s is not an excel file.", file.getName()));
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return excelWorkbook;
    }

    private void saveExcelFile() {
        try (FileOutputStream excelFile = new FileOutputStream(file)) {
            workbook.write(excelFile);
            excelFile.flush();
            excelFile.close();
        } catch (Exception e) {
            if (e.getMessage().contains("being used by another process"))
                LOGGER.warn(String.format("The file: %s is in use. Please close the file and retry. %s", file.getName(), e));
        }
    }

    private String focusCellAndDo(Action action, String cellAddress, String... value) {
        String data = null;
        try {
            CellReference cellRef = new CellReference(cellAddress);
            Cell cell = getCell(cellRef.getRow(), cellRef.getCol());
            CellType cellTyp = cell.getCellType();
            CellAddress cellAd = cell.getAddress();

            switch (action) {
                case GET:
                    data = getCellStringValue(cell, cell.getCellType());
                    LOGGER.debug(String.format("Getting value of [%s] (%s) as [%s]", cellAd, cellTyp, data));
                    break;
                case SET:
                    setCellStringValue(cell, value[0]);
                    LOGGER.debug(String.format("Setting value of [%s] to [%s]", cellAd, value[0]));
                    FileUtils.deleteFile(file.getPath());
                    saveExcelFile();
                    break;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return data;
    }

    private String iterateAndDo(Action action, String label, String... value) {
        String data = null;
        try {
            for (int dataRow = startRow; dataRow < worksheet.getLastRowNum() + 1; dataRow++) {
                Cell lblCell = getCell(dataRow, labelCol);
                Cell valCell = getCell(dataRow, valueCol);
                CellType lblCellTyp = lblCell.getCellType();
                CellType valCellTyp = valCell.getCellType();
                CellAddress valCellAd = valCell.getAddress();

                if (label.equals(getCellStringValue(lblCell, lblCellTyp))) {
                    switch (action) {
                        case GET:
                            data = getCellStringValue(valCell, valCellTyp);
                            LOGGER.debug(String.format("Getting value of [%s] (%s) as [%s]", valCellAd, valCellTyp, data));
                            break;
                        case SET:
                            setCellStringValue(valCell, value[0]);
                            LOGGER.debug(String.format("Setting value of [%s] to [%s]", valCellAd, value[0]));
                            FileUtils.deleteFile(file.getPath());
                            saveExcelFile();
                            break;
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }
        return data;
    }

    private List<Map<String, String>> iterateAndGetMultipleRowData(String scenarioName) {
        String data = null;
        List<Map<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> tempMap = new HashMap<>();
        try {

            for (int dataRow = startRow; dataRow < worksheet.getLastRowNum() + 1; dataRow++) {
                Cell lblCell = getCell(dataRow, labelCol);
                Cell valCell = getCell(dataRow, valueCol);
                CellType lblCellTyp = lblCell.getCellType();
                CellType valCellTyp = valCell.getCellType();
                CellAddress valCellAd = valCell.getAddress();
                data = getCellStringValue(lblCell, lblCellTyp);

                if (data.contentEquals(scenarioName)) {
                    for (int col = 1; col < worksheet.getRow(dataRow).getLastCellNum(); col++) {
                        Cell lblScenarioCell = getCell(0, col);
                        Cell valScenarioCell = getCell(dataRow, col);
                        CellType lblScenarioCellTyp = lblScenarioCell.getCellType();
                        CellType valScenarioCellTyp = valScenarioCell.getCellType();
                        String scenarioKey = getCellStringValue(lblScenarioCell, lblScenarioCellTyp);
                        String scenarioValue = getCellStringValue(valScenarioCell, valScenarioCellTyp);
                        tempMap.put(scenarioKey, scenarioValue);
                    }
                    hashMaps.add(tempMap);
                    System.out.println("*******************");
                    System.out.println(hashMaps);
                }

            }
        } catch (Exception e) {

        }
        return hashMaps;
    }

    private Map<String, String> iterateAndGetRowData(String scenarioName) {
        String data = null;
        HashMap<String, String> tempMap = new HashMap<>();
        try {

            for (int dataRow = startRow; dataRow < worksheet.getLastRowNum() + 1; dataRow++) {
                Cell lblCell = getCell(dataRow, labelCol);
                Cell valCell = getCell(dataRow, valueCol);
                CellType lblCellTyp = lblCell.getCellType();
                CellType valCellTyp = valCell.getCellType();
                CellAddress valCellAd = valCell.getAddress();
                data = getCellStringValue(lblCell, lblCellTyp);

                if (data.contentEquals(scenarioName)) {
                    for (int col = 1; col < worksheet.getRow(dataRow).getLastCellNum(); col++) {
                        Cell lblScenarioCell = getCell(0, col);
                        Cell valScenarioCell = getCell(dataRow, col);
                        CellType lblScenarioCellTyp = lblScenarioCell.getCellType();
                        CellType valScenarioCellTyp = valScenarioCell.getCellType();
                        String scenarioKey = getCellStringValue(lblScenarioCell, lblScenarioCellTyp);
                        String scenarioValue = getCellStringValue(valScenarioCell, valScenarioCellTyp);
                        tempMap.put(scenarioKey, scenarioValue);
                    }
//                    hashMap.add(tempMap);
                    System.out.println("*******************");
                }

            }
        } catch (Exception e) {

        }
        return tempMap;
//        EnvironmentData.transposeAndCast(hashMap, Register.getDefaultInstance());
    }

    private String getCellStringValue(Cell cell, CellType cellType) {
        String data = null;

        try {
            switch (cellType) {
                case STRING:
                    data = cell.getRichStringCellValue().toString();
                    break;
                case NUMERIC:
                    data = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    data = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    data = getCellStringValue(cell, cell.getCachedFormulaResultType());
                    break;
                case BLANK:
                case ERROR:
                case _NONE:
                    data = EMPTY;
                    break;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to get string value for cell %s.", cell.getAddress()), e);
        }
        return data;
    }

    private void setCellStringValue(Cell cell, String value) {
        try {
            cell.setCellValue(value);
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to set string value for cell %s.", cell.getAddress()), e);
        }
    }

    private enum Action {
        GET,
        SET
    }

    private enum Extension {
        XLS(".XLS"),
        XLSX(".XLSX"),
        XLSM(".XLSM");

        private final String value;

        Extension(final String value) {
            this.value = value;
        }
    }

    public static void writeDataToExcel(String valueToWrite) throws IOException {

        String path = "D:\\sources\\testauto_ebaogi\\src\\test\\resources\\Files\\WriteCustomerIDExcel.xlsx";

        FileOutputStream fos = new FileOutputStream(path);

        XSSFWorkbook wkb = new XSSFWorkbook();

        XSSFSheet sheet1 = wkb.createSheet("CustomerIDDataStorage");

        XSSFRow excelRow = sheet1.createRow(1);
        XSSFCell excelCell = excelRow.createCell(0);
        excelCell.setCellValue(valueToWrite);
        fos.flush();
        wkb.write(fos);
        fos.close();

    }


    public static void setExcelData(String filepath, String sheetname, int rowIndex, int cellIndex, String data) {
        try {
            File f = new File(filepath);
            FileInputStream fis = new FileInputStream(f);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet st = wb.getSheet(sheetname);
            Row r = st.getRow(rowIndex);
            Cell c = r.getCell(cellIndex);
            c.setCellValue(data);
            System.out.println(data);
            FileOutputStream fos = new FileOutputStream(f);
            wb.write(fos);
        } catch (Exception e) {

        }
    }
}