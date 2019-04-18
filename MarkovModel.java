package cs310;

import java.io.*;
import java.util.*;

/*this class creates an object that summarizes the sequences found in the string
taken as input during construction, creating a context of all objects of the inputed
size and then creating a list of sequences of size+1 to determine probabilities.
*/

public class MarkovModel {
	
	HashMap<String,Integer> context; //list of substrings of length s
	HashMap<String,Integer> seqMap; //list of substrings of length s+1
	TreeSet<String> cLib = new TreeSet<String>(); //list of unique characters
	int libSize;
	int degree;
	
	public MarkovModel (int size, String textInput) {//creates context, seqMap and cLib based on input
		degree = size;
		int len = textInput.length();
		context = new HashMap<String,Integer>(len);
		seqMap = new HashMap<String,Integer>(len);
		int i = 0; 
		for (;i<(len - size + 1); i++) {//adds all context strings that don't need to loop to start
			cLib.add(("" + textInput.charAt(i)));
			String con = textInput.substring(i, i+size);
			addCon(con);
		}
		for (int j = 1; j<size; j++) {//adds all context strings that need to loop to start
			cLib.add(("" + textInput.charAt(i)));
			String con = textInput.substring(i) + textInput.substring(0, j);
			addCon(con);
			i++;
		}
		
		i = 0;
		int combo = size+1;
		for (;i<(len - combo +1); i++) {//adds all context + character strings that don't loop
			String seq = textInput.substring(i, i+combo);
			addSeq(seq);
		}
		for (int j = 1; j<combo; j++) {//adds all context + character strings that loop
			String seq = textInput.substring(i, len) + textInput.substring(0,j);
			addSeq(seq);
			i++;
		}
		libSize = cLib.size();//set library size
		
	}
	
	public double laplace (String seq) {//find the laplace probability of a sequence
		String con = seq.substring(0, seq.length() -1 );
		int seqCt = 0;
		int conCt = 0;
		if (seqMap.containsKey(seq)) {
			seqCt = seqMap.get(seq);
		}
		if (context.containsKey(con)) {
			conCt = context.get(con);
		}
		return ((double)seqCt + 1)/((double)conCt + cLib.size());
	}
	
	private void addCon (String seq) {//add to the context hashmap
		if (context.containsKey(seq)) {
			int val = (int)context.get(seq);
			val++;
			context.put(seq,(Integer)val);
		}
		else {
			context.put(seq, 1);
		}
	}
	
	private void addSeq (String seq) {//add to the sequence hashmap
		if (seqMap.containsKey(seq)) {
			int val = (int)seqMap.get(seq);
			val++;
			seqMap.put(seq, (Integer)val);
		}
		else {
			seqMap.put(seq, 1);
		}
	}
	
	public float logProbSeq (String seq) {//ln of laplace
		return (float)Math.log((laplace(seq)));
	}
	

	
	

	@Override
	public String toString() {
		return ("S = " + libSize + '\n' + context.toString() + '\n' + seqMap.toString());
	}
	
	
	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		String fileName = args[1];
		String fileCont = "";
		try {Scanner s = new Scanner(new File(fileName));
			fileCont = s.useDelimiter("\\Z").next();
		}catch (Exception e) {
			System.out.println("file " + fileName + " not found");
		}
		
		
		MarkovModel myModel = new MarkovModel(size, fileCont);
		
		System.out.println(myModel.toString());

		for (int i = 2; i<args.length; i++) {//find prob for a seq (not a string)
			System.out.printf("%.4f", myModel.logProbSeq(args[i]));
		}
		
		
	}
	
	
}
