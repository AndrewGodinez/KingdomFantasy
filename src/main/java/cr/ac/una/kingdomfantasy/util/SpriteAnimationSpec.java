package cr.ac.una.kingdomfantasy.util;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class SpriteAnimationSpec {

    public static final String RESOURCE_ROOT = "/cr/ac/una/kingdomfantasy/resource/";
    private static final Map<String, Image> IMAGE_CACHE = new ConcurrentHashMap<>();

    private final String resourcePath;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private final int columns;
    private final int rows;
    private final int startFrame;
    private final double fps;
    private final boolean loop;

    public SpriteAnimationSpec(String resourcePath, int frameWidth, int frameHeight,
            int frameCount, int rows, double fps, boolean loop) {
        this(resourcePath, frameWidth, frameHeight, frameCount, frameCount, rows, 0, fps, loop);
    }

    private SpriteAnimationSpec(String resourcePath, int frameWidth, int frameHeight,
            int columns, int frameCount, int rows, int startFrame, double fps, boolean loop) {
        this.resourcePath = resourcePath;
        this.frameWidth = Math.max(1, frameWidth);
        this.frameHeight = Math.max(1, frameHeight);
        this.columns = Math.max(1, columns);
        this.frameCount = Math.max(1, frameCount);
        this.rows = Math.max(1, rows);
        this.startFrame = Math.max(0, startFrame);
        this.fps = Math.max(0.1, fps);
        this.loop = loop;
    }

    public static SpriteAnimationSpec fromResource(String fileName, int frameWidth, int frameHeight,
            int frameCount, int rows, double fps, boolean loop) {
        return new SpriteAnimationSpec(RESOURCE_ROOT + fileName, frameWidth, frameHeight, frameCount, rows, fps, loop);
    }

    public static SpriteAnimationSpec fromResourceGrid(String fileName, int frameWidth, int frameHeight,
            int columns, int rows, int startFrame, int frameCount, double fps, boolean loop) {
        return new SpriteAnimationSpec(RESOURCE_ROOT + fileName, frameWidth, frameHeight,
                columns, frameCount, rows, startFrame, fps, loop);
    }

    public Image loadImage() {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalStateException("This animation spec does not have a resource path.");
        }
        return loadCachedResource(resourcePath);
    }

    public static Image loadCachedResource(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("Image resource path is required.");
        }
        return IMAGE_CACHE.computeIfAbsent(resourcePath, path -> {
            URL resourceUrl = SpriteAnimationSpec.class.getResource(path);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Sprite resource not found: " + path);
            }
            return new Image(resourceUrl.toExternalForm(), false);
        });
    }

    public static void clearImageCache() {
        IMAGE_CACHE.clear();
    }

    public Rectangle2D viewportFor(int row, int frame) {
        int safeFrame = Math.max(0, Math.min(frameCount - 1, frame));
        int absoluteFrame = startFrame + safeFrame;
        int frameColumn = absoluteFrame % columns;
        int frameRowOffset = absoluteFrame / columns;
        int safeRow = Math.max(0, Math.min(rows - 1, row + frameRowOffset));
        return new Rectangle2D(
                frameColumn * (double) frameWidth,
                safeRow * (double) frameHeight,
                frameWidth,
                frameHeight);
    }

    public SpriteAnimationSpec withLoop(boolean loop) {
        return new SpriteAnimationSpec(resourcePath, frameWidth, frameHeight, columns, frameCount, rows, startFrame, fps, loop);
    }

    public SpriteAnimationSpec withFps(double fps) {
        return new SpriteAnimationSpec(resourcePath, frameWidth, frameHeight, columns, frameCount, rows, startFrame, fps, loop);
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public double getFps() {
        return fps;
    }

    public boolean isLoop() {
        return loop;
    }
}
