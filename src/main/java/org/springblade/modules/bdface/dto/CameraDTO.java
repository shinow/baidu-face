package org.springblade.modules.bdface.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.modules.bdface.cache.MessageCache;
import org.springblade.modules.bdface.entity.Camera;
import org.springblade.modules.bdface.queue.PicEntity;

import java.util.LinkedList;

/**
 * 数据传输对象实体类
 *
 * @author BladeX
 * @since 2020-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraDTO extends Camera {
	private static final long serialVersionUID = 1L;


	//是否在运行
	private boolean running;

	//人脸照片
	private final transient LinkedList<PicEntity> stack = new LinkedList<>();


	//消息缓存
	private final MessageCache messageCache = new MessageCache();


}
