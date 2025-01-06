package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-06  11:02
 * @Description: TODO
 * @Version: 1.0
 */

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Autowired
    private ApUserMapper apUserMapper;

    @Override
    public ResponseResult login(LoginDto dto) {
        /// 1.正常登录 需要用户名以及密码
        if (StringUtils.isNoneBlank(dto.getPhone()) && StringUtils.isNoneBlank(dto.getPassword())){
            // 1.1 查询用户信息，根据手机号查询
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if (dbUser == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }
            // 1.2 对比密码
            //获得SALT规则
            String salt = dbUser.getSalt();
            String password = dto.getPassword();
            //将上面两个获得的信息进行加密
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            //与数据库中的密码进行比对，发现不一样时弹出错误信息
            if (!pswd.equals(dbUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            // 1.3 返回数据 jwt生成
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            //使用map进行封装，方便进行返回
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            //将salt设置为空
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user",dbUser);
            return ResponseResult.okResult(map);
        }else {
            //2.游客登录 无需用户名以及密码
            //直接返回token
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}
