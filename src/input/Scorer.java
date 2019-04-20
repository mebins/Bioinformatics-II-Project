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
	public double max_category_change = 1.0;
	public double min_category_change = 0;
	public double max_variation = 1.0;
	public double min_variation = 0; 
	public double max_indel = 0.1;
	public double min_indel = 0.0;
	public int window = 20;
	private Character nonpolar_letters[] = {'G','A','V','L','I','M','F','W','P'};
	private HashSet<Character> nonpolar_set = (HashSet<Character>) Arrays.asList(nonpolar_letters).stream().collect(Collectors.toSet());
	private Character polar_letters[] = {'S','T','C','Y','N','Q'};
	private HashSet<Character> polar_set = (HashSet<Character>) Arrays.asList(polar_letters).stream().collect(Collectors.toSet());
	private Character charged_letters[] = {'D','E','K','R','H'};
	private HashSet<Character> charged_set = (HashSet<Character>) Arrays.asList(charged_letters).stream().collect(Collectors.toSet());
	
	public Scorer()
	{
		
	}
	public Scorer(double min_category, double min_variation, double max_indels)
	{
		this.max_indel = max_indels;
		this.min_category_change = min_category;
		this.min_variation = min_variation;
	}
	/*
	 * Returns the starting index of the best scored region.
	 */
	public int score(SequenceMatrix s) {
		assert(s.sequences.length > 0); // change to exceptions
		assert(window <= s.sequences[0].length);
		System.out.println(s.sequences[0].length);
		double[] window_vals = new double[s.sequences[0].length+1 - window];
		
		System.out.println("Computing all LOCAL SCORES with WINDOW: " + window);
		for(int i = 0; i < s.sequences[0].length+1 - window; i++)
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
	
	/*
	 * Computes the score according to the amount of indels, variation and category found.
	 * for the amino acids in the sequence. 
	 */
    public double local_score(SequenceMatrix s, int start_index)
    {
    	System.out.println("WINDOW STARTING AT " + start_index);
    	double indel_score = 0.0;
    	double variation_score = 0.0;
    	double category_score = 0.0; 
    	char[][] m = s.sequences;
    	
    	/*
    	 * Gets ever value from the Column and updates each score.
    	 * Repeat for whole window length.
    	 */
		for(int i = start_index; i < start_index + window; i++)
    	{
			ArrayList<Character> rows = new ArrayList<>();
			for(int j = 0; j < m.length; j++)
			{
    			rows.add(m[j][i]);
        	}
    		indel_score += compute_indel_percentage(rows);
    		variation_score += compute_variation_score(rows);
    		category_score += compute_category_score(rows);
    	}
    	
    	/* averaging scores by total rows */ 
    	indel_score /= window;
    	variation_score /= window;
    	category_score /= window;
    	/* Checking if window meets indel percentage requirement */ 
		if(indel_score > max_indel || indel_score < min_indel)
		{
			System.out.println("SKIPPED: INDEL SCORE NOT IN RANGE: " + indel_score);
			return 0.0; // failed to meet indel requirement.
		}
		/* Checking if window meets variation percentage requirement */ 
		if(variation_score > max_variation || variation_score < min_variation)
		{
			System.out.println("SKIPPED: VARIATION SCORE NOT IN RANGE");
			return 0.0; // failed to meet variation requirement.
		}
		/* Checking if window meets category change requirement */ 
		if(category_score > max_category_change || category_score < min_category_change)
		{
			System.out.println("SKIPPED: CATEGORY CHANGE NOT IN RANGE");
			return 0.0; // failed to meet category change requirement. 
		}
		System.out.println(start_index + " Indel_Score: " + indel_score + " Variation_Score: " + variation_score + " Category_Score: " + category_score);
    	return indel_score + variation_score + category_score;
    }
    
    /*
     * Returns the number of indels found and divides it by the number of records.
     */
    public double compute_indel_percentage(List<Character>letters)
    {
    	return letters.stream().filter(x-> x=='-').count()/(double)letters.size();
    }
    /*
     * Returns the total difference in amino acids found.
     * Since there are 20 amino acids, if 20 are found then 1.0 will be returned.
     * If not something in between 0 and 1 depending on how many amino acids are found.
     */
    public double compute_variation_score(List<Character> letters)
    {
    	double var = letters.stream().distinct().filter(x-> x!= '-').count();
    	return var > 0 ? var/20.0 : 0.0;
    }
    
    /*
     * Returns the Score of the category change.
     * There are three groups.
     * If all three are found a total of 1 is returned.
     */
    public double compute_category_score(List<Character> l)
    {
    	List<Character> letters = l.stream().distinct().collect(Collectors.toList());
    	boolean polar = false;
    	boolean nonpolar = false;
    	boolean charged = false;
    	for(Character c : letters)
    	{
    		if(polar_set.contains(c)) polar = true;
    		if(nonpolar_set.contains(c)) nonpolar = true;
    		if(charged_set.contains(c)) charged = true;
    		if(polar && nonpolar && charged) return 1.0; //all 3 categories found
    	}
    	double total = 0.0;
    	/* if 2 different categories found */ 
    	if(polar && nonpolar) total = .5;
    	else if(polar && charged) total = .5;
    	else if(nonpolar && charged) total = .5;
    	/* if only 1 category found return 0, because no change */ 
    	return total;
    } 
}
