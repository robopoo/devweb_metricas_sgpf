package br.com.sgpf.metrica.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Column;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import br.com.sgpf.metrica.core.annotation.LowerCase;
import br.com.sgpf.metrica.core.annotation.UpperCase;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.util.ReflectionUtils;

public class CustomEmptyInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -4469663845222094776L;

	private void executeCustomField(Object entity, Object[] state, String[] propertyNames, boolean onLoad) {
		Class<?> classe = entity.getClass();
		while (!classe.equals(Object.class)) {
			for (Field field : classe.getDeclaredFields()) {
				if (field.isAnnotationPresent(Column.class)) {
					if (field.getType().equals(String.class) && !field.isAnnotationPresent(UpperCase.class) && !field.isAnnotationPresent(LowerCase.class)) {
						for (int i = 0; i < propertyNames.length; i++) {
							if (propertyNames[i].equals(field.getName())) {
								if (state[i] != null) {
									state[i] = state[i].toString().trim();
									if (!onLoad) {
										ReflectionUtils.preencherValorAtributo(classe, entity, field.getName(), state[i]);
									}
								}
								break;
							}
						}
					} else if (field.getType().equals(String.class)
							&& (field.isAnnotationPresent(UpperCase.class) || field.isAnnotationPresent(LowerCase.class))) {
						for (int i = 0; i < propertyNames.length; i++) {
							if (propertyNames[i].equals(field.getName())) {
								if (state[i] != null && field.isAnnotationPresent(UpperCase.class)) {
									state[i] = state[i].toString().trim().toUpperCase();
									if (!onLoad) {
										ReflectionUtils.preencherValorAtributo(classe, entity, field.getName(), state[i]);
									}
								} else if (state[i] != null && field.isAnnotationPresent(LowerCase.class)) {
									state[i] = state[i].toString().trim().toLowerCase();
									if (!onLoad) {
										ReflectionUtils.preencherValorAtributo(classe, entity, field.getName(), state[i]);
									}
								}
							}
						}
					}
				}
			}
			classe = classe.getSuperclass();
		}
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof EntityModel<?>) {
			executeCustomField(entity, state, propertyNames, true);
		}
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof EntityModel<?>) {
			executeCustomField(entity, state, propertyNames, false);
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if (entity instanceof EntityModel<?>) {
			executeCustomField(entity, currentState, propertyNames, false);
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

}
