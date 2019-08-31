package com.ljj.reduce;

import com.ljj.entity.EmailInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * Created by li on 2019/1/5.
 */
public class EmailReduce implements ReduceFunction<EmailInfo>{


    //@Override
    public EmailInfo reduce(EmailInfo emaiInfo, EmailInfo t1) throws Exception {
        String emailtype = emaiInfo.getEmailType();
        Long count1 = emaiInfo.getCount();

        Long count2 = t1.getCount();

        EmailInfo emaiInfofinal = new EmailInfo();
        emaiInfofinal.setEmailType(emailtype);
        emaiInfofinal.setCount(count1+count2);

        return emaiInfofinal;
    }
}
