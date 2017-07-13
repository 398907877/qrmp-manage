package com.yanyan.core.util;

import lombok.extern.slf4j.Slf4j;
import org.hyperic.jni.ArchName;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * User: Saintcy
 * Date: 2017/4/2
 * Time: 22:20
 */
@Slf4j
public class SigarFactory {
    private static Sigar sigar;

    public synchronized static Sigar create() {
        if (sigar == null) {
            try {
                File sigarJar = new File(Sigar.class.getProtectionDomain().getCodeSource().getLocation().getFile());
                String nativeFolder = sigarJar.getName().replace(".jar", "-native");//native文件夹名
                File nativePath = null;
                //[sigar.jar]/../: 在Sigar.jar包同目录下查找
                File sigarJarPath = sigarJar.getParentFile();
                if (existsNative(sigarJarPath)) {
                    nativePath = sigarJarPath;
                }

                //[sigar.jar]/../[native]
                if (nativePath == null) {
                    File path = new File(sigarJarPath.getCanonicalPath() + "/" + nativeFolder);
                    if (existsNative(path)) {
                        nativePath = path;
                    }
                }

                //WER-INF/[native]: 在WEB-INF目录下查找
                if (nativePath == null) {
                    String webRootProperty = System.getProperty("webapp.root");
                    if (webRootProperty != null && !webRootProperty.equals("")) {
                        File path = new File(webRootProperty + "/WEB-INF/" + nativeFolder);

                        if (existsNative(path)) {
                            nativePath = path;
                        }
                    }
                }

                //[APP_PATH]: 应用目录
                if (nativePath == null) {
                    File path = new File("").getCanonicalFile();
                    if (existsNative(path)) {
                        nativePath = path;
                    }
                }

                //[APP_PATH]/[native]: 应用目录下的native目录
                if (nativePath == null) {
                    File path = new File(nativeFolder).getCanonicalFile();
                    if (existsNative(path)) {
                        nativePath = path;
                    }
                }

                if (nativePath == null) {
                    log.error("can't find sigar native library.");
                    return null;
                }


                String path = System.getProperty("java.library.path");
                if (SigarLoader.IS_WIN32) {//Windows
                    path += ";" + nativePath.getCanonicalPath();
                } else {
                    path += ":" + nativePath.getCanonicalPath();
                }
                System.setProperty("java.library.path", path);

                sigar = new Sigar();
            } catch (Throwable e) {
                log.error("load sigar error", e);
            }
        }

        return sigar;
    }

    private static boolean existsNative(File path) throws ArchNotSupportedException, IOException {
        final String namePrefix = SigarLoader.getLibraryPrefix() + "sigar-" + ArchName.getName();
        final String nameExtension = SigarLoader.getLibraryExtension();
        File[] libFiles = path.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith(namePrefix) && name.toLowerCase().endsWith(nameExtension);
            }
        });
        boolean found = (libFiles != null && libFiles.length > 0);
        log.debug((found ? "found " : "no ") + namePrefix + "*" + nameExtension + " in " + path.getCanonicalPath());

        return found;
    }
}
