package com.at.springboot.web.svr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.at.springboot.mybatis.dto.JsonUserDto;
import com.at.springboot.mybatis.mapper.JsonUserMapper;
import com.at.springboot.mybatis.po.JsonUser;
import com.at.springboot.mybatis.po.JsonUserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@Service
public class JsonUserService {
    @Autowired
    private JsonUserMapper jsonUserMapper;
    @Autowired
    private MapperFacade mapperFacade;

    /** 
     * create a new jsonuser
     * @param userName
     * @param lastLoginInfo
     * @return
     */
//    @Transactional
    public JsonUserDto create(String userName, String lastLoginInfo) {
        log.info("create, userName: '{}', last_login_info: '{}' ", userName, lastLoginInfo);
        
        JsonUser jsonUser = new JsonUser();
        jsonUser.setUserName(userName);
        jsonUser.setLastLoginInfo(lastLoginInfo);
        
        
        int affectedRows = jsonUserMapper.insertSelective(jsonUser);
        if(affectedRows <= 0) {
            String msg = String.format("Not able to insert record, jsonUser: '%s'", jsonUser.toString());
            throw new RuntimeException(msg);
        }
        // for testing
        if(!StringUtils.isEmpty(userName) && userName.contains("throw")) {
            throw new RuntimeException(userName);
        }
        
        return mapperFacade.map(jsonUser, JsonUserDto.class);
    }

    /**
     * list jsonUsers, if jsonUserId is not null, show the specific jsonUser
     * @param jsonUserId
     * @return
     */
    public List<JsonUserDto> list(Integer jsonUserId) {
        log.info("list, jsonUserId: '{}' ", jsonUserId);
        
        JsonUserExample jsonUserExample = new JsonUserExample();
        jsonUserExample.setOrderByClause("user_name asc");
        
        if(jsonUserId != null) {
            jsonUserExample
                .or()
                    .andIdEqualTo(jsonUserId)
                    ;
        }

        List<JsonUser> jsonUsers = jsonUserMapper.selectByExampleWithBLOBs(jsonUserExample);

        return mapperFacade.mapAsList(jsonUsers, JsonUserDto.class);
    }

    /**
     * list jsonUsers pagingly, if jsonUserId is not null, show the specific jsonUser
     * @param jsonUserId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<JsonUserDto> list(Integer jsonUserId, Integer pageNo, Integer pageSize) {
        log.info("list, jsonUserId: '{}', pageNo: '{}', pageSize: '{}' ", jsonUserId, pageNo, pageSize);
        
        JsonUserExample jsonUserExample = new JsonUserExample();
        jsonUserExample.setOrderByClause("user_name asc");
        
        if(jsonUserId != null) {
            jsonUserExample
                .or()
                    .andIdEqualTo(jsonUserId);
        }

        PageHelper.startPage(pageNo, pageSize);
        List<JsonUser> jsonUsers = jsonUserMapper.selectByExampleWithBLOBs(jsonUserExample);
        log.info(jsonUsers == null ? "jsonUsers is null" : "jsonUser: " + jsonUsers.toString());
        PageInfo<JsonUser> pageInfo = new PageInfo<>(jsonUsers);
        log.info("pageInfo: '{}'", pageInfo);

        return mapperFacade.mapAsList(jsonUsers, JsonUserDto.class);
    }

}
