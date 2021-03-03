package com.river.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.river.api.dto.system.SysUserDto;
import com.river.api.entity.system.SysMenu;
import com.river.api.entity.system.SysUser;
import com.river.common.core.util.Result;
import com.river.interfaces.system.ILoginSysUserService;
import com.river.system.mapper.SysUserMapper;
import com.river.system.service.ISysMenuService;
import com.river.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author river
 * @since 2020-09-01
 */
@Slf4j
@Service
public class LoginSysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ILoginSysUserService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 获取用户登录信息
     * @param param
     * @return
     */
    @Override
    public Result getLoginUser(SysUserDto param){

        try {
            log.info("获取用户登录信息,loginId="+param.getUserCd());
            if(StringUtils.isBlank(param.getUserCd())){
                return Result.failed("用户名不能为空！");
            }

            param.setRoleStatus("1");
            SysUserDto user = sysUserService.findSysUserLogin(param);

            if(user == null){
                return Result.failed("用户不存在！");
            }

            if(!user.getUserRoles().isEmpty()){
                //获取用户资源权限
                List<SysMenu> sysUserMenu = sysMenuService.findSysUserMenu(user);
                user.setSysUserMenu(sysUserMenu);
            }

            return Result.ok(user);

        }catch (Exception e){
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }


    }

}
