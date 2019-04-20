package input;

import java.util.List;
/*
 * Used to score easily in one data structure.
 * The flaw in this design is that it does take more memory
 * and with FastaRecords ArrayList, you take twice that memory. 
 * One way we can manage memory is deleting the reference all the records 
 * after it is stored in the sequence.
 */
public class SequenceMatrix {
	public char[][] sequences;
	public SequenceMatrix(List<FastaRecord> records){
		int record_length = records.get(0).sequence.length();
		sequences = new char[records.size()][records.get(0).sequence.length()];
		for(int i = 0; i < records.size(); i++)
		{
			sequences[i] = records.get(i).sequence.toCharArray();
		}
	}
}
