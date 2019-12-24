package com.fishkj.starter.term.socket.sftp;

import java.io.Serializable;

import com.fishkj.starter.term.socket.sftp.enums.SftpTyue;

import lombok.Data;

/**
 * sftp消息体
 * @date: 2019年12月25日 上午12:40:48
 * @author: jiuzhou.hu
 */
@Data
public class SftpMessage implements Serializable {

	private static final long serialVersionUID = -7402973369300174223L;
	
	/**
	 * 消息类型
	 */
	private SftpTyue tyue;
	
	/**
	 * 路径
	 */
	private String dir;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 新文件名称
	 */
	private String newName;
	
	/**
	 * 是否文件夹
	 */
	private boolean directory;
}
