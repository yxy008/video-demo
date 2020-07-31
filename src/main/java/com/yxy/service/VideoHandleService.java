package com.yxy.service;

import com.yxy.util.ConverVideoUtils;
import com.yxy.util.VideoHandleUtil;
import com.yxy.util.VideoUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @program: yxydemo
 * @description: 视频相关操作实现
 * @author: yuxinyu
 * @create: 2020-07-24 15:04
 **/
@Component
public class VideoHandleService {

    public List<String> capture(String videoPath, String destPath) {
        List<String> imageNameList = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(videoPath) && !StringUtils.isEmpty(destPath)) {
                imageNameList = VideoHandleUtil.captureImage(videoPath, destPath);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imageNameList;
    }

    /**
     * 转换视频格式
     *
     * @return
     * @throws Exception
     */
    public boolean beginConver(String srcPath, String destPath) throws Exception {
        boolean isSuccess = false;
        System.out.println("接收到文件(" + srcPath + ")需要转换");
        if (!VideoUtil.checkfile(srcPath)) {
            System.out.println(srcPath + "文件不存在" + " ");
            return isSuccess;
        }
        long beginTime = System.currentTimeMillis();
        System.out.println("开始转文件(" + srcPath + ")");
        Process p = ConverVideoUtils.process(srcPath, destPath);
        long endTime = System.currentTimeMillis();
        long timeCha = (endTime - beginTime);
        if (p != null) {
            System.out.println("转换成功");
            Map<String, Process> map;
            String totalTime = ConverVideoUtils.sumTime(timeCha);
            System.out.println("转换视频格式共用了:" + totalTime + " ");
            isSuccess = true;
        } else {
        }
        return isSuccess;
    }

    /**
     * 视频裁剪
     * @param srcPath
     * @param destPath
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public boolean clipVideo(String srcPath, String destPath, String startTime, String endTime) throws Exception {
        return VideoHandleUtil.clipVideo(srcPath, destPath, startTime, endTime);
    }

    /**
     * 视频裁剪
     * @param destPath
     * @param list
     * @return
     * @throws Exception
     */
    public boolean splicVideo(String destPath, List<String> list) throws Exception {
        boolean isSuccess = false;
        if (list != null && list.size() > 0) {
            File fileListPath = new File(list.get(0).substring(0, list.get(0).lastIndexOf("/")) + "/list.txt");
            if (!fileListPath.exists()) {
                if (fileListPath.createNewFile()) {

                }
            }
            FileOutputStream fos = new FileOutputStream(fileListPath);
            StringBuilder stringBuilder = new StringBuilder();
            for (String splicVideoPath : list) {
                stringBuilder.append("file '" + splicVideoPath.substring(splicVideoPath.lastIndexOf("/") + 1) + "'" + "\n");
            }
            fos.write(stringBuilder.toString().getBytes());
            fos.close();
            isSuccess = VideoHandleUtil.splicVideo(list.get(0).substring(0, list.get(0).lastIndexOf("/")) + "/list.txt", destPath);
            if (fileListPath.delete()) {

            }
        }
        return isSuccess;
    }
}
