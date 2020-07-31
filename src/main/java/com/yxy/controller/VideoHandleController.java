package com.yxy.controller;

import com.yxy.bean.OperationResult;
import com.yxy.bean.SplicVideoParam;
import com.yxy.service.VideoHandleService;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: yxydemo
 * @description: 视频操作类
 * @author: yuxinyu
 * @create: 2020-07-24 15:02
 **/
@Controller
public class VideoHandleController {

    @Autowired
    private VideoHandleService videoHandleService;

    /**
     * 视频截取封面图
     * @param srcPath
     * @param destPath
     * @return
     */
    @PostMapping("capture-image")
    @ApiOperation(value="视频截图")
    @ResponseBody
    public OperationResult captureImage(String srcPath, String destPath) {
        OperationResult result = new OperationResult();
        String message = "";
        boolean isSuccess = false;
        List<String> list = new ArrayList<>();
        try {
            list = videoHandleService.capture(srcPath, destPath);
            if (list.size() > 0) {
                message = "截图成功";
                isSuccess = true;
            } else {
                message = "截图失败";
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        JSONArray json = JSONArray.fromObject(list);
        String jsonResult = json.toString();
        result.setMessage(message);
        result.setSuccess(isSuccess);
        result.setResult(jsonResult);
        return result;
    }

    /**
     * 视频转码
     * @param srcPath
     * @param destPath
     * @return
     */
    @PostMapping("video-transcode")
    @ApiOperation(value = "视频转码")
    @ResponseBody
    public OperationResult videoTranscoding(String srcPath, String destPath) {
        OperationResult result = new OperationResult();
        String message = "";
        boolean isSuccess = false;
        if (srcPath == null || destPath == null) {
            message = "视频原路径或视频转码后存放路径为空";
            result.setMessage(message);
            result.setSuccess(isSuccess);
            return result;
        }
        try {
            isSuccess = videoHandleService.beginConver(srcPath, destPath);
            message = "视频转码成功";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = e.getMessage();
        }
        result.setMessage(message);
        result.setSuccess(isSuccess);
        return result;
    }

    /**
     * 视频裁剪
     * @param srcPath
     * @param destPath
     * @return
     */
    @PostMapping("clip-video")
    @ApiOperation(value = "视频裁剪")
    @ResponseBody
    public OperationResult clipVideo(String srcPath, String destPath, SplicVideoParam splicVideoParam) {
        OperationResult result = new OperationResult();
        String message = "";
        boolean isSuccess = false;
        if (srcPath == null || destPath == null) {
            message = "视频原路径或视频转码后存放路径为空";
            result.setMessage(message);
            result.setSuccess(isSuccess);
            return result;
        }
        try {
            isSuccess = videoHandleService.clipVideo(srcPath, destPath, splicVideoParam.getBeginTime(), splicVideoParam.getEndTime());
            message = "视频裁剪成功";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = e.getMessage();
        }
        result.setSuccess(isSuccess);
        result.setMessage(message);
        return result;
    }

    /**
     * 视频拼接
     * @param videoList
     * @param destPath
     * @return
     */
    @PostMapping("splic-video")
    @ApiOperation(value = "视频拼接")
    @ResponseBody
    public OperationResult splicVideo(String destPath, List<String> videoList) {
        OperationResult result = new OperationResult();
        String message = "";
        boolean isSuccess = false;
        if (destPath == null || videoList == null || videoList.size() <= 0) {
            message = "视频原路径或视频拼接后存放路径为空";
            result.setMessage(message);
            result.setSuccess(isSuccess);
            return result;
        }
        try {
            isSuccess = videoHandleService.splicVideo(destPath, videoList);
            message = "视频拼接成功";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = e.getMessage();
        }
        result.setSuccess(isSuccess);
        result.setMessage(message);
        return result;
    }
}
