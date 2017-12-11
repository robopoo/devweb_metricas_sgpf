 /**
Método chamado pelo selectAll das telas de listagem com seleção múltipla. O primeiro parâmetro é o próprio componente selectAll
*/
function checkAllCheckboxesInTable( selectAll ){
	var state = selectAll.checked;
	var inputs = getInputsFromDataTable(selectAll.id);

	for (var i = 0; i < inputs.length; i++){
		var input = inputs[i];
		if(input.type == 'checkbox'){
			input.checked = state;
		}
	}
}

/**
	Quando o usuário muda de página no dataTable (através do componente dataScroler), o checkBox que marca ou desmarca todos os
	checkBoxs individuais, continua com o mesmo estado em que estava da página anterior.
	Isso não chega a ser um problema, não influenciando significativamente. Mas para melhorar a navegabilidade, essa função serve para atualizar o estado
	do checkBox que marca todos quando houver paginação.
	Essa função é para ser chamada através do evento "oncomplete" do dataScroler passando o id do checkBox que marca todos - Ex: oncomplete="atualizaCheckAllPaginacaoDataScroller( '(id do form):(id do dataTable):(id do checkBox)' );".
	Para melhorar mais, pode colocar a chamada desse javascript para executar no carregamento da página. Isso por que quando houver um submit na página, o checkBox poderá
	ficar desatualizado. 
*/
function atualizaCheckAllPaginacaoDataScroller( selectAllId ){
	var selectAll = document.getElementById(selectAllId);
	
	if(selectAll != null){
	
		var state = selectAll.checked;
		var inputs = getInputsFromDataTable(selectAllId);
 		
 		if(inputs.length > 1){ 
			//i inicia em 1, pois o primeiro input, é o checkAll. Achei desnecessário colocar uma validação para isso.
			for (var i = 1; i < inputs.length; i++){
				var input = inputs[i];
				if(input.type == 'checkbox' && input.checked == false){
					selectAll.checked = false;
					return;
				}
			}	
		
			selectAll.checked = true;
			
 		}else{
 			selectAll.checked = false;
 		}
		
	}
}

/**

	Método chamado para seleção de apenas um registro no dataTable. O parâmetro é o próprio componente checkBox.
*/
function onlyOneCheck(componenteClicado){
	var inputs = getInputsFromDataTable(componenteClicado.id);
	
	if(componenteClicado.checked == true){//Se marcou o checkBox, desmarca todos os outros. Se desmarcou, todos estarão desmarcados.
		for (var i = 0; i < inputs.length; i++){
			var input = inputs[i];
			if(input.type == 'checkbox' && input != componenteClicado){
				input.checked = false;
			}
		}
	}	
}

//Para dataTable sem checkAll
function isNenhumCheckBoxMarcadoFromDataTable (nmIdDataTable){
	var tableElement = document.getElementById( nmIdDataTable );
	var inputs = tableElement.getElementsByTagName('input');
	
	for (var i = 0; i < inputs.length; i++){
		var input = inputs[i];
		if(input.type == 'checkbox' && input.checked == true){
			return false;
		}
	}
	
	return true;	
		
}

/**
Função que retorna os inputs do DataTable. O parâmetro é o id de um componente no dataTable (Exemplo: checkBox.id).
*/
function getInputsFromDataTable(idComponenteClicado){
	var elementos = idComponenteClicado.split(':');
	var tableId = elementos[0] + ':' + elementos[1];
	var tableElement = document.getElementById( tableId );
	var inputs = tableElement.getElementsByTagName('input');
	
	return inputs;
}

