package org.springblade.modules.bdface.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FaceRecConfig {

	@Value("${bd-face.threshold}")
	private int threshold = 85;


	@Value("${bd-face.recinterval}")
	private int recInterval = 8000;

	@Value("${bd-face.savefacepic}")
	private boolean savefacepic = false;

}
