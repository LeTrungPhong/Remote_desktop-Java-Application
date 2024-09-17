package General;

public enum Commands {
	PRESS_MOUSE(-1),
	RELEASE_MOUSE(-2),
	PRESS_KEY(-3),
	RELEASE_KEY(-4),
	MOVE_MOUSE(-5),
	CLICK_MOUSE(-6),
	SEND_SCREEN(-7),
	INFOR_SCREEN(-8),
	SIZE_SERVER(-9),
	REQUEST_CONNECT(-10),
	RESPONSE_CONNECT(-11);

	private int abbrev;

	Commands(int abbrev){
		this.abbrev = abbrev;
	}

	public int getAbbrev(){
		return abbrev;
	}
}
