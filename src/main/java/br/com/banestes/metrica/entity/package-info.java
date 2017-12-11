//@GenericGenerators({ @GenericGenerator(name = "triggerAssigned", strategy = "com.pdcase.pdpositivo.core.entity.generator.TriggerAssignedIdentityGenerator") })
@TypeDefs({
	@TypeDef(name = "simNaoEnumUserType",                typeClass = br.com.sgpf.metrica.core.enumeration.types.SimNaoEnumUserType.class),
	@TypeDef(name = "statusEnumUserType", 			 	 typeClass = br.com.sgpf.metrica.core.enumeration.types.StatusEnumUserType.class),
	@TypeDef(name = "tipoPerfilEnumUserType", 		     typeClass = br.com.sgpf.metrica.core.enumeration.types.TipoPerfilEnumUserType.class),
	@TypeDef(name = "categoriaEnumUserType", 			 typeClass = br.com.sgpf.metrica.enumeration.type.CategoriaEnumUserType.class),
	@TypeDef(name = "tipoEntidadeEnumUserType", 		 typeClass = br.com.sgpf.metrica.enumeration.type.TipoEntidadeEnumUserType.class),
	@TypeDef(name = "tipoElementoContagemEnumUserType",  typeClass = br.com.sgpf.metrica.enumeration.type.TipoElementoContagemEnumUserType.class),
	@TypeDef(name = "formatoEnumUserType", 				 typeClass = br.com.sgpf.metrica.enumeration.type.FormatoEnumUserType.class),
	@TypeDef(name = "statusAvaliacaoEnumUserType", 		 typeClass = br.com.sgpf.metrica.enumeration.type.StatusAvaliacaoEnumUserType.class),
	@TypeDef(name = "tipoComplexidadeEnumUserType", 	 typeClass = br.com.sgpf.metrica.enumeration.type.TipoComplexidadeEnumUserType.class),
	@TypeDef(name = "statusProjetoEnumUserType", 		 typeClass = br.com.sgpf.metrica.enumeration.type.StatusProjetoEnumUserType.class),
	@TypeDef(name = "tipoFuncaoDadosProjetoUserType", 	 typeClass = br.com.sgpf.metrica.enumeration.type.TipoFuncaoDadosProjetoUserType.class),
	@TypeDef(name = "tipoFuncaoTransacaoProjetoUserType",typeClass = br.com.sgpf.metrica.enumeration.type.TipoFuncaoTransacaoProjetoUserType.class),
	@TypeDef(name = "tipoItemMensuraveisEnumUserType", 	 typeClass = br.com.sgpf.metrica.enumeration.type.TipoItemMensuraveisEnumUserType.class),
	@TypeDef(name = "tipoAnalistaEnumUserType", 	     typeClass = br.com.sgpf.metrica.enumeration.type.TipoAnalistaEnumUserType.class)
})
package br.com.sgpf.metrica.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

