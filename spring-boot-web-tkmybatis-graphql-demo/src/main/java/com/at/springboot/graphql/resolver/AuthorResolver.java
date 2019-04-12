package com.at.springboot.graphql.resolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.dto.AuthorDto;
import com.at.springboot.mybatis.dto.BookDto;
import com.at.springboot.mybatis.mapper.BookMapper;
import com.at.springboot.mybatis.po.Book;
import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Component
@AllArgsConstructor
public class AuthorResolver implements GraphQLResolver<AuthorDto> {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MapperFacade mapperFacade;

    public String getCreatedTime(AuthorDto author) {
        String ctime = 
                Optional.ofNullable(author)
                    .map(AuthorDto::getCreatedTime)
                    .map((c)->new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(c))
                    .orElse(null);
        log.debug("ctime: '{}'", ctime);
        return ctime;
    }

    public List<BookDto> getBooks(AuthorDto author) {
        log.info("getBooks, author: '{}'", author);
        if(author == null || author.getId() == null) {
            return new ArrayList<>();
        }
            

        Example bookExample = new Example(Book.class);
        bookExample.setOrderByClause(Book.TITLE + " asc");
        
        bookExample
            .or()
                .andEqualTo(Book.AUTHOR_ID, author.getId())
                ;
        
        List<Book> books = bookMapper.selectByExample(bookExample);
        return mapperFacade.mapAsList(books, BookDto.class);
    }
}
