package tool;

import com.xt.excelviewlib.view.ExcelView;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供导入和导出功能
 *
 * @author lxl
 */
public class PoiUtil {

    public static String readExcel(File file) {

        InputStream fis;
        int width,height;
        ArrayList<TaiQuModel> taiQuModelArrayList = new ArrayList<>();
        try {
            fis = new FileInputStream(file);
            Workbook wk;
            if (file.getName().endsWith("xls")) {
                wk = new HSSFWorkbook(fis);
            } else {
                wk = WorkbookFactory.create(fis);
            }
            Sheet sheet = wk.getSheet("111新兴线");
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            height = sheet.getPhysicalNumberOfRows();
            width = row.getPhysicalNumberOfCells() - 1;
            Row row1 = null;
            for(int h = 1;h<height;h++){
                row1 = sheet.getRow(h);
                TaiQuModel taiQuModel = new TaiQuModel();
                taiQuModel.setStation(row1.getCell(0).getStringCellValue());
                taiQuModel.setLine(row1.getCell(1).getStringCellValue());
                taiQuModel.setSwitchOfLin(row1.getCell(2).getStringCellValue());
                taiQuModel.setTaiqu(row1.getCell(3).getStringCellValue());
                taiQuModel.setNum((int) row1.getCell(4).getNumericCellValue());
                taiQuModelArrayList.add(taiQuModel);
                System.out.println(taiQuModel.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "finish";
    }

    public static String write2Excel(String filePath, ExcelView.TableValueModel model) {
        // 判断后缀名，后缀名不同，生成的流不同
        if (filePath.endsWith("xlsx")) {
            poiwriteXlsx(model, filePath);
            return "不支持的文件格式";
        } else {
            return poiWriteXls(model, filePath);
        }

    }

    // SSF对应的是xls格式
    private static String poiWriteXls(ExcelView.TableValueModel model, String filepath) {
        InputStream inp;
        ifexist(filepath);
        try {
            List<List<Double>> dataList = model.dataList;
            inp = new FileInputStream(filepath);
            int rownum = dataList.size();
            int columnum = dataList.get(0).size();

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("sheet1");
            for (int i = 0; i < rownum; i++) {
                // System.out.println("i:"+i);
                Row row = sheet.createRow(i);
                for (int j = 0; j < columnum; j++) {
                    /* System.out.println("j:"+j); */
                    Cell cell = row.createCell(j);
                    // 设置格式
                    cell.setCellType(CellType.NUMERIC);
                    // 设置值
                    cell.setCellValue(dataList.get(i).get(j));
                }
            }
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(filepath);
            wb.write(fileOut);
            fileOut.close();
            inp.close();
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder builder = new StringBuilder();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                builder.append(element.toString() + "\n");
            }
            return builder.toString();
        }
        return "success";
    }

    // XSSF对应的是xlsx格式
    private static void poiwriteXlsx(ExcelView.TableValueModel model, String filepath) {
        InputStream inp;
        ifexist(filepath);
        try {
            inp = new FileInputStream(filepath);
            int rownum = model.dataList.size();
            int columnum = model.dataList.get(0).size();
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.createSheet("sheet1");
            for (int i = 0; i < rownum; i++) {
                // System.out.println("i:"+i);
                Row row = sheet.createRow(i);
                for (int j = 0; j < columnum; j++) {
                    /* System.out.println("j:"+j); */
                    Cell cell = row.createCell(j);
                    // 设置格式
                    cell.setCellType(CellType.STRING);
                    // 设置值
                    cell.setCellValue(model.dataList.get(i).get(j));
                }
            }
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(filepath);
            wb.write(fileOut);
            fileOut.close();
            inp.close();
            System.out.println("写入完成");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 传入文件的地址，判断文件是否存在，如果不存在的话创建该文件
    // 这个功能好像还存在一个小BUG，直接createNewFile();的文件不能用，以后找方法解决。
    public static void ifexist(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("文件不存在，创建该文件，文件地址为：" + path);
                file.createNewFile();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
