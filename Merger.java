import java.io.*;
import java.util.*;
import java.text.*;

public class Merger {

	private static final String DATABASE = "./output.csv";
	public static PrintWriter db;
	public static int filesFinished = 0;

	public static void main(String args[]) throws IOException {
		db = new PrintWriter(DATABASE, "UTF-8");
		db.print("Station ID,County,State,Years,Start Date,End Date,");
		for (int i = 0; i < 32; i++) {
			db.print(i + ",");
		}
		db.println();
		walk("./");
		db.close();
	}
	
	public static void walk(String path) {
		
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null) return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath());
			} else {
				if (f.getName().substring(f.getName().length() - 4).equals(".txt") && !(f.getName().equals("testprecip.txt"))) addFile(f);		
			}
		}
	}

	public static void addFile(File input) {
		//System.out.printf("\nFILE NAME: %s\n", input.getName());
		ArrayList<String> lines = new ArrayList<>();
		Scanner s = new Scanner(System.in);
		try {
			s = new Scanner(input);
		} catch (Exception e) {
			e.printStackTrace();	
			System.exit(1);
		}
		int i = 0;
		int days = 366;
		int years = 0;
		int station = 0;
		String county, state;
		int start = 0;
		int end = 0;
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (i > 8) lines.add(line);
			else if (i < 8) {
				switch (i) {
					case 1:
						String [] words = line.split("\\s+");
						station = Integer.parseInt(words[2]);
						break;
					case 4:
						try {
							start = Integer.parseInt(line.substring(19).trim());
						} catch (Exception e) {
							start = 0;		
						}
						break;
					case 5:
						try {
							end = Integer.parseInt(line.substring(19).trim());
						} catch (Exception e) {
							end = 0;		
						}
						break;
					case 6:
						years = Integer.parseInt(line.substring(8));
						break;
				}	
			}
			i++;
		}
		county = input.getName().substring(0, input.getName().indexOf(".txt"));
		state = input.getParent().substring(input.getParent().lastIndexOf("/") + 1);
		//System.out.printf("State: %s\nCounty: %s\nStation ID: %d\nYears: %d\nStart Date: %d\nEnd Date: %d\n", state, county, station, years, start, end);
		s.close();
			
		String lineToWrite;
		for (i = 1; i <= lines.size(); i++) {
			String line = lines.get(i - 1);
			String [] vals = line.split("\\s+");
			lineToWrite = String.format("%d,%s,%s,%d,10/1/%d,9/30/%d,%d,", station, county, state, years, start, end, i);
			for (String val : vals) {
				lineToWrite += val + ",";	
			}
			lineToWrite = lineToWrite.substring(0, lineToWrite.length() - 2);
			db.println(lineToWrite);
		}
	filesFinished++;
	System.out.println(filesFinished + "");
	}

}
