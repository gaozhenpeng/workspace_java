package com.at.springboot.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.springboot.mybatis.dto.UuidUserDto;
import com.at.springboot.web.svr.UuidUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UuidUserController {
    @Autowired
    private UuidUserService uuidUserService;

    @RequestMapping(method=RequestMethod.POST)
    public UuidUserDto create(@RequestBody UuidUserDto UuidUserDto
            ) {
        log.info("create uuidUser, UuidUserDto: '{}'", UuidUserDto);
        
        UuidUserDto UuidUserDtoResult = 
                uuidUserService.create(UuidUserDto.getName());
        return UuidUserDtoResult;
    }


    @RequestMapping(method=RequestMethod.GET)
    public List<UuidUserDto> list(
            @RequestParam(name="cid", required=false) String cid
            ,@RequestParam(name="pageNo", required=false) Integer pageNo
            ,@RequestParam(name="pageSize", required=false) Integer pageSize
            ) {
        log.info("list uuidUser, cid: '{}', pageNo: '{}', pageSize: '{}' ", cid, pageNo, pageSize);
        
        List<UuidUserDto> UuidUserDtos = null;
        if(pageNo == null || pageSize == null) {
            UuidUserDtos = uuidUserService.list(cid);
        }else {
            UuidUserDtos = uuidUserService.list(cid, pageNo, pageSize);
        }
        return UuidUserDtos;
    }
}
