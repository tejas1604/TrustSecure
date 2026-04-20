package com.sec.trustsecure.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

@Service
public class ImageService {

    public byte[] generateImage(String content) {

        try {
            int width = 800;
            int height = 600;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            // background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            // text color
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 14));

            // split text into lines
            int y = 30;
            for (String line : content.split("\n")) {
                g.drawString(line, 20, y);
                y += 20;
            }

            g.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}