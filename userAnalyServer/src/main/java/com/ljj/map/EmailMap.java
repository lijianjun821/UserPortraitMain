package com.ljj.map;

import com.ljj.entity.EmailInfo;
import com.ljj.utils.EmailUtils;
import com.ljj.utils.HbaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;


public class EmailMap implements MapFunction<String , EmailInfo> {
    //@Override
    public EmailInfo map(String s) throws Exception {
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

        String emailtype = EmailUtils.getEmailtypeBy(email);
        String tablename = "userflaginfo";
        String rowkey = userId;
        String famliyname = "baseinfo";
        String colum = "emailinfo";//运营商
        HbaseUtils.putdata(tablename,rowkey,famliyname,colum,emailtype);

        EmailInfo emailInfo = new EmailInfo();
        String groupfield = "carrierInfo=="+emailtype;
        emailInfo.setCount(1l);
        emailInfo.setEmailType(emailtype);
        emailInfo.setGroupField(groupfield);
        return emailInfo;
    }
}
