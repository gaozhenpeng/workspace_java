package com.at.springboot.graphql.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.mapper.AuthorMapper;
import com.at.springboot.mybatis.mapper.BookMapper;
import com.at.springboot.mybatis.vo.Author;
import com.at.springboot.mybatis.vo.Book;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;

/**
 * <pre><code>
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
 * </code></pre>
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

    public Author findAuthorById(Long id) {
        com.at.springboot.mybatis.po.Author author = authorMapper.selectByPrimaryKey(id);
        
        return mapperFacade.map(author, Author.class);
    }

    public List<Author> findAllAuthors() {
        List<com.at.springboot.mybatis.po.Author> authors = authorMapper.selectAll();
        
        return mapperFacade.mapAsList(authors, Author.class);
    }

    public Long countAuthors() {
        com.at.springboot.mybatis.po.Author author = new com.at.springboot.mybatis.po.Author();
        int cnt = authorMapper.selectCount(author);
        return Long.valueOf(cnt);
    }

    public List<Book> findAllBooks() {
        List<com.at.springboot.mybatis.po.Book> books = bookMapper.selectAll();
        
        return mapperFacade.mapAsList(books, Book.class);
    }

    public Long countBooks() {
        com.at.springboot.mybatis.po.Book book = new com.at.springboot.mybatis.po.Book();
        int cnt = bookMapper.selectCount(book);
        return Long.valueOf(cnt);
    }
}
