package zxm.boot.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxm.boot.dto.UserDto;
import zxm.boot.model.Users;
import zxm.boot.user.UserService;

import java.util.HashMap;
import java.util.List;

/**
 * @author JunXiong
 * @Date 2017/3/3
 */
@RestController
@RequestMapping("/remote/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(){
        return "welcome to user index page !";
    }

    @RequestMapping("/{id}")
    public UserDto findEntity(@PathVariable Integer id){
        return userService.findEntity(id);
    }

    @RequestMapping("/u/{id}")
    public Users findUser(@PathVariable Long id){
        return userService.findUser(id);
    }

    @RequestMapping("/list")
    public List<UserDto> findEntityList(){
        return userService.findEntity(new HashMap<String, Object>());
    }
}
