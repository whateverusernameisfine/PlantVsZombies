package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {
    private static final HashMap<String, Image> imageCache = new HashMap<>();

    public static Image load(String name) {
        if (!imageCache.containsKey(name)) {
            try {
                URL url = ImageLoader.class.getResource("/images/" + name);
                if (url == null) {
                    System.err.println("[ImageLoader] Resource not found: /images/" + name);
                    return null;
                }
                Image image = new ImageIcon(url).getImage();
                if (image.getWidth(null) == -1) {
                    System.err.println("[ImageLoader] Failed to load image: /images/" + name);
                }
                imageCache.put(name, image);
            } catch (Exception e) {
                System.err.println("[ImageLoader] Exception loading image: /images/" + name);
                e.printStackTrace();
            }
        }
        return imageCache.get(name);
    }
}