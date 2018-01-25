package dsqp.db_club;

import dsqp.db_club.dao.PromoterPayDao;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
* Created by Aris on 2017/2/15.
*/
public class main {

    public static void init(){
        InputStream in = main.class.getResourceAsStream("/proxool.xml");
        Reader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JAXPConfigurator.configure(reader, false);
        } catch (ProxoolException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        init();

        System.out.println(PromoterPayDao.getOne(358).getPrice());

    }

}
