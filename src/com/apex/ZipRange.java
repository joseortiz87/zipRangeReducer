package com.apex;

/**
 * 
 * @author jortiz
 *
 */
public class ZipRange {

	private int lower;
	private int upper;
	
	/**
	 * Build Zip Range using a space separate pair of values
	 * "120 125" and always arrange by lower and upper case 
	 * @param range
	 * @throws Exception
	 */
	public ZipRange(String range) throws Exception {
		super();
		if(range != null) {
			String [] arrayRange = range.split(" ");
			if(arrayRange != null && arrayRange.length > 1) {
				int tmpNum = Integer.parseInt(arrayRange[0]);
				int tmpNum2 = Integer.parseInt(arrayRange[1]);
				if(tmpNum <= tmpNum2) {
					this.lower = tmpNum;
					this.upper = tmpNum2;
				}else {
					this.lower = tmpNum2;
					this.upper = tmpNum;	
				}
			}
		}else {
			throw new Exception("Invalid pair");
		}
	}

	/**
	 * Build zip range specifying the lower and upper values
	 * @param lower
	 * @param upper
	 */
	public ZipRange(int lower, int upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	@Override
	public String toString() {
		return "[" + lower + "," + upper + "]";
	}
}
