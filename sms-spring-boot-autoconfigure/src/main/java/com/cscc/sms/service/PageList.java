package com.cscc.sms.service;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 16:32
 */
public class PageList<D> {
    int start = 0;
    int count = 0;
    int total = 0;
    List<D> data;
}
