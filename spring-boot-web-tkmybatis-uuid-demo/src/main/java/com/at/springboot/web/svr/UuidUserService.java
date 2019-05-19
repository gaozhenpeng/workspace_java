package com.at.springboot.web.svr;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.at.springboot.mybatis.dto.UuidUserDto;
import com.at.springboot.mybatis.mapper.UuidUserMapper;
import com.at.springboot.mybatis.po.UuidUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class UuidUserService {
    @Autowired
    private UuidUserMapper uuidUserMapper;
    @Autowired
    private MapperFacade mapperFacade;

    /** 
     * create a new uuiduser
     * @param userName
     * @return
     */
//    @Transactional
    public UuidUserDto create(String userName){
        log.info("create, userName: '{}' ", userName);
        String cid = UUID.randomUUID().toString();
        byte[] bid;
        try {
            bid = Hex.decodeHex(cid.replaceAll("-", ""));
        } catch (DecoderException e) {
            log.error("Hex.decodeHex('{}'.replaceAll(\"-\", \"\")) failed.", cid);
            throw new RuntimeException(e);
        }
        
        
        UuidUser uuidUser = new UuidUser();
        uuidUser.setName(userName);
        uuidUser.setCid(cid);
        uuidUser.setBid(bid);
        
        
        int affectedRows = uuidUserMapper.insertSelective(uuidUser);
        if(affectedRows <= 0) {
            String msg = String.format("Not able to insert record, uuidUser: '%s'", uuidUser.toString());
            throw new RuntimeException(msg);
        }
        // for testing
        if(!StringUtils.isEmpty(userName) && userName.contains("throw")) {
            throw new RuntimeException(userName);
        }
        
        return mapperFacade.map(uuidUser, UuidUserDto.class);
    }

    /**
     * list uuidUsers, if uuidUserId is not null, show the specific jsonUser
     * @param uuidUserId
     * @return
     */
    public List<UuidUserDto> list(String uuidUserId) {
        log.info("list, uuidUserId: '{}' ", uuidUserId);
        
        Example uuidUserExample = new Example(UuidUser.class);
        // NOTE: UuidUser.DB_NAME
        uuidUserExample.setOrderByClause(UuidUser.DB_NAME + " asc");
        
        if(uuidUserId != null) {
            uuidUserExample
                .or()
                    // NOTE: UuidUser.CID, not UuidUser.DB_CID
                    .andEqualTo(UuidUser.CID, uuidUserId)
                    ;
        }
        List<UuidUser> uuidUsers = uuidUserMapper.selectByExample(uuidUserExample);

        return mapperFacade.mapAsList(uuidUsers, UuidUserDto.class);
    }

    /**
     * list jsonUsers pagingly, if jsonUserId is not null, show the specific jsonUser
     * @param uuidUserId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<UuidUserDto> list(String uuidUserId, Integer pageNo, Integer pageSize) {
        log.info("list, uuidUserId: '{}', pageNo: '{}', pageSize: '{}' ", uuidUserId, pageNo, pageSize);

        Example uuidUserExample = new Example(UuidUser.class);
        uuidUserExample.setOrderByClause(UuidUser.DB_NAME + " asc");
        
        
        if(uuidUserId != null) {
            uuidUserExample
                .or()
                    .andEqualTo(UuidUser.CID, uuidUserId)
                    ;
        }

        PageHelper.startPage(pageNo, pageSize);
        List<UuidUser> uuidUsers = uuidUserMapper.selectByExample(uuidUserExample);
        log.info(uuidUsers == null ? "uuidUsers is null" : "uuidUsers: " + uuidUsers.toString());
        PageInfo<UuidUser> pageInfo = new PageInfo<>(uuidUsers);
        log.info("pageInfo: '{}'", pageInfo);

        return mapperFacade.mapAsList(uuidUsers, UuidUserDto.class);
    }

}
