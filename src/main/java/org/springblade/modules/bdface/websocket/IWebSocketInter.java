package org.springblade.modules.bdface.websocket;

public interface IWebSocketInter {

	void sendMessage(String message);


	void onMessage(String message);

}
