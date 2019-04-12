package com.at.springboot.graphql.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.dto.AuthorDto;
import com.at.springboot.mybatis.dto.BookDto;
import com.at.springboot.mybatis.mapper.AuthorMapper;
import com.at.springboot.mybatis.mapper.BookMapper;
import com.at.springboot.mybatis.po.Author;
import com.at.springboot.mybatis.po.Book;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;

/**
 * Query Schema:
 *     POST http://localhost:8080/demo/gql
 *     Content-Type: application/graphql
 *     Request Body:
 *         query {
 *           __schema {
 *             queryType {
 *               name,
 *               fields {
 *                 name
 *               }
 *             }
 *           } 
 *         }
 *         
 * Query the counting of authors:
 *     POST http://localhost:8080/demo/gql
 *     Content-Type: application/graphql
 *     Request Body:
 *         query {
 *           findAuthorById(id : 1 ){
 *             id,
 *             createdTime,
 *             firstName,
 *             lastName,
 *             books {
 *               id,title,isbn,pageCount
 *             }
 *           }
 *         }
 * 
 */
@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public AuthorDto findAuthorById(Long id) {
        Author author = authorMapper.selectByPrimaryKey(id);
        
        return mapperFacade.map(author, AuthorDto.class);
    }

    public List<AuthorDto> findAllAuthors() {
        List<Author> authors = authorMapper.selectAll();
        
        return mapperFacade.mapAsList(authors, AuthorDto.class);
    }

    public Long countAuthors() {
        Author author = new Author();
        int cnt = authorMapper.selectCount(author);
        return Long.valueOf(cnt);
    }

    public List<BookDto> findAllBooks() {
        List<Book> books = bookMapper.selectAll();
        
        return mapperFacade.mapAsList(books, BookDto.class);
    }

    public Long countBooks() {
        Book book = new Book();
        int cnt = bookMapper.selectCount(book);
        return Long.valueOf(cnt);
    }
}
