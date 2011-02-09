package mikera.sound;

import javax.sound.sampled.*;
import javax.sound.sampled.Control.Type;

public class Sample {

	public Clip clip;

	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}
	


}
