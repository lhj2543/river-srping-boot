package com.river.interfaces.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.river.api.dto.system.SysUserDto;
import com.river.api.entity.system.SysUser;
import com.river.common.core.util.Result;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author river
 * @since 2020-09-01
 */
public interface ILoginSysUserService extends IService<SysUser> {


    /**
     * 获取登录用户信息
     * @param param
     * @return
     */
    Result getLoginUser(SysUserDto param);


}
