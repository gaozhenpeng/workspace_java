package com.at.spring_data_mongodb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "mall_industry_statics")
@Data
@SuppressWarnings("serial")
public class MallIndustryStatics implements Serializable {
    @Id
    private IdField id;
    @Field("createTime")
    private String createTime;
    @Field("mall")
    private MallField mall;
    @Field("reqRec")
    private ReqRecField reqRec;
    @Field("goods")
    private GoodsField goods;

    @Data
    public static class IdField implements Serializable {
        private String industryCode;
        private String type;
        private String beginDate;
        private String endDate;
    }
    @Data
    public static class MallField implements Serializable {
        private Double pvAvg = 0.0D;
        private Double rpvAvg = 0.0D;
    }
    @Data
    public static class ReqRecField implements Serializable {
        private Double receivingRateAvg = 0.0D;
    }
    @Data
    public static class GoodsField implements Serializable {
        private Double pvAvg = 0.0D;
    }

}
