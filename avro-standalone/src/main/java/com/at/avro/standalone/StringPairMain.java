package com.at.avro.standalone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.at.avro.standalone.dto.StringPair;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringPairMain {
    private static final String AVRO_FILE_NAME = "src/main/avro/" + StringPair.class.getSimpleName() + ".avro";
    public static void main(String[] args) throws IOException {
        
        log.info("Schema: '{}'", StringPair.getClassSchema());
        
        // data
        List<StringPair> stringPairs = new ArrayList<>();
        stringPairs.add(new StringPair("a", "1"));
        stringPairs.add(new StringPair("c", "2"));
        stringPairs.add(new StringPair("b", "3"));
        stringPairs.add(new StringPair("b", "2"));
        log.info("Data: '{}'", stringPairs);
        
        
        // serializing
        log.info("Serializing to '{}'", AVRO_FILE_NAME);
        
        // data writer, invoked by file writer
        DatumWriter<StringPair> stringPairDatumWriter = new SpecificDatumWriter<>(StringPair.class);
        
        // file writer
        DataFileWriter<StringPair> stringPairDataFileWriter = new DataFileWriter<>(stringPairDatumWriter);
        // create file
        stringPairDataFileWriter.create(StringPair.getClassSchema(), new File(AVRO_FILE_NAME));
        // append data
        for(StringPair stringPair : stringPairs){
            stringPairDataFileWriter.append(stringPair);
        }
        // close
        stringPairDataFileWriter.close();
        
        
        // deserializing
        log.info("Deserializing from '{}'", AVRO_FILE_NAME);
        
        // data reader, invoked by file reader
        DatumReader<StringPair> stringPairDatumReader = new SpecificDatumReader<>(StringPair.class);
        
        // file reader
        DataFileReader<StringPair> stringPairDataFileReader = new DataFileReader<>(new File(AVRO_FILE_NAME), stringPairDatumReader);
        // read data
        while(stringPairDataFileReader.hasNext()) {
            StringPair stringPair = stringPairDataFileReader.next();
            log.info("stringPair: '{}', left: '{}', right: '{}'", stringPair.toString(), stringPair.getLeft(), stringPair.getRight());
        }
        // close
        stringPairDataFileReader.close();
    }
}
