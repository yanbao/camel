package com.nbicc.trasfer;

import com.mongodb.BasicDBObject;
import com.nbicc.utils.MongoUtil;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 大泥藕 on 2017/5/17.
 */

public class SyDaily {

    private MongoUtil mongoUtil = MongoUtil.MongoSigleton.INSTANCE.build();
    private  static  int tag = 0;


    private List<BasicDBObject> getDaily(String tid) {

        long t_id = Long.parseLong(tid);
        BasicDBObject fQueryObject = new BasicDBObject("t_id", t_id);
        List<BasicDBObject> fObjs = mongoUtil.find("daily_data", fQueryObject);

        return fObjs;

    }
    public List syDaily(String token,
                         String tid

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map fmap = new HashMap();
            fmap.put("code", 62);
            fmap.put("isSuccess", "请输入正确口令");
            fl.add(fmap);
            return fl;
        }
        //----------------------------------------------------------
        List<BasicDBObject> fl = this.getDaily(tid);
        if(MongoUtil.isEmpty(fl)){

            List sl = new ArrayList<>();
            Map smap = new HashMap();
            smap.put("code", 63);
            smap.put("data", "");
            sl.add(smap);
            return sl;
        }
        //----------------------------------------------------------
        List nl = new ArrayList<>();
        BasicDBObject squery = new BasicDBObject("t_id", Long.parseLong(tid));
        List<BasicDBObject> sObjs = mongoUtil.find("deviated_data", squery);
        SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ffdf = new SimpleDateFormat("yyyy-MM-dd");
        try{

            for (BasicDBObject fm:fl){
                Date fdate  = fm.getDate("t_date");
                ObjectId _id = fm.getObjectId("_id");
                String sdate = fdf.format(fdate);
                Map smap = new HashMap();
                if (_id==null){
                    smap.put("_id","null");
                    nl.add(smap);
                    continue;
                }




                if(!MongoUtil.isEmpty(sObjs)){
                    int fcount = 0;
                    tag = 0;

                    for(BasicDBObject sObj :sObjs) {
                        Date d_t_time  = sObj.getDate("t_start_time");
                        if(d_t_time==null){
                            continue;
                        }
                        String fd = fdf.format(d_t_time);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fdf.parse(fd));
                        cal.add(Calendar.HOUR_OF_DAY, 0);
                        String tDate = fdf.format(cal.getTime());
                        Date point = ffdf.parse(tDate);

                        if ((point.compareTo(ffdf.parse(sdate)))==0){

                            fcount = fcount + sObj.getInt("t_value");

                            smap.put("d_date", fdate);
                            smap.put("d_date_str",fdf.format(fdate));
                            smap.put("count", fcount);
                            tag = 1;



                        }else{

                            smap.put("d_date", fdate);
                            smap.put("d_date_str",fdf.format(fdate));

                        }


                    }
                    if (tag>0) {
                        Document query = new Document();
                        query.put("_id", _id);
                        BasicDBObject fresult = new BasicDBObject("t_value", fcount);
                        boolean ret = mongoUtil.update("daily_data", query, fresult, false);
                        if (ret) {
                            smap.put("t_valueUpdate", fcount);
                            smap.put("isSynchronised","true");
                        }
                        smap.put("_id", _id.toHexString());
                        nl.add(smap);

                    }else {
                        smap.put("isSynchronised","fasle");
                        smap.put("_id", _id.toHexString());
                        nl.add(smap);
                    }
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nl;
    }
}