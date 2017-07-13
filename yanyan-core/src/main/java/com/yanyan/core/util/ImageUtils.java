package com.yanyan.core.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.util.Iterator;

/**
 * 图片工具
 * User: Saintcy
 * Date: 2017/2/9
 * Time: 22:32
 */
public class ImageUtils {
    public static String getImageFormat(File file) {
        try {
            // create an image input stream from the specified file
            ImageInputStream iis = ImageIO.createImageInputStream(file);

            // get all currently registered readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

            if (!iter.hasNext()) {
                throw new RuntimeException("No readers found!");
            }

            // get the first reader
            ImageReader reader = iter.next();

            //System.out.println("Format: " + reader.getFormatName());

            // close stream
            iis.close();
            return reader.getFormatName();
        } catch (Exception e) {
            return "";
        }
    }
}
