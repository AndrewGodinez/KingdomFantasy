package cr.ac.una.kingdomfantasy.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

/**
 *
 * @author semmo
 */
public class SpriteAnimator {
    private final ImageView imageView;
    private final int frameWidth;
    private final int frameHeight;
    private final int totalFrames;
    private final double frameDuration;

    private int frameIndex = 0;
    private double elapsed = 0;

    public SpriteAnimator(ImageView imageView, int frameWidth, int frameHeight,
                          int totalFrames, double frameDuration) {
        this.imageView = imageView;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.totalFrames = totalFrames;
        this.frameDuration = frameDuration;
        actualizarViewport();
    }

    public void update(double deltaTime) {
        elapsed += deltaTime;
        if (elapsed >= frameDuration) {
            elapsed = 0;
            frameIndex = (frameIndex + 1) % totalFrames;
            actualizarViewport();
        }
    }

    private void actualizarViewport() {
        imageView.setViewport(new Rectangle2D(
            frameIndex * frameWidth, 0, frameWidth, frameHeight
        ));
    }

    public void reiniciar() {
        frameIndex = 0;
        elapsed = 0;
        actualizarViewport();
    }
}
