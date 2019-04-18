package cs310;

import java.util.*;
import java.io.*;

public class BestModel {


	MarkovModel firstModel;  
	MarkovModel secondModel;
	int modDeg;   //degree of model
	TreeMap<Float, seqNode> addQueue; //sorted map for nodes
	int nodeCount;  //quantity of nodes
	
	//constructor
	public BestModel(int degree, String first, String second, String... compFiles) {
		try{Scanner firstMod = new Scanner(new File(first));//scan first file for string
		String firstStr = firstMod.useDelimiter("\\Z").next();
		firstModel = new MarkovModel(degree, firstStr);//build first model
		firstMod.close();
		}
		catch(Exception e) {
			System.out.printf("file " + first + "not found");//exception catch
		}
		try{Scanner secMod = new Scanner(new File(second));//scan second file for string
		String secondStr = secMod.useDelimiter("\\Z").next();
		secondModel = new MarkovModel(degree, secondStr);//build second model
		secMod.close();
		}
		catch(Exception e) {
			System.out.printf("file " + second + "not found");//exception catch
		}
			
		modDeg = degree;
		addQueue = new TreeMap<Float, seqNode>();//initializer
		
		for (int i = 0; i < compFiles.length; i++) {//run function once for each extra arg
			String compStr = "";
			try{Scanner comp = new Scanner(new File(compFiles[i]));//string to be compared
			compStr = comp.useDelimiter("\\Z").next();
			comp.close();
			}
			catch(Exception e) {
				System.out.printf("file " + compFiles[i] + " not found");
			}
			categorize(compFiles[i], compStr);//call function that returns values
		}
		
		
		
	}
	
	public void categorize (String fileName, String toCat) {//add nodes to tree starting from each character
		nodeCount = 0;
		int strLen = toCat.length();
		int i = 0;
		for (; i < (strLen-modDeg); i++) {//two loops to cover the circular cases
			String seq = toCat.substring(i, i+modDeg+1);
			makeNode(seq);
		}
		for (; i < strLen; i++) {
			String seq = toCat.substring(i, strLen) + toCat.substring(0, (i + modDeg - strLen + 1));
			makeNode(seq);
		}
		
		calcProb(fileName);//calc probability given the tree
		
		
	}
	
	private void makeNode (String seq) {//create an insert a node into the tree
		nodeCount++;
		float valA = firstModel.logProbSeq(seq);
		float valB = secondModel.logProbSeq(seq);
		seqNode toAdd = new seqNode(valA, valB, seq);
		if (!addQueue.containsKey(Math.abs(valA-valB))) {//add directly
			addQueue.put(Math.abs(toAdd.diff), toAdd);
		}
		else {                                          //add to linklist
			seqNode base = addQueue.get(Math.abs(toAdd.diff));
			while (base.hasNext()) {
				base = base.next;
			}
			base.next = toAdd;
		}
	}
	
	private void calcProb(String fileName) {//add up probabilities
		int count = 0;
		float totalA = 0;
		float totalB = 0;
		
		int denom = nodeCount;
		
		seqNode[] topScore = new seqNode[10]; //store the highest values
		int topIndex = 0;		
		
		
		while (!addQueue.isEmpty()) {  //go until all nodes have been removed
			float currKey = addQueue.lastKey();  //get largest value key
			seqNode topNode = addQueue.get(currKey);
			seqNode currNode = topNode;
			int totNode = currNode.chainCount(); //get number of nodes in linkedList

			for (int i = 0; i<totNode; i++) {  //add up values in chain
				totalA = totalA + (currNode.modA);
				totalB = totalB + (currNode.modB);
				currNode = currNode.next;
			}
			
			if (count < 10) {  //add values to array until full
				count = count + totNode;
				currNode = topNode;
				for (int i = 0; i < totNode; i++) {
					if (topIndex > 9) {   //don't add values in invalid locations
						break;
					}
					topScore[topIndex] = currNode;
					topIndex++;
					currNode = currNode.next;
				}
			}
			addQueue.remove(currKey);  //remove nodes once used
		}
		
		
		
		System.out.printf("%s  %.4f   %.4f  %.4f\n", fileName, totalA/denom, totalB/denom, (totalA - totalB)/denom);
		for (int i = 0; i < topScore.length; i++) {//print 10 largest differences
			System.out.println(topScore[i].toString());
		}
	}
	
	public static void main(String[]args) {
		
		int deg = Integer.parseInt(args[0]);
		String file1 = args[1];
		String file2 = args[2];
		
		String[] compFiles = Arrays.copyOfRange(args, 3, args.length);
		
		new BestModel (deg, file1, file2, compFiles);
		
	}
	
	
}
