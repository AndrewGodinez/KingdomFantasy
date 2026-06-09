package cr.ac.una.kingdomfantasy.util;

import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public final class OverlayAnimationSequence {

    private final Image image;
    private final List<Rectangle2D> frames;
    private final double fps;
    private final boolean loop;

    public OverlayAnimationSequence(Image image, List<Rectangle2D> frames, double fps, boolean loop) {
        this.image = image;
        this.frames = frames;
        this.fps = Math.max(0.1, fps);
        this.loop = loop;
    }

    public Image getImage(){
        return image; 
    }
    
    public List<Rectangle2D> getFrames(){ 
        return frames; 
    }
    
    public double getFps(){
        return fps; 
    }
    
    public boolean isLoop(){
        return loop; 
    }
    
}
