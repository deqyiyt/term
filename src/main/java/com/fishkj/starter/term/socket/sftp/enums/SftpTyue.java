package com.fishkj.starter.term.socket.sftp.enums;

import java.io.IOException;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fishkj.starter.term.socket.sftp.SftpClient;
import com.fishkj.starter.term.socket.sftp.SftpMessage;
import com.fishkj.starter.term.socket.sftp.beans.SftpBean;
import com.fishkj.starter.term.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息类型枚举
 * @date: 2019年12月25日 上午12:42:06
 * @author: jiuzhou.hu
 */
@Slf4j
public enum SftpTyue implements SftpCommand {
	UP("up","上一级") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			SftpBean sftp = client.changeUp();
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
    ,QUERY("query","查询") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			SftpBean sftp = client.changeDirectory(message.getDir());
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
    ,ADD_FILE("addfile","新增文件") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			if(log.isDebugEnabled()) {
				log.debug("新增文件：{}", message.getFileName());
			}
			client.createFile(message.getFileName());
			SftpBean sftp = client.ls();
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
    ,ADD_FOLDER("addfolder","新增文件夹") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			if(log.isDebugEnabled()) {
				log.debug("新增文件夹：{}", message.getFileName());
			}
			client.mkDir(message.getFileName());
			SftpBean sftp = client.ls();
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
    ,UPDATE("update","修改文件名称") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			if(log.isDebugEnabled()) {
				log.debug("修改文件名称：{} -> {}", message.getFileName(), message.getNewName());
			}
			client.mv(message.getFileName(), message.getNewName());
			SftpBean sftp = client.ls();
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
	,REMOVE("remove","删除") {
		@Override
		public void exec(Session session, SftpClient client, SftpMessage message) throws IOException {
			if(log.isDebugEnabled()) {
				log.debug("删除文件：{}", message.getFileName());
			}
			if(message.getDirectory()) {
				client.deleteFolder(message.getFileName());
			} else {
				client.deleteFile(message.getFileName());
			}
			SftpBean sftp = client.ls();
			session.getBasicRemote().sendText(JsonUtils.buildNormalBinder().toJson(sftp));
		}
	}
	;

    private String value;
    private String text;

    private SftpTyue(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
    public String getText() {
        return text;
    }

	
}
