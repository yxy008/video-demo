package com.yxy.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @program: yxydemo
 * @description: 通用工具类
 * @author: yuxinyu
 * @create: 2020-07-24 16:28
 **/
public class VideoUtil {

    /**
     * 从名字上判断是否是目录
     *
     * @param path
     * @return
     */
    public static boolean isDir(String path) throws Exception{
        if (path == null) {
            throw new Exception("path is empty");
        }
        path = fixSep(path);
        if (path.endsWith("/")) {
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
        if (path != null) {
            path = StringUtils.replace(path, "\\", "/");
            path = path.replaceAll("\\.+/", "");
        }
        return path;
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
}
