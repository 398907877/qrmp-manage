package com.yanyan.web.controller.user;

import com.yanyan.core.util.RSAUtil;
import com.yanyan.data.vo.ProfileModel;
import com.yanyan.service.system.StaffService;
import com.yanyan.core.util.PasswordUtils;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.Configs;
import com.yanyan.service.system.RSAService;
import com.yanyan.core.web.DataResponse;
import com.yanyan.web.SessionUtils;
import com.yanyan.data.vo.PasswordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.interfaces.RSAPrivateKey;

/**
 * 用户账户
 * User: Saintcy
 * Date: 2016/5/27
 * Time: 17:49
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private RSAService rsaService;
    @Autowired
    private StaffService staffService;

    @RequestMapping("/profile")
    public String profile(Model model) {
        Staff staff = staffService.getStaff(SessionUtils.getStaffId());
        staff.setPassword("");
        staff.setSalt("");
        model.addAttribute("staff", staff);

        return "/user/profile";
    }

    @RequestMapping("/update_profile")
    @ResponseBody
    public DataResponse updateProfile(@RequestBody ProfileModel profile) {
        try {
            Staff staff = staffService.getStaff(profile.getId());
            BeanUtils.copyProperties(profile, staff);
            staffService.updateStaff(staff);
            return DataResponse.success();
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/password")
    public String password(Model model) {
        model.addAttribute("publicKey", rsaService.getPublicKey());
        model.addAttribute("staffId", SessionUtils.getStaffId());
        model.addAttribute("staffName", SessionUtils.getStaffName());

        return "/user/password";
    }

    @RequestMapping("/change_password")
    @ResponseBody
    public DataResponse changePassword(@RequestBody PasswordModel password) {
        try {
            com.yanyan.data.domain.system.RSAPrivateKey privateKey = rsaService.getPrivateKey();
            java.security.interfaces.RSAPrivateKey pk = RSAUtil.generateRSAPrivateKeyFromHex(privateKey.getModulus(), privateKey.getPrivateExponent());
            String plainNewPassword = PasswordUtils.decryptPasswordWithRSA(pk, password.getNew_password());
            String plainOldPassword = PasswordUtils.decryptPasswordWithRSA(pk, password.getOld_password());

            staffService.changeStaffPassword(password.getStaff_id(), plainNewPassword, plainOldPassword);
            return DataResponse.success();
        } catch (Exception e) {
            return DataResponse.failure(e.getMessage());
        }
    }
}
