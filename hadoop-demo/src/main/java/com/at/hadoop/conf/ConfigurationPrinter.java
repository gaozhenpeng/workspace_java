package com.at.hadoop.conf;

import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ConfigurationPrinter extends Configured implements Tool {
//	static {
//		Configuration.addDefaultResource("com/at/hadoop/conf/hdfs-default.xml");
//		Configuration.addDefaultResource("com/at/hadoop/conf/hdfs-site.xml");
//		Configuration.addDefaultResource("com/at/hadoop/conf/yarn-default.xml");
//		Configuration.addDefaultResource("com/at/hadoop/conf/yarn-site.xml");
//		Configuration.addDefaultResource("com/at/hadoop/conf/mapred-default.xml");
//		Configuration.addDefaultResource("com/at/hadoop/conf/mapred-site.xml");
//	}
	
	@Override
	public int run(String[] args) throws Exception{
		Configuration conf = getConf();
		for(Entry<String, String> entry : conf){
			System.out.printf("%s=%s\n", entry.getKey(), entry.getValue());
		}
		return 0;
	}
	
	public static void main(String[] args) throws Exception{
		int exitCode = ToolRunner.run(new ConfigurationPrinter(), args);
		System.out.print("\n\n\n\n");
		System.getProperties().list(System.out);
		System.out.print("\n\n\n\n");
		
		System.exit(exitCode);
	}
}
