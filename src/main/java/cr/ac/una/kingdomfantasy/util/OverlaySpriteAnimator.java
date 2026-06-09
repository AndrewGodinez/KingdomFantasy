package cr.ac.una.kingdomfantasy.util;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

public final class OverlaySpriteAnimator {

    private final ImageView view;
    private OverlayAnimationSequence sequence;
    private int currentFrame;
    private long lastFrameTime;
    private long frameDurationNs;
    private boolean playing;
    private boolean looping;
    private Runnable onFinished;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (!playing || sequence == null) {
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

    public OverlaySpriteAnimator(ImageView view) {
        this.view = view;
        view.setPreserveRatio(true);
        view.setSmooth(true);
    }

    public void setSequence(OverlayAnimationSequence sequence) {
        if (sequence == null || sequence.getFrames().isEmpty()) {
            return;
        }
        this.sequence = sequence;
        this.currentFrame = 0;
        this.frameDurationNs = (long) (1_000_000_000L / sequence.getFps());
        this.lastFrameTime = 0L;
        view.setImage(sequence.getImage());
        applyFrame();
    }

    public void play(boolean loop, Runnable onFinished) {
        if (sequence == null || sequence.getFrames().isEmpty()) {
            return;
        }
        this.looping = loop;
        this.onFinished = onFinished;
        this.currentFrame = 0;
        this.lastFrameTime = 0L;
        this.playing = true;
        applyFrame();
        timer.start();
    }

    public void stop() {
        playing = false;
        onFinished = null;
        timer.stop();
    }

    private void advanceFrame() {
        currentFrame++;
        if (currentFrame >= sequence.getFrames().size()) {
            if (looping) {
                currentFrame = 0;
            } else {
                currentFrame = sequence.getFrames().size() - 1;
                playing = false;
                timer.stop();
                if (onFinished != null) {
                    onFinished.run();
                }
            }
        }
        applyFrame();
    }

    private void applyFrame() {
        if (sequence != null && !sequence.getFrames().isEmpty()) {
            int safeFrame = Math.max(0, Math.min(currentFrame, sequence.getFrames().size() - 1));
            view.setViewport(sequence.getFrames().get(safeFrame));
        }
    }
}
