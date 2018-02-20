package com.at.hadoop.io;

import java.io.IOException;

import org.apache.avro.hadoop.io.AvroSequenceFile.Writer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.zookeeper.common.IOUtils;

public class SequenceFileWrite {
	private static final String[] DATA = {
			"One, two, buckle my shoe",
			"Three, four, shut the door",
			"Five, six, pick up sticks",
			"Seven, eight, lay them straight",
			"Nine, ten, a big fat hen"
	};
	
	public static void main(String[] args) throws IOException{
		String uri = args[0];
		Configuration conf = new Configuration();
		Path path = new Path(uri);
		
		IntWritable key = new IntWritable();
		Text value = new Text();
		SequenceFile.Writer writer = null;
		try{
			writer = SequenceFile.createWriter(
			                    conf
			                    , Writer.file(path), Writer.keyClass(key.getClass()), Writer.valueClass(value.getClass())
			                    // compression type and compression codec
			                    //    1. gzip may not be available in windows hadoop
			                    //    2. the writer.getLength() returns a constant value for 'CompressionType.Block' (block)
//			                    , Writer.compression(CompressionType.BLOCK, new DeflateCodec())
                              , Writer.compression(CompressionType.BLOCK, new GzipCodec())
			                    
			        );
			
			for(int i = 0 ; i < 100 ; i++){
				key.set(100 - i);
				value.set(DATA[i % DATA.length]);
				System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
				writer.append(key, value);
			}
		}finally{
			IOUtils.closeStream(writer);
		}
	}
}
