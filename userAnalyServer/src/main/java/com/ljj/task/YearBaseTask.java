package com.ljj.task;

import com.ljj.entity.YearBase;
import com.ljj.map.YearBaseMap;
import com.ljj.reduce.YearBaseReduce;
import com.ljj.utils.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.List;

public class YearBaseTask {
    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataSource<String> text = env.readTextFile(params.get("input"));

        DataSet<YearBase> mapresult = text.map(new YearBaseMap());
        DataSet<YearBase> reduceresult  = mapresult.groupBy("groupField").reduce(new YearBaseReduce());
        try {
            List<YearBase> resultList = reduceresult.collect();
            for (YearBase yearBase : resultList){
                String yeartype = yearBase.getYearType();
                Long count = yearBase.getCount();
                Document doc = MongoUtils.findoneby("yearbasestatics","userPortrait",yeartype);
                if(doc == null){
                    doc = new Document();
                    doc.put("info",yeartype);
                    doc.put("count",count);
                }else{
                    Long countpre = doc.getLong("count");
                    Long total = countpre+count;
                    doc.put("count",total);
                }
                MongoUtils.saveorupdatemongo("yearbasestatics","userPortrait",doc);
            }
            env.execute("year base analy");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
