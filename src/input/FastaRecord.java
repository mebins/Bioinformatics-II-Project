package input;

public class FastaRecord {
	public String head;
	public String sequence;
	
	public String toString()
	{
		return head + ": \n" + sequence;
	}
}
