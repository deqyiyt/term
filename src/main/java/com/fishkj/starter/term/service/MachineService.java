package com.fishkj.starter.term.service;

import java.util.List;
import java.util.Optional;

import com.fishkj.starter.term.socket.Machine;

public interface MachineService {
	
	/**
	 * 根据用户查询所有服务器列表
	 * @author jiuzhou.hu
	 * @date 2019年12月26日 下午12:31:29
	 * @param userId
	 * @return
	 */
	List<Machine> list(String userId);
	
	/**
	 * 根据ID获取单个服务器配置
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:30:15
	 * @param id
	 * @return
	 */
	Optional<Machine> get(String id);
	
	/**
	 * 新增/编辑配置
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:30:30
	 * @param machine
	 */
	void saveOrUpdate(Machine machine);
	
	/**
	 * 根据ID删除
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:30:43
	 * @param id
	 */
	void remove(String id);
}
