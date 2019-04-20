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
		records.add(record); // last record
		for(FastaRecord rec : records)
		{
			rec.sequence = rec.sequence.substring(4);
		}
		br.close();
		fr.close();
		return records;
	}

	public static void main(String[] args)
	{
		FastaReader fr = new FastaReader("/Users/mebinskaria/Documents/bioinformatics-ii/project/training-set/aligned/pax-test.txt");
		ArrayList<FastaRecord> records = new ArrayList<>();
		
		try {
			records = fr.readRecords();
			System.out.println(records.size());
			SequenceMatrix sequences = new SequenceMatrix(records);
			Scorer s = new Scorer();
			int start_index = s.score(sequences);
			FastaWriter fw = new FastaWriter("/Users/mebinskaria/Documents/bioinformatics-ii/project/training-set/aligned/pax-test-scored.txt");
			fw.generate_fasta(start_index, s.getWindow(), records);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
