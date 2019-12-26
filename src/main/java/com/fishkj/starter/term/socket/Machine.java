package com.fishkj.starter.term.socket;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Serializable {

	private static final long serialVersionUID = 2267325419527164577L;
	
	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 用户
	 */
	private String userId;
	
	/**
	 * 服务器名称
	 */
	private String serverName;
	
	/**
	 * host地址
	 */
	private String hostName;
	
	/**
	 * 登录用户名
	 */
	private String userName;
	
	/**
	 * 登录密码
	 */
	private String pwd;
	
	/**
	 * 登录端口号
	 */
	private Integer port;
	
	/**
	 * 备注说明
	 */
	private String remark;
}
