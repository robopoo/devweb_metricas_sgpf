(function($) {

		var i = 0;
		
		var methods = {
		
		    init : function( options ) {
		    
				var opt	= $.extend({}, $.fn.maxLength.defaults, options);
		    	opt = $.extend(opt, {mensagem: 'Valor maior que o m�ximo de '+ opt.MaxLength +' permitido'});
				
				$span = methods.mostrarCaracterRestante.call($(this),opt);
				
				width = parseInt($(this).css("width"));
				height = parseInt($(this).css("height"));
				
				$(this)
				.parent()
				.css(
					{
					"height":  height + 50});
				
				$(this).after($span);	
				
				methods.validar.call($(this), opt, $span);
  			},
  			
  			mostrarCaracterRestante: function(opt, qtRestante){
  				
				var calc;			
				if($(this).val() !== undefined && $(this).val() != ''){
	  				if(qtRestante === undefined){
	  					calc = parseInt(opt.MaxLength) - $(this).val().length;
	  				}else{
	  					calc = parseInt(qtRestante) - $(this).val().length;
	  				}
  				}else{
  				
	  				if(qtRestante === undefined){
	  					calc = opt.MaxLength;
	  				}else{
	  					calc = qtRestante ;
	  				}
  				}
  				
  			//	posLeft = $(this).position().left ; 
  			//	pos = $(this).position().top + $(this).height() + 35;
				
	  			msg = "<span>Resta(m): "+ calc +" caracter(es)</span>";
  				$span = $(msg)
				.attr("id", "spCaracterRestante_"+ i++)
				.css(
					{ 'color': '#228B22'
					, 'font-size': '10'
					, 'font-family': 'Arial'
					, 'display': 'block'
					});

				return $span; 
  			
  			},
  		
	  		validar: function(opt, $span){
	  		
	  			$(this)
				.keypress(function() {
	  					if(this.value.length == opt.MaxLength ){
	  						return false;
	  					}
	  					return true;
				})
				.live('paste', function(e){
					var _this = this;
					var _val_old = $(_this).val();
					// Short pause to wait for paste to complete
			        setTimeout( function() {
			            if( $(_this).val().length > opt.MaxLength){
			            	methods.mostrarMensagem.call($(_this), opt);
			            	$(_this).val(_val_old);
							result = parseInt(opt.MaxLength) - parseInt($(_this).val().length);
							
							$span.text("Resta(m): " + result + " caracter(es)");
			            }
			        }, 100);
				})
				.keyup(function() {
					var maxLength = parseInt(opt.MaxLength); 
					var lengthAtual = parseInt(this.value.length);
					 
					if(this.value.length > opt.MaxLength){
						return true;
					}
					 
					result =  maxLength - lengthAtual;
	  				$span.text("Resta(m): " + result + " caracter(es)");
				});
	  		
	  		},
  		
	  		mostrarMensagem: function(opt){
	  			$div = $("<div>")
			    .attr("id", "divMsg")
			    .css(
			    	{ 'backgroundColor': '#FFD700'
			        , 'color': 'black'
			        , 'border': '2px solid black'
			        , 'font-weight': 'bolder'
			        , 'padding': '5px'
			        , 'z-index': '2' 
			        , 'position': 'relative'
			        , 'float': 'right'
			        , 'top': '-100'
			        , 'display': 'none'})
			    .click(function(){
			    	$("#divMsg").remove();
			    })
			    .appendTo($(this).parent());
		    
			    $("<span>")
			    .html(opt.mensagem)
			    .appendTo($div);
		    
			    $div.fadeIn({
			        duration: 'slow',
			        complete:function(){
			        	// Short pause to wait for paste to complete
				        setTimeout( function() {	
				        	$("#divMsg").remove();
				        }, 1000);
			            
			        }
			    });
	  		}
	  	};
  		
        
        $.fn.maxLength = function(method){
	        if (methods[method]) {
				return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
			} else if (typeof method === 'object' || !method) {
				return methods.init.apply(this, arguments);
			} else {
				$.error('Method ' + method + ' does not exist!');
			}
		};
		
		$.fn.maxLength.defaults = {
			MaxLength: '50'
		};	
				
})(jQuery);