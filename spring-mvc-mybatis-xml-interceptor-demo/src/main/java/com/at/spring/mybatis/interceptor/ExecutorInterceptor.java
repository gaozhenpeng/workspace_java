package com.at.spring.mybatis.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

/**
 * <p>Use for count results without modifying sql</p>
 * <p>Usage example:</p>
 * <code><pre>
 *      Object countObject = 
 *              (Object) getSqlSession()
 *                  .selectOne(
 *                      getStatement(sqlId)
 *                      , new ExecutorInterceptor.CountParameter(paramMap)
 *                  );
 * </pre></code>
 */
@Intercepts({
	@Signature(type = Executor.class
			, method = "query"
			, args = { MappedStatement.class
					, Object.class
					, RowBounds.class
					, ResultHandler.class
					}
			)
	}
)
public class ExecutorInterceptor  implements Interceptor {
	private final static Logger logger = Logger.getLogger(ExecutorInterceptor.class);
	private static final int MAPPED_STATEMENT_INDEX = 0;
	private static final int PARAMETER_INDEX = 1;
	private static final int ROWBOUNDS_INDEX = 2;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		processIntercept(invocation.getArgs());
		return invocation.proceed();
	}

	private void processIntercept(final Object[] queryArgs) {
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		
		BoundSql boundSql = ms.getBoundSql(parameter);

		String sql = boundSql.getSql().replaceAll("\\s{2,}", " ").trim();
		sql += " limit " + offset + ", " + limit; 
		offset = RowBounds.NO_ROW_OFFSET;
		limit = RowBounds.NO_ROW_LIMIT;

		queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
		
		
		// When new BoundSql(), a new HashMap will be set to additional parameters,
		// while these informations are used to locate anonymous variables.
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		// Set additional parameters.
		// This is **REQUIRED** for Collections (list, map, set)
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		
		MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql), false);
		queryArgs[MAPPED_STATEMENT_INDEX] = newMs;

		logger.debug("==>" + sql);

	}
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

	protected MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource, boolean isCount) {
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		String[] s = ms.getKeyProperties();
		if (s == null) {
			builder.keyProperty(null);
		} else {
			builder.keyProperty(s[0]);
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		if (isCount) {
			List<ResultMap> resultMaps = new ArrayList<ResultMap>();
			resultMaps.add(new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Integer.class, new ArrayList<ResultMapping>()).build());
			builder.resultMaps(resultMaps);
		} else {
			builder.resultMaps(ms.getResultMaps());
		}
		builder.cache(ms.getCache());
		MappedStatement newMs = builder.build();
		return newMs;
	}

	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

}
