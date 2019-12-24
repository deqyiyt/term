package com.fishkj.starter.term.jpa;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.fishkj.starter.term.utils.UUIDUtils;

public class UuidGenerator implements IdentifierGenerator {

	/**
	 * springjpa主键生成策略
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:33:30
	 * @param session
	 * @param object
	 * @return
	 * @throws HibernateException
	 */
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return UUIDUtils.createSystemDataPrimaryKey();
	}
}
