package com.at.springboot.graphql.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.mapper.BookMapper;
import com.at.springboot.mybatis.vo.Author;
import com.at.springboot.mybatis.vo.Book;
import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Component
@AllArgsConstructor
public class AuthorResolver implements GraphQLResolver<Author> {
    
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public String getCreatedTime(Author author) {
        String ctime = 
                Optional.ofNullable(author)
                    .map(Author::getCreatedTime)
                    .orElse(null);
        log.debug("ctime: '{}'", ctime);
        return ctime;
    }

    public List<Book> getBooks(Author author) {
        log.info("getBooks, author: '{}'", author);
        if(author == null || author.getId() == null) {
            return new ArrayList<>();
        }
            

        Example bookExample = new Example(Book.class);
        bookExample.setOrderByClause(com.at.springboot.mybatis.po.Book.TITLE + " asc");
        
        bookExample
            .or()
                .andEqualTo(com.at.springboot.mybatis.po.Book.AUTHOR_ID, author.getId())
                ;
        
        List<com.at.springboot.mybatis.po.Book> books = bookMapper.selectByExample(bookExample);
        return mapperFacade.mapAsList(books, Book.class);
    }
}
