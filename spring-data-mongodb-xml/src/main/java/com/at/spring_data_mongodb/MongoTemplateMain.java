package com.at.spring_data_mongodb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import com.at.spring_data_mongodb.model.MallIndustryStatics;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.ReadPreference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MongoTemplateMain {
    private static final String INPUT_COLLECTION = "mall_industry_statics";
    private static final String OUTPUT_COLLECTION = "mall_industry_statics2";

    // inject the actual template
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ApplicationContext applicationContext;

    public void mapreduce_returnAll(Long docNum) {
        log.info("mapreduce_returnAll start:");
        Assert.notNull(mongoTemplate, "mongoTemplate should not be null");
        Assert.notNull(docNum, "docNum should not be null");
        
        
        long startTime = System.currentTimeMillis();
        
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
        
        
        
        // !!!! WARNING !!!!
        // 
        //      mapReduceResults contains ALL the records in OUTPUT_COLLECTION !!!!
        //
        // !!!! WARNING !!!!
        Assert.notNull(mapReduceResults, "mapReduceResults should not be null");
        
        
        // clean variables
        mapReduceResults = null;
        
        
        long endTime = System.currentTimeMillis();
        log.info("mapreduce_returnAll consumed ms: {}", endTime - startTime);
        
        
        log.info("mapreduce_returnAll end;");
    }

    public void mapreduce_noReturn(Long docNum) {
        log.info("mapreduce_noReturn start:");
        Assert.notNull(mongoTemplate, "mongoTemplate should not be null");
        Assert.notNull(docNum, "docNum should not be null");
        
        
        long startTime = System.currentTimeMillis();
        
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
        
        MapReduceCommand mrc = new MapReduceCommand(
                mongoTemplate.getCollection(INPUT_COLLECTION)
                ,replaceWithResourceIfNecessary("classpath:mapreduce/mall_industry_statics_cp_map.js")
                ,replaceWithResourceIfNecessary("classpath:mapreduce/mall_industry_statics_cp_reduce.js")
                ,OUTPUT_COLLECTION
                ,OutputType.MERGE
                ,query.getQueryObject()
                );
        mrc.setFinalize(replaceWithResourceIfNecessary("classpath:mapreduce/mall_industry_statics_cp_finalize.js"));
        mrc.setScope(scopeVariables);
        CommandResult commandResult = mongoTemplate.executeCommand(mrc.toDBObject(), ReadPreference.primary());
        
        
        // commandResult is very clean
        // e.g. 
        //     commandResult: 
        //        { "result" : "mall_industry_statics2" , "timeMillis" : 2645 , "timing" : { "mapTime" : 83 , "emitLoop" : 758 , "reduceTime" : 882 , "mode" : "mixed" , "total" : 2645} , "counts" : { "input" : 1 , "emit" : 10000 , "reduce" : 0 , "output" : 20000} , "ok" : 1.0}
        Assert.notNull(commandResult, "commandResult should not be null");
        
        
        
        // clean variables
        commandResult = null;
        
        
        
        long endTime = System.currentTimeMillis();
        log.info("mapreduce_noReturn consumed ms: {}", endTime - startTime);
        
        
        log.info("mapreduce_noReturn end;");
    }
    /**
     * <p>If <strong>str</strong> specified a resource, then return the content of the resource, or return the <strong>str</strong> itself.
     * </p>
     * <p>e.g. <br />
     * str="classpath:mapreduce/map.js", then calling replaceWithResourceIfNecessary(str) would return the content of map.js
     * </p>
     * @param <strong>str</strong>
     * @return <strong>str</strong> or the content of the resource specified by <strong>str</strong>
     */
    public String replaceWithResourceIfNecessary(String str) {
        String func = str;
        if (applicationContext != null && ResourceUtils.isUrl(str)) {
            Resource functionResource = applicationContext.getResource(func);
            if (!functionResource.exists()) {
                throw new RuntimeException(String.format("Resource %s not found!", str));
            }
    
            Scanner scanner = null;
            try {
                scanner = new Scanner(functionResource.getInputStream());
                return scanner.useDelimiter("\\A").next();
            } catch (IOException e) {
                throw new RuntimeException(String.format("Cannot read map-reduce file %s!", str), e);
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    
        return func;
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
//        main.dropCollection(OUTPUT_COLLECTION);
//        main.createCollection(OUTPUT_COLLECTION);
//        main.mapreduce_returnAll(6000000L);
        main.dropCollection(OUTPUT_COLLECTION);
        main.createCollection(OUTPUT_COLLECTION);
        main.mapreduce_noReturn(6000000L);
        main.findMallIndustryStatics();

        long endTime = System.currentTimeMillis();
        log.info("Consumed time: {}ms", (endTime - startTime));
        // System.in.read();

        ctx.close();
        log.info("Exiting Main.");
    }
}
