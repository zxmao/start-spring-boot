package zxm.boot;

import java.util.List;
import java.util.Map;

/**
 * @author JunXiong
 * @Date 2017/3/3
 */
public interface SimpleService<T,F> {

    int addEntity(T t);
    int delEntity(Integer id);
    int delEntity(Map<String,Object> param);
    int updateEntity(T t);
    F findEntity(Integer id);
    List<F> findEntity(Map<String,Object> param);
}
