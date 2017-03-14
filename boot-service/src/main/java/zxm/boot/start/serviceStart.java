//package zxm.boot.start;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * @author JunXiong
// * @Date 2017/3/2
// */
//
//@EnableAutoConfiguration
//@ComponentScan("zxm/boot")
//public class serviceStart {
//
//    private static final Logger logger = LoggerFactory.getLogger(serviceStart.class);
//
//    @Bean
//    public CountDownLatch closeLatch() {
//        return new CountDownLatch(1);
//    }
//
//    public static void main(String[] args) throws Exception {
//        ApplicationContext ctx = new SpringApplicationBuilder()
//                .sources(serviceStart.class)
//                .web(false)
//                .run(args);
//        logger.info("项目启动!");
//        CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
//        closeLatch.await();
//    }
//}