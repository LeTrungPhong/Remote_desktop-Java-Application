package General;

import java.io.Serializable;

public class ProcessWindow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ImageName;
	private int PID;
	private String SessionName;
	private int SessionIDs;
	private String MemUsage;

	public ProcessWindow(String ImageName, int PID, String SessionName, int SessionIDs, String MemUsage) {
		this.ImageName = ImageName;
		this.PID = PID;
		this.SessionName = SessionName;
		this.SessionIDs = SessionIDs;
		this.MemUsage = MemUsage;
	}
	
	public String getImageName() {
		return ImageName;
	}
	public void setImageName(String imageName) {
		ImageName = imageName;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public String getSessionName() {
		return SessionName;
	}

	public void setSessionName(String sessionName) {
		SessionName = sessionName;
	}

	public int getSessionIDs() {
		return SessionIDs;
	}

	public void setSessionIDs(int sessionIDs) {
		SessionIDs = sessionIDs;
	}

	public String getMemUsage() {
		return MemUsage;
	}

	public void setMemUsage(String memUsage) {
		MemUsage = memUsage;
	}
	
	@Override
	public String toString() {
		return this.ImageName + "  " + this.PID + "  " + this.SessionName + "  " + this.SessionIDs + "  " + this.MemUsage;
	}
}
