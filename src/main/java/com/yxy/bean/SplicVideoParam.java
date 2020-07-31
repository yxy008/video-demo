package com.yxy.bean;

/**
 * @description: 视频裁剪、拼接参数
 * @author: yuxinyu
 * @create: 2020-06-19 16:43
 **/
public class SplicVideoParam {

    /**
     * 开始截取视频的时间
     */
    private String beginTime;

    /**
     * 结束截取视频的时间
     */
    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
