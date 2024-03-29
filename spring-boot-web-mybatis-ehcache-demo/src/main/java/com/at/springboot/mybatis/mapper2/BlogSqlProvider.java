package com.at.springboot.mybatis.mapper2;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.at.springboot.mybatis.po.Blog;
import com.at.springboot.mybatis.po.BlogExample;
import com.at.springboot.mybatis.po.BlogExample.Criteria;
import com.at.springboot.mybatis.po.BlogExample.Criterion;

public class BlogSqlProvider {

    public String countByExample(BlogExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("blog");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(BlogExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("blog");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(Blog record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("blog");
        
        if (record.getBlogId() != null) {
            sql.VALUES("blog_id", "#{blogId,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getContent() != null) {
            sql.VALUES("content", "#{content,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatedDatetime() != null) {
            sql.VALUES("created_datetime", "#{createdDatetime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedDatetime() != null) {
            sql.VALUES("updated_datetime", "#{updatedDatetime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String selectByExample(BlogExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("blog_id");
        } else {
            sql.SELECT("blog_id");
        }
        sql.SELECT("name");
        sql.SELECT("content");
        sql.SELECT("created_datetime");
        sql.SELECT("updated_datetime");
        sql.FROM("blog");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        Blog record = (Blog) parameter.get("record");
        BlogExample example = (BlogExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("blog");
        
        if (record.getBlogId() != null) {
            sql.SET("blog_id = #{record.blogId,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        }
        
        if (record.getContent() != null) {
            sql.SET("content = #{record.content,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatedDatetime() != null) {
            sql.SET("created_datetime = #{record.createdDatetime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedDatetime() != null) {
            sql.SET("updated_datetime = #{record.updatedDatetime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("blog");
        
        sql.SET("blog_id = #{record.blogId,jdbcType=BIGINT}");
        sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        sql.SET("content = #{record.content,jdbcType=VARCHAR}");
        sql.SET("created_datetime = #{record.createdDatetime,jdbcType=TIMESTAMP}");
        sql.SET("updated_datetime = #{record.updatedDatetime,jdbcType=TIMESTAMP}");
        
        BlogExample example = (BlogExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Blog record) {
        SQL sql = new SQL();
        sql.UPDATE("blog");
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getContent() != null) {
            sql.SET("content = #{content,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatedDatetime() != null) {
            sql.SET("created_datetime = #{createdDatetime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedDatetime() != null) {
            sql.SET("updated_datetime = #{updatedDatetime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("blog_id = #{blogId,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, BlogExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}