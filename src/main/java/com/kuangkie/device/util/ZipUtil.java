package com.kuangkie.device.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZipUtil工具类
 *
 * @author liuxb
 * @date 2022/7/28 8:31
 */
public class ZipUtil {
	
	static final Logger log = LoggerFactory.getLogger(ZipUtil.class); 

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param keepDirStructure 是否保留原来的目录结构，true：保留目录结构，false: 所有文件盼到压缩包跟目录下（注意：不保留目录结构可能会出现同名文件，压缩失败）
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        try (ZipOutputStream zos = new ZipOutputStream(out);) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
            log.info("------------压缩完成，耗时： " + (end - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error", e);
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构， true：保留目录结构  false：所有文件跑到压缩包根目录下（注意：不保留目录结构可能会出现同名文件，会压缩失败）
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy 文件到zip输出流中
            int len;
            try (FileInputStream fis = new FileInputStream(sourceFile);) {
                while ((len = fis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("读取异常", e);
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时，需要对空文件夹进行处理
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName() 前面需要带上父文件夹的名字加一斜杠
                        // 不然最后压缩保重不能保留原来的文件结构，即所有文件都跑到压缩包根目录下
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFilePath zip压缩文件路径
     * @param descDir 目录
     */
    public static boolean unzip(String zipFilePath, String descDir) {
        try {
            File descFile = new File(descDir);
            if (!descFile.exists()) {
                descFile.mkdirs();
            }
            ZipFile zipFile = new ZipFile(zipFilePath);
            //列出所有项，包含子目录和子目录内文件
            Enumeration<? extends ZipEntry> zs = zipFile.entries();
            while (zs.hasMoreElements()) {
                ZipEntry zipEntry = zs.nextElement();
                log.info(zipEntry.getName());
                if (!zipEntry.isDirectory()) {
                    InputStream in = zipFile.getInputStream(zipEntry);
                    OutputStream os = new FileOutputStream(descDir + File.separator + zipEntry.getName());
                    byte[] data = new byte[1024];
                    int len = -1;
                    while ((len = in.read(data)) != -1) {
                        os.write(data, 0, len);
                    }
                    os.flush();
                    os.close();
                    in.close();
                } else {
                    new File(descDir + File.separator + zipEntry.getName()).mkdirs();
                }
            }
            zipFile.close();
            log.info("解压完成...");
            return true;
        } catch (IOException e) {
            log.error(zipFilePath + " 解压失败...", e);
        }
        
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param dirPath 文件夹路径及名称 如c:/test
     */
    public static void delFolder(String dirPath) {
        try {
            //删除完里面所有内容
            delAllFile(dirPath);
            log.info("删除{}内所有文件及子目录文件", dirPath);
            File myFilePath = new File(dirPath);
            myFilePath.delete(); //删除空文件夹
            log.info("删除目录: {}", dirPath);
        } catch (Exception e) {
            log.info("删除文件夹fail！", e);
        }
    }

    /**
     * 删除文件夹里面的所有文件 (不删除最外层目录)
     *
     * @param path 文件夹路径 如 c:/test/
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 获取给定路径内所有是文件的绝地地址列表
     *
     * @param dir 路径
     * @return 遍历的路径集合
     */
    public static List<String> getFiles(String dir) {
        List<String> lstFiles = new ArrayList<String>();
        File file = new File(dir);
        if (!file.exists()) {
            return lstFiles;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                lstFiles.addAll(getFiles(f.getAbsolutePath()));
            } else {
                lstFiles.add(f.getAbsolutePath());
            }
        }
        return lstFiles;
    }
}
