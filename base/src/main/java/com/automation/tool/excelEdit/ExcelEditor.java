package com.automation.tool.excelEdit;

import com.automation.exception.MException;
import com.automation.exception.RunException;
import com.automation.keyword.ExcelType;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.StringUtil;
import com.automation.tool.util.Print;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by chris.li on 2015/11/4.
 */
public class ExcelEditor {

    //workBook
    private Workbook excelBook = null;
    //保存最后的结果集
    private Map<String, Map<String, List<Map<String, String>>>> excelCaseResult = new LinkedHashMap<String, Map<String, List<Map<String, String>>>>();
    //保存有效的sheet名称
    private List<String> sheetList = new ArrayList<String>();
    private String currentSheetName = null;
    //存储sheet对应的模块列表，并记录开始行号、结束行号，总行数，样式
    private Map<String, Map<String, Object[]>> excelCaseStyles = new LinkedHashMap<String, Map<String, Object[]>>();
    //按顺序存储sheet对应的模块名
    private Map<String, List<String>> excelCaseNameList = new LinkedHashMap<String, List<String>>();
    //一个sheet的模块列表
    private Map<String, List<Map<String, String>>> singleSheetCaseMap = null; //数据
    private Map<String, Object[]> singleSheetCaseStyle = null; //样式
    private List<String> singleSheetCaseNames = null; //用例名
    //当前的excel
    private String currentFilePath = null;
    public int excelMode = -1; //0--open; 1--save;

    public ExcelEditor(){

    }

    private void initParameters() {
        excelBook = null;
        excelCaseResult = new LinkedHashMap<String, Map<String, List<Map<String, String>>>>();
        sheetList = new ArrayList<String>();
        currentSheetName = null;
        excelCaseStyles = new LinkedHashMap<String, Map<String, Object[]>>();
        excelCaseNameList = new LinkedHashMap<String, List<String>>();
        singleSheetCaseMap = null; //数据
        singleSheetCaseStyle = null; //样式
        singleSheetCaseNames = null; //用例名
    }

    /**
     * 加载excel中的testCase
     * @param filePath
     */
    public void openExcelTestCase(String filePath) {
        currentFilePath = filePath;
        File excelFile = new File(filePath);
        if (excelFile.exists()) {
            InputStream fileIS = null;
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                fileIS = new FileInputStream(excelFile);
                byte buf[] = IOUtils.toByteArray(fileIS);
                byteArrayInputStream = new ByteArrayInputStream(buf);
                if(filePath.endsWith(".xls")) {
                    excelBook = new HSSFWorkbook(byteArrayInputStream);
                }else if(filePath.endsWith(".xlsx")) {
                    excelBook = new XSSFWorkbook(byteArrayInputStream);
                }else {
                    Print.logRed("文件格式不正确");
                }
                if(excelBook!=null) {
                    readExcelToMap(excelBook);
                    if(excelMode!=-1) {
                        Print.log("打开了文件："+filePath);
                    }
                }else {
                    Print.logRed("文件读取失败");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Print.logRed("文件读取异常："+e.getMessage());
            } finally {
                if (fileIS != null) {
                    try {
                        fileIS.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Print.logRed("文件流关闭异常："+e.getMessage());
                    }
                }
            }
        }else {
            Print.logRed("文件不存在");
        }
    }

    public void saveFile(String filePath) throws RunException{
        try {
            File excelFile = new File(filePath);
            if(!excelFile.exists()) {
                FileOutputStream fileOut = new FileOutputStream(filePath);
                if (filePath.endsWith(".xls")) {
                    excelBook = new HSSFWorkbook();
                } else if (filePath.endsWith(".xlsx")) {
                    excelBook = new XSSFWorkbook();
                } else {
                    Print.logRed("文件格式不正确");
                }
                if (excelBook.getNumberOfSheets() <= 0) excelBook.createSheet();
                excelBook.write(fileOut);
                fileOut.close();
                Print.log("新建了文件：" + filePath);
            }else {
                Print.log("文件已存在：" + filePath);
            }
        } catch (Exception e) {
            Print.logRed("文件保存失败");
            throw new RunException(e);
        }
    }

    //将excel中的数据读取至map
    private void readExcelToMap(Workbook excelBook) {
        int sheetCount = excelBook.getNumberOfSheets();
        if(sheetCount>0) {
            boolean validateSheet = false;
            for(int i=0; i<sheetCount; i++) {
                Sheet sheet = excelBook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                singleSheetCaseMap = null;
                singleSheetCaseStyle = null;
                singleSheetCaseNames = null;
                //如果存在两行以上，并且第一格为是否执行，则视为正常的sheet
                if(sheet.getPhysicalNumberOfRows()>1 && ExcelType.DOES_RUN.equals(getValue(sheet, 0, 0))) {
                    //读取sheet,获取数据和样式
                    getDataFromSheet(sheet);
                    if(singleSheetCaseMap!=null && singleSheetCaseMap.size()>0) {
                        excelCaseResult.put(sheetName, singleSheetCaseMap); //数据
                        excelCaseStyles.put(sheetName, singleSheetCaseStyle); //样式
                        excelCaseNameList.put(sheetName, singleSheetCaseNames); //用例名称
                        sheetList.add(sheetName);
                        if(!validateSheet) {
                            currentSheetName = sheetName; //设第一个有效sheet为当前的
                            validateSheet = true;
                        }
                    }else {
                        if(excelMode==0) Print.log("sheet:"+sheet.getSheetName()+"，未获取到用例");
                        sheetList.add(sheetName);
                        continue;
                    }
                }else {
                    if(excelMode==0) Print.logGray("sheet:"+sheet.getSheetName()+"，格式不正确，没有数据需被读取");
                    sheetList.add(sheetName);
                    continue;
                }
            }
        }else {
            Print.logRed("文件中sheet数目小于0");
            return;
        }
    }

    //获取单独sheet的数据
    private void getDataFromSheet(Sheet sheet) {
        singleSheetCaseMap = new LinkedHashMap<String, List<Map<String, String>>>();
        singleSheetCaseStyle = new LinkedHashMap<String, Object[]>();
        singleSheetCaseNames= new ArrayList<String>();
        try {
            //获取标题行
            int sheetRowsNum = sheet.getPhysicalNumberOfRows(); //获取物理行数
            Map<Integer, String> titles = null;
            if(sheetRowsNum>1 && sheet.getRow(0)!=null) {
                titles = getTitle(sheet);
                if(titles==null || titles.size()<=0) {
                    Print.logRed("sheet:"+sheet.getSheetName()+",获取标题行失败");
                    return;
                }
            }else {
                Print.logRed("获取数据失败，错误原因为：sheet:"+sheet.getSheetName()+" 格式不正确，缺少标题行");
                return;
            }
            int endRowNum = sheetRowsNum;
            //记录结束行,如果该行的第一格(是否执行)或者第二格(用例名称)为空，或者是否执行与预期不符，则视为结束
            for(int i=0;i<sheetRowsNum;i++){
                if(StringUtil.isEmpty(getValue(sheet, i, 0)) || StringUtil.isEmpty(getValue(sheet, i, 1))){
                    endRowNum=i;
                    break;
                }
            }

            //将excel值存入Map中
            //是否执行{"Y", "1"}
            List<String> handlers = new ArrayList<>();
            handlers.add("Y");
            handlers.add("1");
            handlers.add("N");
            handlers.add("0");
            //要求：excel第一列为是否执行，第二列为用例名称
            List<String> caseNames = new ArrayList<>(); //按顺序存储用例
            for(int i=0;i<endRowNum;i++){
                List<Map<String, String>> datas = null; //单个用例的结果集
                Object[] style = null; //存储开始行，结束行，行数，style
                String doesRun = StringUtil.trim(getValue(sheet, i, 0)); //是否执行
                String caseName = StringUtil.trim(getValue(sheet, i, 1)); //用例名称
                //单个用例数据
                //如果map中包含这个用例，那么就加载map中的list，否则新建
                if(handlers.contains(doesRun) && singleSheetCaseMap.containsKey(caseName)) {
                    //数据
                    datas = singleSheetCaseMap.get(caseName);
                    //样式
                    style = singleSheetCaseStyle.get(caseName);
                    style[1] = i;
                    style[2] = ((int)style[2])+1;
                }else if(handlers.contains(doesRun) && !singleSheetCaseMap.containsKey(caseName)) {
                    //数据
                    datas = new ArrayList<Map<String, String>>();
                    //样式
                    style = new Object[3];
                    style[0] = i;
                    style[1] = i;
                    style[2] = 1;
                    caseNames.add(caseName);
                }else if(!ExcelType.DOES_RUN.equals(doesRun) && !handlers.contains(doesRun)) {
                    Print.log("行："+i+"，是否执行与预期不符，视为结束，进行下一个sheet");
                    continue;
                }
                if(datas!=null) {
                    Map<String, String> valuesMap = new LinkedHashMap<String, String>(); //当前行每一列的值
                    for(Integer colNum:titles.keySet()) {
                        valuesMap.put(titles.get(colNum), getValue(sheet, i, colNum));
                    }
                    datas.add(valuesMap);
                    if(datas.size()>0) {
                        //单个用例的数据
                        singleSheetCaseMap.put(caseName, datas);
                        //单个用例样式以及起讫行
                        singleSheetCaseStyle.put(caseName, style);
                    }
                }
            }

            //按顺序存储case
            for(String caseName:caseNames) {
                if(singleSheetCaseStyle.containsKey(caseName)) {
                    singleSheetCaseNames.add(caseName);
                }
            }
            return;
        }catch (Exception e) {
            Print.logRed("获取"+sheet.getSheetName()+"数据失败，错误原因为："+e.getMessage());
        }
        return;
    }

    /**
     * 获取Excel单元格上的数据
     * @param col 列号
     * @param row 行号
     * @return 全部返回成String类型
     */
    private String getValue(Sheet excelSheet, int row, int col){
        if(excelSheet.getPhysicalNumberOfRows()<=0 || excelSheet.getRow(row)==null){
            return null;
        }else if(excelSheet.getRow(row).getCell(col)==null){
            return null;
        }

        int cellType = excelSheet.getRow(row).getCell(col).getCellType();

        switch(cellType){

            case Cell.CELL_TYPE_STRING:
                return excelSheet.getRow(row).getCell(col).getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return StringUtil.objectToString(excelSheet.getRow(row).getCell(col).getNumericCellValue());
//                return new DecimalFormat("0").format(excelSheet.getRow(row).getCell(col).getNumericCellValue());
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(excelSheet.getRow(row).getCell(col).getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return null;
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_ERROR:
                return null;
            default :
                return null;
        }

    }

    private Map<Integer, String> getTitle(Sheet sheet) {
        //获取标题行
        Map<Integer, String> titles = new LinkedHashMap<Integer, String>(); //标题行
        int titleNum = sheet.getRow(0).getLastCellNum();
        for(int i=0; i<titleNum; i++) {
            if(!StringUtil.isEmpty(getValue(sheet, 0, i))) {
                titles.put(i, StringUtil.trim(getValue(sheet, 0, i)));
            }
        }

        if(titles==null || titles.size()<1) {
            return null;
        }else {
            return titles;
        }
    }

    private Map<String, Integer> getTitleIndex(Sheet sheet) {
        //获取标题行
        Map<String, Integer> titles = new LinkedHashMap<String, Integer>(); //标题行
        int titleNum = sheet.getRow(0).getLastCellNum();
        for(int i=0; i<titleNum; i++) {
            if(!StringUtil.isEmpty(getValue(sheet, 0, i))) {
                titles.put( StringUtil.trim(getValue(sheet, 0, i)), i);
            }
        }

        if(titles==null || titles.size()<1) {
            return null;
        }else {
            return titles;
        }
    }

    //通过sheet页，用例名称，保存数据
    public boolean saveModuleCase(String sheetName, String caseName, List<Map<String, String>> tableCaseData) {
        //如果结果集没有数据，那么视为错误
        if(tableCaseData==null || tableCaseData.size()<=0) {
            Print.logRed("没有数据需要保存，请查看");
            return false;
        }

        boolean newCase = false;
        //读取sheet页
        Sheet sheet = excelBook.getSheet(sheetName);
        if(sheet==null) {
            //sheet不存在，则创建
            sheet  = excelBook.createSheet(sheetName);
            newCase = true;
        }

        //找到用例所在区间
        int startCount = -1;
        int endCount = -1;

        //获取style
        if(excelCaseStyles!=null && excelCaseStyles.containsKey(sheetName)) {
            Map<String, Object[]> sheetStyle = excelCaseStyles.get(sheetName);
            if(sheetStyle!=null && sheetStyle.containsKey(caseName)) {
                //开始行，结束行，行数，style
                Object[] styles = sheetStyle.get(caseName);
                if(styles!=null && styles.length==3) {
                    startCount = (int)styles[0];
                    endCount = (int)styles[1];
                }
            }
        }

        //如果不存在用例，那么直接将结果加到最后；用例存在，则插入结果
        if(startCount<0 || endCount<0 || startCount>endCount) {
            int endRowNum = -1;
            //记录结束行,如果该行的第一格(是否执行)或者第二格(用例名称)为空，或者是否执行与预期不符，则视为结束
            for(int i=0;i<=sheet.getLastRowNum();i++){
                if(StringUtil.isEmpty(getValue(sheet, i, 0)) || StringUtil.isEmpty(getValue(sheet, i, 1))){
                    break;
                }
                endRowNum=i;
            }
            //如果不存在用例，那么直接将结果加到最后
            startCount = endRowNum + 1;
            endCount = endRowNum + 1;
            newCase = true;
        }

        //判断保存时数据是否都正确
        boolean allDataRight = true;
        //用例存在，则插入结果
        int tableSize = tableCaseData.size();
        int diff = tableSize - (endCount - startCount + 1);
        //如果为新的用例，那么添加标题行
        if(newCase) {
            diff++;
        }
        if(diff>0){
            while(diff!=0){
                try {
                    insertRow(sheet, startCount);
                } catch (Exception e) {
                    allDataRight = false;
                } finally {
                    diff--;
                }
            }
        }else if(diff<0){
            while(diff!=0){
                try {
                    deleteRow(sheet, startCount);
                } catch (Exception e) {
                    allDataRight = false;
                } finally {
                    diff++;
                }
            }
        }
        if(!allDataRight) {
            Print.logRed("行数调整错误，保存失败，请检查");
            return false;
        }

        //保存数据
        //先添加标题行
        if(newCase) {
            try {
                addTitle(sheet, startCount);
            } catch (Exception e) {
                allDataRight = false;
            } finally {
                startCount++;
                endCount++;
            }
            if(!allDataRight) {
                Print.logRed("添加标题行错误，保存失败，请检查");
                return false;
            }
        }

        //获取数据行的样式
        CellStyle style = getDataStyle();
        //添加数据
        for(Map<String, String> rowData:tableCaseData) {
            try {
                addRowData(sheet, startCount, rowData, style);
            } catch (Exception e) {
                allDataRight = false;
                break;
            } finally {
                startCount++;
            }
        }
        if(!allDataRight) {
            Print.logRed("添加数据错误，保存失败，请检查");
            return false;
        }else {
            try {
                boolean flag = save(currentFilePath);
                if(excelMode!=-1) {
                    Print.log("保存用例:sheet:"+sheetName+",模块:"+caseName+",共"+tableSize+"行");
                }
                return flag;
            } catch (MException e) {
                Print.logRed("excel保存失败");
                return false;
            }
        }
    }

    //添加头
    private void addTitle(Sheet sheet, int rowNum) throws Exception{
        try {
            //增加头
            CellStyle titleStyle = getTitleStyle();
            for(int i=0; i<Parameters.titles.length; i++) {
                setValue(sheet, rowNum, i, Parameters.titles[i], titleStyle);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //添加数据
    private void addRowData(Sheet sheet, int rowNum, Map<String, String> rowData, CellStyle style) throws Exception{
        try {
            //获取头信息
            Map<String, Integer> titles = null;
            if(sheet.getPhysicalNumberOfRows()>0 && sheet.getRow(0)!=null) {
                titles = getTitleIndex(sheet);
                if(titles==null || titles.size()<=0) {
                    Print.logRed("添加第" + rowNum + "行数据时，sheet:"+sheet.getSheetName()+",获取标题行失败");
                    throw new Exception("添加数据出错");
                }
            }else {
                Print.logRed("添加第" + rowNum + "行数据时，获取数据失败，错误原因为：sheet:"+sheet.getSheetName()+" 格式不正确，缺少标题行");
                throw new Exception("添加数据出错");
            }
            //写入数据
            for(String key:rowData.keySet()) {
                boolean isSetValue = false;
                if(ExcelType.ISPASS.equals(key)) {
                    isSetValue = setValue(sheet, rowNum, titles.get(key), rowData.get(key), isPassedStyle(rowData.get(key)));
                }else if(ExcelType.ACTUAL.equals(key)) {
                    isSetValue = setValue(sheet, rowNum, titles.get(key), rowData.get(key), actualValueStyle());
                }else {
                    isSetValue = setValue(sheet, rowNum, titles.get(key), rowData.get(key), style);
                }
                if(!isSetValue) throw new Exception("添加数据出错");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //获取头的样式
    private CellStyle getTitleStyle() throws Exception{
        try {
            //列表标题(用例编号,相关用例等)
            CellStyle menuStyle = setContentBorder();
            Font menuFont = excelBook.createFont();
            menuFont.setFontName("楷体");
            menuFont.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体显示
            menuFont.setFontHeightInPoints((short) 12);
            menuStyle.setFont(menuFont);
            menuStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            menuStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            return menuStyle;
        } catch (Exception e) {
            throw e;
        }
    }

    //获取数据行的样式
    private CellStyle getDataStyle() {
        //内容
        CellStyle contentStyle = setContentBorder();
        return contentStyle;
    }

    //设置边框
    private CellStyle setContentBorder() {
        CellStyle style = excelBook.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        style.setWrapText(true);
        return style;
    }

    //是否通过
    private CellStyle isPassedStyle(String isPassed) {
        CellStyle style = excelBook.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        style.setWrapText(true);
        if(StringUtil.isEmpty(isPassed) || "DONE".equals(isPassed)) {
        }else if("PASS".equals(isPassed)) {
            Font font = excelBook.createFont();
            font.setColor(HSSFColor.GREEN.index);
            style.setFont(font);
        }else if("SKIP".equals(isPassed)) {
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }else {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return style;
    }

    //实际值
    private CellStyle actualValueStyle() {
        CellStyle style = excelBook.createCellStyle();
        Font font = excelBook.createFont();
        font.setColor(HSSFColor.BLUE.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
        style.setWrapText(true);
        return style;
    }

    //保存文件
    private boolean save(String path) throws MException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            excelBook.write(out);
            out.close();
            return true;
        } catch (IOException e) {
            throw new MException(e);
        } finally{
            initParameters();
            openExcelTestCase(path);
        }
    }

    //塞值
    private boolean setValue(Sheet sheet, int row, int col, Object value, CellStyle style){
        try{
            if (sheet.getRow(row) == null) {
                sheet.createRow(row);
            }
            Row cRow = sheet.getRow(row);
            if (cRow.getCell(col) == null) {
                cRow.createCell(col);
            }
            Cell cell = sheet.getRow(row).getCell(col);
            cell.setCellStyle(style);
            if(value instanceof Double || value instanceof Integer){
                //如果单元格原来是String类型 无法转化 必须删除
                cRow.removeCell(cell);
                cell = cRow.createCell(col);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                if(value instanceof Integer){
                    value = ((Integer) value).doubleValue();
                }
                cell.setCellValue((Double) value);
            }else if(value instanceof RichTextString){
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((RichTextString)value);
            }else{
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) value);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //插入行
    private void insertRow(Sheet sheet, int rowNum) throws Exception{
        try {
            if(rowNum >= sheet.getLastRowNum()){
                insertLastRow(sheet);
            }else{
                sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //增加行
    private void insertLastRow(Sheet sheet) throws Exception{
        try {
            sheet.createRow((short)(sheet.getLastRowNum()+1));
        } catch (Exception e) {
           throw e;
        }
    }

    //删除行
    private boolean deleteRow(Sheet sheet, int rowNum) throws Exception{
        try {
            if(rowNum <= sheet.getLastRowNum()){
                sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), -1);
                return true;
            }else{
                Print.logYellow("删除行已超出最大行数");
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //新增sheet
    public boolean createSheet(String sheetName) {
        if(StringUtil.isEmpty(sheetName)) {
            Print.logRed("sheet名称为空，无法新增sheet");
            return false;
        }
        for(String name:sheetList) {
            if(sheetName.equals(name)) {
                Print.logRed("sheet名称已存在，无法新增sheet");
                return false;
            }
        }
        //读取sheet页
        Sheet sheet = excelBook.getSheet(sheetName);
        if(sheet==null) {
            //sheet不存在，则创建
            excelBook.createSheet(sheetName);
            try {
                save(currentFilePath);
            } catch (MException e) {
                Print.logRed("excel保存失败");
                return false;
            }
//            sheetList.add(sheetName);
            Print.log("新增sheet:" + sheetName + ",成功");
            return true;
        }else {
            Print.logRed("sheet已存在，无法新增sheet");
            return false;
        }
    }

    //修改sheet
    public boolean modifySheetName(String oldName, String newName) {
        if(StringUtil.isEmpty(oldName) || StringUtil.isEmpty(newName)
                || oldName.equals(newName)) {
            Print.logRed("sheet名称不合法，无法修改sheet名");
            return false;
        }
        //读取sheet页
        int sheetIndex = excelBook.getSheetIndex(oldName);
        if(sheetIndex<0) {
            Print.logRed("sheet不存在，无法修改sheet名");
            return false;
        }
        excelBook.setSheetName(sheetIndex, newName);
        try {
            save(currentFilePath);
        } catch (MException e) {
            Print.logRed("excel保存失败");
            return false;
        }
        //往下拉框中添加
        List<String> newSheetList = new ArrayList<String>();
        for(String sheetName:sheetList) {
            if(oldName.equals(sheetName)) {
                newSheetList.add(newName);
            }else {
                newSheetList.add(sheetName);
            }
        }
        if(newSheetList!=null && newSheetList.size()>0) {
            sheetList = newSheetList;
        }
        Print.log("sheet名:"+oldName+"，修改为:"+newName);
        return true;
    }

    //新增用例
    public boolean createCase(String sheetName, String caseName) {
        if(StringUtil.isEmpty(sheetName) || StringUtil.isEmpty(caseName)) {
            Print.logRed("sheet名或用例名称为空，无法新增");
            return false;
        }
        //验证用例是否存在
        List<String> caseList = new ArrayList<String>();
        if(excelCaseNameList.containsKey(sheetName)) {
            for(String name:excelCaseNameList.get(sheetName)) {
                if(caseName.equals(name)) {
                    Print.logRed("用例名称已存在，无法新增");
                    return false;
                }
            }
            caseList = excelCaseNameList.get(sheetName);
        }
        caseList.add(caseName);
        excelCaseNameList.put(sheetName, caseList);
        Print.log("新增用例:" + caseName + ",成功");
        return true;
    }

    public boolean modifyCase(String sheetName, String oldName, String newName) {
        if(StringUtil.isEmpty(sheetName) || StringUtil.isEmpty(oldName) || StringUtil.isEmpty(newName)
                || oldName.equals(newName)) {
            Print.logRed("用例名称不合法，无法修改");
            return false;
        }
        if(excelCaseResult.get(sheetName)==null || excelCaseResult.get(sheetName).size()<=0
                || excelCaseResult.get(sheetName).get(oldName)==null || excelCaseResult.get(sheetName).get(oldName).size()<=0) {
            //只修改list里的数据
            List<String> caseList = new ArrayList<String>();
            if(excelCaseNameList.containsKey(sheetName)) {
                for(String name:excelCaseNameList.get(sheetName)) {
                    if(oldName.equals(name)) {
                        caseList.add(newName);
                    }else {
                        caseList.add(name);
                    }
                }
            }
            excelCaseNameList.put(sheetName, caseList);
            Print.log("用例名:"+oldName+"，修改为:"+newName);
            return true;
        }

        int rowCount = excelCaseResult.get(sheetName).get(oldName).size();
        List<Map<String, String>> caseNameList = new ArrayList<Map<String, String>>();
        Map<String, String> singleRowData = null;
        for(int i=0; i<rowCount; i++) {
            singleRowData = new LinkedHashMap<String, String>();
            singleRowData.put(ExcelType.CASE_NAME, newName);
            caseNameList.add(singleRowData);
        }
        saveModuleCase(sheetName, oldName, caseNameList);
        Print.log("excel中用例名:"+oldName+"，修改为:"+newName);
        return true;
    }

    public Map<String, Map<String, List<Map<String, String>>>> getExcelCaseResult() {
        return excelCaseResult;
    }

    public void setExcelCaseResult(Map<String, Map<String, List<Map<String, String>>>> excelCaseResult) {
        this.excelCaseResult = excelCaseResult;
    }

    public List<String> getSheetList() {
        return sheetList;
    }

    public void setSheetList(List<String> sheetList) {
        this.sheetList = sheetList;
    }

    public Map<String, List<String>> getExcelCaseNameList() {
        return excelCaseNameList;
    }

    public void setExcelCaseNameList(Map<String, List<String>> excelCaseNameList) {
        this.excelCaseNameList = excelCaseNameList;
    }

    public Map<String, Map<String, Object[]>> getExcelCaseStyles() {
        return excelCaseStyles;
    }

    public void setExcelCaseStyles(Map<String, Map<String, Object[]>> excelCaseStyles) {
        this.excelCaseStyles = excelCaseStyles;
    }

    public Workbook getExcelBook() {
        return excelBook;
    }

    public void setExcelBook(Workbook excelBook) {
        this.excelBook = excelBook;
    }

    public String getCurrentSheetName() {
        return currentSheetName;
    }

    public void setCurrentSheetName(String currentSheetName) {
        this.currentSheetName = currentSheetName;
    }

}
