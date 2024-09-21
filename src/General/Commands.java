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
	RESPONSE_CONNECT(-11),
	EVENTS(-12),
	SCREEN(-13),
	REQUEST_PROCESS(-14),
	RESPONSE_PROCESS(-15),
	REQUEST_START_PROCESS(-16),
	RESPONSE_START_PROCESS(-17),
	REQUEST_SCREEN_SHOT(-18);

	private int abbrev;

	Commands(int abbrev){
		this.abbrev = abbrev;
	}

	public int getAbbrev(){
		return abbrev;
	}
}
