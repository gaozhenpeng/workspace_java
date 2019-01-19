package com.at.springboot.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.springboot.mybatis.dto.JsonUserDto;
import com.at.springboot.web.svr.JsonUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class JsonUserController {
    @Autowired
    private JsonUserService jsonUserService;

    @RequestMapping(method=RequestMethod.POST)
    public JsonUserDto create(@RequestBody JsonUserDto jsonUserDto
            ) {
        log.info("create jsonUser, jsonUserDto: '{}'", jsonUserDto);
        
        JsonUserDto jsonUserDtoResult = jsonUserService.create(jsonUserDto.getUserName(), jsonUserDto.getLastLoginInfo());
        return jsonUserDtoResult;
    }


    @RequestMapping(method=RequestMethod.GET)
    public List<JsonUserDto> list(
            @RequestParam(name="id", required=false) Integer jsonUserId
            ,@RequestParam(name="pageNo", required=false) Integer pageNo
            ,@RequestParam(name="pageSize", required=false) Integer pageSize
            ) {
        log.info("list jsonuser, jsonUserId: '{}', pageNo: '{}', pageSize: '{}' ", jsonUserId, pageNo, pageSize);
        
        List<JsonUserDto> jsonUserDtos = null;
        if(pageNo == null || pageSize == null) {
            jsonUserDtos = jsonUserService.list(jsonUserId);
        }else {
            jsonUserDtos = jsonUserService.list(jsonUserId, pageNo, pageSize);
        }
        return jsonUserDtos;
    }
}
