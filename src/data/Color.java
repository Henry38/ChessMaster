package data;


public enum Color {
	
	White(1),
	Black(-1);
	
	private int color;
	
	/** Constructeur */
	Color(int color) {
		this.color = color;
	}
	
	private int getInteger() {
		return color;
	}
	
	public boolean isWhite() {
		return (getInteger() == 1);
	}
	
	public boolean isBlack() {
		return (getInteger() == -1);
	}
	
	public boolean equals(Color color) {
		return (getInteger() == color.getInteger());
	}
	
	public Color getInverse() {
		if (isWhite()) {
			return Color.Black;
		}
		return Color.White;
	}
}
