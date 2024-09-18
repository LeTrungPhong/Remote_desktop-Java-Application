package General;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.LinkedList;

public class ListProcessesWindows implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LinkedList<ProcessWindow> getListProcess(){
		try {
            Process process = Runtime.getRuntime().exec("tasklist");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            LinkedList<ProcessWindow> listProcess = new LinkedList<ProcessWindow>(); 
            
            line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();
            
            int[] sizeToken = new int[5];
            
            String[] tokens = line.split("\\s+");
            for(int i = 0; i < sizeToken.length; ++i) {
            	sizeToken[i] = tokens[i].length();
            }
            
            while ((line = reader.readLine()) != null) {
            	
                int count = 0;
                
                String ImageName = line.substring(count, count + sizeToken[0]).trim();
                count = count + sizeToken[0] + 1;
                
                int PID = Integer.parseInt(line.substring(count, count + sizeToken[1]).trim());
                count = count + sizeToken[1] + 1;
                
                String SessionName = line.substring(count, count + sizeToken[2]).trim();
                count = count + sizeToken[2] + 1;
                
                int SessionIDs = Integer.parseInt(line.substring(count, count + sizeToken[3]).trim());
                count = count + sizeToken[3] + 1;
                
                String MemUsage = line.substring(count, count + sizeToken[4]).trim();
                
                System.out.println(ImageName + " " + PID + " " + SessionName + " " + SessionIDs + " " + MemUsage);
                
                listProcess.add(new ProcessWindow(ImageName, PID, SessionName, SessionIDs, MemUsage));
            }
            return listProcess;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	
}
