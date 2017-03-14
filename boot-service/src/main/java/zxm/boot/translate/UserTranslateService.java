package zxm.boot.translate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import zxm.boot.dto.UserDto;
import zxm.boot.model.User;

import java.util.List;

/**
 * @author JunXiong
 * @Date 2017/3/6
 */
@Mapper(componentModel = "spring")
public interface UserTranslateService {

    @Mappings({
       @Mapping(source = "id", target = "userId"),
       @Mapping(source = "age", target = "userAge"),
       @Mapping(source = "name", target = "userName"),
       @Mapping(source = "remark", target = "details")
    })
    UserDto toUserDto(User s);

    List<UserDto> toUserDtoList(List<User> s);
}
