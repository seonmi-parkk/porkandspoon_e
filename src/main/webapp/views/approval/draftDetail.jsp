<%@page import="kr.co.porkandspoon.dto.ApprovalDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>기안문 상세페이지</title>

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

<!-- 파일 업로더 -->
<link rel="stylesheet"
	href="/resources/assets/extensions/filepond/filepond.css">
<link rel="stylesheet"
	href="/resources/assets/extensions/filepond-plugin-image-preview/filepond-plugin-image-preview.css">

<!-- summernote bootstrap-->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<!-- 부트스트랩 -->
<link rel="stylesheet" href="/resources/assets/compiled/css/app.css">
<link rel="stylesheet" href="/resources/assets/compiled/css/app-dark.css">
<link rel="stylesheet" href="/resources/assets/compiled/css/iconly.css">
<link rel="stylesheet" href="/resources/css/common.css">

<!-- summernote -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">


<style>
	.draftDetail table{
		border: 1px solid #ddd;
		table-layout: auto;
	}
	.draftDetail table th, .draftDetail table td{
		padding: 4px 10px;
		border-right: 1px solid #ddd;
		border-width: 1px;
	}
	.draftDetail input{
		width: 100%;
	    height: 100%;
	    border: none;
    }
    .draftDetail .form-group {
	    margin-bottom: 0;
	}
    .draftDetail .form-control {
    	background: none;
    	color: #333;
    }
	.draftDetail select {
		border: none;
	}
	.draftDetail input[type=date].form-control {
		line-height: 1.5;
	}
	.draftDetail .top-area {
		display: flex;
	    justify-content: space-between;
	}
	.draftDetail .top-area > table {
		width: 340px;
	}
	.draftDetail table.user_info th{
		width: 120px;
	}
	.draftDetail table.user_info input {
		width: 100%;
		border: none;
		background: none;
	}
	.draftDetail input:focus {
		outline: none;
		box-shadow: none;
		border: none;
	}
	.draftDetail table.appr_line {
		width: 410px;
	}
	.draftDetail table.appr_line th{
		width: 44px;
		 writing-mode: vertical-rl; /* 텍스트를 세로로 표시 (오른쪽에서 왼쪽) */
   		 text-orientation: upright; /* 문자의 방향을 정상적으로 유지 */
	}
	.draftDetail table.appr_line th, .draftDetail table.appr_line td {
    	padding: 4px 0;
    }
	.draftDetail table.appr_line tr:nth-child(2) td p{
		margin-top: 0.7rem;
		margin-bottom: 0.5rem;
	}
	.draftDetail table.appr_line .sign-area {
	    display: flex;
	    justify-content: center;
	    align-items: center;
		height: 40px;
	    margin-top: 0.7rem;
	}
	.draftDetail table.appr_line .sign-area .status {
		padding: 0px 8px;
		border: 2px solid;
		font-weight: 500;
		border-color: var(--bs-secondary);
		color: var(--bs-secondary);
	} 
	.draftDetail table.appr_line .sign-area .status.return {
		border-color: var(--bs-danger);
		color: var(--bs-danger);
	}
	.draftDetail table.appr_line .sign-area .status.ing {
		border-color: var(--bs-primary);
		color: var(--bs-primary);
	}

	.draftDetail table.appr_line .sign{
		width: 40px;
	}
	.draftDetail table.appr_line .date{
		font-size: 13px;
	}
	.draftDetail table.appr_line .date > td {
		height: 28px;
		vertical-align: baseline;
	}
	.draftDetail table.appr_line .date .return {
		margin-bottom: 0;
		text-decoration: underline;
		color: var(--bs-danger);
		cursor: pointer;
	}
	.draftDetail .buttons {
	    border-bottom: 1px solid #ddd;
	    padding: 4px 40px;
    }
	.draftDetail .buttons .btn {
	    margin: 14px 2px;
	}
	.draftDetail h4.doc-subject{
		margin: 20px 0 50px;
		text-align: center;
	}
	
	.draftDetail .btm-area {
		display: flex;
		flex-wrap: wrap;
		border-left: 1px solid #ddd;
	 	border-top: 1px solid #ddd;
	 	margin-top : 40px;
	}

	.draftDetail .btm-area .line {
		display: flex;
		width: 50%;
	}
	.draftDetail .btm-area #addr-area {
		display: flex;
    	width: 100%;
	}
	.draftDetail .btm-area #addr-area .line {
		width: 100%;
	}
	.draftDetail .btm-area #addr-area .left {
		width: 100%;
	}
	.draftDetail .btm-area #addr-area input[type="button"]{
		width: 120px;
	}
	
	.draftDetail .btm-area .line > div {
	    border-right: 1px solid #ddd;
	    border-bottom: 1px solid #ddd;
	}
	
	.draftDetail .btm-area .tit {
		width: 120px;
	    border-width: 1px;
	    background: #f5f5f5;
	    padding: 7px 16px;
	    font-weight: 600;
	    text-align: center;
	}

	.draftDetail .btm-area .txt {
		width: calc(100% - 120px);
	}
	
	.draftDetail .editor-area {
		border : 1px solid #ddd;
		margin: 40px 0 20px;
		padding: 14px;
	}
	
	.editor-area .note-editor {
		width: 100% !important;
		margin-top: 20px;
	}
	
	.attached-file {
		background: #eaeaea;
		border-radius: 6px;
   		padding: 26px 18px;
	    color: #4f4f4f;
	}
	.attached-file ul {
		margin: 0;
		padding: 0;	
	}
	.attached-file ul li:not(:last-child){
		margin-bottom: 10px;
	}
	.attached-file .file-item{
		display: flex;
		align-items: center;
		background: #666;
		border-radius: 6px;
	    padding: 8px 10px;
	    color: #ccc;
	}
	.attached-file .file-item p{
		color: #eee;
		font-size: 13px;
		font-weight: 500;
		margin-bottom: 0;
	}
	.attached-file .file-item .file-size{
		color: #ccc;
	}
	.attached-file .delete{
		background: #333;
		border-radius: 50%;
		width: 28px;
		height: 28px;
		border: none;
		margin-right: 10px;
		color: #eee;
	}


	/* 모달 */
	/* 기본 모달 스타일 */
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
	    font-size: 20px;
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
		width: 78px;
	    flex-shrink: 0;
		margin-top: 4px;
	}
	#modalBox input {
		padding-left: 10px;
		border: none;
	    box-shadow: none;
	    background: none;
	}
	#modalBox input:focus {
		box-shadow: none;
	}
	#modalBox input,#modalBox textarea {
		width: calc(100% - 70px);
	}	
 	#modalBox textarea {
 		resize: none;
		padding-left: 8px;
 	} 
 	#modalBox .btn{ 
	 	margin: 16px 5px 0;
	}
	#returnCkFrom .input-row p, #approvalFrom .input-row p{
	    margin: 4px 0 0 4px;
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
			<jsp:include page="../header.jsp"/>

			<div class="page-content draftDetail">
				<section id="menu">
					<h4 class="menu-title">나의문서함</h4>
					<ul>
						<li <c:if test="${listType == 'my'}">class="active"</c:if>><a href="/approval/listView/my">기안문서함</a></li>
						<li <c:if test="${listType == 'tobe'}">class="active"</c:if>><a href="/approval/listView/tobe">결재할 문서</a></li>
						<li <c:if test="${listType == 'did'}">class="active"</c:if>><a href="/approval/listView/did">결재한 문서</a></li>
						<li <c:if test="${listType == 'sv'}">class="active"</c:if>><a href="/approval/listView/sv">임시저장 문서</a></li>
						<li><a href="/approval/listView/line">나의 결재라인</a></li>
					</ul>
				</section>
				<section class="cont">

					<div class="col-12 col-lg-12">
						<div class="tit-area">
							<h5>기안 상세페이지</h5>
						</div>
						<div class="buttons">
							<button class="btn btn-outline-primary" onclick="backPrev()">돌아가기</button>
							<!-- 수정/상신: 기안자이면서, 임시저장인 경우 -->
							<c:if test="${isDraftSender and DraftInfo.status == 'sv'}">
								<button class="btn btn-primary" onclick="location.href='/approval/updateView/${DraftInfo.draft_idx}/false'">수정</button>
								<button class="btn btn-primary" onclick="layerPopup('기안문을 상신하시겠습니까?', '확인', '취소', changeStatusToSend, btnCloseAct);">상신</button>
							</c:if>
							<!-- 회수: 기안자이면서, 결재가 진행중인(완료x) 경우 -->
							<c:if test="${isDraftSender and DraftInfo.status == 'sd'}">
								<button class="btn btn-outline-primary" onclick="layerPopup('기안문을 회수하시겠습니까?', '확인', '취소', recallAct, btnCloseAct);">문서 회수</button>
							</c:if>
							<!-- 재기안: 기안자이면서, 회수 상태이거나 반려 상태인 경우 -->
							<c:if test="${isDraftSender and (DraftInfo.status == 'ca' || DraftInfo.status == 're')}">
								<button class="btn btn-primary" onclick="location.href='/approval/updateView/${DraftInfo.draft_idx}/true'">재기안</button>
							</c:if>
							<!-- 반려내용: 반려 상태인 경우 -->
							<c:if test="${DraftInfo.status == 're'}">
								<button class="btn btn-outline-primary" onclick="loadModal('draft','CkRefusal',this)">반려내용</button>
							</c:if>
							<!-- 결재/반려: 내가 결재자이면서 결재하지 않은 경우 이면서 이전결재자들이 결재완료한경우 + 기안 진행중인 경우 -->
							<c:if test="${(approverStatus == 'ap001' or approverStatus == 'ap002') and approverTurn and DraftInfo.status == 'sd'}">
								<button class="btn btn-primary"  onclick="loadModal('draft','Approval')">결재</button>
								<button class="btn btn-outline-primary" onclick="loadModal('draft','Refusal')">반려</button>
							</c:if>
							<!-- 삭제: 기안자이면서 임시저장 혹은 회수상태인 경우-->
							<c:if test="${isDraftSender  and (DraftInfo.status == 'sv' || DraftInfo.status == 'ca')}">
								<button class="btn btn-outline-secondary" onclick="layerPopup('기안문을 삭제하시겠습니까?', '삭제', '취소', deleteAct, btnCloseAct);">삭제</button>
							</c:if>
						</div>
						<div class="cont-body">  
							<h4 class="doc-subject">업무 기안 (<span class="change-tit">브랜드 등록</span>)</h4>
							<form id="formDraft">
								<input type="hidden" name="draft_idx" value="${DraftInfo.draft_idx}"/>
								<input type="hidden" name="target_type" value="${DraftInfo.target_type}"/>
								<input type="hidden" name="action_type" value="${DraftInfo.action_type}"/>
								<div class="top-area">
									<table class="user_info">
										<tr>
											<th>문서상태</th>
											<td>
												<input type="text" name="draft_status" value="${DraftInfo.status_name}" readonly/>
											</td>
										</tr>
										<tr>
											<th>기안자</th>
											<td>
												<input type="hidden" name="username" value="${DraftInfo.username}"/>
												<input type="text" name="sender_name" value="${DraftInfo.user_name}" readonly/>
											</td>
										</tr>
										<tr>
											<th>기안일</th>
											<td>
												<c:choose>
													<c:when test="${DraftInfo.status == 'sv'}">
														<input type="text" name="today" value="-" readonly/>
													</c:when>
													<c:otherwise>
														<input type="text" name="today" value="${DraftInfo.create_date}" readonly/>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th>소속</th>
											<td>
												<input type="hidden" name="dept_id" value="${DraftInfo.dept_id}" readonly/>
												<input type="text" name="team_name" value="${DraftInfo.dept_name}" readonly/>
											</td>
										</tr>
									</table>
									
									<table class="appr_line">
										<thead>
											<colgroup>
												<col width="10%"></col>
												<col width="22%"></col>
												<col width="22%"></col>
												<col width="22%"></col>
												<col width="22%"></col>
											</colgroup>
										</thead>
										<tr>
											<th rowspan="3">결재</th>
											<td>${ApprLine[0].position}</td>
											<td>${ApprLine[1].position}</td>
											<td>${ApprLine[2].position}</td>
											<td>${ApprLine[3].position}</td>
										</tr>
										<tr class="status-area">
											<td>
												<input type="hidden" name="appr_user" value="${ApprLine[0].username}"/>
												<div class="sign-area">
													<c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap004'}">
														<img class="sign" src="/photo/${ApprLine[0].sign}" alt="서명"/>
													</c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap003'}"><span class="status return">반려</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap002'}"><span class="status ing">결재중</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap001'}"><span class="status no-read">미확인</span></c:if>																	
												</div>
												<p>${ApprLine[0].user_name}</p>
											</td>
											<td>
												<input type="hidden" name="appr_user" value="${ApprLine[1].username}"/>
												<div class="sign-area">
													<c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap004'}">
														<img class="sign" src="/photo/${ApprLine[1].sign}" alt="서명"/>
													</c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap003'}"><span class="status return">반려</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap002'}"><span class="status ing">결재중</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap001'}"><span class="status no-read">미확인</span></c:if>		
												</div>
												<p>${ApprLine[1].user_name}</p>
											</td>
											<td>
												<input type="hidden" name="appr_user" value="${ApprLine[2].username}"/>
												<div class="sign-area">
													<c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap004'}">
														<img class="sign" src="/photo/${ApprLine[2].sign}" alt="서명"/>
													</c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap003'}"><span class="status return">반려</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap002'}"><span class="status ing">결재중</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap001'}"><span class="status no-read">미확인</span></c:if>		
												</div>
												<p>${ApprLine[2].user_name}</p>
											</td>
											<td>
												<input type="hidden" name="appr_user" value="${ApprLine[3].username}"/>
												<div class="sign-area">
													<c:if test="${ApprLine[3].status == 'ap004'}">
														<img class="sign" src="/photo/${ApprLine[3].sign}" alt="서명"/>
													</c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap003'}"><span class="status return">반려</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap002'}"><span class="status ing">결재중</span></c:if>
													<c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap001'}"><span class="status no-read">미확인</span></c:if>		
												</div>
												<p>${ApprLine[3].user_name}</p>
											</td>
										</tr>
										
										<tr class="date">
											<td>
												<c:if test="${DraftInfo.status !='sv'}"> ${ApprLine[0].approval_date} </c:if>
												<c:if test="${ApprLine[0].status == 'ap003'}"><p class="return"  onclick="loadModal('draft','CkRefusal',this)">반려</p></c:if>			
											</td>
											<td>
												<c:if test="${DraftInfo.status !='sv'}"> ${ApprLine[1].approval_date} </c:if>
												<c:if test="${ApprLine[1].status == 'ap003'}"><p class="return"  onclick="loadModal('draft','CkRefusal',this)">반려</p></c:if>			
											</td>
											<td>
												<c:if test="${DraftInfo.status !='sv'}"> ${ApprLine[2].approval_date} </c:if>
												<c:if test="${ApprLine[2].status == 'ap003'}"><p class="return"  onclick="loadModal('draft','CkRefusal',this)">반려</p></c:if>			
											</td>
											<td>
												<c:if test="${DraftInfo.status !='sv'}"> ${ApprLine[3].approval_date} </c:if>
												<c:if test="${ApprLine[3].status == 'ap003'}"><p class="return"  onclick="loadModal('draft','CkRefusal',this)">반려</p></c:if>			
											</td>
										</tr>
										
									</table>
									
								</div>
								
								<div class="btm-area">
									<div class="line">
										<div class="tit">제목</div>
										<div class="txt"><input class="form-control" type="text" name="subject" value="${DraftInfo.subject}" readonly required/></div>
									</div>
									<div class="line">
										<div class="tit">협조부서</div>
										<div class="txt">
											<c:forEach items="${deptList}" var="dept">
												<c:if test="${dept.id == DraftInfo.cooper_dept_id}">
													<input class="form-control" type="text" name="cooper_dept_id" value="${dept.text}" readonly required/>
												</c:if>
											</c:forEach>
										</div>
									</div>
									<div class="line">
										<div class="tit">브랜드명</div>
										<div class="txt"><input class="form-control" type="text" name="name" value="${DraftInfo.name}" readonly required/></div>
									</div>
									<div class="line">
										<div class="tit">시행일자</div>
										<div class="txt"><input class="form-control" type="date" name="from_date" value="${DraftInfo.from_date}" readonly required/></div>
									</div>
								</div>
								<div class="editor-area">
									${DraftInfo.content}
								</div>
								<br/>

								<h5>파일 첨부</h5>
								<br/>
								<h6>첨부된 로고 파일</h6>
								<div class="attached-file">
									<ul>
										<c:if test="${logoFile != null}">
											<li class="file-item" onclick="download('${logoFile.ori_filename}', '${logoFile.new_filename}')">
												<div>
													<input type="hidden" name="new_filename" value="${logoFile.new_filename}"/>
													<input type="hidden" name="pk_idx" value="${logoFile.pk_idx}"/>
													<p class="file-name">${logoFile.ori_filename}</p>
												</div>
											</li>
										</c:if>
										<c:if test="${logoFile == null}">
											<div class="align-c">첨부된 로고 파일이 없습니다.</div>
										</c:if>
									</ul>
								</div>
								<br/>
								<h6>첨부된 기타 파일</h6>
								<div class="attached-file">
									<ul>
										<c:forEach items="${attachedFiles}" var="file"> 
											<li class="file-item" onclick="download('${file.ori_filename}', '${file.new_filename}')">
											<div>
												<input type="hidden" name="new_filename" value="${file.new_filename}"/>
												<input type="hidden" name="pk_idx" value="${file.pk_idx}"/>
												<p class="file-name">${file.ori_filename}</p>
											</div>
										</li>
										</c:forEach>
										<c:if test="${empty attachedFiles}">
											<div class="align-c">첨부된 기타 파일이 없습니다.</div>
										</c:if>
									</ul>
								</div>
								<br/>
								
													
							<!-- 	<input type="hidden" name="status"/> -->
							</form>

						</div>
					</div> 
				</section>
			</div>
		</div>
	</div>

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

<!-- 파일업로더 -->
<script
	src="/resources/assets/extensions/filepond-plugin-file-validate-size/filepond-plugin-file-validate-size.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-file-validate-type/filepond-plugin-file-validate-type.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-image-crop/filepond-plugin-image-crop.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-image-exif-orientation/filepond-plugin-image-exif-orientation.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-image-filter/filepond-plugin-image-filter.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-image-preview/filepond-plugin-image-preview.min.js"></script>
<script
	src="/resources/assets/extensions/filepond-plugin-image-resize/filepond-plugin-image-resize.min.js"></script>
<script src="/resources/assets/extensions/filepond/filepond.js"></script>
<script src="/resources/assets/static/js/pages/filepond.js"></script>

<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

	
<script src='/resources/js/common.js'></script>
<script src='/resources/js/textEaditor.js'></script>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>

//페이지가 처음 로드될 때 직전 URL을 저장(돌아가기에서 사용)
if (!sessionStorage.getItem('previousUrl')) {
    sessionStorage.setItem('previousUrl', document.referrer || '/');
}

//.appr_line .status-area
console.log('dd','${ApprLine}');
/* var apprLineList = JSON.parse('${ApprLine}');
apprLineList.forEach(function(line,index){
	console.log('${line}');
}); */

	console.log('${approverStatus}');
	console.log('${approverTurn}');

if(('${approverStatus}' == 'ap001' || '${approverStatus}' == 'ap002') && '${approverTurn}'){
	console.log('만조오오ㅗㅇ!!!!!!!!!!!!!11');
}


/* <c:if test="${ApprLine[0].status == 'ap004'}">
<img class="sign" src="/photo/${ApprLine[0].sign}" alt="서명"/>
</c:if>
<c:if test="${ApprLine[0].status == 'ap003'}"><span class="status return">반려</span></c:if>
<c:if test="${ApprLine[0].status == 'ap002'}"><span class="status return">결재중</span></c:if>
<c:if test="${ApprLine[0].status == 'ap001'}"><span class="status return">미확인</span></c:if> */


//결재라인 상태변경(결재중으로)
if('${approverStatus}'== 'ap001'){
	console.log("status : ","${approverStatus}");
	console.log("draft_idx11 : ","${DraftInfo.draft_idx}");
	/* var params = {
			'draft_idx' : '${DraftInfo.draft_idx}'
		};  */
	
	httpAjax('PUT', '/approval/changeStatusToRead/${DraftInfo.draft_idx}');
	function httpSuccess(response){
		location.href= '/approval/detail/${DraftInfo.draft_idx}';
	}
}





var target = '${DraftInfo.target_type}' == 'df001' ?  '브랜드' : '직영점';
console.log('target: '+target);
var action = '${DraftInfo.action_type}' == 'df011' ?  '등록' : '폐점';
console.log('action: '+action);

$('.change-tit').text(target+' ' +action);


console.log('ApprLine: '+ '${ApprLine}');
console.log('DraftInfo: '+ '${DraftInfo}');

// 기안문 종류에 따른 양식
function setForm(type1, type2, element){
	document.querySelectorAll('.change-tit').forEach(function(titEl) {
		titEl.innerText = element.innerText; // 클릭한 텍스트로 변경
	});
	
	// 브랜드 기안의 경우
	if(type1 == 'brand'){
		//document.getElementsByClassName('addr-area')[0].style.display = 'none';
		document.querySelector('input[name="target_type"]').value = 'df001';
		// 주소칸 제거
		if($("#addr-area").length >= 1){
			$("#addr-area").remove();
		}
	// 직영점 기안의 경우
	}else if(type1 == 'direct'){
		document.querySelector('input[name="target_type"]').value = 'df002';
		// 주소칸 추가
		if($("#addr-area").length == 0){
	
			let addressForm = `
			    <div id="addr-area">
			        <div class="left">
			            <div class="line addr">
			                <div class="tit"><span class="ico-required">*</span>주소</div>
			                <div class="txt">
			                    <input class="form-control" type="text" id="sample6_address" name="address" required/>
			                </div>
			            </div>
			            <div class="line addr">
			                <div class="tit"><span class="ico-required">*</span>상세주소</div>
			                <div class="txt">
			                    <input class="form-control" type="text" id="sample6_detailAddress" name="address" required/>
			                </div>
			            </div>
			        </div>
			        <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
			    </div>
		    `;
	
			document.getElementsByClassName('btm-area')[0].insertAdjacentHTML('beforeend', addressForm);
		}
	}
	
}


// 반려 모달데이터
function setModalData(type, data){
	console.log("data:::",data);
	console.log("data:index::",$(data).parent().index());
    
	
	 // 현재날짜
	const today = new Date();   
	const year = today.getFullYear(); 
	const month = today.getMonth() + 1; 
	const date = today.getDate();  
	// 반려사유 작성 모달
	$('#returnFrom input[name="today"]').val(year + '-' + month + '-' + date);

	// 반려사유 작성, 반려내역, 결재 모달 공통
	$('input[name="draft_idx"]').val('${DraftInfo.draft_idx}');
	
	// 반려내역 모달
	if(document.getElementById('returnCkFrom')){
		var lineIndex = $(data).parent().index();

	    var ApprLine = [];
	    <c:forEach var="item" items="${ApprLine}" varStatus="status">
	        ApprLine.push({
	            "comment": "${item.comment}",
	            "approval_date": "${item.approval_date}"
	        });
	    </c:forEach>
	    console.log('Comment at lineIndex ' + lineIndex + ': ', ApprLine[lineIndex].comment);
		$('p.comment').text(ApprLine[lineIndex].comment);
		$('p.date').text(ApprLine[lineIndex].approval_date);
	}

	// 결재 모달
	if(document.getElementById('approvalFrom')){
		$('p.draft_title').text('${DraftInfo.subject}');
	}

	//$("#draftCkRefusalModal form").attr("action", "/appoval/returnDraft/${DraftInfo.draft_idx}");
}


document.addEventListener('click', function(event){
    // 일정 등록 버튼 클릭
    if(event.target.id === 'returnBtn'){
    	returnDraft();
    }
    if(event.target.id === 'ApprovalBtn'){
    	ApprovalDraft();
    }
    
});

var csrfToken = document.querySelector('meta[name="_csrf"]').content;
var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

// 기안문 반려
function returnDraft() {
	var commentData = $('textarea[name="comment"]').val();
	console.log("commentData",commentData);
	if(commentData != null && commentData.trim() != ""){
		$.ajax({
		    type: 'PUT',
		    url: '/approval/returnDraft',
		    data: new FormData($('#returnFrom')[0]), 
		    dataType: 'json', 
		    contentType: false,  
		    processData: false,
		    beforeSend: function(xhr) {
		        xhr.setRequestHeader(csrfHeader, csrfToken);
		    },
		    success: function(response) {
		        if(response.success){
	        	 	document.getElementById('modalBox').style.display = "none";
	        	 	layerPopup('반려 처리되었습니다.', '확인', false, btn1Act, btn1Act);
		        }
		    },
		    error: function(e) {
		        console.log(e);
		    }
		});
	}else{
		layerPopup('반려 사유를 입력하세요.', '확인', false, removeAlert, removeAlert);
	}
	
	
	//document.getElementById('modalBox').style.display = "none";
} 

// 기안문 승인
function ApprovalDraft(){
	var csrfToken = document.querySelector('meta[name="_csrf"]').content;
	var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

	$.ajax({
	    type: 'POST',
	    url: '/approval/ApprovalDraft',
	    data: new FormData($('#approvalFrom')[0]), 
	    dataType: 'json', 
	    contentType: false,  
	    processData: false,
	    beforeSend: function(xhr) {
	        xhr.setRequestHeader(csrfHeader, csrfToken);
	    },
	    success: function(response) {
	        if(response.success){
        	 	document.getElementById('modalBox').style.display = "none";
        	 	layerPopup('결재 처리되었습니다.', '확인', false, btn1Act, btn1Act);
	        }else{
        	 	layerPopup('결재 처리에 실패하였습니다.', '재시도', '취소', removeAlert, btn1Act);
	        }
	    },
	    error: function(e) {
	        console.log(e);
	    }
	});
	
}


//기안문 회수버튼
function recallAct(){
	$.ajax({
        type : 'PUT',
        url : '/approval/recall/${DraftInfo.draft_idx}',
        data : {},
        dataType : 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success : function(response){
        	 if(response.success){
     		 	removeAlert(); 
      			layerPopup('회수 처리되었습니다.', '확인', false, btn1Act, btn1Act);
     		 }else{
     		 	removeAlert(); 
      			layerPopup('회수 처리에 실패하였습니다.', '재시도', '취소', recallAct, btn1Act);
     		 }
        },error: function(e){
            console.log(e);
        }
    });
	//httpAjax('PUT', '/approval/recall/${DraftInfo.draft_idx}');
	// console.log("httpAjax!");
	 /* function httpSuccess(response){
		 if(response.success){
		 	removeAlert(); 
 			layerPopup('회수 처리되었습니다.', '확인', false, btn1Act, btn1Act);
		 }else{
		 	removeAlert(); 
 			layerPopup('회수 처리에 실패하였습니다.', '재시도', '취소', recallAct, btn1Act);
		 }
	 } */
}

// 임시저장 -> 상신 
function changeStatusToSend(){
	$.ajax({
        type : 'PUT',
        url : '/approval/changeStatusToSend/${DraftInfo.draft_idx}',
        data : {},
        dataType : 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success : function(response){
        	 if(response.success){
     		 	removeAlert();
      			layerPopup('상신 완료되었습니다.', '확인', false, btn1Act, btn1Act);
     		 }else{
     		 	removeAlert();
      			layerPopup('상신 처리에 실패하였습니다.', '재시도', '취소', changeStatusToSend, btn1Act);
     		 }
        },error: function(e){
            console.log(e);
        }
    });
}

// 기안문 삭제
function deleteAct() {
	$.ajax({
        type : 'PUT',
        url : '/approval/changeStatusToDelete/${DraftInfo.draft_idx}',
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


// 반려, 결재 팝업
function btn1Act() {
	location.reload();
	removeAlert(); 
}

// 팝업 취소 버튼 공통
function btnCloseAct(){
	removeAlert(); 
}


//돌아가기
function backPrev() {
    var previousUrl = sessionStorage.getItem('previousUrl');
    console.log('previousUrl:!!!!',previousUrl);
    if (previousUrl) {
        // 직전 URL로 리디렉션
        window.location.href = previousUrl;
    } else {
        // 직전 URL이 없을경우
        window.location.href = '/approval/listView/my';
    }
}



</script>

</html>