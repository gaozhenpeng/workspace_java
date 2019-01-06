package com.at.springboot.web.ctrl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.web.ctrl.BlogController1;
import com.at.springboot.web.svr1.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogController1.class)
@Slf4j
public class BlogController1MockIntTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean(name="blogService1")
    private BlogService blogService;
    
    private List<BlogDto> blogDtoMockList = null;
    @Before
    public void setUp() {
        blogDtoMockList = new ArrayList<>();
        for(int i = 0 ; i < 4 ; i++) {
            BlogDto blogDto = new BlogDto();
            blogDto.setBlogId(new Long(i).toString());
            blogDto.setContent("content" +i);
            blogDto.setCreatedDatetime("2018-08-08 11:22:33");
            blogDto.setName("name" +i);
            blogDto.setUpdatedDatetime("2018-08-08 11:22:33");
            blogDtoMockList.add(blogDto);
        }
    }
    @Test
    public void testListAll() throws Exception {
        when(blogService.list(null)).thenReturn(blogDtoMockList);
        String blogsStr = mockMvc
                            .perform(get("/blog1"))
                            .andDo(print())
                            .andExpect(status().is2xxSuccessful())
                            .andReturn().getResponse().getContentAsString()
                            ;
        log.info("blogsStr: '{}'", blogsStr);
        assertNotNull("blogsStr is null", blogsStr);
        BlogDto[] blogDtos = new ObjectMapper().readValue(blogsStr, BlogDto[].class);
        assertNotNull("blogDtos is null", blogDtos);
        for(int i = 0 ; i < blogDtos.length ; i++) {
            BlogDto blogDto = blogDtos[i];
            assertNotNull("blogId is null, '"+blogDto+"'", blogDto.getBlogId());
        }
    }


}
