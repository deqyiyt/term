package com.fishkj.starter.term.service;

import java.util.List;
import java.util.Optional;

import com.fishkj.starter.term.socket.Machine;

public interface MachineService {
	
	List<Machine> list();
	
	Optional<Machine> get(String id);
	
	void saveOrUpdate(Machine machine);
	
	void remove(String id);
}
