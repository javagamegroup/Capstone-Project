import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
public class Achievements {
	
	BufferedReader reader = null;
	private PrintWriter out = null;
	
	public String [] getAllAchievement(){
		String sCurrentLine = null;
		String[] achievements = null;
		try {
			achievements = new String[getnumAchievement("src/Achievements/Achieve.txt")];
			int m = 0;
			reader = new BufferedReader(new FileReader("src/Achievements/Achieve.txt"));
			while ((sCurrentLine = reader.readLine()) != null){
				achievements[m] = sCurrentLine;
				m++;
			}
			reader.close();
		} catch (IOException e) {e.printStackTrace();}
		return achievements;
	}
	
	public String [] getEarnedAchievement(){
		String sCurrentLine = null;
		String[] achievements = null;
		try {
			achievements = new String[getnumAchievement("src/Achievements/EarnedAchieve.txt")];
			int m = 0;
			reader = new BufferedReader(new FileReader("src/Achievements/EarnedAchieve.txt"));
			while ((sCurrentLine = reader.readLine()) != null){
				achievements[m] = sCurrentLine;
				m++;
			}
			reader.close();
		} catch (IOException e) {e.printStackTrace();}
		return achievements;
	}
	
	public int getnumAchievement(String file){
		int lines = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) lines++;
			reader.close();
		} catch (IOException e) {e.printStackTrace();}
		return lines;
	}
	
	public void resetAchieve(){
		try{
			 
    		File file = new File("src/Achievements/EarnedAchieve.txt");
    		file.delete();
    		out = new PrintWriter(new BufferedWriter(new FileWriter("src/Achievements/EarnedAchieve.txt", true)));
    		out.close();}catch(Exception e){}
	}
	
	public void storeAchievement(String achieve)
	{
		String sCurrentLine = null;
		try 
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter("src/Achievements/EarnedAchieve.txt", true)));
			reader = new BufferedReader(new FileReader("src/Achievements/EarnedAchieve.txt"));
			while ((sCurrentLine = reader.readLine()) != null){
				if(achieve.equals(sCurrentLine)){
					reader.close();
					out.close();
					return;
				}
					
			}
		} catch (IOException e) {e.printStackTrace();}
		out.println(achieve);
		out.close();
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
