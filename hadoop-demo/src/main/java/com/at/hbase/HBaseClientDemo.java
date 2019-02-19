package com.at.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * mvn clean package
 * set HBASE_CLASSPATH=target\hadoop-0.0.1-SNAPSHOT.jar
 * 
 * %HBASE_HOME%\bin\hbase.cmd com.at.hbase.HBaseClientDemo
 */
public class HBaseClientDemo {
    
    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        
        Connection connection = ConnectionFactory.createConnection(config);
        Admin hbaseAdmin = connection.getAdmin();
        try {
            TableName tableName = TableName.valueOf("test");
            HTableDescriptor htableDescriptor = new HTableDescriptor(tableName);
            HColumnDescriptor hcolumnDescriptor = new HColumnDescriptor("data");
            htableDescriptor.addFamily(hcolumnDescriptor);
            
            hbaseAdmin.createTable(htableDescriptor);
            HTableDescriptor[] tables = hbaseAdmin.listTables();
            if(tables.length != 1 && Bytes.equals(tableName.getName(), tables[0].getTableName().getName())) {
                throw new IOException("Failed create of table");
            }
            
            Table htable = connection.getTable(tableName);
            
            try {
                for(int i = 1; i <= 3 ; i++) {
                    byte[] row = Bytes.toBytes("row" + i);
                    Put put = new Put(row);
                    byte[] columnFamily = Bytes.toBytes("data");
                    byte[] qualifier = Bytes.toBytes(String.valueOf(i));
                    byte[] value = Bytes.toBytes("value" +i);
                    put.addColumn(columnFamily, qualifier, value);
                    htable.put(put);
                }
                
                Get get = new Get(Bytes.toBytes("row1"));
                Result result = htable.get(get);
                System.out.println("Get: '"+ result + "'");
                
                
                Scan scan = new Scan();
                ResultScanner scanner = htable.getScanner(scan);
                try{
                    for(Result scannerResult : scanner ) {
                        System.out.println("Scan: '" + scannerResult + "'");
                    }
                }finally {
                    scanner.close();
                }
            }finally {
                htable.close();
            }
        }finally {
            hbaseAdmin.close();
        }
    }
}
