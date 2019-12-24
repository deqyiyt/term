package com.fishkj.starter.term.socket.sftp.enums;

import java.io.IOException;

import javax.websocket.Session;

import com.fishkj.starter.term.socket.sftp.SftpClient;
import com.fishkj.starter.term.socket.sftp.SftpMessage;

public interface SftpCommand {
	
	void exec(Session session, SftpClient client, SftpMessage message) throws IOException;
}
