package br.com.sgpf.metrica.core.entity;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxy;

public abstract class EntityModel<PK extends Serializable> implements Serializable, Cloneable {

	private static final long serialVersionUID = 2920166582443624856L;

	public abstract PK getEntityId();

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Class<?> c = null;
		if (obj instanceof HibernateProxy) {
			HibernateProxy hibernateProxy = (HibernateProxy) obj;
			c = hibernateProxy.getHibernateLazyInitializer().getImplementation().getClass();
		} else {
			c = obj.getClass();
		}
		if (getClass() != c) {
			return false;
		}
		if (!(obj instanceof EntityModel)) {
			return false;
		}
		if (this.getEntityId() == null || ((EntityModel<PK>) obj).getEntityId() == null) {
			return false;
		}
		if (!this.getEntityId().equals(((EntityModel<PK>) obj).getEntityId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		if (getEntityId() == null) {
			return 7;
		}
		return ((getEntityId().hashCode() * 45) << 2) & 7;

	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}