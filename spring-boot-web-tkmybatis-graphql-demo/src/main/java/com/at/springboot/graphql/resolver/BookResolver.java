package com.at.springboot.graphql.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.mapper.AuthorMapper;
import com.at.springboot.mybatis.vo.Author;
import com.at.springboot.mybatis.vo.Book;
import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;

@Component
@AllArgsConstructor
public class BookResolver implements GraphQLResolver<Book> {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public Author getAuthor(Book book) {

        com.at.springboot.mybatis.po.Author author = authorMapper.selectByPrimaryKey(book.getAuthorId());
        
        return mapperFacade.map(author, Author.class);
    }
}
