package club.main;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Aris on 2017/12/13.
 */
public class ShutDownHook extends Thread {

    private ConfigurableApplicationContext applicationContext;

    public ShutDownHook(ConfigurableApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        System.out.println("关闭资源");
        applicationContext.close();
    }
}