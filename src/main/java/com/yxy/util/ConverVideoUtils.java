package com.yxy.util;

import com.yxy.constants.ToolConstant;
import com.yxy.constants.VideoTypeConstant;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频视频转码工具类
 * @Author yuxinyu
 * @Date 2019/11/15 10:09
 */
public class ConverVideoUtils {

    public static final String SEPARATE = "/";

    /**
     * 实际转换视频格式的方法
     *
     * @param sourceVideoPath 原文件地址
     * @param desPath 目标视频扩展名
     * @return
     */
    public static Process process(String sourceVideoPath, String desPath) {
        String ffmpegPath = ToolConstant.FFMPEG_PATH;
        String menCoderPath = ToolConstant.MECODER_PATH;
        int type = checkContentType(sourceVideoPath);
        String path = "";
        Process p = null;
        if (type == 0) {
            //如果type为0用ffmpeg直接转换
            p = processVideoFormat(sourceVideoPath, desPath, ffmpegPath);
        } else if (type == 1) {
            //如果type为1，将其他文件先转换为avi，然后在用ffmpeg转换为指定格式
            String aviFilePath = processAvi(desPath, sourceVideoPath, menCoderPath);
            if (aviFilePath == null) {
                // avi文件没有得到
                return null;
            } else {
                System.out.println("开始转换:");
                p = processVideoFormat(aviFilePath, desPath, ffmpegPath);
            }
        } else {

        }
        return p;
    }

    /**
     * 检查文件类型
     *
     * @param sourceVideoPath 原文件地址
     * @return
     */
    private static int checkContentType(String sourceVideoPath) {
        String type = sourceVideoPath.substring(sourceVideoPath.lastIndexOf(".") + 1).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals(VideoTypeConstant.AVI_TYPE)) {
            return 0;
        } else if (type.equals(VideoTypeConstant.MPG_TYPE)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.WMV_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.GP_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.MOV_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.MPFORE_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.ASF_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.ASX_TYPE, type)) {
            return 0;
        } else if (StringUtils.equals(VideoTypeConstant.FLV_TYPE, type)) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (StringUtils.equals(VideoTypeConstant.WMVNINE_TYPE, type)) {
            return 1;
        } else if (StringUtils.equals(VideoTypeConstant.RM_TYPE, type)) {
            return 1;
        } else if (StringUtils.equals(VideoTypeConstant.RMVB_TYPE, type)) {
            return 1;
        } else {

        }
        return 9;
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件地址
     * @return
     */
    public static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     *
     * @param desPath    文件名不带扩展名
     * @param sourceVideoPath 原video文件地址
     * @param  menCoderPath  menCoder插件地址
     * @return
     */
    private static String processAvi(String desPath, String sourceVideoPath, String menCoderPath) {
        /**
         * 转码后的存放视频地址  avi格式
         */
        List<String> commend = new ArrayList<>();
        commend.add(menCoderPath);
        commend.add(sourceVideoPath);
        commend.add("-oac");
        commend.add("mp3lame");
        commend.add("-lameopts");
        commend.add("preset=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(desPath + ".avi");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            p.waitFor();
            return desPath + ".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换为指定格式
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     * @param oldFilePath     源文件地址
     * @param desPath    目标文件地址
     * @param ffmpegPath ffmpeg插件地址
     * @return
     */
    private static Process processVideoFormat(String oldFilePath, String desPath, String ffmpegPath) {
        /**
         * 转码后的存放视频地址 mp4格式
         */
        if (!checkfile(oldFilePath)) {
            System.out.println(oldFilePath + "文件不存在");
            return null;
        }
        List<String> commend = new ArrayList<>();

        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(oldFilePath);
        commend.add("-ar");
        commend.add("22050");
        commend.add(desPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();

//            String pid = Md5Util.encode(oldFilePath + desPath);
//            String pid = DigestUtils.md5Hex(oldFilePath + desPath);
//            Map<String, Process> map;
//            if (VideoConvertService.processMap.size() == 0) {
//                map = new HashMap<>(ToolConstant.MAP_INIT_SIZE);
//                map.put(pid, p);
//                VideoConvertService.processMap.put("process", map);
//            } else {
//                map = VideoConvertService.processMap.get("process");
//                map.put(pid, p);
//                VideoConvertService.processMap.replace("process", map);
//            }
            doWaitFor(p);
//            if (VideoConvertService.processMap.get(PROCESS).get(pid) == null) {
//                VideoConvertService.messageMap.put(pid, "取消");
//                return null;
//            } else {
            p.destroy();
            String videoPath = desPath;
            return p;
//            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 将mpeg4转为h264编码 为了支持播放器
     *
     * @param path
     * @param ffmpegPath
     * @return
     */
    private static String processVideoFormatH264(String path, String ffmpegPath, String desPath) {
        if (!checkfile(path)) {
            System.out.println(path + "文件不存在");
            return "";
        }
        String newFilePath = desPath;
        List<String> commend = new ArrayList<>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(path);
        commend.add("-vcodec");
        commend.add("h264");
        commend.add("-q");
        commend.add("0");
        commend.add("-y");
        commend.add(newFilePath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            p.waitFor();
            p.destroy();
            deleteFile(path);
            return newFilePath;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public static int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;
        try {
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false;

            while (!finished) {
                try {
                    while (in.available() > 0) {
                        in.read();
                    }
                    while (err.available() > 0) {
                        err.read();
                    }

                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            System.out.println("doWaitFor();: unexpected exception - " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return exitValue;
    }

    /**
     * 删除文件方法
     *
     * @param filepath
     */
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.delete()) {
            System.out.println("文件" + filepath + "已删除");
        }
    }

    /**
     * 计算转码时间
     *
     * @param ms
     * @return
     */
    public static String sumTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day + "天" : "" + day + "天";
        String strHour = hour < 10 ? "0" + hour + "小时" : "" + hour + "小时";
        String strMinute = minute < 10 ? "0" + minute + "分" : "" + minute + "分";
        String strSecond = second < 10 ? "0" + second + "秒" : "" + second + "秒";
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond + "毫秒" : ""
                + strMilliSecond + " 毫秒";
        return strDay + " " + strHour + ":" + strMinute + ":" + strSecond + " "
                + strMilliSecond;

    }

    /**
     * 从名字上判断是否是目录
     *
     * @param path
     * @return
     */
    public static boolean isDir(String path) throws Exception{
        if (StringUtils.isEmpty(path)) {
            throw new Exception("path is empty");
        }
        path = fixSep(path);
        if (path.endsWith(SEPARATE)) {
            return true;
        }
        return false;
    }

    /**
     * 转换 \ 为 /
     *
     * @param path
     * @return
     */
    public static String fixSep(String path) {
        if (StringUtils.isNotEmpty(path)) {
            path = StringUtils.replace(path, "\\", "/");
            path = path.replaceAll("\\.+/", "");
        }
        return path;
    }

    public static boolean isExit(String path) {
        boolean isSuccess = false;
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {

            } else {
                return false;
            }
        }
        try {
            if (file.createNewFile()) {
                if (!file.isFile()) {
                    isSuccess = false;
                } else {
                    isSuccess = true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return isSuccess;
    }
}
