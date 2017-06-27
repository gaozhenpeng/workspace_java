package com.at.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

public class PooledStreamCompressor {
	/**
	 * echo "Text" | hadoop jar target/hadoop-0.0.1-SNAPSHOT.jar com.at.hadoop.io.StreamCompressor org.apache.hadoop.io.compress.GzipCodec |gunzip -
	 */
	public static void main(String[] args) throws Exception{
		String codecClassname = args[0];
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		
		CompressionOutputStream out = codec.createOutputStream(System.out);
		IOUtils.copyBytes(System.in, out, 4096, false);
		out.finish();
	}
}
