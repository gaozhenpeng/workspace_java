package com.at.springboot.graphql.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.dto.AuthorDto;
import com.at.springboot.mybatis.dto.BookDto;
import com.at.springboot.mybatis.mapper.AuthorMapper;
import com.at.springboot.mybatis.po.Author;
import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;

@Component
@AllArgsConstructor
public class BookResolver implements GraphQLResolver<BookDto> {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public AuthorDto getAuthor(BookDto book) {

        Author author = authorMapper.selectByPrimaryKey(book.getAuthorId());
        
        return mapperFacade.map(author, AuthorDto.class);
    }
}
