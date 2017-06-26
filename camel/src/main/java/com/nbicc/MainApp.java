package com.nbicc;

import com.nbicc.trasfer.BatchSyn;
import com.nbicc.utils.MongoUtil;
import org.apache.camel.main.Main;

import java.util.List;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        List<String> l = BatchSyn.batSyn("zmkm");
        if (!MongoUtil.isEmpty(l)){
            for (String s:l){
                System.out.println(l);
            }

        }else {
                System.out.println("false");
        }
        Main main = new Main();
        main.addRouteBuilder(new MyRouteBuilder());
        main.run(args);
    }

}

