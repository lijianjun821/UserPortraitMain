package com.ljj.map;

import com.ljj.entity.YearBase;
import com.ljj.utils.DateUtils;
import com.ljj.utils.HbaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;


public class YearBaseMap implements MapFunction<String , YearBase> {
    //@Override
    public YearBase map(String s) throws Exception {
        if(StringUtils.isBlank(s)){
            return null;
        }

        String[] userInfos = s.split(",");
        String userId = userInfos[0];
        String userName = userInfos[1];
        String sex = userInfos[2];
        String telPhone = userInfos[3];
        String email = userInfos[4];
        String age = userInfos[5];
        String registerTime = userInfos[6];
        String useType = userInfos[7];//'终端类型：0、pc端；1、移动端；2、小程序端'

        String yearbasetype = DateUtils.getYearBaseByAge(age);
        String tablename = "userflaginfo";
        String rowkey = userId;
        String famliyname = "baseinfo";
        String colum = "yearbase";//年代
        HbaseUtils.putdata(tablename,rowkey,famliyname,colum,yearbasetype);
        HbaseUtils.putdata(tablename,rowkey,famliyname,"age",age);

        YearBase yearBase = new YearBase();
        String groupfield = "yearbase=="+yearbasetype;
        yearBase.setYearType(yearbasetype);
        yearBase.setCount(1l);
        yearBase.setGroupField(groupfield);
        return yearBase;
    }
}
