package cr.ac.una.kingdomfantasy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteAnimator {

    private final List<ImageView> imageViews = new ArrayList<>();
    private final List<Image> spriteSheets = new ArrayList<>();
    private SpriteAnimationSpec spec;
    private int currentRow;
    private int currentFrame;
    private double animationFps;
    private long lastFrameTime;
    private long frameDurationNs;
    private boolean playing;
    private boolean looping;
    private boolean smooth;
    private Runnable onAnimationFinished;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (!playing) {
                return;
            }
            if (lastFrameTime == 0L) {
                lastFrameTime = now;
                return;
            }
            if (now - lastFrameTime >= frameDurationNs) {
                lastFrameTime = now;
                advanceFrame();
            }
        }
    };

    public SpriteAnimator(ImageView imageView, SpriteAnimationSpec spec) {
        this(Collections.singletonList(imageView), Collections.singletonList(spec.loadImage()), spec, 0);
    }

    public SpriteAnimator(List<ImageView> imageViews, List<Image> spriteSheets, SpriteAnimationSpec spec) {
        this(imageViews, spriteSheets, spec, 0);
    }

    public SpriteAnimator(List<ImageView> imageViews, List<Image> spriteSheets,
            SpriteAnimationSpec spec, int startRow) {
        if (imageViews == null || imageViews.isEmpty()) {
            throw new IllegalArgumentException("At least one ImageView is required.");
        }
        this.imageViews.addAll(imageViews);
        configure(spriteSheets, spec, startRow);
    }

    public SpriteAnimator(ImageView imageView, Image spriteSheet,
            int frameWidth, int frameHeight, int columns, int rows,
            int startRow, double animationFps) {
        this(Collections.singletonList(imageView),
                Collections.singletonList(spriteSheet),
                new SpriteAnimationSpec(null, frameWidth, frameHeight, columns, rows, animationFps, true),
                startRow);
    }

    public static SpriteAnimator fromSpecs(List<ImageView> imageViews, List<SpriteAnimationSpec> specs, int startRow) {
        if (specs == null || specs.isEmpty()) {
            throw new IllegalArgumentException("At least one animation spec is required.");
        }
        List<Image> images = new ArrayList<>();
        for (SpriteAnimationSpec layerSpec : specs) {
            images.add(layerSpec.loadImage());
        }
        return new SpriteAnimator(imageViews, images, specs.get(0), startRow);
    }

    public void setSpriteSheet(Image sheet, int frameWidth, int frameHeight, int columns, int rows, int row, double fps) {
        configure(Collections.singletonList(sheet),
                new SpriteAnimationSpec(null, frameWidth, frameHeight, columns, rows, fps, true),
                row);
    }

    public void setAnimation(SpriteAnimationSpec newSpec) {
        if (newSpec == null) {
            return;
        }
        configure(Collections.singletonList(newSpec.loadImage()), newSpec, Math.min(currentRow, newSpec.getRows() - 1));
    }

    public void setAnimationStack(List<SpriteAnimationSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return;
        }
        List<Image> images = new ArrayList<>();
        for (SpriteAnimationSpec layerSpec : specs) {
            images.add(layerSpec.loadImage());
        }
        configure(images, specs.get(0), Math.min(currentRow, specs.get(0).getRows() - 1));
    }

    private void configure(List<Image> sheets, SpriteAnimationSpec newSpec, int startRow) {
        if (newSpec == null) {
            throw new IllegalArgumentException("Animation spec is required.");
        }
        spriteSheets.clear();
        if (sheets != null) {
            spriteSheets.addAll(sheets);
        }
        if (spriteSheets.isEmpty()) {
            throw new IllegalArgumentException("At least one sprite sheet is required.");
        }
        this.spec = newSpec;
        this.currentRow = Math.max(0, Math.min(newSpec.getRows() - 1, startRow));
        this.currentFrame = 0;
        this.looping = newSpec.isLoop();
        this.animationFps = newSpec.getFps();
        this.frameDurationNs = (long) (1_000_000_000L / animationFps);
        this.lastFrameTime = 0L;
        applyImagesToViews();
        updateViewport();
    }

    private void applyImagesToViews() {
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView view = imageViews.get(i);
            if (i < spriteSheets.size()) {
                view.setImage(spriteSheets.get(i));
                view.setFitWidth(spec.getFrameWidth());
                view.setFitHeight(spec.getFrameHeight());
                view.setPreserveRatio(false);
                view.setSmooth(smooth);
                view.setVisible(true);
            } else {
                view.setImage(null);
                view.setVisible(false);
            }
        }
    }

    public void setRow(int row) {
        int safeRow = Math.max(0, Math.min(spec.getRows() - 1, row));
        if (currentRow == safeRow) {
            return;
        }
        currentRow = safeRow;
        currentFrame = 0;
        updateViewport();
    }

    public void play() {
        looping = spec.isLoop();
        playing = true;
        timer.start();
    }

    public void playLoop() {
        looping = true;
        playing = true;
        timer.start();
    }

    public void playOnce(Runnable onFinished) {
        looping = false;
        onAnimationFinished = onFinished;
        currentFrame = 0;
        playing = true;
        lastFrameTime = 0L;
        timer.start();
        updateViewport();
    }

    public void pause() {
        playing = false;
    }

    public void resume() {
        if (!playing) {
            playing = true;
            timer.start();
        }
    }

    public void stop() {
        playing = false;
        currentFrame = 0;
        timer.stop();
        updateViewport();
    }

    public void showFrame(int frame) {
        playing = false;
        currentFrame = Math.max(0, Math.min(frame, spec.getFrameCount() - 1));
        updateViewport();
    }

    public void setFlipX(boolean flip) {
        for (ImageView view : imageViews) {
            view.setScaleX(flip ? -1 : 1);
        }
    }

    public void setScale(double scale) {
        double safeScale = Math.max(0.05, scale);
        for (ImageView view : imageViews) {
            view.setFitWidth(spec.getFrameWidth() * safeScale);
            view.setFitHeight(spec.getFrameHeight() * safeScale);
        }
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
        for (ImageView view : imageViews) {
            view.setSmooth(smooth);
        }
    }

    public void setAnimationFps(double fps) {
        animationFps = Math.max(0.1, fps);
        frameDurationNs = (long) (1_000_000_000L / animationFps);
    }

    private void advanceFrame() {
        currentFrame++;
        if (currentFrame >= spec.getFrameCount()) {
            if (looping) {
                currentFrame = 0;
            } else {
                currentFrame = spec.getFrameCount() - 1;
                playing = false;
                timer.stop();
                if (onAnimationFinished != null) {
                    onAnimationFinished.run();
                }
            }
        }
        updateViewport();
    }

    private void updateViewport() {
        for (ImageView view : imageViews) {
            if (view.isVisible()) {
                view.setViewport(spec.viewportFor(currentRow, currentFrame));
            }
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public double getAnimationFps() {
        return animationFps;
    }

    public SpriteAnimationSpec getSpec() {
        return spec;
    }

    public Image getSpriteSheet() {
        return spriteSheets.get(0);
    }

    public ImageView getImageView() {
        return imageViews.get(0);
    }

    public List<ImageView> getImageViews() {
        return Collections.unmodifiableList(imageViews);
    }
}
