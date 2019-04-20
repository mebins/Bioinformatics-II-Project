package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FastaReader {
	private File path;
	private FileReader fr;
	private BufferedReader br; 
	public FastaReader(String path){
		this.path = new File(path);
	}
	
	public ArrayList<FastaRecord> readRecords() throws IOException {
		fr = new FileReader(path);
		br = new BufferedReader(fr);
		ArrayList<FastaRecord> records = new ArrayList<>();
		String line = null;
		FastaRecord record = new FastaRecord();
		while((line = br.readLine()) != null)
		{
			if(line.charAt(0) == ('>')) //part of head
			{
				//old record needs to be add to list and start a new record
				if(record.head != null)
				{
					records.add(record);
					record = new FastaRecord();
				}
				record.head = line;
			}
			else
			{
				//sequence
				record.sequence += line;
			}
		}
		for(FastaRecord rec : records)
		{
			rec.sequence = rec.sequence.substring(4);
		}
		return records;
	}

	public static void main(String[] args)
	{
		FastaReader fr = new FastaReader("/Users/mebinskaria/Documents/bioinformatics-ii/project/training-set/aligned/pax-2-aligned.txt");
		ArrayList<FastaRecord> records = new ArrayList<>();
		
		try {
			records = fr.readRecords();
			
			for(int i = 0; i < s.sequences.length; i++)
			{
				for(int j = 0; j < s.sequences[0].length; j++)
				{
					System.out.print(s.sequences[i][j]);
				}
				System.out.println();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
