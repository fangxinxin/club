package admin.util;

import admin.vo.ExcelVO;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;

import java.util.Map;

/**
 * Created by ds on 2017/8/15.
 */
public class OutExcelUtil {

    /**
     * 导出excel 内容
     * by ds
     */
/*    public static ExcelVO download(String name, Map<String, String> rowNames, DataTable dt){
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append(name)
                .append(System.currentTimeMillis());

        //拼接要输出的字符串
        StringBuilder sb = new StringBuilder();

        for (String value: rowNames.values()){
            sb.append("\t"+value).append(",");
        }
        sb.append("\r\n");

        //拼接cvs内容
        for (DataRow row : dt.rows) {
            for (String key: rowNames.keySet()) {
//                System.out.println(row.getColumnValue(key));
                sb.append("\t").append(row.getColumnValue(key)).append(",");
            }
            sb.append("\r\n");
        }
        ExcelVO vo = new ExcelVO();
        vo.setFileName(fileName.toString());
        vo.setContent(sb.toString());

        return vo;
    }*/

    public static ExcelVO download(String name, Map<String, String> rowNames, DataTable dt){
        //拼接文件名
        String fileName = getFileName(name);

        //拼接要输出的字符串
        StringBuilder sb = new StringBuilder(128);

        //拼接表头
        sb.append(getThead(rowNames));

        //拼接表格主体内容
        sb.append(getTbody(rowNames, dt));

        ExcelVO vo = new ExcelVO();
        vo.setFileName(fileName);
        vo.setContent(sb.toString());

        return vo;
    }

    /**
     * 设置文件名
     */
    public static String getFileName(String name){
        StringBuilder fileName = new StringBuilder(64);

        fileName.append(name)
                .append(System.currentTimeMillis());

        return fileName.toString();
    }

    /**
     * 设置表头
     */
    public static String getThead(Map<String, String> rowNames){
        StringBuilder thead = new StringBuilder(64);

        for (String value: rowNames.values()){
            thead.append("\t"+value).append(",");
        }
        thead.append("\r\n");
        return thead.toString();
    }

    /**
     * 设置表格主体
     */
    public static String getTbody(Map<String, String> rowNames, DataTable dt){
        StringBuilder tbody = new StringBuilder(128);

        for (DataRow row : dt.rows) {
            for (String key: rowNames.keySet()) {
                tbody.append("\t").append(row.getColumnValue(key)).append(",");
            }
            tbody.append("\r\n");
        }
        return tbody.toString();
    }

}
