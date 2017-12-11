// JavaScript Document   
//-------------------------------------------------------------------------------// 
//
// Arquivo      : geral.js
// Descricao    : Funcoes Auxiliares (de Apoio aos Componentes Jsf) 
//
//-------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------//
//Função para posicionar a seta de recolher e expandir menu lateral ao centro.
//-------------------------------------------------------------------------------//
function posicionaSeta() {

	altura = jQuery('#menupainel').height();
	alturaMsg = jQuery('#divMensagem').height();

	var meiaAltura = parseFloat(altura) / 2;

	var topo = meiaAltura + alturaMsg + 84;

	jQuery('#linkSetaOn').animate({
		top : topo
	}, 'slow');
	jQuery('#linkSetaOff').css('top', topo);

}

// -------------------------------------------------------------------------------------
// Funcao trim para retirar espacos em branco no inicio e no final de um objeto
// string.
// -------------------------------------------------------------------------------------
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/, '');
};

// -----------------------------------------------
// Funcao que Alerta para sair do sistema
// -----------------------------------------------
function ConfirmaSaida() {
	question = confirm("Deseja sair da aplica&#231;&#227;o?")
	if (question != "0") {
		// window.open('','_parent','');
		// window.close();
		return true;
	} else {
		return false;
	}
}

function limiteTextArea(textarea, limit, infodiv) {
	var text = textarea.value;
	var textlength = text.length;
	var info = document.getElementById(infodiv);

	if (textlength > limit) {
		info.innerHTML = 'Voc&#234; n&#245;o pode digitar mais de ' + limit
				+ ' caracteres!';
		textarea.value = text.substr(0, limit);
		return false;
	} else {
		info.innerHTML = (limit - textlength) + ' caracteres restantes.';
		return true;
	}
}

function limparTextArea(limit, infodiv) {
	var info = document.getElementById(infodiv);
	info.innerHTML = 'Limite de ' + limit + ' caracteres.';
	return true;
}

function mascara(campo, tipo) {
	mascaraFormulario('formulario', campo, tipo);
}

function loadjQuery() {
	var $j = jQuery.noConflict();
}


function mascaraFormulario(formulario, campo, tipo) {
	var $j = jQuery.noConflict();
	if ($j('input[name=' + formulario + ':' + campo + ']') != null
			|| $j('input[name=' + formulario + ':' + campo + 'InputDate]') != null) {
		removerMascara(formulario, campo, tipo);
		$j(function() {
			switch (tipo) {
			case 'NUMEROAINF':
				$j('input[name=' + formulario + ':' + campo + ']').mask('999999/9999');
				break;
			case 'DATA':
				$j('input[name=' + formulario + ':' + campo + 'InputDate]')
						.unmask();
				$j('input[name=' + formulario + ':' + campo + 'InputDate]')
						.mask('99/99/9999');
				break;
			case 'MESANO':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'99/9999');
				break;
			case 'ANOMES':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'9999/99');
				break;
			case 'INTEIRO':
				$j('input[name=' + formulario + ':' + campo + ']').numeric({
					allow : ""
				});
				break;
			case 'HORA':
				$j('input[name=' + formulario + ':' + campo + ']')
						.mask('99:99');
				break;
			case 'DATAHORA':
				$j('input[name=formulario:' + campo + ']').unmask();
				$j('input[name=' + formulario + ':' + campo + 'InputDate]')
						.mask('99/99/9999 99:99');
				break;
			case 'DECIMAL1':
			
//				$j('input[name=' + formulario + ':' + campo + ']')
//				.numeric({ decimal : ".",  negative : false, scale: 1 }).addClass('right');
			    	$j('input[name=' + formulario + ':' + campo + ']').unmaskMoney();
                		$j('input[name=' + formulario + ':' + campo + ']').maskMoney({showSymbol:false, thousands:'.', decimal:',', precision:1});
                		$j('input[name=' + formulario + ':' + campo + ']').addClass('right');
				break;	
			case 'MOEDA':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial();
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').addClass(
						'right');
				break;
			case 'AREA4':
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					precision : 4
				});
				$j('input[name=' + formulario + ':' + campo + ']').addClass(
						'right');
				break;
			case 'MOEDAMENOS':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({menos:true});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					allowNegative : true
				});
				$j('input[name=' + formulario + ':' + campo + ']').addClass(
						'right');
				break;
			case 'PORCENTAGEM':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({max:'6'});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '6'
				});
				$j('input[name=' + formulario + ':' + campo + ']').addClass(
						'right');
				break;
			case 'TELEFONE':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'9999-9999');
				break;
			case 'DDD':
				$j('input[name=' + formulario + ':' + campo + ']').mask('(99)');
				break;
			case 'TEXTO':
				$j(
						'input[name=' + formulario + ':' + campo
								+ '], textarea[name=' + formulario + ':'
								+ campo + ']').masktext({
					allow : ""
				});
				break;
			case 'TEXTOAREA':
				$j('textarea[name=' + formulario + ':' + campo + ']').masktext(
						{
							allow : ""
						});
				break;
			case 'TEXTOAREANUMERO':
				$j('textarea[name=' + formulario + ':' + campo + ']')
						.masktextnumeric({
							allow : " "
						});
				break;
			case 'TEXTONUMERO':
				$j('input[name=' + formulario + ':' + campo + ']')
						.masktextnumeric({
							allow : ""
						});
				break;
			case 'CPF':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'999.999.999-99');
				break;
			case 'CNPJ':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'99.999.999/9999-99');
				break;
			case 'INSCRICAOESTADUAL':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'99.999.999-9');
				break;
			case 'CEP':
				$j('input[name=' + formulario + ':' + campo + ']').mask(
						'99999-999');
				break;
			case 'EMAIL':
				$j('input[name=' + formulario + ':' + campo + ']')
						.alphanumeric({
							allow : "._-@"
						});
				break;
			case 'SITE':
				$j('input[name=' + formulario + ':' + campo + ']')
						.alphanumeric({
							allow : "/:.-?&;~+=%#@"
						});
				break;
			case 'MOEDA74':
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '7',
					precision : 4
				});
				break;
			case 'MOEDA84':
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '8',
					precision : 4
				});
				break;
			case 'MOEDA95':
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '9',
					precision : 5
				});
				break;
			case 'MOEDA102':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({max:'14'});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '10'
				});
				break;
			case 'MOEDA142':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({max:'14'});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '14'
				});
				break;
			case 'MOEDA152':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({max:'14'});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '15'
				});
				break;
			case 'MOEDA162':
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '16'
				});
				break;
			case 'MOEDA202':
				// $j('input[name=' + formulario + ':' + campo +
				// ']').maskFinancial({max:'20'});
				$j('input[name=' + formulario + ':' + campo + ']')
						.unmaskMoney();
				$j('input[name=' + formulario + ':' + campo + ']').maskMoney({
					max : '20'
				});
				break;
			case 'LOV':
				var nome = formulario + ':' + campo;
				if ($(nome) != null && !$(nome).disabled) {

					var lov = $j('input[name=' + nome + ']');

					if (lov.attr('type') == undefined) {
						lov = $j('textarea[name=' + nome + ']');
						expandirLov(lov);
					}

					if (lov.val() == '') {
						lov.val('CLIQUE NA LUPA');
					}
					setReadonly(campo);
				}
				break;
			}
		});

	}

}

// -------------------------------------------------------------------
// Funcao para remover a mascara de um campo
// -------------------------------------------------------------------
function removerMascara(formulario, campo, tipo) {
	var $j = jQuery.noConflict();
	if ((tipo == 'DATAHORA') || (tipo == 'DATA')) {
		$j('input[name=' + formulario + ':' + campo + 'InputDate]').unmask();
	} else {
		$j('input[name=' + formulario + ':' + campo + ']').unmask();
	}
}

// -------------------------------------------------------------------
// Função para carregar o dialog de confirm com layout customizado
// -------------------------------------------------------------------
var lista = new Array();

function confirma(mensagem, componente) {
	var id = jQuery(componente).attr('id');
	var index = jQuery.inArray(id, lista);

	// Não encontrou o elemento na lista
	if (index == -1) {
		jConfirm(mensagem, 'Confirma&#231;&#227;o', function(retorno) {
			// Adiciona elemento na lista e executa o botão Confirmar
			if (retorno) {
				lista.push(id);
				jQuery(componente).click();
			}
			return retorno;
		});
		return false;
	} else {
		// Remove o elemento da lista e executa clique do botão
		lista.splice(index, 1);
		return true;
	}
}
function closeLov() {
	window.top.hidePopWin(true);
}

// blocqueia voltar - INICIO
document.onkeydown = function() {
	var e = event || window.event;
	var keyASCII = parseInt(e.keyCode, 10);
	var src = e.srcElement;
	var tag = src.tagName.toUpperCase();
	if (keyASCII == 8) {
		if (src.readOnly || src.disabled
				|| (tag != "INPUT" && tag != "TEXTAREA")) {
			return false;
		}
		if (src.type) {
			var type = ("" + src.type).toUpperCase();
			return type != "CHECKBOX" && type != "RADIO" && type != "BUTTON"
					&& type != "SUBMIT";
		}
	}
	return true;
}
// blocqueia voltar - FIM

function irTopoPagina() {
	window.location = '#irTopoPagina';
}