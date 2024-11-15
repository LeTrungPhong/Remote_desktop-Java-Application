package General;

public enum Commands {
	PRESS_MOUSE(-1),
	RELEASE_MOUSE(-2),
	PRESS_KEY(-3),
	RELEASE_KEY(-4),
	MOVE_MOUSE(-5),
	CLICK_MOUSE(-6),
	MOUSE_WHEEL(-50),
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
	ERROR_PROCESS(-18),	
	REQUEST_STOP_PROCESS(-19),
	REQUEST_SCREEN_SHOT(-20),
	RESPONSE_SCREEN_SHOT(-21),
	REQUEST_START_KEYLOGGER(-22),
	PRESS_KEY_KEYLOGGER(-23),
	REQUEST_APP_RUNNING(-24),
	RESPONSE_APP_RUNNING(-25),
	REQUEST_SHUTDOWN(-26),
	REQUEST_DISCONNECT(-27);
	
	private int abbrev;

	Commands(int abbrev){
		this.abbrev = abbrev;
	}

	public int getAbbrev(){
		return abbrev;
	}
	
	public static Commands fromAbbrev(int abbrev) {
        for (Commands command : Commands.values()) {
            if (command.getAbbrev() == abbrev) {
                return command;
            }
        }
        return null;
    }
}
