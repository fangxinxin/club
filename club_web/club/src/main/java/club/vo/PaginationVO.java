package club.vo;

import com.google.gson.JsonArray;

import java.io.Serializable;

/**
 * Created by Aris on 2017/7/21.
 */
public class PaginationVO implements Serializable {
    private String total;
    private JsonArray rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public JsonArray getRows() {
        return rows;
    }

    public void setRows(JsonArray rows) {
        this.rows = rows;
    }

}
