package com.dd.automation.utilities;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EnvironmentData {
    private ExcelUtils excelUtils;

    public EnvironmentData(String sheetName) {
        String FILE_PATH = "src/test/resources/data/";
        String FILE_NAME = "testdata";
        String FILE_FORMAT = ".xlsx";
        String LABEL_COL = "0";
        String VALUE_COL = "0";
        Integer START_ROW = 0;
        String filePath = String.format("%s%s%s", FILE_PATH, FILE_NAME, FILE_FORMAT);

        excelUtils = new ExcelUtils(new File(filePath));
        excelUtils.setWorksheet(sheetName);
    }

    public String getCellData(String cellAddress) {
        return excelUtils.getCellData(cellAddress);
    }

    public void setCellData(String cellAddress, String value) {
        excelUtils.setCellData(cellAddress, value);
    }

    public String getLabelData(String label) {
        return excelUtils.getLabelData(label);
    }

    public List<Map<String, String>> getRowsData(String scenarioName) {
         return excelUtils.getRowsData(scenarioName);
    }

    public Map<String, String> getRowData(String scenarioName) {
        return excelUtils.getRowData(scenarioName);
    }

    public void setLabelData(String label, String value) {
        excelUtils.setLabelData(label, value);
    }

}
