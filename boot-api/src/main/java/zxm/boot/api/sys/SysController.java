package zxm.boot.api.sys;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JunXiong
 * @Date 2017/3/3
 */
@RestController
@RequestMapping("/sys")
public class SysController {

    @RequestMapping("/")
    String home() {
        return "welcome to system index page !!";
    }
}
