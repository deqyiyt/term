package com.fishkj.starter.term.jpa;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.fishkj.starter.term.utils.UUIDUtils;

public class UuidGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return UUIDUtils.createSystemDataPrimaryKey();
	}
}
