package com.at.hadoop.mapreduce.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * hadoop jar target/hadoop-0.0.1-SNAPSHOT-hadoop.jar com.at.hadoop.mapreduce.avro.AvroSort src/main/avro/WeatherRecord.avsc output_avrosort output_avro/
 * 
 * <p><strong>Notice for tez:</strong>
 *  Just doesn't work!
 * </p>
 */
public class AvroSort extends Configured implements Tool {
	static class SortMapper<K> extends Mapper<AvroKey<K>, NullWritable, AvroKey<K>, AvroValue<K>>{
		@Override
		protected void map(AvroKey<K> key, NullWritable value, Context context) throws IOException, InterruptedException{
			context.write(key,  new AvroValue<K>(key.datum()));
		}
	}
	
	static class SortReducer<K> extends Reducer<AvroKey<K>, AvroValue<K>, AvroKey<K>, NullWritable>{
		@Override
		protected void reduce(AvroKey<K> key, Iterable<AvroValue<K>> values, Context context) throws IOException, InterruptedException{
			for(AvroValue<K> value : values){
				context.write(new AvroKey<K>(value.datum()), NullWritable.get());
			}
		}
	}
	public int run(String[] args) throws Exception {
		if(args.length != 3){
			System.err.printf("Usage: %s [generic options] <avroschema> <outputpath> <inputpath>...\n", getClass().getName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		Job job = Job.getInstance(getConf(), "Avro sort");
		job.setJarByClass(getClass());
		
		Schema schema = new Schema.Parser().parse(new File(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		for(int i = 2; i < args.length ; i++){
			FileInputFormat.addInputPath(job, new Path(args[i]));
		}

//		job.getConfiguration().setBoolean(Job.MAPREDUCE_JOB_USER_CLASSPATH_FIRST, true);
//		AvroJob.setDataModelClass(job, GenericData.class);
		
		AvroJob.setInputKeySchema(job, schema);
		AvroJob.setMapOutputKeySchema(job, schema);
		AvroJob.setMapOutputValueSchema(job, schema);
		AvroJob.setOutputKeySchema(job, schema);
		
		job.setInputFormatClass(AvroKeyInputFormat.class);
		job.setOutputFormatClass(AvroKeyOutputFormat.class);
		
		job.setOutputKeyClass(AvroKey.class);
		job.setOutputValueClass(NullWritable.class);
		
		job.setMapperClass(SortMapper.class);
		job.setReducerClass(SortReducer.class);
		
		return job.waitForCompletion(true)?0:1;
	}
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new AvroSort(), args);
		System.exit(exitCode);
	}
}
