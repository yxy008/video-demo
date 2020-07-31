package com.yxy.util;

import com.yxy.constants.ToolConstant;
import it.sauronsoftware.jave.Encoder;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @program: yxydemo
 * @description: 视频相关操作工具类
 * @author: yuxinyu
 * @create: 2020-07-24 15:05
 **/
public class VideoHandleUtil {

    private static final Integer FIVE = 5;

    private static final Integer TEN = 10;

    private static final Integer FIFTEEN = 15;

    private static final Integer SIXTY = 60;

    public static List<String> captureImage(String videoPath, String destPath) throws FileNotFoundException {

        List<String> picNameList = new ArrayList<>();
        System.out.println("=======================videoPath: " + videoPath);
        File source = new File(videoPath);
        String ffmpegPath = ToolConstant.FFMPEG_PATH;
        Encoder encoder = new Encoder();
        String size = "";
        try {
            it.sauronsoftware.jave.MultimediaInfo m = null;
            m = encoder.getInfo(source);
            long ls = m.getDuration();
            System.out.println("此视频时长为:" + (int)((ls) / 1000) + "秒！");
            int second = (int)((ls) / 1000);
            List<String> secondList = new ArrayList();
            if (second < 1) {
                secondList.add("0");
            } else if (second >= 1 && second < FIVE) {
                secondList.add("1");
            } else if (second >= FIVE && second < TEN) {
                secondList.add("1");
                secondList.add("5");
            } else if (second >= TEN && second < FIFTEEN) {
                secondList.add("1");
                secondList.add("5");
                secondList.add("10");
            } else if (second >= FIFTEEN) {
                secondList.add("1");
                secondList.add("5");
                secondList.add("10");
                secondList.add("15");
            } else {

            }
            for (String seconds : secondList) {
                String imagePath = processImg(videoPath, ffmpegPath, seconds, destPath);
                if (!StringUtils.isEmpty(imagePath)) {
                    picNameList.add(imagePath.substring(imagePath.lastIndexOf("/")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return picNameList;
    }

    public static String processImg(String veidoPath, String ffmpegPath, String second, String tmpDir) {
        File file = new File(veidoPath);
        if (!file.exists()) {
            System.err.println("路径[" + veidoPath + "]对应的视频文件不存在!");
            return null;
        }
        String vedioName = veidoPath.substring(veidoPath.lastIndexOf("/"));
        File fileDir = new File(tmpDir + "/");
        if (!fileDir.exists()) {
            if (fileDir.mkdirs()) {

            }
        }
        String destPath = tmpDir + vedioName.substring(0, vedioName.lastIndexOf("."))  + "S" + second + ".jpg";
        System.out.println("destPath : " + destPath);
        File desFile = new File(destPath);
        if (desFile.exists()) {
            if (desFile.delete()) {

            }
        }
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(veidoPath);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-ss");
        //这个参数是设置截取视频多少秒时的画面
        commands.add(second);
        commands.add("-s");
        commands.add("700x525");
        commands.add(destPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            Process process = builder.start();
            process.waitFor();
            process.destroy();
            System.out.println("截取成功");
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 视频剪辑
     * @param srcPath
     * @param destPath
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean clipVideo(String srcPath, String destPath, String startTime, String endTime) throws Exception {
        boolean isSuccess = false;
        String ffempegPath = ToolConstant.FFMPEG_PATH;
        endTime = timeHandle(startTime, endTime);
        //生成输出视频文件格式，也可以用源文件的格式
        // 创建一个List集合来保存转换视频文件为flv格式的命令
        List<String> convert = new ArrayList<String>();
        // 添加转换工具路径
        convert.add(ffempegPath);

        convert.add("-i");
        // 添加要转换格式的视频文件的路径
        convert.add(srcPath);

        //起始时间
        convert.add("-ss");
        convert.add(startTime);
        //结束时间
        convert.add("-to");
        convert.add(endTime);
        //操作方式
        convert.add("-c");
        convert.add("copy");
        convert.add(destPath);
        // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add("-y");

        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(convert);
            Process p = builder.start();
            p.waitFor();
            isSuccess = true;
        }  catch (Exception e) {
            // 系统异常 默认 异常信息为 "远程调用错误",可以自行指定
            throw new Exception();
        }
        return isSuccess;
    }

    /**
     * 视频剪辑拼接
     * @param filePath
     * @param destPath
     * @return
     */
    public static boolean splicVideo(String filePath, String destPath) throws Exception {
        boolean isSuccess = false;
        String ffempegPath = ToolConstant.FFMPEG_PATH;
        //生成输出视频文件格式，也可以用源文件的格式
        // 创建一个List集合来保存转换视频文件为flv格式的命令
        List<String> convert = new ArrayList<String>();
        // 添加转换工具路径
        convert.add(ffempegPath);

        convert.add("-f");
        convert.add("concat");
        convert.add("-i");
        // 添加要转换格式的视频文件的路径
        convert.add(filePath);

        //操作方式
        convert.add("-codec");
        convert.add("copy");
        convert.add(destPath);
//		// 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add("-y");

        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(convert);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            isSuccess = true;
        } catch (Exception e) {
            throw new Exception();
        }
        return isSuccess;
    }

    /**
     *时间处理，当时间差小于5秒时，开始时间默认+1
     * @param startTime
     * @param endTime
     * @return
     */
    public static String timeHandle (String startTime, String endTime) {
        long startHour = Long.parseLong(startTime.substring(0, 2));
        long startminute = Long.parseLong(startTime.substring(3, 5));
        long startSeconds = Long.parseLong(startTime.substring(6, 8));
        long endHour = Long.parseLong(endTime.substring(0, 2));
        long endminute = Long.parseLong(endTime.substring(3, 5));
        long endSeconds = Long.parseLong(endTime.substring(6, 8));
        long subStartSeconds = (startHour * 60 * 60) + (startminute * 60) + (startSeconds);
        long subEndSeconds = (endHour * 60 * 60) + (endminute * 60) + (endSeconds);
        long timecha = subEndSeconds - subStartSeconds;
        if (timecha < FIVE) {
            endSeconds = endSeconds + 1;
            if (endSeconds >= SIXTY) {
                endminute = endminute + 1;
                endSeconds = 0;
                if (endminute >= SIXTY) {
                    endHour = endHour + 1;
                    endminute = 0;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (endHour < TEN) {
                stringBuilder.append("0").append(endHour).append(":");
            } else {
                stringBuilder.append(endHour).append(":");
            }
            if (endminute < TEN) {
                stringBuilder.append("0").append(endminute).append(":");
            } else {
                stringBuilder.append(endminute).append(":");
            } if (endSeconds < TEN) {
                stringBuilder.append("0").append(endSeconds);
            } else {
                stringBuilder.append(endSeconds);
            }
            endTime = stringBuilder.toString();
            return endTime;
        } else {
            return endTime;
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
}
