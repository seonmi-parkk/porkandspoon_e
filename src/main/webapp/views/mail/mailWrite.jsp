<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<!-- summernote bootstrap-->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">

<!-- 부트스트랩 -->
<link rel="stylesheet" href="/resources/assets/compiled/css/app.css">
<!-- <link rel="stylesheet" href="/resources/assets/compiled/css/app-dark.css"> -->
<!-- <link rel="stylesheet" href="/resources/assets/compiled/css/iconly.css"> -->
<link rel="stylesheet" href="/resources/css/common.css">

<!-- FilePond CSS -->
<link href="https://unpkg.com/filepond@^4/dist/filepond.css" rel="stylesheet" />
<!-- summernote -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<!-- Autocomplete -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- 모달 -->
<link rel="stylesheet" href="/resources/css/chartModal.css">
<!-- jstree -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />



<style>
.mailList .tit-area {
	display: flex;
    align-items: center;
    justify-content: space-between;
}

.mailList .tit-area .left {
	display: flex;
    align-items: center;
    justify-content: space-between;
}

.mailList .tit-area .left button{
	background: none;
	border: none;
}

.mailList .tab {
	margin-left: 14px;
}

.mailList .tab .bar {
	display: inline-block;
	width: 1px;
	height: 14px;
	background: #ddd;
}

.mailList .tab button {
	color: #888;
}

.mailList .tab button.selected {
	color: #333;
}

.mailList table tr:hover {
	background: #f7f7f7;
}

.mailList .util-area {
	display: flex;
	justify-content: space-between;
    padding: 20px 40px;
    border-bottom: 1px solid #ddd;
}

.mailList .util-area .left > * {
	margin-right: 10px;
}

.mailList .cont-body {
    padding: 20px 40px;
}

.mailList .list-area .mail-item {
	display: flex;
	justify-content: space-between;
    padding: 16px 40px;
    border-bottom: 1px solid #ddd;
}
.mailList .list-area .mail-item .left > *{
	margin-right: 10px;
}
.mailList .list-area .mail-item .name {
	margin-right: 30px;
}
.mailList .line {
	margin-bottom: 30px;
}
.mailList .line label {
	width: 72px;	
}
.mailList .line .filepond--drop-label label {
	width: auto;	
}
.mailList .btn {
	margin: 0;
}
.mailList .note-editor {
    width: 100% !important;
}
 .mailList .receivers-area {
     flex-grow: 1;
     margin-right: 10px;
}
 .mailList .receivers-area label {
     flex-shrink: 0;
 	 margin-right: 5px;
 }
 .mailList #receivers {
 	display: flex;
    flex-wrap: wrap;
    gap: 4px;
 	width: 100%;
 	min-height: 40px;
    padding: 4px;
    line-height: 1.5;
    border: 1px solid #dce7f1;
    border-radius: .25rem;
    cursor: text;
 }
 .mailList #receivers .search-area {
 	position: relative;
    display: inline-block;
    width: 160px;
 }
 .mailList #receivers .search-area span{
 	position: absolute;
 	top: 50%;
 	transform: translateY(-50%);
 	display: inline-block;
	width: 18px;
	height: 18px;
	box-sizing: border-box;
 	opacity:0.8;
 	cursor: pointer;
 	display:none;
 }
 .mailList #receivers .search-area span:hover{
 	opacity: 1;
 }
 .mailList #receivers .search-area span.btn-edit{
 	right: 22px;
	background: url('/resources/img/ico/ico_edit_s.png') no-repeat center/cover;
 }
 .mailList #receivers .search-area .btn-delete{
	right: 4px;
	background: url('/resources/img/ico/ico_close_s.png') no-repeat center/cover;
 }
 
 .mailList #receivers input {
 	width: 100%;
 	height: 30px;
 	padding: 6px 50px 6px 10px;
 	display: inline-block;
 	font-size: 14px;
 }
 .mailList #receivers input:focus {
 	box-shadow: 0 0 0 .145rem rgba(67,94,190,.2);
 }
/* .mailList #receivers input {
    display: block;
    height: 34px;
    padding: .375rem .75rem;
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
    color: #607080;
    -webkit-appearance: none;
    appearance: none;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid #dce7f1;
    border-radius: .25rem;
    transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
} */
/* .mailList #receivers input:focus{
    color: #607080;
    background-color: #fff;
    border-color: #a1afdf;
    outline: 0;
    box-shadow: 0 0 0 .25rem rgba(67, 94, 190, .25);
} */
.mailList #receivers input[readonly],
.mailList #receivers input.invalid {
	cursor: default;
	outline: none;
	box-shadow: none;
	background-color: var(--bs-bg);
	border: 1px solid #dae0eb;
}
.mailList #receivers input.invalid {
	background-color: var(--bs-light-danger);
}
.mailList #receivers input[readonly] + span, 
.mailList #receivers input[readonly] + span + span,
.mailList #receivers input.invalid + span, 
.mailList #receivers input.invalid + span + span {
	display: block;
}
.mailList .form-control{
	display: inline-block;
    width: calc(100% - 77px);
	padding: 19px 10px;
}
.mailList #filepond {
	width: 100%;
}
.mailList .fc-gray {
	color: var(--bs-secondary);
	font-size: 19px;
	margin-left: 10px;
}

    .ui-autocomplete { position: absolute; max-height: 200px; margin: 0; padding: 0; background-color: #fff; border: 1px solid #ccc; border-radius: 8px; z-index: 1; list-style: none; overflow-y: auto; } 
    .ui-autocomplete li { padding: 10px; cursor: pointer; font-size: 16px; color: #000; } 
    .ui-autocomplete li strong { color: #0077cc; } 
    .ui-helper-hidden-accessible { display: none; } 
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

			<div class="page-content mailList">
				<section id="menu">
					<h4 class="menu-title">사내메일</h4>
					<ul>
						<li><a href="/mail/listView/recv">받은메일함</a></li>
						<li><a href="/mail/listView/sd">보낸메일함</a></li>
						<li><a href="/mail/listView/sv">임시보관함</a></li>
						<li><a href="/mail/listView/bk">중요메일함</a></li>
						<li><a href="/mail/listView/del">휴지통</a></li>
					</ul>
				</section>
				<section class="cont">

					<div class="col-12 col-lg-12">
						<div class="tit-area">
							<div class="left">
								<h5>메일쓰기 <button class="fc-gray" onclick="location.href='/mail/listView/sv'">임시보관 메일 <span class="mail-count">${savedMailCount}</span></button></h5>
							</div>
						</div>
						<div class="util-area">
							<div class="left">
								<button class="btn btn-primary" onclick="sendMail()">보내기</button>
								<button class="btn btn-outline-primary" onclick="saveMail()">임시저장</button>
							</div>
						</div>

						<div class="cont-body">
							<form id="mailWriteForm">
								<input type="hidden" name="idx"/>
								<div class="line">
									<div class="flex between">
										<div class="flex receivers-area">
											<label class="fw-600">받는 사람</label> 
											<!-- <input name="myself" type="checkbox" class="form-check-input" id="checkbox2">
											<label for="myself">나에게</label> -->
											
											<!-- 검색어를 입력할 input box 구현부 -->
		    								<div id="receivers">
		    									<div class="search-area">
		    										<input class="searchBox form-control" name="username" required/>
		    										<span class="btn-edit"></span>
		    										<span class="btn-delete"></span>
		    									</div>
	    									</div>
											<!-- <input type="text" id="autocomplete" class="autocomplete-input"> -->
										</div>
										<button class="btn btn-outline-primary btn-sm" type="button" onclick="loadChartModal('chartInputModal')">조직도</button>
									</div>
								</div>
								<div class="line">
									<label class="fw-600">제목</label>
									<input class="form-control" type="text" name="title" required/>
								</div>
								<div class="line clearfix">
									<label class="fw-600">파일첨부</label> 
									<!-- <button class="btn btn-outline-primary btn-sm">파일첨부</button> -->
								<!-- 	<p class="float-r">
										<span>0KB</span>
										/
										<span>10MB</span>
									</p> -->
									<input type="file" class="filepond-multiple" multiple data-max-file-size="10MB" data-max-files="5" id="filepond" multiple="" name="attachedFiles" type="file"/>
								</div>			
								<!-- <input type="file" class="with-validation-filepond" required multiple data-max-file-size="10MB"> -->
								<div class="editor-area">
									<textarea id="summernote" maxlength="10000"></textarea>
								</div>		
							</form>
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

<!-- select  -->
<script
	src="/resources/assets/extensions/choices.js/public/assets/scripts/choices.js"></script>
<script src="/resources/assets/static/js/pages/form-element-select.js"></script>

<!-- FilePond JavaScript -->
<script src="https://unpkg.com/filepond@^4/dist/filepond.js"></script>

<!-- jQuery -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<!-- jstree -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
	
<script src='/resources/js/common.js'></script>
<script src='/resources/js/charjstree.js'></script>

<!-- select  -->
<script
	src="/resources/assets/extensions/choices.js/public/assets/scripts/choices.js"></script>
<script src="/resources/assets/static/js/pages/form-element-select.js"></script>

<script src='/resources/js/textEaditor.js'></script>

<!-- <script src="https://code.jquery.com/jquery-1.12.4.js"></script> -->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src='/resources/js/mailWrite.js'></script>

<!-- jstree -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
<script src='/resources/js/charjstree.js'></script>

<script>
	//FilePond 등록
	FilePond.registerPlugin();
	const attachedFilesPond = FilePond.create(document.querySelector('input.filepond-multiple'), {
	    allowMultiple: true,
	    maxFiles: 5,
	    allowImagePreview: false,
	    labelIdle: '파일을 드래그하거나 클릭하여 업로드하세요 (최대 3개)',
	    instantUpload: false
	});
	
	
	// 전송
	function textEaditorWrite(url){
		console.log('approval.js textEaditorWrite실행');
		// check!!! 이거두줄
		var csrfToken = document.querySelector('meta[name="_csrf"]').content;
	    var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
	
		var formData = new FormData($('form#mailWriteForm')[0]); // formData
		var content = $('#summernote').summernote('code'); // summernote로 작성된 코드
		console.log("content!!@@##",content);
		console.log("formData!!@@##",formData);
		formData.append('content', content);
	    
	    //첨부 파일 추가
	    const attachedFiles = attachedFilesPond.getFiles();
	    if (attachedFiles.length > 0) {
	    	attachedFiles.forEach(function(file, index) {
	    	    formData.append('attachedFiles', file.file); 
	    	});
	    }
		
		var tempDom = $('<div>').html(content);
	    var imgsInEditor = []; // 최종 파일을 담을 배열
	 
	 	 tempDom.find('img').each(function () {
	            var src = $(this).attr('src');
	            if (src && src.includes('/photoTem/')) {  // 경로 검증
	                var filename = src.split('/').pop();  // 파일명 추출
	                imgsInEditor.push(filename);  // 추출된 파일명 배열에 추가
	            }
	    });
	 
		 // new_filename과 일치하는 항목만 필터링
	    var finalImgs = tempImg.filter(function (temp) {
	        return imgsInEditor.includes(temp.new_filename);  // 에디터에 있는 파일과 tempImg의 new_filename 비교
	    });
	 
	 	formData.append('imgsJson', JSON.stringify(finalImgs));
	 
	 	 fileAjax('POST', url, formData);
	     console.log("textEaditorWrite 실행완료");
	     //console.log("textEaditorWrite after : "+after);
	}
	
	
	
	
	/* 조직도노드  */
	 //초기 데이터
	const initialData = {
	    headers: ['아이디', '이름', '부서', '직급', '삭제'],
	    rows: [
	        //['${userDTO.name}', '${userDTO.dept.text}', '${userDTO.position_content}', '기안', '<button class="btn btn-primary">삭제</button>'],
	    ]
	};
	var exampleData = JSON.parse(JSON.stringify(initialData));

	//서버에 넘길 결재자 id배열
	var approvalLines = [];

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
	        	 
	        	 var userId = response.username;
	        	 var userName = response.name;
	        	 var userPosition = response.position_content;
	        	 var userDept = response.dept.text;
	        	 
	             // 새로운 row 데이터 생성
	             const newRow = [userId, userName, userDept, userPosition, '<button class="btn btn-primary">삭제</button>'];

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
 	    $(this).closest('tr').remove();
 	    initialData.rows.splice(idx, 1);
 	    exampleData.rows.splice(idx, 1);
 	    approvalLines.splice(idx, 1);
 	    console.log("approvalLines 수정 : ",approvalLines);
	 });
		
		
	 // 조직도노드 등록버튼 (결재라인 설정)
	 var addBtn = document.getElementById('addModal');
	/*  addBtn.addEventListener("click", function () {
		 console.log("등록 : ", document.getElementById('orgBody').childNodes);
	 }); */
	 
	function addBtnFn(){
		 var lineNodes = document.getElementById('orgBody').childNodes;
		 console.log("lineNodes++",lineNodes);
		 // 기존 수신자 설정 초기화 
		 console.log("$('.search-area:not(first-child) input')",$('.search-area:not(first-child) input'));
		 var exsitingInputs = $('.search-area:not(first-child) input');
		 for(input of exsitingInputs){
			 $(input).empty();
			 console.log("input",input);
		 }
		 
		 
		 $('.search-area:first-child input').val('');
		 $('.search-area:first-child input').attr('readonly',false);
		 //$('.appr_line tr.name > td > p').text('');
		 //$('.appr_line tr.position > td').text('');
		 //$('input[name="appr_user"]').val('');
		 // 기안문 결재라인 설정
		 for(var i = 0; i <= approvalLines.length -1 ; i++){
			 addNewInput();
			 var lastInput = $('.search-area:last-child input');
			 console.log('lineNodes[i] : ',lineNodes[i]);
			 console.log('lineNodes[i].childNodes[0] : ',lineNodes[i].childNodes[0]);
			 userName = lineNodes[i].childNodes[0].innerText;
			 $(lastInput).val(userName);
			 $(lastInput).attr('readonly',true);
			
			 //userName = lineNodes[i].childNodes[0].innerText;
			 //userPosition = lineNodes[i].childNodes[2].innerText;
			 //document.querySelectorAll('.appr_line tr.name > td > p')[i].innerText = userName;
			// document.querySelectorAll('.appr_line tr.position > td')[i].innerText = userPosition;
			 //document.querySelectorAll('input[name="appr_user"]')[i].value = approvalLines[i];
		 	// document.getElementById('chartModalBox').style.display = "none"; 
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
</script>

</html>