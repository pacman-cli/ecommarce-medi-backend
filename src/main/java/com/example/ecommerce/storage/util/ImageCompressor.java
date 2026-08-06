package com.example.ecommerce.storage.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Image scaling, optimization, and compression utility for uploaded assets.
 */
@Slf4j
public final class ImageCompressor {

    private static final int MAX_DIMENSION = 1920;
    private static final float COMPRESSION_QUALITY = 0.82f;

    private ImageCompressor() {
    }

    /**
     * Compresses and resizes an uploaded image file if needed, returning optimized byte array.
     */
    public static byte[] compressImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new byte[0];
        }

        byte[] inputBytes = file.getBytes();
        String contentType = file.getContentType();

        // Skip compression for non-raster formats (like GIF animation)
        if ("image/gif".equalsIgnoreCase(contentType)) {
            return inputBytes;
        }

        try (ByteArrayInputStream in = new ByteArrayInputStream(inputBytes)) {
            BufferedImage originalImage = ImageIO.read(in);
            if (originalImage == null) {
                return inputBytes;
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            BufferedImage targetImage = originalImage;
            if (width > MAX_DIMENSION || height > MAX_DIMENSION) {
                targetImage = resize(originalImage, width, height);
            }

            return compressToJpeg(targetImage, COMPRESSION_QUALITY);
        } catch (Exception e) {
            log.warn("Image compression fallback to original bytes due to error: {}", e.getMessage());
            return inputBytes;
        }
    }

    private static BufferedImage resize(BufferedImage src, int width, int height) {
        int newWidth = width;
        int newHeight = height;

        if (width > height) {
            newWidth = MAX_DIMENSION;
            newHeight = (height * MAX_DIMENSION) / width;
        } else {
            newHeight = MAX_DIMENSION;
            newWidth = (width * MAX_DIMENSION) / height;
        }

        Image resized = src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = output.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(resized, 0, 0, null);
        g2d.dispose();

        return output;
    }

    private static byte[] compressToJpeg(BufferedImage image, float quality) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }

        return out.toByteArray();
    }
}
