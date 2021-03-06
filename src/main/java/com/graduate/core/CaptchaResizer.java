package com.graduate.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaResizer {


    // Есть ли более простой способ изменения разрешения?
    public static byte[] resizeCaptcha(int width, int height, BufferedImage image) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage resizedImg = new BufferedImage(width, height, image.getType());
            Graphics g = resizedImg.createGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            ImageIO.write(resizedImg, "png", bout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();
    }
}
