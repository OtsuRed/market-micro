package top.otsuland.user.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

public class PictureUtils {

    private static final int picNarrowWidth = 300;
    private static final int picNarrowLength = 300;

    public static byte[] getNarrowPhoto(MultipartFile file) {
        try {
            // 获取图片缩略图
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(file.getInputStream()).size(picNarrowWidth, picNarrowLength);
            // 将返回值转换为 byte
            BufferedImage BufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
