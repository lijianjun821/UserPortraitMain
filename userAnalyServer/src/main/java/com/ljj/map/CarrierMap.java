package com.ljj.map;

import com.ljj.entity.CarrierInfo;
import com.ljj.utils.CarrierUtils;
import com.ljj.utils.HbaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;


public class CarrierMap implements MapFunction<String , CarrierInfo> {
    //@Override
    public CarrierInfo map(String s) throws Exception {
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

        int carriertype = CarrierUtils.getCarrierByTel(telPhone);
        String carriertypestring = carriertype==0?"未知运营商":carriertype==1?"移动用户":carriertype==2?"联通用户":"电信用户";
        String tablename = "userflaginfo";
        String rowkey = userId;
        String famliyname = "baseinfo";
        String colum = "carrierinfo";//运营商
        HbaseUtils.putdata(tablename,rowkey,famliyname,colum,carriertypestring);

        CarrierInfo carrierInfo = new CarrierInfo();
        String groupfield = "carrierInfo=="+carriertype;
        carrierInfo.setCount(1l);
        carrierInfo.setCarrier(carriertypestring);
        carrierInfo.setGroupField(groupfield);
        return carrierInfo;
    }
}
