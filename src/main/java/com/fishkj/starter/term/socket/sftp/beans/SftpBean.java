package com.fishkj.starter.term.socket.sftp.beans;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ftp websocket返回文件列表
 * @date: 2019年12月25日 上午12:42:25
 * @author: jiuzhou.hu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SftpBean implements Serializable {

	private static final long serialVersionUID = -4831312017592136613L;
	
	/**
	 * 当前路径
	 */
	private String currentCatalog;
	
	/**
	 * 文件集合
	 */
	private List<SftpFileBean> files;
}