package pb.wi.cohp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControlOfHealthParametersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlOfHealthParametersApplication.class, args);
    }

}
