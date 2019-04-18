package cs310;

public class seqNode {
	float modA;
	float modB;
	float diff;
	String seq;
	seqNode next;
	
	public seqNode(float aVal, float bVal, String letters) {
		modA = aVal;
		modB = bVal;
		diff = modA - modB;
		seq = letters;
	}
	
	public boolean hasNext() {
		if (next != null) {
			return true;
		}
		return false;
	}
	
		
	public int chainCount() {//length of linkedList
		int num = 1;
		if (this.hasNext()) {
			num += this.next.chainCount();
		}
		return num;
	}
	
	public String toString() {
		String s = seq.replaceAll("\\s", " ");
		String mA = String.format("%.3f", modA);
		String mB = String.format("%.3f", modB);
		String di = String.format("%.3f", diff);
		
		
		return ("\"" + s + "\"    " + mA + "  " + mB + "  " + di);	
	}
	
}
