package com.fishkj.starter.term.socket.sftp.enums;

import java.io.IOException;

import javax.websocket.Session;

import com.fishkj.starter.term.socket.sftp.SftpClient;
import com.fishkj.starter.term.socket.sftp.SftpMessage;

/**
 * 枚举接口工厂
 * @date: 2019年12月25日 上午12:41:47
 * @author: jiuzhou.hu
 */
public interface SftpCommand {
	
	void exec(Session session, SftpClient client, SftpMessage message) throws IOException;
}
