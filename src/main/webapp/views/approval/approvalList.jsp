<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>문서함</title>

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


<style>
.draftList .cont-body{
	padding-top: 10px;
}
.draftList table tr:hover {
	background: #fbfbfb;
}
.draftList input {
	width: 100%;
	height: 100%;
	border: none;
}

.draftList .search-area {
	float: right;
	display: flex;
	gap: 4px;
}

.draftList input[name="search-data"] {
	width: 300px;
	height: 38px;
	margin: 14px 0;
	border: 1px solid #DFE3E7;
}

.draftList .search-area select {
	margin: 14px 0;
}


.draftList .buttons {
	margin-top: 18px;
	padding: 4px 40px;
}

.draftList .buttons .btn-group {
	float:left;
}

.draftList .buttons .btn {
	margin: 14px 0;
}
.draftList .buttons .btn.selected {
    background: var(--bs-primary);
    border-color: var(--bs-primary);
	color: #fff;
}

    
.draftList h4.doc-subject {
	margin: 20px 0 50px;
	text-align: center;
}

.draftList .btm-area {
	display: flex;
	flex-wrap: wrap;
	border-left: 1px solid #ddd;
	border-top: 1px solid #ddd;
	margin-top: 40px;
}

.draftList table {
	table-layout: unset;
}
.draftList table td.cursor-pt {
	cursor: pointer;
}
.draftList table td.status.sd{
	color: var(--bs-primary);
}
.draftList table td.status.re{
	color: var(--bs-danger);
}
.draftList table td.status.ca{
	color: var(--bs-secondary);
}
.draftList table td.status.co{
	color: var(--bs-success);
}
.draftList table .subject {
	cursor: pointer;
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

			<div class="page-content draftList">
				<section id="menu">
					<c:if test="${listType != 'dept'}">
						<h4 class="menu-title">나의문서함</h4>
						<ul>
							<li <c:if test="${listType == 'my'}">class="active"</c:if>><a href="/approval/listView/my">기안문서함</a></li>
							<li <c:if test="${listType == 'tobe'}">class="active"</c:if>><a href="/approval/listView/tobe">결재할 문서</a></li>
							<li <c:if test="${listType == 'did'}">class="active"</c:if>><a href="/approval/listView/did">결재한 문서</a></li>
							<li <c:if test="${listType == 'sv'}">class="active"</c:if>><a href="/approval/listView/sv">임시저장 문서</a></li>
							<li><a href="/approval/listView/line">나의 결재라인</a></li>
						</ul>
					</c:if>
					<c:if test="${listType == 'dept'}">
						<h4 class="menu-title">부서문서함</h4>
					</c:if>
					<div class="btn btn-primary full-size" onclick="location.href='/approval/writeView'">결재작성</div>
				</section>
				<section class="cont">
					<div class="col-12 col-lg-12">
						<div class="tit-area">
							<h5>
								<c:choose>
									<c:when test="${listType == 'my'}">기안문서함</c:when>
									<c:when test="${listType == 'tobe'}">결재할 문서</c:when>
									<c:when test="${listType == 'did'}">결재한 문서</c:when>
									<c:when test="${listType == 'sv'}">임시저장 문서</c:when>
									<c:when test="${listType == 'dept'}">부서문서함</c:when>
								</c:choose>
						
							</h5>
						</div>
						<div class="buttons filter-btns clearfix">
							<c:if test="${listType == 'my'}">
								<div class="btn-group" role="group" aria-label="Basic example">
									<button type="button" data-filter="all" class="btn selected">전체</button>
									<button type="button" data-filter="sd" class="btn">진행</button>
									<button type="button" data-filter="co" class="btn">완료</button>
									<button type="button" data-filter="re" class="btn">반려</button>
									<button type="button" data-filter="ca" class="btn">회수</button>
								</div>
							</c:if>
							<div class="search-area">
								<select name="search" class="form-select">
									<option value="subject">제목</option>
								</select> <input type="text" name="search-data" class="form-control"
									placeholder="검색내용을 입력하세요" width="80%" />
								<button id="searchBtn" class="btn btn-primary">
									<i class="bi bi-search"></i>
								</button>
							</div>
							<!-- <button href="#" class="btn btn-primary"></button>
							<button href="#" class="btn btn-outline-primary">결재 정보</button>
							<button href="#" class="btn btn-outline-primary">임시저장</button>
							<button href="#" class="btn btn-outline-primary">취소</button> -->
						</div>
						<div class="cont-body">
							<table class="list">
								<colgroup>
									<col>
									<col width="30%">
									<col>
									<col>
									<col>
									<col>
								</colgroup>
								<thead>
									<tr>
										<!-- 임시저장 -->
										<c:if test="${listType == 'sv'}">
											<th>저장일시</th>
											<th class="align-l">제목</th>
											<th>결재양식</th>
											<th>삭제</th>
										</c:if>		
										<!-- 임시저장 외 나머지 기본항목 -->
										<c:if test="${listType != 'sv'}">
											<th>문서번호</th>
											<th class="align-l">제목</th>
										</c:if>
										<!-- 부서 문서 -->
										<c:if test="${listType == 'dept'}">
											<th>구분</th>
										</c:if>
										<!-- 임시저장 외 나머지 기본항목 -->
										<c:if test="${listType != 'sv'}">
											<th>기안자</th>
											<th>부서</th>
										</c:if>
										<!-- 결재한 문서 -->
										<c:if test="${listType == 'did'}">
											<th>결재일자</th>
										</c:if>
										<!-- 기안문서 -->
										<c:if test="${listType == 'my'}">
											<th>결재일자</th>
											<th>결재상태</th>
										</c:if>
										<!-- 결재할 문서 -->
										<c:if test="${listType == 'tobe'}">
											<th>기안일자</th>
										</c:if>
										<!-- 부서 문서 -->
										<c:if test="${listType == 'dept'}">
											<th>결재일자</th>
											<th>결재상태</th>
										</c:if>
									
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
</body>

<!-- 부트스트랩 -->
<script src="/resources/assets/compiled/js/app.js"></script>
<script src='/resources/js/common.js'></script>

<!-- 페이지네이션 -->
<script src="/resources/js/jquery.twbsPagination.js"
	type="text/javascript"></script>

<script>
	var listType = '${listType}';
	console.log("listType : ",listType);
	var show = 1;
	var paginationInitialized = false;
	var option = '';
	var search = '';
	var filter = '';
	
	var totalCount = 0;
    var pageSize = 15;  // 한 페이지당 게시글 수  //check!!! cnt를 얘로??
    var totalPage = 0;
	var filterElem = '';
	
	pageCall(show, listType);
	
	function pageCall(page, listType) {
		option = document.querySelector('select[name="search"]').value;
		search = document.querySelector('input[name="search-data"]').value;
		filterElem = document.querySelector('.filter-btns .btn.selected');
		if(filterElem){		
			filter = filterElem.getAttribute('data-filter');
			console.log("filter!!!! ",filter );
		}
		console.log("option",option);
		console.log("search",search);
		
		$.ajax({
			type:'GET',
			url:'/approval/list/'+listType,
			data:{
				'page':page,
				'cnt':15,
				'search' : search,
				'option' : option,
				'filter': filter,
				'listType': listType
			},
			datatype:'JSON',
			success:function(data){
				console.log(data);
				drawList(data.approvalList, listType);

				if(data.approvalList.length > 0){
					totalCount = data.approvalList[0].total_count;  // 총 게시글 수
		            totalPage = Math.ceil(totalCount / pageSize);  // 총 페이지 수 계산
				}
				console.log("totalCount",totalCount,"totalPage",totalPage);
	            
	            
				 /* 페이지네이션 */       
				 if(!paginationInitialized){
						$('#pagination').twbsPagination('destroy');
						$('#pagination').twbsPagination({
							startPage:1, 
			           		totalPages: totalPage, 
			           		visiblePages:10,
			           		onPageClick:function(evt,page){
			           			console.log('evt',evt); 
			           			console.log('page',page); 
			           			pageCall(page, listType);
			           		}
						});
						paginationInitialized = true;
		            }
			},
			error:function(e){
				console.log(e);
			}
		});
	}

	function drawList(list, listType) {
		var content ='';
		var approvalDate = '';
		var type1='';
		var type2='';
		if(totalCount == 0){
			content +='<tr><td colspan="8">조회된 데이터가 없습니다.</td></tr>';
			$('.list tbody').html(content);
			return false;
		}
		for (var view of list) {
			if(view.approval_date != null){
				approvalDate = view.approval_date.split(' ')[0];
			}else{
				approvalDate = '-';
			}
			
			content +='<tr>';
			if(listType == 'sv'){
				type1 = view.target_type == 'df001' ? '브랜드' : '직영점';
				type2 = view.action_type == 'df011' ? '오픈' : '폐점';
				
				content += '<td>'+view.create_date+'</td>';
	            content += '<td class="align-l elipsis cursor-pt" onclick="location.href=\'/approval/detail/'+view.draft_idx+'\'">'+view.subject+'</td>';
	            content += '<td>'+type1+' '+type2+'</td>';
	            content += '<td class="cursor-pt delete" data-draftidx="'+view.draft_idx+'" >삭제</td>';
			}
			if(listType != 'sv'){
	            content += '<td>'+view.document_number+'</td>';
	            content += '<td class="align-l elipsis subject" onclick="location.href=\'/approval/detail/'+view.draft_idx+'\'">'+view.subject+'</td>';
			}
			if(listType == 'dept'){
				// mapper에 user_parent 가져오도록함!!!! 오류안나는지 체크하기!! => dept 테이블 조인해서  텍스트가져오기 추가해야함!!
				 if(view.user_dept_text == view.dept_text){				 
					 content += '<td>부서</td>';
				 }else{
					 content += '<td>협조부서</td>';
				 }
			}
			if(listType != 'sv'){
				content +='<td>'+view.name+'</td>';
				content +='<td>'+view.dept_text+'</td>';	
			}
			if(listType == 'did'){
				content +='<td>'+approvalDate+'</td>';
			}
			if(listType == 'my'){
				content +='<td>'+approvalDate+'</td>';
				content +='<td class="status '+view.status+'">'+view.status_name+'</td>';
			}
			if(listType == 'tobe'){
				 content += '<td>'+view.create_date+'</td>';
			}
			if(listType == 'dept'){
				 content += '<td>'+approvalDate+'</td>';
     			 content +='<td class="status '+view.status+'">'+view.status_name+'</td>';
			}
			content +='</tr>';
			console.log("view.draft_idx",view.draft_idx);
		  }
	     $('.list tbody').html(content);
	   }
	
	$(document).on('click','.list td.delete',function(){
		var draftIdx = $(this).data('draftidx');
		layerPopup('해당 기안문을 삭제하시겠습니까?', '삭제', '취소', deleteAct, btn1Act);
		
		// 기안문 삭제
		function deleteAct() {
			console.log("draftIdx값좀 받아와라 : "+draftIdx);
			$.ajax({
		        type : 'PUT',
		        url : '/approval/changeStatusToDelete/'+draftIdx,
		        data : {},
		        dataType : 'JSON',
		        beforeSend: function(xhr) {
		            xhr.setRequestHeader(csrfHeader, csrfToken);
		        },
		        success : function(response){
		        	 if(response.success){
		     		 	removeAlert(); 
		      			layerPopup('삭제 완료되었습니다.', '확인', false, btn1Act, btn1Act);
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
	
		// 검색이벤트
		$('#searchBtn').on('click', function() {
		    show = 1;
		    paginationInitialized = false;
		    pageCall(show, listType);  // 검색어가 추가된 상태에서 호출
		});
	
		// 필터링
		$('.filter-btns .btn').on('click', function() {
			$(this).siblings().removeClass('selected');
			$(this).addClass('selected');
			show = 1;
		    paginationInitialized = false;
		    pageCall(show, listType);  // 검색어가 추가된 상태에서 호출
		});
		
		var csrfToken = document.querySelector('meta[name="_csrf"]').content;
		var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
		
		function btn1Act() {
			location.reload();
			removeAlert(); 
		}
	

</script>

</html>