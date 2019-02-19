package com.at.avro.standalone.dto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringPairTest {
    private static final String AVRO_PATH = "src/main/avro/" + StringPair.class.getSimpleName() + ".avro";
    private static final String AVSC_PATH = "src/main/avro/" + StringPair.class.getSimpleName() + ".avsc";
    
    @Test
    public void testSpecificRecord() throws IOException {
        
        log.info("Schema: '{}'", StringPair.getClassSchema());
        
        // data
        List<StringPair> stringPairs = new ArrayList<>();
        stringPairs.add(new StringPair("a", "1"));
        stringPairs.add(new StringPair("c", "2"));
        stringPairs.add(new StringPair("b", "3"));
        stringPairs.add(new StringPair("b", "2"));
        log.info("Data: '{}'", stringPairs);
        
        
        // serializing
        log.info("Serializing to '{}'", AVRO_PATH);
        
        // data writer, invoked by file writer
        DatumWriter<StringPair> stringPairDatumWriter = new SpecificDatumWriter<>(StringPair.class);
        
        // file writer
        DataFileWriter<StringPair> stringPairDataFileWriter = new DataFileWriter<>(stringPairDatumWriter);
        // create file
        stringPairDataFileWriter.create(StringPair.getClassSchema(), new File(AVRO_PATH));
        // append data
        for(StringPair stringPair : stringPairs){
            stringPairDataFileWriter.append(stringPair);
        }
        // close
        stringPairDataFileWriter.close();
        
        
        // deserializing
        log.info("Deserializing from '{}'", AVRO_PATH);
        
        // data reader, invoked by file reader
        DatumReader<StringPair> stringPairDatumReader = new SpecificDatumReader<>(StringPair.class);
        
        // file reader
        DataFileReader<StringPair> stringPairDataFileReader = new DataFileReader<>(new File(AVRO_PATH), stringPairDatumReader);
        // read data
        while(stringPairDataFileReader.hasNext()) {
            StringPair stringPair = stringPairDataFileReader.next();
            log.info("stringPair: '{}', left: '{}', right: '{}'", stringPair.toString(), stringPair.getLeft(), stringPair.getRight());
        }
        // close
        stringPairDataFileReader.close();
    }

    @Test
    public void testGenericRecord() throws IOException {
        
        // schema
        Schema schema = new Schema.Parser().parse(new File(AVSC_PATH));
        log.info("Schema: '{}'", schema);
        
        // data
        List<GenericRecord> stringPairs = new ArrayList<>();
        GenericRecord stringPair = null;
        
        stringPair = new GenericData.Record(schema);
        stringPair.put("left", "a");
        stringPair.put("right", "1");
        stringPairs.add(stringPair);
        
        stringPair = new GenericData.Record(schema);
        stringPair.put("left", "c");
        stringPair.put("right", "2");
        stringPairs.add(stringPair);
        
        stringPair = new GenericData.Record(schema);
        stringPair.put("left", "b");
        stringPair.put("right", "3");
        stringPairs.add(stringPair);
        
        stringPair = new GenericData.Record(schema);
        stringPair.put("left", "b");
        stringPair.put("right", "2");
        stringPairs.add(stringPair);
        
        log.info("Data: '{}'", stringPairs);
        
        
        
        // serializing
        log.info("Serializing to '{}'", AVRO_PATH);
        
        // data writer, invoked by file writer
        DatumWriter<GenericRecord> genericRecordDatumWriter = new GenericDatumWriter<GenericRecord>(schema);
        
        // file writer
        DataFileWriter<GenericRecord> genericRecordDataFileWriter = new DataFileWriter<>(genericRecordDatumWriter);
        // create file
        genericRecordDataFileWriter.create(schema, new File(AVRO_PATH));
        // append data
        for(GenericRecord genericRecordStringPair : stringPairs){
            genericRecordDataFileWriter.append(genericRecordStringPair);
        }
        // close
        genericRecordDataFileWriter.close();
        
        
        // deserializing
        log.info("Deserializing from '{}'", AVRO_PATH);
        
        // data reader, invoked by file reader
        DatumReader<GenericRecord> genericDatumReader = new GenericDatumReader<>(schema);
        
        // file reader
        DataFileReader<GenericRecord> genericRecordDataFileReader = new DataFileReader<>(new File(AVRO_PATH), genericDatumReader);
        // read data
        while(genericRecordDataFileReader.hasNext()) {
            GenericRecord genericRecordStringPair = genericRecordDataFileReader.next();
            log.info("genericRecordStringPair: '{}', left: '{}', right: '{}'", genericRecordStringPair, genericRecordStringPair.get("left"),  genericRecordStringPair.get("right"));
        }
        // close
        genericRecordDataFileReader.close();
    }
}
