package com.nbicc.trasfer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 大泥藕 on 2017/6/19.
 */

public class BatchSyn {



    public static List<String> batSyn(String token){
        SyDaily syDaily;
        CrDaily crDaily;
        CrDeviated crDeviated;

        Map ts = TrasSingleton.INSTANCE.buildTrasSingleton();
        syDaily = (SyDaily)ts.get("SyDaily");
        crDeviated = (CrDeviated)ts.get("CrDeviated");
        crDaily = (CrDaily)ts.get("CrDaily");
        List fl = new ArrayList();
        String[] fa = {"2061","2063","1413","1415","1959","3099","2996","3032","2995","2955","2943","0"};

        for (int i=0;i<fa.length;i++) {

            String tid = fa[i];
            crDeviated.crDeviate(token,tid,-5,150,1,0,10);
            crDaily.crDaily(token,tid,-5,150,1,0,10);
            syDaily.syDaily(token,tid);
            fl.add(tid);
        }


        return fl;
    }

}
