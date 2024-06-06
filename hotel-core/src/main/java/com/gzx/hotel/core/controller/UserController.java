package com.gzx.hotel.core.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzx.hotel.base.controller.BaseController;
import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.core.constant.Role;
import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.po.Room;
import com.gzx.hotel.core.po.User;
import com.gzx.hotel.core.po.UserRole;
import com.gzx.hotel.core.pojo.LoginUser;
import com.gzx.hotel.core.service.RecordService;
import com.gzx.hotel.core.service.RoomService;
import com.gzx.hotel.core.service.UserRoleService;
import com.gzx.hotel.core.service.UserService;
import com.gzx.hotel.core.service.impl.UserServiceImpl;
import com.gzx.hotel.core.utils.CommonUtil;
import com.gzx.hotel.core.vo.CheckinVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Preconditions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController<UserService, User> {


    @Resource
    BCryptPasswordEncoder encoder;

    @Resource
    RecordService recordService;

    @Resource
    RoomService roomService;

    @Resource
    UserRoleService urService;

    @PostMapping("/checkin")
    public ResponseBean checkin(CheckinVo checkinVo) {
        Room room = roomService.getById(checkinVo.getRoomId());
        if (room.getUse() == 1) {
            return ResponseBean.error("操作失败！当前房间正在被使用！");
        }

        room.setUse(1);
        try {
            // 1. 将房间设置为正在被使用
            if (!roomService.update(room, Wrappers.<Room>update().eq("id_", checkinVo.getRoomId()))) {
                return ResponseBean.error("操作失败！");
            }

            // 2. 生成一个房间使用者账号和record记录
            Record record = new Record(checkinVo.getRoomId(), new Date(), null, 0);
            recordService.save(record);

            String password = CommonUtil.randomSeq(6);
            User user = new User(record.getId(), CommonUtil.randomNum(6), encoder.encode(password), checkinVo.getUserName(), null, 0, new Date());
            service.save(user);

            urService.save(new UserRole(user.getId(), Role.CUSTOMER));

            user.setPassword(password);
            return ResponseBean.ok().data("account", user)
                    .data("room", room);
//                    .data("record", record);
        } catch (Exception e) {
            log.error("checkin时发生错误， {}", e.getMessage());
            return ResponseBean.error();
        }
    }

    @PostMapping("/checkout")
    public ResponseBean checkout(Integer id) {
        try {
            Record record = recordService.getById(id);
            Preconditions.checkNotNull(record, "记录不存在");
            service.update(Wrappers.<User>update().eq("record_id", record.getId()).set("locked", 1));

            record.setEndTime(new Date());
            record.setComplete(1);
            recordService.update(record, Wrappers.<Record>update().eq("id_", id));

            roomService.update(Wrappers.<Room>update().eq("id_", record.getRoomId()).set("`use`", 0));
            return ResponseBean.ok().data(record);
        } catch (Exception e) {
            log.error("checkout时发生错误， {}", e.getMessage());
            return ResponseBean.error();
        }
    }

    @PostMapping("/login")
    public ResponseBean loginByUsernamePassword(User user) {
        try {
            LoginUser loginUser = service.login(user.getUsername(), user.getPassword());
            String token = ((UserServiceImpl) service).generateToken(loginUser);
            return ResponseBean.ok().data("token", token)
                    .data("role", loginUser.getAuthorities());
        } catch (Exception e) {
            log.error("login时发生错误， {}", e.getMessage());
            return ResponseBean.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseBean logout() {
        try {
            if (service.logout()) {
                return ResponseBean.ok();
            }
            return ResponseBean.error();
        } catch (Exception e) {
            log.error("logout时发生错误， {}", e.getMessage());
            return ResponseBean.error();
        }
    }
}
