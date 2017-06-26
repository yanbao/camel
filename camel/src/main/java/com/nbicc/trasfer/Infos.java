package com.nbicc.trasfer;

import com.mongodb.BasicDBObject;
import com.nbicc.utils.MongoUtil;
import org.bson.types.ObjectId;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 大泥藕 on 2017/5/17.
 * 120.27.151.170:8083/bugtrapper/syDailyDate
 */

public class Infos {

    private MongoUtil mongoUtil = MongoUtil.MongoSigleton.INSTANCE.build();


    public List getDaily(String token,
                         String tid

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map smap = new HashMap();
            smap.put("code", 44);
            smap.put("isSuccess", "请输入正确口令");
            fl.add(smap);
            return fl;
        }
        //----------------------------------------------------------
        List nl = new ArrayList<>();
        long t_id = Long.parseLong(tid);
        BasicDBObject fQueryObject = new BasicDBObject("t_id", t_id);
        List<BasicDBObject> fObjs = mongoUtil.find("daily_data", fQueryObject);

        if (!MongoUtil.isEmpty(fObjs)) {
            SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd");
            for (BasicDBObject fObj : fObjs) {

                Map smap = new HashMap();
                Date tdate = fObj.getDate("t_date");
                smap.put("t_date", tdate);
                smap.put("t_date_str", fdf.format(tdate));
                smap.put("t_value", fObj.getInt("t_value"));
                nl.add(smap);

            }

        }
        return nl;

    }

    public List getDeviate(String token,
                            String tid

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map fmap = new HashMap();
            fmap.put("code", 46);
            fmap.put("isSuccess", "请输入正确口令");
            fl.add(fmap);
            return fl;
        }
        //----------------------------------------------------------
        List<Map> fl = this.getDaily("zmkm", tid);
        if (MongoUtil.isEmpty(fl)) {

            List sl = new ArrayList<>();
            Map smap = new HashMap();
            smap.put("code", 1);
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
        try {


            if (!MongoUtil.isEmpty(sObjs)) {

                for (BasicDBObject sObj : sObjs) {
                    Map smap = new HashMap();
                    ObjectId _id = sObj.getObjectId("_id");
                    smap.put("_id", _id.toHexString());
                    Date d_t_time = sObj.getDate("t_start_time");
                    if (d_t_time == null) {
                        smap.put("t_start_time", "null");
                        smap.put("t_value", sObj.get("t_value"));
                        nl.add(smap);
                        continue;
                    }
                    String fd = fdf.format(d_t_time);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fdf.parse(fd));
                    cal.add(Calendar.HOUR_OF_DAY, 12);
                    String tDate = fdf.format(cal.getTime());
                    Date point = ffdf.parse(tDate);

                    smap.put("before", fd);
                    smap.put("after", ffdf.format(point));
                    smap.put("t_value", sObj.get("t_value"));
                    nl.add(smap);
                }

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nl;
    }

    public List getDeviate2(String token,
                             String tid

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map fmap = new HashMap();
            fmap.put("code", 45);
            fmap.put("isSuccess", "请输入正确口令");
            fl.add(fmap);
            return fl;
        }
        //----------------------------------------------------------
        List<Map> fl = this.getDaily("zmkm", tid);
        if (MongoUtil.isEmpty(fl)) {

            List sl = new ArrayList<>();
            Map smap = new HashMap();
            smap.put("code", 0);
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
        try {

            for (Map fm : fl) {
                Date fdate = (Date) fm.get("t_date");
                String sdate = fdf.format(fdate);
                int fcount = 0;
                Map smap = new HashMap();
                if (!MongoUtil.isEmpty(sObjs)) {
                    for (BasicDBObject sObj : sObjs) {
                        Date d_t_time = sObj.getDate("t_start_time");
                        if (d_t_time == null) {
                            continue;
                        }
                        String fd = fdf.format(d_t_time);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fdf.parse(fd));
                        cal.add(Calendar.HOUR_OF_DAY, 12);
                        String tDate = fdf.format(cal.getTime());
                        Date point = ffdf.parse(tDate);

                        if ((point.compareTo(ffdf.parse(sdate))) == 0) {

                            fcount = fcount + sObj.getInt("t_value");
                            smap.put("h_t_date", d_t_time);
                            smap.put("h_t_date_str", fdf.format(d_t_time));
                            smap.put("h_date", point);
                            smap.put("h_date_str", fdf.format(point));
                            smap.put("d_date", fdate);
                            smap.put("d_date_str", fdf.format(fdate));
                            smap.put("count", fcount);

                        } else {

                            smap.put("d_date", fdate);
                            smap.put("d_date_str", fdf.format(fdate));

                        }

                    }
                    nl.add(smap);
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nl;
    }

    public List getRaw(String token,
                        String tid

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map smap = new HashMap();
            smap.put("code", 44);
            smap.put("isSuccess", "请输入正确口令");
            fl.add(smap);
            return fl;
        }
        //----------------------------------------------------------
        List nl = new ArrayList<>();
        long t_id = Long.parseLong(tid);
        BasicDBObject fQueryObject = new BasicDBObject("t_id", t_id);
        List<BasicDBObject> fObjs = mongoUtil.find("raw_data", fQueryObject);

        if (!MongoUtil.isEmpty(fObjs)) {
            SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd");
            for (BasicDBObject fObj : fObjs) {

                Map smap = new HashMap();
                Date tdate = fObj.getDate("t_time");
                smap.put("t_time", tdate);
                smap.put("t_time_str", fdf.format(tdate));
                smap.put("t_value", fObj.getInt("t_value"));
                nl.add(smap);

            }

        }
        return nl;

    }
}