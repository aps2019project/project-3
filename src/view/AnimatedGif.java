package view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

import java.awt.image.BufferedImage;

public class AnimatedGif extends Animation {
    public AnimatedGif(String filename, double durationMs) {

        GifDecoder d = new GifDecoder();
        d.read(filename);

        Image[] sequence = new Image[d.getFrameCount()];
        for (int i = 0; i < d.getFrameCount(); i++) {

            WritableImage wimg = null;
            BufferedImage bimg = d.getFrame(i);
            sequence[i] = SwingFXUtils.toFXImage(bimg, wimg);
        }

        super.init(sequence, durationMs);
    }

}

class Animation extends Transition {

    private ImageView imageView;
    private int count;

    private int lastIndex;

    private Image[] sequence;

    Animation() {
    }

    public Animation(Image[] sequence, double durationMs) {
        init(sequence, durationMs);
    }

    void init(Image[] sequence, double durationMs) {
        this.imageView = new ImageView(sequence[0]);
        this.sequence = sequence;
        this.count = sequence.length;

        setCycleCount(1);
        setCycleDuration(Duration.millis(durationMs));
        setInterpolator(Interpolator.LINEAR);

    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            imageView.setImage(sequence[index]);
            lastIndex = index;
        }

    }

    public ImageView getView() {
        return imageView;
    }

}
