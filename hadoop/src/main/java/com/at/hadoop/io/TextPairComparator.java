package com.at.hadoop.io;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;

public class TextPairComparator extends WritableComparator {
	private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();
	static{
		WritableComparator.define(TextPair.class, new TextPairComparator());
	}
	public TextPairComparator(){
		super(TextPair.class);
	}
	
	@Override
	public int compare(byte[] b1, int start1, int length1, byte[] b2, int start2, int length2){
		try{
			int firstL1 = WritableUtils.decodeVIntSize(b1[start1]) + WritableComparator.readVInt(b1, start1);
			int firstL2 = WritableUtils.decodeVIntSize(b2[start2]) + WritableComparator.readVInt(b2, start1);
			int cmp = TEXT_COMPARATOR.compare(b1, start1, firstL1, b2, start2, firstL2);
			if(cmp != 0){
				return cmp;
			}
			return TEXT_COMPARATOR.compare(b1, start1 + firstL1, length1 - firstL1, b2, start2 + firstL2, length2 - firstL2);
		}catch(IOException e){
			throw new IllegalArgumentException(e);
		}
	}
	
}
