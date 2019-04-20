package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Class which scores the Sequence Matrix according to the given parameters.
 * Helps find the best region to use as a part of the training-set for the HMM.
 * 
 */
public class Scorer {
	public double category_change = .1;
	public double variation = .1; 
	public double expected_indels = .1; 
	public int window = 20; 
	Character nonpolar_letters[] = {'G','A','V','L','I','M','F','W','P'};
	HashSet<Character> nonpolar_set = (HashSet<Character>) Arrays.asList(nonpolar_letters).stream().collect(Collectors.toSet());
	Character polar_letters[] = {'S','T','C','Y','N','Q'};
	HashSet<Character> polar_set = (HashSet<Character>) Arrays.asList(polar_letters).stream().collect(Collectors.toSet());
	Character charged_letters[] = {'D','E','K','R','H'};
	HashSet<Character> charged_set = (HashSet<Character>) Arrays.asList(polar_letters).stream().collect(Collectors.toSet());
	/*
	 * Returns the starting index of the best scored region.
	 */
	public int score(SequenceMatrix s) {
		
		assert(window <= s.sequences.length);
		double[] window_vals = new double[s.sequences.length - window];
		
		for(int i = 0; i < s.sequences.length; i++)
		{
			window_vals[i] = local_score(s, i);
		}
		
		double a = 0; //highest score
		int b = 0; //best index
		for(int i = 0; i < window_vals.length; i++)
		{
			if(window_vals[i] > a)
			{
				a = window_vals[i];
				b = i;
			}
		}
		return b;
	}
	
    public double local_score(SequenceMatrix s, int start_index)
    {
    	double computed_score = 0.0;
    	char[][] m = s.sequences;
    	
    	for(int j = 0; j < m.length; j++)
    	{
    		ArrayList<Character> rows = new ArrayList<>();
    		for(int i = start_index; i < i+ window; i++)
        	{
    			rows.add(m[j][i]);
        	}
    		/* Checking if window meets indel percentage requirement */ 
    		if(compute_indel_percentage(rows) > expected_indels)
    		{
    			return 0.0; // failed indel requirement.
    		}
    	}
    	return computed_score;
    }
    public double compute_indel_percentage(List<Character>letters)
    {
    	return letters.stream().filter(x-> x=='-').count()/letters.size();
    }
    /*
     * Returns the total difference in amino acids found.
     * Since there are 20 amino acids, if 20 are found then 1.0 will be returned.
     * If not something in between 0 and 1 depending on how many amino acids are found.
     */
    public double compute_variation_score(List<Character> letters)
    {
    	return letters.stream().distinct().filter(x-> x!= '-').count()/20;
    }
    
    /*
     * Returns the Score of the category change.
     * There are three groups.
     * If all three are found a total of 1 is returned.
     */
    public double compute_category_score(List<Character> letters)
    {
    	boolean polar = false;
    	boolean nonpolar = false;
    	boolean charged = false;
    	for(Character c : letters)
    	{
    		if(polar && nonpolar && charged) return 1.0;
    		if(polar_set.contains(c)) polar = true;
    		if(nonpolar_set.contains(c)) nonpolar = true;
    		if(charged_set.contains(c)) charged = true;
    	}
    	double total = 0.0;
    	if(polar)total+= 1/3;
    	if(nonpolar)total+= 1/3;
    	if(charged)total+= 1/3;
    	return total;
    }
    
}
