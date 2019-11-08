package com.ljj.task;

import com.ljj.entity.EmailInfo;
import com.ljj.map.EmailMap;
import com.ljj.reduce.EmailReduce;
import com.ljj.utils.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.List;

public class EmailTask {
    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataSource<String> text = env.readTextFile(params.get("input"));

        DataSet<EmailInfo> mapresult = text.map(new EmailMap());
        DataSet<EmailInfo> reduceresult  = mapresult.groupBy("groupField").reduce(new EmailReduce());
        try {
            List<EmailInfo> resultList = reduceresult.collect();
            for (EmailInfo emailInfo : resultList){
                String emailtype = emailInfo.getEmailType();
                Long count = emailInfo.getCount();
                Document doc = MongoUtils.findoneby("emailstatics","userPortrait",emailtype);
                if(doc == null){
                    doc = new Document();
                    doc.put("info",emailtype);
                    doc.put("count",count);
                }else{
                    Long countpre = doc.getLong("count");
                    Long total = countpre+count;
                    doc.put("count",total);
                }
                MongoUtils.saveorupdatemongo("emailstatics","userPortrait",doc);
            }
            //env.execute("email analy");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
