package com.at.springboot.graphql.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.dto.AuthorDto;
import com.at.springboot.mybatis.dto.BookDto;
import com.at.springboot.mybatis.mapper.AuthorMapper;
import com.at.springboot.mybatis.mapper.BookMapper;
import com.at.springboot.mybatis.po.Author;
import com.at.springboot.mybatis.po.Book;
import com.at.springboot.mybatis.vo.BookReq;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

/**
 * POST http://localhost:8080/demo/gql
 * Content-Type: application/graphql
 * Request Body:
 *     mutation {
 *       newAuthor( firstName : "fn1", lastName : "fn1"){
 *         id,createdTime,firstName,lastName,books
 *       }
 *     }
 * 
 */
@Slf4j
@Component
@AllArgsConstructor
public class Mutation implements GraphQLMutationResolver {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public AuthorDto newAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        authorMapper.insertSelective(author);
        author.getId();
        log.debug("author: '{}'", author);
        
        return mapperFacade.map(author, AuthorDto.class);
    }

    public BookDto newBook(String title, String isbn, int pageCount, Long authorId) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount);
        book.setAuthorId(authorId);
        bookMapper.insertSelective(book);
        log.debug("book: '{}'", book);
        
        return mapperFacade.map(book, BookDto.class);
    }


    public BookDto saveBook(BookReq bookReq) {
        return newBook(bookReq.getTitle(), bookReq.getIsbn(), bookReq.getPageCount(), bookReq.getAuthorId());
    }

    public Boolean deleteBook(Long id) {
        int affectedRows = bookMapper.deleteByPrimaryKey(id);
        return affectedRows == 1;
    }

    public BookDto updateBookPageCount(int pageCount,long id) {
        Book book = new Book();
        book.setId(id);
        book.setPageCount(pageCount);
        int affectedRows = bookMapper.updateByPrimaryKeySelective(book);
        log.info("affectedRows: '{}'", affectedRows);
        
        return mapperFacade.map(book, BookDto.class);
    }

}
