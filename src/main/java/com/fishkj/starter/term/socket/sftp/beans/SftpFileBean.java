package com.fishkj.starter.term.socket.sftp.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件属性
 * @date: 2019年12月25日 上午12:43:12
 * @author: jiuzhou.hu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SftpFileBean implements Comparable<SftpFileBean>{
	
	/**
	 * 文件名称
	 */
	private String filename = null;
	
	/**
	 * 文件大小
	 */
	private Long size = null;
	
	/**
	 * 文件权限
	 */
	private Integer intPermissions = null;
	
	/**
	 * 文件权限
	 */
	private String strPermissions = null;
	
	private String octalPermissions = null;
	
	/**
	 * 最后修改时间
	 */
	private String mtime = null;
	
	/**
	 * 是否文件夹
	 */
	private boolean directory;

	@Override
	public int compareTo(SftpFileBean o) {
		if (this.directory && !o.directory) {
			return -1;
		} else if (!(this.directory ^ o.directory)) {
			return this.filename.compareTo(o.filename);
		} else if (!this.directory && o.directory) {
			return 1;
		}
		return 0;
	}
}
