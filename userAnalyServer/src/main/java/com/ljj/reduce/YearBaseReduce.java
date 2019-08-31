package com.ljj.reduce;

import com.ljj.entity.YearBase;
import org.apache.flink.api.common.functions.ReduceFunction;

public class YearBaseReduce implements ReduceFunction<YearBase> {
    //@Override
    public YearBase reduce(YearBase yearBase, YearBase t1) throws Exception {
        String yearType = yearBase.getYearType();
        Long count1 = yearBase.getCount();
        Long count2 = t1.getCount();
        YearBase yearBase1 = new YearBase();
        yearBase1.setYearType(yearType);
        yearBase1.setCount(count1+count2);
        return yearBase1;
    }
}
