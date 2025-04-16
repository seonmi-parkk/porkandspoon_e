<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>결재라인 즐겨찾기 리스트</title>

<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!-- 부트스트랩 -->
<link rel="shortcut icon"
	href="/resources/assets/compiled/svg/favicon.svg" type="image/x-icon">
<link rel="shortcut icon"
	href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAiCAYAAADRcLDBAAAEs2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS41LjAiPgogPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgeG1sbnM6ZXhpZj0iaHR0cDovL25zLmFkb2JlLmNvbS9leGlmLzEuMC8iCiAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIKICAgIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIKICAgIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIKICAgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIgogICAgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIKICAgZXhpZjpQaXhlbFhEaW1lbnNpb249IjMzIgogICBleGlmOlBpeGVsWURpbWVuc2lvbj0iMzQiCiAgIGV4aWY6Q29sb3JTcGFjZT0iMSIKICAgdGlmZjpJbWFnZVdpZHRoPSIzMyIKICAgdGlmZjpJbWFnZUxlbmd0aD0iMzQiCiAgIHRpZmY6UmVzb2x1dGlvblVuaXQ9IjIiCiAgIHRpZmY6WFJlc29sdXRpb249Ijk2LjAiCiAgIHRpZmY6WVJlc29sdXRpb249Ijk2LjAiCiAgIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiCiAgIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIKICAgeG1wOk1vZGlmeURhdGU9IjIwMjItMDMtMzFUMTA6NTA6MjMrMDI6MDAiCiAgIHhtcDpNZXRhZGF0YURhdGU9IjIwMjItMDMtMzFUMTA6NTA6MjMrMDI6MDAiPgogICA8eG1wTU06SGlzdG9yeT4KICAgIDxyZGY6U2VxPgogICAgIDxyZGY6bGkKICAgICAgc3RFdnQ6YWN0aW9uPSJwcm9kdWNlZCIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWZmaW5pdHkgRGVzaWduZXIgMS4xMC4xIgogICAgICBzdEV2dDp3aGVuPSIyMDIyLTAzLTMxVDEwOjUwOjIzKzAyOjAwIi8+CiAgICA8L3JkZjpTZXE+CiAgIDwveG1wTU06SGlzdG9yeT4KICA8L3JkZjpEZXNjcmlwdGlvbj4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InIiPz5V57uAAAABgmlDQ1BzUkdCIElFQzYxOTY2LTIuMQAAKJF1kc8rRFEUxz9maORHo1hYKC9hISNGTWwsRn4VFmOUX5uZZ36oeTOv954kW2WrKLHxa8FfwFZZK0WkZClrYoOe87ypmWTO7dzzud97z+nec8ETzaiaWd4NWtYyIiNhZWZ2TvE946WZSjqoj6mmPjE1HKWkfdxR5sSbgFOr9Ll/rXoxYapQVik8oOqGJTwqPL5i6Q5vCzeo6dii8KlwpyEXFL519LjLLw6nXP5y2IhGBsFTJ6ykijhexGra0ITl5bRqmWU1fx/nJTWJ7PSUxBbxJkwijBBGYYwhBgnRQ7/MIQIE6ZIVJfK7f/MnyUmuKrPOKgZLpEhj0SnqslRPSEyKnpCRYdXp/9++msneoFu9JgwVT7b91ga+LfjetO3PQ9v+PgLvI1xkC/m5A+h7F32zoLXug38dzi4LWnwHzjeg8UGPGbFfySvuSSbh9QRqZ6H+Gqrm3Z7l9zm+h+iafNUV7O5Bu5z3L/wAdthn7QIme0YAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAJTSURBVFiF7Zi9axRBGIefEw2IdxFBRQsLWUTBaywSK4ubdSGVIY1Y6HZql8ZKCGIqwX/AYLmCgVQKfiDn7jZeEQMWfsSAHAiKqPiB5mIgELWYOW5vzc3O7niHhT/YZvY37/swM/vOzJbIqVq9uQ04CYwCI8AhYAlYAB4Dc7HnrOSJWcoJcBS4ARzQ2F4BZ2LPmTeNuykHwEWgkQGAet9QfiMZjUSt3hwD7psGTWgs9pwH1hC1enMYeA7sKwDxBqjGnvNdZzKZjqmCAKh+U1kmEwi3IEBbIsugnY5avTkEtIAtFhBrQCX2nLVehqyRqFoCAAwBh3WGLAhbgCRIYYinwLolwLqKUwwi9pxV4KUlxKKKUwxC6ZElRCPLYAJxGfhSEOCz6m8HEXvOB2CyIMSk6m8HoXQTmMkJcA2YNTHm3congOvATo3tE3A29pxbpnFzQSiQPcB55IFmFNgFfEQeahaAGZMpsIJIAZWAHcDX2HN+2cT6r39GxmvC9aPNwH5gO1BOPFuBVWAZue0vA9+A12EgjPadnhCuH1WAE8ivYAQ4ohKaagV4gvxi5oG7YSA2vApsCOH60WngKrA3R9IsvQUuhIGY00K4flQG7gHH/mLytB4C42EgfrQb0mV7us8AAMeBS8mGNMR4nwHamtBB7B4QRNdaS0M8GxDEog7iyoAguvJ0QYSBuAOcAt71Kfl7wA8DcTvZ2KtOlJEr+ByyQtqqhTyHTIeB+ONeqi3brh+VgIN0fohUgWGggizZFTplu12yW8iy/YLOGWMpDMTPXnl+Az9vj2HERYqPAAAAAElFTkSuQmCC"
	type="image/png">

<!-- select -->
<link rel="stylesheet"
	href="/resources/assets/extensions/choices.js/public/assets/styles/choices.css">

<!-- 부트스트랩 -->
<link rel="stylesheet" href="/resources/assets/compiled/css/app.css">
<link rel="stylesheet" href="/resources/assets/compiled/css/iconly.css">
<link rel="stylesheet" href="/resources/css/common.css">

<!-- jstree -->
<link rel="stylesheet" href="/resources/css/chartModal.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />

<style>
.apprLineList table tr:hover {
	background: #fbfbfb;
}

.apprLineList input {
	width: 100%;
	height: 100%;
	border: none;
}

.apprLineList .search-area {
	display: flex;
	gap: 4px;
}

.apprLineList input[name="search-data"] {
	width: 300px;
	height: 38px;
	margin: 14px 0;
	border: 1px solid #DFE3E7;
}

.apprLineList .search-area select {
	margin: 14px 0;
}

.apprLineList .buttons {
	display: flex;
    justify-content: flex-end;
	padding: 4px 40px;
}

.apprLineList .buttons .btn {
	margin: 18px 0 0;
}

.apprLineList h4.doc-subject {
	margin: 20px 0 50px;
	text-align: center;
}

.apprLineList .btm-area {
	display: flex;
	flex-wrap: wrap;
	border-left: 1px solid #ddd;
	border-top: 1px solid #ddd;
	margin-top: 40px;
}
.apprLineList table {
    table-layout: auto;
}
.apprLineList table td.delete:hover{
    cursor: pointer;
}



/* 조직도 모달 */
	#chartModalBox {
    	height: 550px;
    }
	#chartModalBox .chart-td.active {
		border-bottom: 2px solid #333;
	}
	#chartModalBox .left thead {
		margin-bottom: 10px;
	}
	#chartModalBox .bookmark {
	    transform: translateY(10px);
	}
	#chartModalBox .bookmark tr {
		border: none;
	}
	#chartModalBox .bookmark td {
		padding: 8px 16px;
	    text-align: left;
   		font-weight: 500;
   		cursor: pointer;
	}
	
	
	/* 모달 */
	/* 기본 모달 스타일 */
	.modal-dialog {
    	position: static;
    }
	.modal {
	    display: none;
	    position: fixed;
	    top: 0;
	    left: 0;
	    width: 100%;
	    height: 100%;
	    background-color: rgba(0, 0, 0, 0.5);
	    z-index: 1100;
	}
	
	/* 모달 내부 콘텐츠 */
	.modal-content {
	    position: absolute;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
	    background: #fff;
	    padding: 20px;
	    border-radius: 8px;
	    width: 400px;
	    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	}

	/* 모달 헤더 */
	.modal-header {
	    display: flex;
	    justify-content: end;
	    align-items: center;
	    border-bottom: 1px none #ddd;
	    margin-bottom: 15px;
	}

	/* 닫기(x) 버튼 */
	.modal-close {
	    font-size: 30px;
	    cursor: pointer;
	}
	
	/* 모달 바디 */
	.modal-body .form-group {
	    margin-bottom: 15px;
	}
	
	.form-label {
	    display: block;
	    font-size: 14px;
	    margin-bottom: 5px;
	}
	
	.form-input {
	    width: 100%;
	    padding: 8px;
	    font-size: 14px;
	    border: 1px solid #ddd;
	    border-radius: 4px;
	}
	
	/* 모달 푸터 */
	.modal-footer {
	    display: flex;
	    justify-content: center;
	    gap: 10px;
	}
	#modalBox .modal-close {
    	font-size: 30px;
    }
	#modalBox h5 {
		padding-bottom: 16px;
	}
	#modalBox .form-control {
		display: inline-block;
	}
	#modalBox .input-row {
	    display: flex;
		margin: 6px 0;
	}
	#modalBox .item-tit {
		width: 88px;
	    flex-shrink: 0;
		margin-top: 4px;
	}
	#modalBox input {
		padding-left: 10px;
	}
 	#modalBox .btn{ 
	 	margin: 16px 5px 0;
	}
</style>

</head>

<body>
	<!-- 부트스트랩 -->
	<script src="/resources/assets/static/js/initTheme.js"></script>
	<div id="app">

		<!-- 사이드바 -->
		<jsp:include page="../sidebar.jsp" />

		<div id="main">
			<!-- 헤더 -->
			<jsp:include page="../header.jsp" />

			<div class="page-content apprLineList">
				<section id="menu">
					<h4 class="menu-title">나의문서함</h4>
					<ul>
						<li><a href="/approval/listView/my">기안문서함</a></li>
						<li><a href="/approval/listView/tobe">결재할 문서</a></li>
						<li><a href="/approval/listView/did">결재한 문서</a></li>
						<li><a href="/approval/listView/sv">임시저장 문서</a></li>
						<li class="active"><a href="/approval/listView/line">나의 결재라인</a></li>
					</ul>
					<div class="btn btn-primary full-size" onclick="location.href='/approval/writeView'">결재작성</div>
				</section>
				<section class="cont">

					<div class="col-12 col-lg-12">
						<div class="tit-area">
							<h5>나의 결재라인</h5>
						</div>
						<div class="buttons">
							<div class="btn-group" role="group" aria-label="Basic example">
								<button type="button" class="btn btn-primary" onclick="loadChartModal('chartInputModal')">결재라인 등록/수정</button>
							</div>
						</div>
						<div class="cont-body">

							<table class="table-list">
								<colgroup>
									<col width="60px;">
									<col width="20%;">
									<col>
									<col>
									<col>
									<col>
									<col>
								</colgroup>
								<thead>
									<tr>
										<th>NO</th>
										<th class="align-l">결재라인명</th>
										<th>결재자1</th>
										<th>결재자2</th>
										<th>결재자3</th>
										<th>삭제</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>

							<nav aria-label="Page navigation">
								<ul class="pagination justify-content-center" id="pagination"></ul>
							</nav>
						</div>
					</div>
				</section>
			</div>
		</div>
	</div>
	
	
	<!-- 조직도 모달 사용 시 추가 -->
	<div id="chartModalBox" class="modal" style="display: none;">
    	<div class="chartModal-content"></div>
	</div>
	<input type="hidden" id="selectedNodeInput" value=""/>
	
	<!-- 모달 -->
	<div id="modalBox" class="modal" style="display: none;">
        <div class="modal-content"></div>
    </div>
    
</body>

<!-- 부트스트랩 -->
<script src="/resources/assets/compiled/js/app.js"></script>
<script src='/resources/js/common.js'></script>

<!-- 페이지네이션 -->
<script src="/resources/js/jquery.twbsPagination.js"
	type="text/javascript"></script>
	
<!-- jstree -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
<!-- 조직도 js -->
<script src='/resources/js/charjstree.js'></script>
<script>
var show = 1;

var totalCount = 0;
var pageSize = 15;  // 한 페이지당 게시글 수  //check!!! cnt를 얘로??
var totalPage = 0;

pageCall(show);

function pageCall(page) {
	//console.log("page : ", page);
	$.ajax({
		type:'GET',
		url:'/approval/list/line',
		data:{
			'page':page,
			'cnt':15
		},
		datatype:'JSON',
		success:function(data){
			console.log(data);
			drawList(data.bookmarkList);

			if(data.bookmarkList.length > 0){
				totalCount = data.bookmarkList[0].total_count;  // 총 게시글 수
	            totalPage = Math.ceil(totalCount / pageSize);  // 총 페이지 수 계산
			}
			console.log("totalCount",totalCount,"totalPage",totalPage);
            
			
            
			 /* 페이지네이션 */       

			$('#pagination').twbsPagination({
				startPage: 1, 
           		totalPages: totalPage, 
           		visiblePages:10,
           		onPageClick:function(evt,page){
           			console.log('evt',evt); 
           			console.log('page',page); 
           			pageCall(page);
           		}
			});

		},
		error:function(e){
			console.log("에러러러ㅓ");
			console.log(e);
		}
	});
}

function drawList(list) {
	var thead ='';
	var content ='';
	if(totalCount == 0){
		content +='<tr><td colspan="8">조회된 데이터가 없습니다.</td></tr>';
		$('.table-list tbody').html(content);
		return false;
	}
	
	var approverNames = '';
 	for (var view of list) {
 		approverNames = view.approver_names.split(', ');
 		var approverPositions = view.approver_positions.split(', ');
 		console.log("approverNames : ", approverNames);
 		
 	 	// tbody
		content += '<tr>';
		content += '<td>'+view.line_idx+'</td>';
		content += '<td class="align-l">'+view.line_name+'</td>';
		// 결재자 수 만큼
 	 	for (var i=0; i<approverNames.length; i++) {
 	 		content += '<td>'+ approverNames[i] +' '+ approverPositions[i] +'</td>';
 	 	}

		// 결재자 수에 따라 달라지는 td갯수 맞추기
	    var numColumns = approverNames.length;  
	    var maxColumns = 3;  // 최대
	    var missingColumns = maxColumns - numColumns; 
        // <td> 추가
	    if (missingColumns > 0) {
	        for (var j = 0; j < missingColumns; j++) {
	        	content += '<td></td>';
	        }
	    }
		content += '<td class="delete" data-lineidx="'+view.line_idx+'" ><i class="bi bi-trash3"></i></td>';
		content += '</tr>';
   } 
 	
    $('.table-list tbody').html(content);
	
	console.log("bookmarkList:  ", '${bookmarkList}');
}
$(document).on('click','.table-list td.delete',function(){
	var lineIdx = $(this).data('lineidx');
	layerPopup('해당 결재라인을 삭제하시겠습니까?', '삭제', '취소', deleteAct, btn1Act);

	function deleteAct(){
		$.ajax({
	        type : 'DELETE',
	        url : '/approval/DeleteBookmark/'+lineIdx,
	        data : {},
	        dataType : 'JSON',
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader(csrfHeader, csrfToken);
	        },
	        success : function(response){
	        	 if(response.success){
	     		 	removeAlert(); 
	      			layerPopup('삭제 완료되었습니다.', '확인', false, reloadAct, btn1Act);
	     		 }else{
	     		 	removeAlert(); 
	      			layerPopup('삭제 실패하였습니다.', '재시도', '취소', deleteAct, btn1Act);
	     		 }
	        },error: function(e){
	            console.log(e);
	        }
	    });
	}
});

/* $(document).on('click', '.td.delete', function() {
	alert('dd');
}); */

// 삭제 팝업 (삭제버튼)
var csrfToken = document.querySelector('meta[name="_csrf"]').content;
var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
		


// 삭제 팝업 (취소버튼)
function btn1Act(){
	removeAlert();
}
//삭제확인 후 팝업 (확인버튼)
function reloadAct() {
	location.reload();
	removeAlert();
}




/* 조직도노드  */
//초기 데이터
var lineIdx = null;
var lineName = null;
const initialData = {
 headers: ['이름', '부서', '직급', '구분', '삭제'],
 rows: [
     ['${userDTO.name}', '${userDTO.dept.text}', '${userDTO.position_content}', '기안', '<button class="btn btn-primary">삭제</button>'],
 ],
 footer: '<button class="btn btn-outline-secondary btn-line-write" onclick="loadModal(\'ApprLine\',\'Bookmark\',{\'lineIdx\':lineIdx, \'lineName\':lineName})">저장</button>'
};
var exampleData = JSON.parse(JSON.stringify(initialData));

//서버에 넘길 결재자 id배열
var approvalLines = ['${userDTO.username}'];

//선택된 ID를 rows에 추가하는 함수
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

//선택된 ID를 받아서 처리
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
	    		$('#chartModalBox .bookmark').hide();
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
		    		$('#chartModalBox .bookmark').show();
		    		
		    		$.ajax({
		    			type:'GET',
		    			url:'/approval/list/line',
		    			data:{
		    			},
		    			datatype:'JSON',
		    			success:function(data){
		    				console.log(data);
		    				drawBookmarkList(data.bookmarkList);
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
	function drawBookmarkList(list) {
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
	
	
//즐겨찾기 -> 결재라인 추가 
$(document).on('click', '.bookmark .line', function() {
	 //비우고
 	//resetTableData();
    // 기존 rows에 추가
    console.log('exampleData.rows',exampleData.rows);
   // initialData.rows = ['${userDTO.username}'];
   // exampleData.rows = ['${userDTO.username}'];
   // approvalLines = ['${userDTO.username}'];
   var lineLength = approvalLines.length;
    initialData.rows.splice(1, lineLength-1);
    exampleData.rows.splice(1, lineLength-1);
    approvalLines.splice(1, lineLength-1);
    
    console.log('approvalLines',approvalLines);

    // 테이블 업데이트 (id가 'customTable'인 테이블에 적용)
   updateTableData('customTable', exampleData);
    
    
    
    
	var view = JSON.parse($(this).attr('data-view'));
 	var usernames = JSON.parse(view.approver_usernames);
	lineIdx = JSON.parse(view.line_idx);
	lineName = view.line_name;
	console.log('lineIdx',lineIdx);
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
	
	
//조직도노드 등록버튼 (결재라인 설정)
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
	console.log("approvalLines저장전",approvalLines);
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
         	layerPopup( "결재라인 저장이 저장되었습니다.","확인",false,function(){location.reload();},removeAlert);
         }
     },error : function(e){
         console.log(e);
     }
 });
	
}



//모달데이터(라인저장)
function setModalData(type, data){
	console.log("data:::",data);
    
	$('#BookmarkFrom input[name="line_idx"]').val(data.lineIdx);
	$('#BookmarkFrom input[name="line_name"]').val(data.lineName);
}

</script>

</html>