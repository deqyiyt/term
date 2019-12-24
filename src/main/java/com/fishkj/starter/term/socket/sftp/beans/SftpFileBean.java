package com.fishkj.starter.term.socket.sftp.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SftpFileBean implements Comparable<SftpFileBean>{
	
	private String filename = null;
	private Long size = null;
	private Integer intPermissions = null;
	private String strPermissions = null;
	private String octalPermissions = null;
	private String mtime = null;
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
