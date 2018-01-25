package dsqp.db_game.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Aris on 2017/7/17.
 */
public class BaseDao {
    public static final String DBNAME_DEV_SUFFIX       = "_dev";
    public static final String DBNAME_DEV_SUFFIX_READ  = "_dev_read";
    public static final String DBNAME_LOG_SUFFIX       = "_log_dev";
    public static final String DBNAME_LOG_SUFFIX_READ  = "_log_dev_read";

    private static Properties props;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
            in = BaseDao.class.getClassLoader().getResourceAsStream("proxool.properties");
            props.load(new InputStreamReader(in,"utf-8"));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    protected static Properties getProperty(){
        if(null == props) {
            loadProps();
        }
        return props;
    }

}
