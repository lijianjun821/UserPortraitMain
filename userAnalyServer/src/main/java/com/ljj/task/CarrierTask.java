package com.ljj.task;

import com.ljj.entity.CarrierInfo;
import com.ljj.map.CarrierMap;
import com.ljj.reduce.CarrierReduce;
import com.ljj.utils.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.List;

public class CarrierTask {
    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataSource<String> text = env.readTextFile(params.get("input"));

        DataSet<CarrierInfo> mapresult = text.map(new CarrierMap());
        DataSet<CarrierInfo> reduceresult  = mapresult.groupBy("groupField").reduce(new CarrierReduce());
        try {
            List<CarrierInfo> resultList = reduceresult.collect();
            for (CarrierInfo carrierInfo : resultList){
                String carriertype = carrierInfo.getCarrier();
                Long count = carrierInfo.getCount();
                Document doc = MongoUtils.findoneby("carrierstatics","userPortrait",carriertype);
                if(doc == null){
                    doc = new Document();
                    doc.put("info",carriertype);
                    doc.put("count",count);
                }else{
                    Long countpre = doc.getLong("count");
                    Long total = countpre+count;
                    doc.put("count",total);
                }
                MongoUtils.saveorupdatemongo("carrierstatics","userPortrait",doc);
            }
            //env.execute("carrier analy");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
