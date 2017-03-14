package zxm.boot.user;

import org.springframework.data.repository.CrudRepository;
import zxm.boot.model.Users;

/**
 * @author JunXiong
 * @Date 2017/3/9
 */
public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findByEmail(String email);
}
