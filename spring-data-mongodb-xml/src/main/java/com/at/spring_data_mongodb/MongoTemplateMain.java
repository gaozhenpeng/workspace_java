package com.at.spring_data_mongodb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.at.spring_data_mongodb.model.MallIndustryStatics;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MongoTemplateMain {
    private static final String INPUT_COLLECTION = "mall_industry_statics";
    private static final String OUTPUT_COLLECTION = "mall_industry_statics2";

    // inject the actual template
    @Autowired
    private MongoTemplate mongoTemplate;

    public void mapreduce(Long docNum) {
        log.info("map reduce 'copy' start:");
        Assert.notNull(mongoTemplate, "mongoTemplate should not be null");
        Assert.notNull(docNum, "docNum should not be null");

        Query query = new BasicQuery("{}").limit(1);
//        Query query=new Query();
//        String createTime = "";
//        List<String> typeList = new ArrayList<>();
//        query.addCriteria(
//                Criteria
//                    .where("createTime").is(createTime)
//                    .and("type").in(typeList)
//        );
        Map<String, Object> scopeVariables = new HashMap<>();
        scopeVariables.put("doc_num", docNum);
        
        MapReduceOptions mapReduceOptions =
                new MapReduceOptions()
                    .outputTypeMerge()
                    .scopeVariables(scopeVariables)
                    .outputCollection(OUTPUT_COLLECTION)
                    .finalizeFunction("classpath:mapreduce/mall_industry_statics_cp_finalize.js")
                    ;
        
        MapReduceResults<MallIndustryStatics> mapReduceResults = mongoTemplate.mapReduce(
                query
                , INPUT_COLLECTION
                , "classpath:mapreduce/mall_industry_statics_cp_map.js"
                , "classpath:mapreduce/mall_industry_statics_cp_reduce.js"
                , mapReduceOptions
                , MallIndustryStatics.class);
        
        log.info("map reduce 'copy' end;");
    }
    
    
    /**
     * db.getCollection('mall_industry_statics').save({
     *      "_id" : {
     *          "industryCode" : "type_123"
     *          ,"type" : "REQREC"
     *          ,"beginDate" : "2018-01-01"
     *          ,"endDate" : "2018-01-02"
     *      }
     *      ,"createTime" : "2018-01-14 03:53:04"
     *      ,"reqRec" : {
     *          "receivingRateAvg" : 0.123
     *      }
     *  })
     */
    public List<MallIndustryStatics> findMallIndustryStatics() {
        List<MallIndustryStatics> mallIndustryStaticsList = mongoTemplate.findAll(MallIndustryStatics.class);
        log.info("mallIndustryStaticsList.size(): '{}'", mallIndustryStaticsList.size());
        if(mallIndustryStaticsList != null) {
            mallIndustryStaticsList.stream().parallel()
                .forEach((mis) -> {
                    log.info("mis : '{}'", mis);
                });
        }
        return mallIndustryStaticsList;
    }
    public void dropCollection(String collectionName) {
        log.info("mongoTemplate.dropCollection({})", collectionName);
        mongoTemplate.dropCollection(collectionName);
    }

    public void createCollection(String collectionName) {
        log.info("mongoTemplate.createCollection({})", collectionName);
        DBCollection dbCollection = mongoTemplate.createCollection(collectionName);
        dbCollection.createIndex(
                new BasicDBObject()
                    .append("_id.industryCode", 1)
                    .append("_id.beginDate", 1)
                    .append("_id.endDate", 1));
    }

    public static void main(String[] args) throws IOException {
        log.info("Enterring Main.");
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath*:/spring/applicationContext-resources.xml");

        ctx.registerShutdownHook();
        ctx.start();

        long startTime = System.currentTimeMillis();
        
        MongoTemplateMain main = ctx.getBean(MongoTemplateMain.class);
        main.dropCollection(OUTPUT_COLLECTION);
        main.createCollection(OUTPUT_COLLECTION);
        main.mapreduce(10000L);
        main.findMallIndustryStatics();

        long endTime = System.currentTimeMillis();
        log.info("Consumed time: {}ms", (endTime - startTime));
        // System.in.read();

        ctx.close();
        log.info("Exiting Main.");
    }
}
