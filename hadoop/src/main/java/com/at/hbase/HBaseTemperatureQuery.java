package com.at.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
/**
 * mvn clean package
 * set HBASE_CLASSPATH=target\hadoop-0.0.1-SNAPSHOT.jar
 * 
 * %HBASE_HOME%\bin\hbase.cmd com.at.hbase.HBaseTemperatureQuery 011990-99999
 */
public class HBaseTemperatureQuery  extends Configured implements Tool {
    static final byte[] DATA_COLUMNFAMILY = Bytes.toBytes("data");
    static final byte[] AIRTEMP_QUALIFIER = Bytes.toBytes("airtemp");
    
    public NavigableMap<Long, Integer> getStationObservations(Table table, String stationId, long maxStamp, int maxCount) throws IOException{

        NavigableMap<Long, Integer> resultMap = new TreeMap<>();
        
        byte[] startRow = RowKeyConverter.makeObservationRowKey(stationId, maxStamp);
        
        Scan scan = new Scan(startRow);
        scan.addColumn(DATA_COLUMNFAMILY, AIRTEMP_QUALIFIER);
        
        ResultScanner scanner = table.getScanner(scan);
        
        try {
            Result res = null;
            int count = 0;
            
            while( (res = scanner.next()) != null && count++ < maxCount) {
                byte[] row = res.getRow();
                byte[] value = res.getValue(DATA_COLUMNFAMILY, AIRTEMP_QUALIFIER);
                
                Long stamp = Long.MAX_VALUE - Bytes.toLong(row, row.length - Bytes.SIZEOF_LONG, Bytes.SIZEOF_LONG);
                Integer temp = Bytes.toInt(value);
                resultMap.put(stamp, temp);
            }
        }finally {
            scanner.close();
        }
        
        return resultMap;
    }
    
    @Override
    public int run(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Usage: " + this.getClass().getSimpleName() + "<station_id>");
            return -1;
        }
        Connection connection = ConnectionFactory.createConnection(getConf());
        
        Table table = connection.getTable(TableName.valueOf("observations"));
        try {
            NavigableMap<Long, Integer> observations = getStationObservations(table, args[0], Long.MAX_VALUE, 10).descendingMap();
            for(Map.Entry<Long, Integer> observation : observations.entrySet()) {
                System.out.printf("%1$tF %1$tR\t%2$s\n", observation.getKey(), observation.getValue());
            }
            return 0;
        }finally {
            table.close();
        }
    }
    

    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(HBaseConfiguration.create(), new HBaseTemperatureQuery(), args);
        
        System.exit(exitCode);
    }
    
}
