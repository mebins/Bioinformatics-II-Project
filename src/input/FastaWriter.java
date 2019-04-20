package input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FastaWriter {
	File path;
	FileWriter fw;
	BufferedWriter bw;
	public FastaWriter(String path)
	{
		this.path = new File(path);
	}
	public void generate_fasta(int start_index, int window, ArrayList<FastaRecord> records) throws IOException
	{
		fw = new FileWriter(path);
		bw = new BufferedWriter(fw);
		System.out.println("GENERATING REPORT: " + path);
		path.createNewFile();
		for(FastaRecord rec : records)
		{
			bw.write(rec.head);
			bw.newLine();
			bw.write(rec.sequence.substring(start_index,start_index+window));
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
}
