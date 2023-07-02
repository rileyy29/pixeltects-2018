package com.pixeltects.core.utils.map;

public final class MapTexture {
    private final byte[] buffer;
    private final int width, height;

    private MapTexture(int width, int height, byte[] buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public byte readPixel(int x, int y) {
        if (x >= 0 && x < width) {
            int index = (x + (y * width));
            if (index >= 0 && index < buffer.length) {
                return buffer[index];
            }
        }
        return (byte) 0;
    }

    public void writePixel(int x, int y, byte color) {
        if (x >= 0 && x < width) {
            int index = (x + (y * width));
            if (index >= 0 && index < buffer.length) {
                buffer[index] = color;
            }
        }
    }

    /*
    public static MapTexture loadPluginResource(JavaPlugin plugin, String filename) throws TextureLoadException {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin is null");
        }
        try {
            try (InputStream stream = plugin.getResource(filename)) {
                if (stream == null) {
                    throw new TextureLoadException("Resource not found in " + plugin.getName() + ": " + filename);
                }
                return fromStream(stream);
            }
        } catch (IOException e) {
            throw new TextureLoadException("Failed to open image resource stream", e);
        }
    }

    /**
     * Loads an image file that is part of a class loader's resources (jar file)
     * 
     * @param ownerClass Class whose Class Loader to query for the resource
     * @param filename Class path to the image contents (png, jpeg, etc.)
     * @return loaded texture
     * @throws TextureLoadException Thrown when loading the texture fails
     */
   /*
    public static MapTexture loadResource(Class<?> ownerClass, String filename) throws TextureLoadException {
        if (ownerClass == null) {
            throw new IllegalArgumentException("ownerClass is null");
        }
        try {
            try (InputStream stream = ownerClass.getResourceAsStream(filename)) {
                if (stream == null) {
                    throw new TextureLoadException("Resource not found: " + filename);
                }
                return fromStream(stream);
            }
        } catch (IOException e) {
            throw new TextureLoadException("Failed to open image resource stream", e);
        }
    }

    /**
     * Loads an image file from a resource accessible by URL and decodes it into a MapTexture
     * 
     * @param imageResourceURL Resource URL to the image contents (png, jpeg, etc.)
     * @return loaded texture
     * @throws TextureLoadException Thrown when loading the texture fails
     */
    /*public static MapTexture loadResource(URL imageResourceURL) throws TextureLoadException {
        try {
            try (InputStream stream = imageResourceURL.openStream()) {
                if (stream == null) {
                    throw new TextureLoadException("Resource not found: " + imageResourceURL.toString());
                }
                return fromStream(stream);
            }
        } catch (IOException e) {
            throw new TextureLoadException("Failed to open image resource stream", e);
        }
    }

    /**
     * Loads an image file from disk and decodes it into a MapTexture
     * 
     * @param filePath Image file path (.png, .jpg, etc.)
     * @return loaded texture
     * @throws TextureLoadException Thrown when loading the texture fails
     */
    /*public static MapTexture fromImageFile(String filePath) throws TextureLoadException {
        try {
            try (FileInputStream stream = new FileInputStream(filePath)) {
                return fromStream(stream);
            }
        } catch (IOException e) {
            throw new TextureLoadException("Failed to open image file", e);
        }
    }

    /**
     * Loads a map texture by decoding a png, jpeg or other image from stream.
     * The input stream is not closed.
     * 
     * @param imageStream
     * @return loaded texture
     * @throws TextureLoadException Thrown when loading the texture fails
     */
   /* public static MapTexture fromStream(InputStream imageStream) throws TextureLoadException {
        try {
            Image image = ImageIO.read(imageStream);
            try {
                return fromImage(image);
            } finally {
                image.flush();
            }
        } catch (IOException e) {
            throw new TextureLoadException("Failed to load image from stream", e);
        }
    }

    /**
     * Loads a map texture from an image
     * 
     * @param image input image
     * @return loaded texture
     */
   // public static MapTexture fromImage(Image image) {
 //       return new MapTexture(image.getWidth(null), image.getHeight(null), MapColorPalette.convertImage(image));
  //  }
/*
    /**
     * Loads a map texture by reading a Bukkit font character sprite
     * 
     * @param sprite
     * @return sprite texture
     */
    /*
    public static MapTexture fromBukkitSprite(org.bukkit.map.MapFont.CharacterSprite sprite) {
        if (sprite == null) {
            return createEmpty();
        }
        MapTexture texture = createEmpty(sprite.getWidth() + 1, sprite.getHeight());
        for (int dx = 0; dx < sprite.getWidth(); dx++) {
            for (int dy = 0; dy < sprite.getHeight(); dy++) {
              //  texture.writePixel(dx, dy, sprite.get(dy, dx) ?
              //          MapColorPalette.COLOR_WHITE : MapColorPalette.COLOR_TRANSPARENT);
            }
        }
        return texture;
    }

    /**
     * Creates a new map texture with the contents of a canvas flipped horizontally
     * 
     * @param input canvas
     * @return flipped texture
     */
    /*
    public static MapTexture flipH(MapCanvas input) {
        MapTexture result = MapTexture.createEmpty(input.getWidth(), input.getHeight());
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.writePixel(x, y, input.readPixel(result.getWidth() - x - 1, y));
            }
        }
        return result;
    }

    /**
     * Creates a new map texture with the contents of a canvas flipped vertically
     * 
     * @param input canvas
     * @return flipped texture
     */
    /*
    public static MapTexture flipV(MapCanvas input) {
        MapTexture result = MapTexture.createEmpty(input.getWidth(), input.getHeight());
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.writePixel(x, y, input.readPixel(x, result.getHeight() - y - 1));
            }
        }
        return result;
    }

    /**
     * Creates a new map texture with the contents rotated 0, 90, 180, or 270 degrees.
     * 
     * @param input canvas
     * @param angle to rotate, must be a multiple of 90
     * @return rotated texture
     */
    /*
    public static MapTexture rotate(MapCanvas input, int angle) {
        MapTexture result;
        if (MathUtil.getAngleDifference(angle, 90) <= 45) {
            result = MapTexture.createEmpty(input.getHeight(), input.getWidth());
            for (int x = 0; x < result.getWidth(); x++) {
                for (int y = 0; y < result.getHeight(); y++) {
                    result.writePixel(x, result.getHeight() - y - 1, input.readPixel(y, x));
                }
            }
        } else if (MathUtil.getAngleDifference(angle, 180) <= 45) {
            result = MapTexture.createEmpty(input.getWidth(), input.getHeight());
            for (int x = 0; x < result.getWidth(); x++) {
                for (int y = 0; y < result.getHeight(); y++) {
                    result.writePixel(x, y, input.readPixel(result.getWidth() - x - 1, result.getHeight() - y - 1));
                }
            }
        } else if (MathUtil.getAngleDifference(angle, 270) <= 45) {
            result = MapTexture.createEmpty(input.getHeight(), input.getWidth());
            for (int x = 0; x < result.getWidth(); x++) {
                for (int y = 0; y < result.getHeight(); y++) {
                    result.writePixel(result.getWidth() - x - 1, y, input.readPixel(y, x));
                }
            }
        } else {
            result = input.clone(); // no rotation
        }
        return result;
    }

    /**
     * Exception thrown when loading a texture failed
     */
    public static final class TextureLoadException extends RuntimeException {
        private static final long serialVersionUID = 7530009132906543361L;

        public TextureLoadException(String message) {
            super(message);
        }

        public TextureLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}