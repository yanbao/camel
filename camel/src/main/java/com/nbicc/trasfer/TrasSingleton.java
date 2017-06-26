package com.nbicc.trasfer;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/21 0021.
 */
public enum  TrasSingleton {

    INSTANCE;
    private  TrasSingleton(){};
    public Map<String,Object> buildTrasSingleton(){
        Map m = new HashMap();
        CrDeviated deviated = new CrDeviated();
        CrDaily daily = new CrDaily();
        SyDaily syDaily = new SyDaily();
        m.put("CrDeviated",deviated);
        m.put("CrDaily",daily);
        m.put("SyDaily",syDaily);
        return m;
    }
}
