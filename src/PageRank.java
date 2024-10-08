import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class PageRank {

	public static class MyMapper extends Mapper<Object, Text, Text, DoubleWritable> {

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			String[] nodes = value.toString().split("->");
			String nodeId = nodes[0];
			double initRank = 1.0;
			context.write(new Text(nodeId), new DoubleWritable(initRank)); //(a,1)
			
			String[] outLinks = nodes[1].split(",");								
			double ratio = initRank / outLinks.length;			
			for (int i = 0; i < outLinks.length; i++) {
				context.write(new Text(outLinks[i]), new DoubleWritable(ratio)); //(b,1/4) (c,1/4) (d,1/4) (e, 1/4)
			}
		}
	}

	/* From the mapper we will get,
 		1st iter: (a,1) (b,1/4) (c,1/4) (d,1/4) (e, 1/4)
   		2nd iter: (b,1) (c,1/3) (d, 1/3) (e, 1/3)
     		3rd iter: (c,1) (d,1/2) (e, 1/2)
       		4th iter: (d,1) (c,1/2) (b,1/2)
	 	5th iter: (e,1) (c,1/2) (b,1/2)
 	
  	After shuffling we will get the actual output which will be fed into the reducer function,
   		<a,(1)>
     		<b,(1/4,1,1/2,1/2)>
       		<c,(1/4,1/3,1,1/2,1/2)>
  		<d,(1/4,1/3,1/2,1)>
 		<e,(1/4,1/3,1/2,1)>
 	*/
	

	public static class MyReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
			
			double pagerank = 0.0;
			//for b, 1/4+1+1/2+1/2 = 2.25
			for (DoubleWritable value : values) { 
				pagerank += Double.parseDouble(value.toString()); 
			}
			context.write(key, new DoubleWritable(pagerank));
		}
	}

	public static void main(String[] args) throws Exception {

		Job job = Job.getInstance(new Configuration());
		job.setJarByClass(PageRank.class);
		job.setJobName("Page Rank");
		
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		job.setNumReduceTasks(1);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
