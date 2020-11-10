package org.springblade.modules.bdface.queue;

import lombok.Data;
import org.opencv.core.Mat;
import org.springblade.modules.bdface.dto.CameraDTO;

import java.io.Serializable;

@Data
public class PicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String credentialsId;

	private String base64;

	private transient String imgFilePath;

	private transient byte[] bytes;

	private transient CameraDTO cameraDTO;

	private Mat mat = new Mat();

}
