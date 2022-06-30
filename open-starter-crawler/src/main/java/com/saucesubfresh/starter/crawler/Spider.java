package com.saucesubfresh.starter.crawler;

import com.saucesubfresh.starter.crawler.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijunping on 2022/6/29
 */
public class Spider implements Runnable{

    private List<Pipeline> pipelines = new ArrayList<Pipeline>();

    @Override
    public void run() {

    }
}
