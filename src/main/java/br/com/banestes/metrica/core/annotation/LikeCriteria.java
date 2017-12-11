package br.com.sgpf.metrica.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LikeCriteria {

	boolean caseSensitive() default false;

	MatchModeEnum matchMode() default MatchModeEnum.ANYWHERE;

}