
package org.springblade;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;




@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		BladeApplication.run(CommonConstant.APPLICATION_NAME, Application.class, args);
	}

}

