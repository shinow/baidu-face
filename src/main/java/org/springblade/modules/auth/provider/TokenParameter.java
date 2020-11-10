
package org.springblade.modules.auth.provider;

import lombok.Data;
import org.springblade.core.tool.support.Kv;

/**
 * TokenParameter
 *
 * @author Chill
 */
@Data
public class TokenParameter {

	private Kv args = Kv.create();

}
