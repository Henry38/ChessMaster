package sound;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SoundBoard {
	
	private Sound player;
	private InputStream stream;
	private String path;
	
	/** Constructeur */
	public SoundBoard() {
		this.player = null;
		this.stream = null;
		this.path = "./ressource/sound/";
	}
	
	public void playBmove() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "bmove.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
		
	}
	
	public void playCapture() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "capture.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playClapping() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "clapping.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playDraw() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "draw.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playEchec() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "echec.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playHit() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "hit.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playIllegal() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "illegal.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playMate() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "mate.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playNewgame() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "newgame.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playPromo() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "promo.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
	
	public void playSelect() {
		(new Thread() {
			public void run() {
				player = new Sound(path + "select.wav");
				stream = new ByteArrayInputStream(player.getSamples());
				player.play(stream);
			}
		}).start();
	}
}
