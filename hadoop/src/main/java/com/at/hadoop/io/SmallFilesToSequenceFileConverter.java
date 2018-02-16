package com.at.hadoop.io;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SmallFilesToSequenceFileConverter extends Configured implements Tool{
    static class SeqenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable>{
        private Text filenameKey;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException{
            InputSplit split = context.getInputSplit();
            Path path = ((FileSplit) split).getPath();
            filenameKey = new Text(path.toString());
        }

        @Override
        protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException{
            context.write(filenameKey, value);
        }
    } // SequenceFileMapper end

    
    
    @Override
    public int run(String[] args) throws Exception {
        Job job = parseInputAndOutput(this, getConf(), args);
        if( job == null){
            return -1;
        }
        
        // read the file as a whole
        job.setInputFormatClass(WholeFileInputFormat.class);
        // output the contents in SequenceFile format
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setMapperClass(SeqenceFileMapper.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    private Job parseInputAndOutput(Tool tool, Configuration conf, String[] args) throws IOException {
        if(args.length != 2) {
            printUsage(tool, "<input> <output>");
            return null;
        }
        Job job = Job.getInstance(conf);
        job.setJarByClass(tool.getClass());
        job.setJobName(tool.getClass().getSimpleName());
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        return job;
    }

    private void printUsage(Tool tool, String extraArgsUsage) {
        System.err.printf("Usage: %s [genericOptions] %s\n\n", tool.getClass().getSimpleName(), extraArgsUsage);
        GenericOptionsParser.printGenericCommandUsage(System.err);
    }

    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(new SmallFilesToSequenceFileConverter(), args);
        System.exit(exitCode);
    }
}
