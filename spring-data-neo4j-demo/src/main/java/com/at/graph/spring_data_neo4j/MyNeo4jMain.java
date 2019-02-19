package com.at.graph.spring_data_neo4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.at.graph.spring_data_neo4j.domain.ActedIn;
import com.at.graph.spring_data_neo4j.domain.Movie;
import com.at.graph.spring_data_neo4j.repository.ActedInRepository;
import com.at.graph.spring_data_neo4j.repository.MovieRepository;

public class MyNeo4jMain {
	private static final Logger logger = LoggerFactory.getLogger(MyNeo4jMain.class);

	public static void main(String[] args) throws IOException {
		logger.debug("Enterring Main.");

		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyNeo4jConfiguration.class);
		MovieRepository movieRepository = context.getBean(MovieRepository.class);
		
		logger.info("MovieRepository.findAll(): ");
		Iterable<Movie> movies = movieRepository.findAll();
		movies.forEach((movie) -> {
			logger.info("title: " + movie.getTitle() + "; released: " + movie.getReleased());
		});
		logger.info("");
		
		
		logger.info("MovieRepository.findByTitle('A Few Good Men'): ");
		Movie movie = movieRepository.findByTitle("A Few Good Men");
		if(movie != null){
			logger.info("title: " + movie.getTitle() + "; released: " + movie.getReleased());
		}
		logger.info("");
		
		logger.info("MovieRepository.findByTitle('A Few Good Men'): ");
		List<Map<String,Object>> movieGraph = movieRepository.graph(1000);
		if(movieGraph != null){
			for(Map<String,Object> mg: movieGraph){
				StringBuilder sb = new StringBuilder();
				
				String movieTitle = (String)mg.get("movie");
				if(movieTitle == null){
					continue;
				}
				sb.append("title: '");
				sb.append(movieTitle);
				sb.append("' ;");

				sb.append(" casts: ");
				Object obj = mg.get("cast");
				if(obj != null){
					String[] movieCasts = (String[]) obj;
					for(String movieCast : movieCasts){
						sb.append(movieCast).append(",");
					}
					sb.deleteCharAt(sb.length()-1);
				}
				sb.append(";");
				logger.info(sb.toString());
			}
		}
		logger.info("");
		
		ActedInRepository actedInRepository = context.getBean(ActedInRepository.class);
		Iterable<ActedIn> actedIns = actedInRepository.findAll();
		if(actedIns != null){
			for(ActedIn actedIn : actedIns){
				logger.info("\tmovie:" + actedIn.getMovie().getTitle());
				logger.info("\tperson:" + actedIn.getPerson().getName());
				logger.info("\troles:");
				actedIn.getRoles().forEach((role) -> {logger.info("\t\t" + role);});
			}
		}
	}
}
