import java.io.*;
import java.util.StringTokenizer;
public class StatsWriter {
	final File file= new File("StackerStats.txt") ;
	BufferedReader fin;
	BufferedWriter fout;
	int wins, loses;
	String line;
	String token;
	
	public StatsWriter(){
		if(!file.canRead())
			createStatsFile();
		
		readFromFile();
		
	}
	
	private void readFromFile(){
		try {
			fin = new BufferedReader(new FileReader(file));
			line= fin.readLine();
			while(line != null){
				StringTokenizer tokenizer= new StringTokenizer(line,":");
				while(tokenizer.hasMoreTokens()){
					token=tokenizer.nextToken();
					if(token.equals("wins"))
						wins=Integer.parseInt(tokenizer.nextToken());
					else if(token.equals("loses"))
						loses=Integer.parseInt(tokenizer.nextToken());
				}
				line= fin.readLine();
			}

			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Cannot read stats file, trying to create new one");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createStatsFile(){
		try {
			file.createNewFile();
			fout = new BufferedWriter(new FileWriter(file));
			fout.write("wins:"+0);
			fout.newLine();
			fout.write("loses:"+0);
			fout.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Can't create stats file");
		}
	}
	
	void addLoss(){
		loses++;
		write();
	}
	
	void addWin(){
		wins++;
		write();
	}
	
	private void write(){
		try {
			file.delete();
			file.createNewFile();
			fout = new BufferedWriter(new FileWriter(file));
			fout.write("wins:"+wins);
			fout.newLine();
			fout.write("loses:"+loses);
			fout.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Can't create stats file");
		}
	}

	public int getWins() {
		return wins;
	}
	
	public int getLosses(){
		return loses;
	}
	
	
}
