package zxm.boot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxm.boot.SimpleService;
import zxm.boot.dto.UserDto;
import zxm.boot.model.User;
import zxm.boot.model.Users;
import zxm.boot.translate.UserTranslateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JunXiong
 * @Date 2017/3/3
 */
@Service
public class UserService implements SimpleService<User,UserDto> {

    @Autowired
    UserTranslateService userTranslateService;

//    @Resource(name = "usersRepository")
    @Autowired
    UsersRepository usersRepository;

    @Override
    public int addEntity(User user) {
        return 0;
    }

    @Override
    public int delEntity(Integer id) {
        return 0;
    }

    @Override
    public int delEntity(Map<String, Object> param) {
        return 0;
    }

    @Override
    public int updateEntity(User user) {
        return 0;
    }

    @Override
    public UserDto findEntity(Integer id) {
        User u = new User();
        u.setId(1);
        u.setAge(1);
        u.setName("张峻熊");
        u.setRemark("描述1");
        return userTranslateService.toUserDto(u);
    }

    @Override
    public List<UserDto> findEntity(Map<String, Object> param) {
        User u = new User();
        u.setId(2);
        u.setAge(2);
        u.setName("张峻熊2");
        u.setRemark("描述1");

        User u1 = new User();
        u1.setId(3);
        u1.setAge(3);
        u1.setName("张峻熊3");
        u1.setRemark("描述2");

        User u2 = new User();
        u2.setId(1);
        u2.setAge(1);
        u2.setName("张峻熊");
        u2.setRemark("描述3");

        List<User> list = new ArrayList<>();
        list.add(u);
        list.add(u1);
        list.add(u2);
        return userTranslateService.toUserDtoList(list);
    }

    public Users findUser(Long id){
        return usersRepository.findOne(id);
    }
}
