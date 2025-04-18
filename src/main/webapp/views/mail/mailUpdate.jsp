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

<!-- summernote bootstrap-->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">

<!-- 부트스트랩 -->
<link rel="stylesheet" href="/resources/assets/compiled/css/app.css">
<!-- <link rel="stylesheet" href="/resources/assets/compiled/css/app-dark.css"> -->
<!-- <link rel="stylesheet" href="/resources/assets/compiled/css/iconly.css"> -->
<link rel="stylesheet" href="/resources/css/chartModal.css">
<link rel="stylesheet" href="/resources/css/common.css">

<!-- FilePond CSS -->
<link href="https://unpkg.com/filepond@^4/dist/filepond.css" rel="stylesheet" />

<!-- summernote -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">

<!-- jstree -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />

<!-- Autocomplete -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

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
					<div class="btn btn-primary full-size" onclick="location.href='/mail/write'">새로작성</div>
				</section>
				<section class="cont">

					<div class="col-12 col-lg-12">
						<div class="tit-area">
							<div class="left">
								<h5>메일쓰기 
									<button class="fc-gray" onclick="location.href='/mail/listView/sv'">
										임시보관 메일
										<span class="mail-count">${savedMailCount}</span>
									</button>
								</h5>
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
								<input type="hidden" name="updateStatus" value="${status}"/>
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
										<button class="btn btn-outline-primary btn-sm">조직도</button>
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

<script>
	// 기존 데이터 보여지기
	var titleTag = '';
	if(${status eq 'delivery'}){
		// 전달일 경우
		titleTag = 'FW: ';
	}
	if(${status eq 'reply'}){
		// 답장일 경우		
		titleTag = 'RE: ';
		var $receiverInput = $('input[name="username"]');
		$receiverInput.val('${mailInfo.sender}');
		$receiverInput.attr("readonly", true);
	}
	// 임시저장메일 수정일 경우
	if(${status eq 'update'}){
		console.log('receivers:: ','${mailInfo.username}');
		$('input[name="idx"]').val('${mailInfo.idx}');
		// 주어진 문자열
		let receivers = '${mailInfo.username}';

		// 정규 표현식을 사용하여 < > 사이의 내용을 추출
		let receiversArr = receivers.match(/<([^>]+)>/g).map(item => item.slice(1, -1));
		console.log('receiversArr : ',receiversArr);
		for(receiver of receiversArr){
			var $receiverInput = $('.search-area:last-child input');
			console.log('receiver : ',receiver);
			$receiverInput.val(receiver);
			$receiverInput.attr('readonly',true);
			addNewInput();
		}
		$('#receivers input:not([readonly])').blur();
	}
	
	// 본문내용(텍스트 에디터부분)
	// 전달/답장일 경우
	var content = '';
	if(${status eq 'delivery' or status eq 'reply'}){
		content = '<br/><br/><br/>-----Original Message-----<br/>';
		content += 'From: ${mailInfo.sender}<br/>';
		content += 'To: ${mailInfo.sender}<br/>';
		content += 'Sent: ${mailInfo.send_date}<br/>';
		content += 'Sent: ${mailInfo.title}<br/>';
	}
	content += '${mailInfo.content}';
	$('#summernote').val(content);
	// 제목
	$('input[name="title"]').val(titleTag+'${mailInfo.title}');


	// 기존 파일 불러오기
	const attachedFiles = [
		<c:forEach var="file" items="${attachedFiles}" varStatus="status">
		{
			source: "<c:out value='${file.new_filename}'/>", // 고유 식별값 (파일 로드 시 서버에 넘길 값)
			options: {
				type: 'local',
				file: {
					name: "<c:out value='${file.ori_filename}'/>",
					type: "image/jpeg",
					size: 123456
				},
				metadata: {
					poster: "/file/filepond/${file.new_filename}" // 이미지일 경우 썸네일
				}
			}
		}<c:if test="${!status.last}">, </c:if>
		</c:forEach>
	];

	// 삭제된 첨부파일 id
	let deletedFiles = [];

	// FilePond 등록
	FilePond.registerPlugin();
	const attachedFilesPond = FilePond.create(document.querySelector('input.filepond-multiple'), {
		allowImagePreview: true,
		allowProcess: false,     // ✅ 중요! 썸네일만 보고 업로드는 막는 역할
		files: attachedFiles,
		allowMultiple: true,
		maxFiles: 5,
		labelIdle: '파일을 드래그하거나 클릭하여 업로드하세요 (최대 3개)',
		allowImagePreview: false,
		allowRevert: true,
		instantUpload: false,
		server: {
			// 썸네일 이미지 로딩용
			load: (source, load, error, progress, abort, headers) => {
				console.log("📸 썸네일 요청 source:", source);
				fetch(`/mail/filepond/${source}`)
						.then(res => res.blob())
						.then(load)
						.catch(error);
			}
		},
		onremovefile: (error, file) => {
			// 삭제된 파일명 저장
			deletedFiles.push({'new_filename' : file.source});
			//console.log("deletedFiles :", deletedFiles);
		}
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
		formData.append('content', content);
	    
	    //첨부 파일 추가
/* 	    const attachedFiles = attachedFilesPond.getFiles();
	    if (attachedFiles.length > 0) {
	    	attachedFiles.forEach(function(file, index) {
	    	    formData.append('attachedFiles', file.file); 
	    	});
	    } */
	    // 첨부파일 추가
	    //const existingFiles = attachedFilesPond.getFiles().filter(file => file.origin === FilePond.FileOrigin.LOCAL);
	    const newFiles = attachedFilesPond.getFiles().filter(file => file.origin !== FilePond.FileOrigin.LOCAL);
		console.log("newFiles : ", newFiles);

	    // 기존 파일의 ID만 서버로 전송
	    //existingFiles.forEach(file => {
	    //	formData.append('existingFileIds', file.source);
	    //});
		const existingFiles = attachedFiles.map(file => file.source);

		// 삭제된 파일 ID 전송
		formData.append("deletedFiles", JSON.stringify(deletedFiles));

		// 기존 파일 ID 전송 (전달의 경우)
		//formData.append("existingFiles", JSON.stringify(attachedFiles.map(file => file.source)));
		formData.append("existingFiles", JSON.stringify(existingFiles));

	    //새로운 파일 정보
	    newFiles.forEach(file => {
	    	formData.append('attachedFiles', file.file);  // 새로운 파일을 서버로 전송
	    });
	    
    	formData.append('originalIdx', '${mailInfo.idx}'); 
	    
		
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
	 
	     console.log("아작스전");
	 	 fileAjax('POST', url, formData);
	     console.log("textEaditorWrite 실행완료");
	}
 	
	

	
</script>

</html>