package com.piaomiao.web;

import com.piaomiao.command.AuthCommand;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.ChangePasswordVO;
import com.piaomiao.web.vo.LoginResultVO;
import com.piaomiao.web.vo.LoginVO;
import com.piaomiao.web.vo.OperationResultVO;
import com.piaomiao.web.vo.RegisterResultVO;
import com.piaomiao.web.vo.RegisterVO;
import com.piaomiao.web.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private AuthCommand authCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 是否允许公开注册
     */
    @Value("${auth.register-enabled:false}")
    private boolean registerEnabled;

    @PostMapping("/login")
    public Response<LoginResultVO> login(@RequestBody LoginVO loginVO) {
        return templateRest.request(new CallbackRest<LoginResultVO>() {
            @Override
            public void check() {
                if (loginVO == null) {
                    throw new IllegalArgumentException("登录请求不能为空");
                }
                if (loginVO.getUsername() == null || loginVO.getUsername().isEmpty()) {
                    throw new IllegalArgumentException("用户名不能为空");
                }
                if (loginVO.getPassword() == null || loginVO.getPassword().isEmpty()) {
                    throw new IllegalArgumentException("密码不能为空");
                }
            }

            @Override
            public LoginResultVO execute() {
                Map<String, Object> result = authCommand.login(LoginVO.toDTO(loginVO));
                LoginResultVO vo = new LoginResultVO();
                vo.setSuccess((Boolean) result.get("success"));
                vo.setMessage((String) result.get("message"));
                vo.setToken((String) result.get("token"));
                if (result.get("user") instanceof UserModel) {
                    UserModel user = (UserModel) result.get("user");
                    vo.setUsername(user.getUsername());
                    vo.setNickname(user.getNickname());
                }
                return vo;
            }
        }, true);
    }

    @PostMapping("/register")
    public Response<RegisterResultVO> register(@RequestBody RegisterVO registerVO) {
        return templateRest.request(new CallbackRest<RegisterResultVO>() {
            @Override
            public void check() {
                if (!registerEnabled) {
                    throw new IllegalStateException("公开注册已关闭");
                }
                if (registerVO == null) {
                    throw new IllegalArgumentException("注册请求不能为空");
                }
                if (registerVO.getUsername() == null || registerVO.getUsername().isEmpty()) {
                    throw new IllegalArgumentException("用户名不能为空");
                }
                if (registerVO.getPassword() == null || registerVO.getPassword().isEmpty()) {
                    throw new IllegalArgumentException("密码不能为空");
                }
            }

            @Override
            public RegisterResultVO execute() {
                Map<String, Object> result = authCommand.register(RegisterVO.toDTO(registerVO));
                RegisterResultVO vo = new RegisterResultVO();
                vo.setSuccess((Boolean) result.get("success"));
                vo.setMessage((String) result.get("message"));
                vo.setToken((String) result.get("token"));
                vo.setUsername((String) result.get("username"));
                return vo;
            }
        }, true);
    }

    @PostMapping("/changePassword")
    public Response<OperationResultVO> changePassword(@RequestBody ChangePasswordVO changePasswordVO) {
        return templateRest.request(new CallbackRest<OperationResultVO>() {
            @Override
            public void check() {
                if (changePasswordVO == null) {
                    throw new IllegalArgumentException("修改密码请求不能为空");
                }
                if (changePasswordVO.getPassword() == null || changePasswordVO.getPassword().isEmpty()) {
                    throw new IllegalArgumentException("新密码不能为空");
                }
            }

            @Override
            public OperationResultVO execute() {
                Map<String, Object> result = authCommand.changePassword(ChangePasswordVO.toDTO(changePasswordVO));
                return OperationResultVO.success((String) result.get("message"));
            }
        }, true);
    }

    @GetMapping("/user/info")
    public Response<UserVO> getUserInfo() {
        return templateRest.request(new CallbackRest<UserVO>() {
            @Override
            public void check() {
            }

            @Override
            public UserVO execute() {
                return UserVO.fromModel(authCommand.getCurrentUser());
            }
        });
    }

    @PostMapping("/logout")
    public Response<OperationResultVO> logout() {
        return templateRest.request(new CallbackRest<OperationResultVO>() {
            @Override
            public void check() {
            }

            @Override
            public OperationResultVO execute() {
                SecurityContextHolder.clearContext();
                return OperationResultVO.success("登出成功");
            }
        }, true);
    }
}
