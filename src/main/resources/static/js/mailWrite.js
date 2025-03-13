	console.log('mailWrite연결');
	
	
 
 	// auto-complete
	setAutoComplete();
	function setAutoComplete(){
	
	    $('.searchBox').autocomplete({
	        source : function(request, response) {
	            $.ajax({
	                type : 'get',
	                url: '/mail/autoComplete',
	                dataType : 'json',
	                success : function(cont) {
	                	const inputText = request.term.toLowerCase();
	                    // 서버에서 json 데이터 response 후 목록 추가
	                    response(
	                        $.map(cont.filter(item => item.text.toLowerCase().includes(inputText)), function(item) {
	                            return {
	                                label : item.text,
	                                value : item.username,
	                                //test : item + 'test'
	                            }
	                        })
	                    );
	                    
	                    
	                    /* const filteredItems = data.filter(item => item.toLowerCase().includes(inputText));
		                response(filteredItems); */
	                }
	            });
	        },
	        select : function(event, ui) {
	        	// 현재 input 입력못하게
	        	event.target.setAttribute("readonly", true);
	        	event.target.blur();
	        	//event.target.parentElement.classList.add("readonly");
	        	event.target.classList.remove("invalid");
	        	
	        	addNewInput();
	        	
	            //var clonedElement = e.target.cloneNode();
	            //clonedElement.setAttribute("readonly", false);
	            //document.getElementById("receivers").appendChild(clonedElement);
	            //var inputElement = document.createElement("input");
	            //inputElement.setAttribute("name", "username");
	            //inputElement.setAttribute("autofocus", true);
				//inputElement.classList.add("searchBox");
	            //document.getElementById("receivers").appendChild(inputElement);
	           
				//inputElement.focus();
	        	
	            console.log("ui: ", ui);
	            console.log(ui.item.label);
	            console.log(ui.item.value);
	            console.log(ui.item.test);
	        },
	        focus : function(event, ui) {
	            return false;
	        },
	        minLength : 1,
	        autoFocus : true,
	        classes : {
	            'ui-autocomplete': 'highlight'
	        },
	        //delay : 500,
	        position : { my : 'right top', at : 'right bottom' },
	        close : function(e) {
	            console.log("close: ",e);
	            
	        }
    	});
	}
	
	// 새로운 input 생성
	function addNewInput(){
	   	var inputCont = '<div class="search-area">';
	   	inputCont += '<input class="searchBox form-control" name="username" required/>';
	   	inputCont += '<span class="btn-edit"></span>';
	   	inputCont += '<span class="btn-delete"></span>';
	   	inputCont += '</div>';
	 	$('#receivers').append(inputCont);
	 	$('#receivers input:not([readonly])').focus();
	    setAutoComplete();									
	}
	
	// 받는 사람 input 편집버튼 클릭시
	$(document).on('click','#receivers .btn-edit',function(e){
		$(this).siblings('input').attr('readonly', false);
	});
	
	// 받는 사람 input 삭제버튼 클릭시
	$(document).on('click','#receivers .btn-delete',function(e){
		$(this).parents('.search-area').remove();
	});
		
		/* $(document).ready(function () {
	    const data = [
	        "HTML",
	        "CSS",
	        "JavaScript",
	        "jQuery",
	        "Ajax",
	        "React",
	        "Angular",
	        "Vue.js",
	        "Node.js",
	        "Express"
	    ];
	
	    // 자동 완성 셋업
	    $("#autocomplete").autocomplete({
	        // 입력 문자열에 대해 필터링된 제안을 반환하는 함수
	        source: function (request, response) {
	            const inputText = request.term.toLowerCase();
	            const filteredItems = data.filter(item => item.toLowerCase().includes(inputText));
	            response(filteredItems);
	        },
	        
	        // 자동 완성 위치 조정
	        position: { my: "left top+5" },
	
	        // 자동 완성 메뉴가 열렸을 때, 하이라이팅 로직
	        open: function () {
	            const inputText = $("#autocomplete").val().toLowerCase();
	            // 각 제안 아이템에 대한 하이라이팅
	            $(".ui-autocomplete li").each(function () {
	                let suggestion = $(this).text();
	                const index = suggestion.toLowerCase().indexOf(inputText);
	
	                if (index !== -1) {
	                    // 단어의 일부를 강조하는 HTML을 사용하여 새로운 제안 생성
	                    const highlightedPart = `<strong>${suggestion.substr(index, inputText.length)}</strong>`;
	                    suggestion = suggestion.substring(0, index) + highlightedPart + suggestion.substring(index + inputText.length);
	                    // 새로운 제안으로 항목 내용을 업데이트
	                    $(this).html(suggestion);
	                }
	            });
	        },
	        delay : 100, // 입력창에 글자가 써지고 나서 autocomplete 이벤트 발생될 떄 까지 지연 시간(ms)
	    });
	}); */
		
	// 메일전송
	function sendMail(){
		const form = document.getElementById("mailWriteForm");
	    const inputs = form.querySelectorAll("input[required]");
		let isValid = true;
		let missingFields = [];
		
	    // input 필드 유효성 검사
	    inputs.forEach(input => {
	    	console.log("input.value.trim() :" ,input.value.trim());
	    	console.log("input.value :" ,input.value);
	        if (!input.value.trim()) {
	            isValid = false;
	            let text = $(input).parents('.line').find('label').text();
	            console.log("text!!!!",$(input).parents('.line'));
	            missingFields.push(text);
	            return; 
	        }
	    });
	    // 받는사람 input
	    var existReceiver = $('#receivers input[readonly]').length >= 1;
	    var invalidReceiver = $('#receivers input.invalid').length >= 1;
	    //console.log("$('#receivers input[readonly]')",$('#receivers input[readonly]'));
	    //console.log("existReceiver",existReceiver);
	    if (isValid && existReceiver && !invalidReceiver) {
	    	textEaditorWrite('/mail/write/sd');
	    }else {
	    	if(!existReceiver){
	    		missingFields.push('받는 사람');
	    	}
	    	if(invalidReceiver){
	    		layerPopup("받는 사람이 유효하지 않습니다.",'확인',false,removeAlert,removeAlert);
				return;
	    	}else{
		    	let missingText = missingFields.join(', ');
		    	layerPopup("필수 값을 모두 입력하세요: "+missingText,'확인',false,removeAlert,removeAlert);
	    	}
	    }
	}	
	
	// 임시저장
	function saveMail(){
		textEaditorWrite('/mail/write/sv');
	}
	
	//1분마다 자동 임시저장 check!!! 나중에 풀기
	//setInterval(saveMail, 60000);
	
	
	
	
	
	function fileSuccess(response){
		if(response.status == 'sd'){
			location.href='/mail/detail/'+response.mailIdx;
		}else{
			$('input[name="idx"]').val(response.mailIdx);
			layerPopup("임시저장 되었습니다.",'확인',false,removeAlert,removeAlert);
		}
	}
	
	// 받는 사람 영역 클릭시 새로운 input생성
	$(document).on('click','#receivers',function(e){
		// 마지막 input 요소
		var lastInput = $(this).find('.search-area:last-child input');
		console.log("lastInput : : :",lastInput);
		console.log("lastInput : readonly: :",$(lastInput).attr("readonly"));
		if($(lastInput).attr('readonly') == 'readonly' || lastInput.length < 1){
			addNewInput();
		}else{
			$(lastInput).focus();
		}
	});
	$(document).on('focusout','.searchBox',function(e){
		if($(this).val() == ''){
			$(this).parent().remove();
		}
		if(!$(this).attr('readonly')){
			$(this).addClass('invalid');
		}
	});
		