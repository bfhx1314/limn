package com.extend.start;

import com.extend.dashboard.WriteLog;
import com.extend.keyword.OSKeyWordImpl;
import com.automation.frame.run.Run;
import com.extend.keyword.WindOSKeyWordImpl;

/**
 * Created by snow.zhang on 2015/12/18.
 */
public class RunCase extends Run {

    public static void main(String[] args){
        Run run = new Run();
        run.addKeyWordDriver("OS关键字", new OSKeyWordImpl());
        run.addKeyWordDriver("WindOS关键字", new WindOSKeyWordImpl());
        run.runCase(args);
        // 写入Dashboard
        WriteLog.insert(run.getTestGather());
    }
}
