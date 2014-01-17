package com.junglebird.webframe.common;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PkGenerator implements IdentifierGenerator{
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return l;
	}

}
