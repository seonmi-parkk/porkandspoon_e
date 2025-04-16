console.log('ddddddfawega이야이야');
/* 조직도노드  */
 //초기 데이터
 function initData(userDTO){
 	console.log("userDTOuserDTO::",userDTO);
 	console.log("userDTOuserDTO::",userDTO.name);

	const initialData = {
	    headers: ['이름', '부서', '직급', '구분', '삭제'],
	    rows: [
	        ['${userDTO.name}', '${userDTO.dept.text}', '${userDTO.position_content}', '기안', '<button class="btn btn-primary">삭제</button>'],
	    ],
	    footer: '<button class="btn btn-outline-secondary btn-line-write" onclick="loadModal(\'ApprLine\',\'Bookmark\')">즐겨찾기 저장</button>'
	};
 }
var exampleData = JSON.parse(JSON.stringify(initialData));

//서버에 넘길 결재자 id배열
var approvalLines = ['${userDTO.username}'];

 // 선택된 ID를 rows에 추가하는 함수
 function addSelectedIdToRows(selectedId) {
     console.log("가져온 ID:", selectedId);
     if(approvalLines.includes(selectedId)){
    	 layerPopup( "이미 등록된 결재자입니다.","확인",false,removeAlert,removeAlert);
     	 return false;
     }
     approvalLines.push(selectedId);
     console.log("approvalLines:", approvalLines);
     $.ajax({
         type: 'GET',
         url: '/approval/getUserInfo/'+selectedId,
         data: {},
         dataType: "JSON",
         success: function(response) {
        	 console.log("유저이름: ",response.name);
        	 console.log("유저정보: ",response.position_content);
        	 console.log("유저정보: ",response.dept.text);
        	 
        	 var userName = response.name;
        	 var userPosition = response.position_content;
        	 var userDept = response.dept.text;
        	 
             // 새로운 row 데이터 생성
             const newRow = [userName, userDept, userPosition, '결재', '<button class="btn btn-primary">삭제</button>'];

             // 기존 rows에 추가
             initialData.rows.push(newRow);
             exampleData.rows.push(newRow);

             // 테이블 업데이트 (id가 'customTable'인 테이블에 적용)
             updateTableData('customTable', exampleData);
         },
         error: function(e) {
             console.log(e);
         }
     });
 }

 // 선택된 ID를 받아서 처리
 getSelectId(function (selectedId) {
     addSelectedIdToRows(selectedId);
 });
 
 
 function chartPrint(response) {
	    console.log(response, '받아온 데이터');

	    // 데이터 정렬 (menuDepth -> menuOrder 순서로 정렬)
	    response.sort(function (a, b) {
	        if (a.menuDepth === b.menuDepth) {
	            return a.menuOrder - b.menuOrder; // 같은 depth라면 menuOrder로 정렬
	        }
	        return a.menuDepth - b.menuDepth; // depth 기준 정렬
	    });

	    console.log("AJAX 응답 데이터 (정렬 후):", response);

	    // jsTree 데이터 형식으로 변환
	    const processedData = processJsTreeData(response);

	    console.log("jsTree 변환 데이터:", processedData);

	    $('#jstree').jstree('destroy').empty();
	    // jsTree 초기화
	    $('#jstree').jstree({
	        'core': {
	            'data': function (node, callback) {
	                // 루트 노드 (#) 또는 특정 노드의 children 반환
	                if (node.id === "#") {
	                    callback(processedData.filter(item => item.parent === "#"));
	                } else {
	                    callback(processedData.filter(item => item.parent === node.id));
	                }
	            },
	            'themes': {
	                'dots': true,
	                'icons': true
	            }
	        },
	        "plugins": ["types", "search"],
	        "types": {
	            "default": { "icon": "bi bi-house-fill" }, // 기본 폴더 아이콘
	            "file": { "icon": "bi bi-person-fill" }    // 파일 아이콘
	        },
	        "search": {
	            "show_only_matches": true,
	            "show_only_matches_children": true
	        }
	    }).on('loaded.jstree', function () {
	        console.log("jsTree가 성공적으로 초기화되었습니다.");
	        $("#jstree").jstree("open_all");

	        // 검색 이벤트 처리
	        let searchTimeout = null;
	        $('.input-test').on('input', function () {
	            const search = $(this).val();

	            // 이전 타임아웃 제거
	            if (searchTimeout) {
	                clearTimeout(searchTimeout);
	            }

	            // 입력 후 300ms 후에 검색 실행
	            searchTimeout = setTimeout(function () {
	                $('#jstree').jstree('search', search);
	            }, 300);
	        });

	    }).on('changed.jstree', function (e, data) {
	        console.log("선택된 노드:", data.selected);
	        if (data.selected.length > 0) {
	        	if(document.getElementById('orgBody').childNodes.length > 3){
            		layerPopup( "결재자는 최대3명까지 선택 가능합니다.","확인",false,removeAlert,removeAlert);
			    	return false;
			    } 
	            const selectedId = data.selected[0]; // 선택된 노드의 ID
	            console.log("선택된 노드 ID:", selectedId);

	            // 설정된 콜백 함수 호출
	            if (typeof selectIdCallback === "function") {
	                selectIdCallback(selectedId); // 콜백 함수에 선택된 ID 전달
	            }
	        } else {
	            console.log("선택된 노드가 없습니다.");
	        }
	    }).on("load_node.jstree", function (e, data) {
	        console.log("노드 로드 완료:", data.node);
	    });
	    
	    
	    // 나의 결재라인 추가
        $('#approvalBtn').text('나의 결재라인');
        
    	$('.chart-btn-style').on('click', function(){
	        /* 조직도 버튼 클릭 시 조직도 버튼 show 나의 결재선 hide */
	    	if($(this).hasClass('chart-btn')){
	    		$('#chartModalBox .tbody-style').show();
	    		/* 조직도 전체 열기 */
	    		/* $("#jstree").jstree("open_all");
	    		
	    		// 다른 td 스타일 초기화
	    		$('td').css({
	    	        'border-bottom': 'none', 
	    	        'font-weight': 'normal'
	    	    });
	    		
	    		$('#jstree').show();
	    		
	    		$('#myjstree').hide();
	    		
	    		// 클릭된 버튼의 td 스타일 변경 
	    		$(this).closest('td').css({
	    	        'border-bottom': '1px solid gray', 
	    	    }); */
	    	}
	        
	    	if($(this).hasClass('chart-mybtn')){
		    	/* 나의 결재선 버튼 클릭 시 나의 결재선 show 조직도 버튼 hide */
		    	//$('.chart-mybtn').on('click', function() {
		    		$('#chartModalBox .tbody-style').hide();
		    		
		    		$.ajax({
		    			type:'GET',
		    			url:'/approval/list/line',
		    			data:{
		    			},
		    			datatype:'JSON',
		    			success:function(data){
		    				console.log(data);
		    				drawList(data.bookmarkList);
		    			},
		    			error:function(e){
		    				console.log(e);
		    			}
		    		});
			}
   		}); 
   	}
 
 
 	// 즐겨찾기 
	var thead ='';
 	function drawList(list) {
		var content ='';
		var approverNames = '';
		content += '<tbody class="bookmark">';
	 	for (var view of list) {
	 		approverNames = view.approver_names.split(', ');
	 		var approverPositions = view.approver_positions.split(', ');
	 		console.log("approverNames : ", approverNames);
	 		
			content += '<tr>';
			content += '<td class="line" data-view=\'' + JSON.stringify(view) + '\' colspan="2">'+view.line_name+'</td>';
			// 결재자 수 만큼
	 	 	for (var i=0; i<approverNames.length; i++) {
	 	 		//content += '<td>'+ approverNames[i] +' '+ approverPositions[i] +'</td>';
	 	 	}
			content += '</tr>';
	   } 
		content += '</tbody>';
		
		$('#chartModalBox .bookmark').remove();
	    $('#chartModalBox table.left').append(content);
		
	    // thead
		thead += '<tr>';
		thead += '<th>NO</th>';
		thead += '<th class="align-l">결재라인명</th>';
	 	//결재자 수만큼
		for (var i=0; i<approverNames.length; i++) {
			thead += '<th>결재자'+(i+1)+'</th>';
		}
		thead += '<th>삭제</th>';
		thead += '</tr>';
	    $('.list thead').html(thead);
		
		//console.log("bookmarkList:  ", '${bookmarkList}');
	}
 	
 	
 // 즐겨찾기 -> 결재라인 추가 
 $(document).on('click', '.bookmark .line', function() {
    //비우고
    resetTableData();
	var view = JSON.parse($(this).attr('data-view'));
    var usernames = JSON.parse(view.approver_usernames);
    for (var i=1; i<usernames.length; i++){
  		addSelectedIdToRows(usernames[i]);
    }
});
 	
 	
//조직도 노드 해당 사원 삭제
 $(document).on('click', '#chartModalBox #orgBody .btn', function() {
     var idx = $(this).closest('tr').index();
     if(idx != 0){
 	    $(this).closest('tr').remove();
 	    initialData.rows.splice(idx, 1);
 	    exampleData.rows.splice(idx, 1);
 	    approvalLines.splice(idx, 1);
 	    console.log("approvalLines 수정 : ",approvalLines);
     }else{
     	layerPopup( "기안자는 삭제하실 수 없습니다.","확인",false,removeAlert,removeAlert);
     }
 });
	
	
 // 조직도노드 등록버튼 (결재라인 설정)
 var addBtn = document.getElementById('addModal');
/*  addBtn.addEventListener("click", function () {
	 console.log("등록 : ", document.getElementById('orgBody').childNodes);
 }); */
 
function addBtnFn(){
	 var lineNodes = document.getElementById('orgBody').childNodes;
	 // 기안문 기존 결재라인 설정 초기화 
	 $('.appr_line tr.name > td > p').text('');
	 $('.appr_line tr.position > td').text('');
	 $('input[name="appr_user"]').val('');
	 // 기안문 결재라인 설정
	 for(var i = 0; i <= lineNodes.length ; i++){
		 userName = lineNodes[i].childNodes[0].innerText;
		 userPosition = lineNodes[i].childNodes[2].innerText;
		 document.querySelectorAll('.appr_line tr.name > td > p')[i].innerText = userName;
		 document.querySelectorAll('.appr_line tr.position > td')[i].innerText = userPosition;
		 document.querySelectorAll('input[name="appr_user"]')[i].value = approvalLines[i];
	 	 document.getElementById('chartModalBox').style.display = "none"; 
	 	 //console.log("approvalLines",approvalLines[i]);

	 	 //console.log("###appr_line tr.name ",document.querySelectorAll('.appr_line tr.name > td > p')[i].innerText );
	 	// console.log("###position ",document.querySelectorAll('.appr_line tr.position > td')[i].innerText );
	 	 //console.log("###appr_user ",document.querySelectorAll('.input[name="appr_user"]'));
	 }
}
 
 function setModalData(){}
 document.addEventListener('click', function(event){
    // 결재라인 즐겨찾기 모달 등록 버튼 클릭
    if(event.target.id === 'SaveBookmark'){
    	saveApprvalLine();
    }
});
 
 

var csrfToken = document.querySelector('meta[name="_csrf"]').content;
var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

function saveApprvalLine() {
	console.log("approvalLines",approvalLines);
	var formData = new FormData($('#BookmarkFrom')[0]);
	formData.append('approvalLines', JSON.stringify(approvalLines));
	$.ajax({
        type : 'POST',
        url : '/approval/setApprLineBookmark',
        data : formData,
        processData: false, 
        contentType: false,
        dataType : 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success : function(response){
            console.log("성ㅇ공:", response.success);
            if(response.success){
            	document.getElementById('modalBox').style.display = "none";
            	layerPopup( "결재라인 저장이 저장되었습니다.","확인",false,removeAlert,removeAlert);
            }
        },error : function(e){
            console.log(e);
        }
    });
	
}
