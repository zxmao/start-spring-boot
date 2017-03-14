package zxm.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

/**
 * @author JunXiong
 * @Date 2017/3/2
 */

@RestController
@EnableAutoConfiguration
@ComponentScan("zxm.boot")
public class ApiStart {

    private static final Logger log = LoggerFactory.getLogger(ApiStart.class);

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {SpringApplication.run(ApiStart.class, args);}

//    @Bean
//    public CommandLineRunner demo(UserRepository repository) {
//        return (args) -> {
//            repository.save(new Users(1L, "Bauer"));
//            repository.save(new Users(2L, "O'Brian"));
//            repository.save(new Users(3L, "Bauer"));
//            repository.save(new Users(4L, "Palmer"));
//
//            log.info("Customers found with findAll():");
//            log.info("-------------------------------");
//            for (Users customer : repository.findAll()) {
//                log.info(customer.toString());
//            }
//            log.info("");
//
//            // fetch an individual customer by ID
//            Users customer = repository.findOne(1L);
//            log.info("Customer found with findOne(1L):");
//            log.info("--------------------------------");
//            log.info(customer.toString());
//            log.info("");
//
//            // fetch customers by last name
//            log.info("Customer found with findByLastName('Bauer'):");
//            log.info("--------------------------------------------");
//            for (Users u : repository.findByAccountName("Bauer")) {
//                log.info(u.toString());
//            }
//            log.info("");
//        };
//    }
}