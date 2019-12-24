package com.fishkj.starter.term.socket.terminal;

import java.io.InputStream;

import javax.websocket.Session;

import com.fishkj.starter.term.utils.WebSshUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 执行shell命令
 * @date: 2019年12月25日 上午12:39:58
 * @author: jiuzhou.hu
 */
@Slf4j
public class ShellOutPutTask extends Thread {
	
	private final Session session;
	private final InputStream out;

	public ShellOutPutTask(Session session, InputStream out) {
		super();
		this.session = session;
		this.out = out;
	}

	@Override
	public void run() {
		super.run();
		
		byte[] buff = new byte[8192];
		StringBuilder sb = new StringBuilder();
		try {
			while (session != null && session.isOpen()) {
				sb.setLength(0);
				int len = out.read(buff);
				if (len == -1) {
					return;
				}
				
				for (int i = 0; i < len; i++) {
					char c = (char) (buff[i] & 0xff);
					sb.append(c);
				}
				if (WebSshUtils.getEncoding(sb.toString()).equals("ISO-8859-1")) {
					session.getBasicRemote().sendText(new String(sb.toString().getBytes("ISO-8859-1"),"UTF-8"));
				} else {
					session.getBasicRemote().sendText(new String(sb.toString().getBytes("gb2312"),"UTF-8"));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	
}
