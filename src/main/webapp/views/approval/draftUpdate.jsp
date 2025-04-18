<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>기안문 수정</title>

    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

    <!-- 부트스트랩 -->
    <link rel="shortcut icon"
          href="/resources/assets/compiled/svg/favicon.svg" type="image/x-icon">
    <link rel="shortcut icon"
          href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAiCAYAAADRcLDBAAAEs2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS41LjAiPgogPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgeG1sbnM6ZXhpZj0iaHR0cDovL25zLmFkb2JlLmNvbS9leGlmLzEuMC8iCiAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIKICAgIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIKICAgIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIKICAgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIgogICAgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIKICAgZXhpZjpQaXhlbFhEaW1lbnNpb249IjMzIgogICBleGlmOlBpeGVsWURpbWVuc2lvbj0iMzQiCiAgIGV4aWY6Q29sb3JTcGFjZT0iMSIKICAgdGlmZjpJbWFnZVdpZHRoPSIzMyIKICAgdGlmZjpJbWFnZUxlbmd0aD0iMzQiCiAgIHRpZmY6UmVzb2x1dGlvblVuaXQ9IjIiCiAgIHRpZmY6WFJlc29sdXRpb249Ijk2LjAiCiAgIHRpZmY6WVJlc29sdXRpb249Ijk2LjAiCiAgIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiCiAgIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIKICAgeG1wOk1vZGlmeURhdGU9IjIwMjItMDMtMzFUMTA6NTA6MjMrMDI6MDAiCiAgIHhtcDpNZXRhZGF0YURhdGU9IjIwMjItMDMtMzFUMTA6NTA6MjMrMDI6MDAiPgogICA8eG1wTU06SGlzdG9yeT4KICAgIDxyZGY6U2VxPgogICAgIDxyZGY6bGkKICAgICAgc3RFdnQ6YWN0aW9uPSJwcm9kdWNlZCIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWZmaW5pdHkgRGVzaWduZXIgMS4xMC4xIgogICAgICBzdEV2dDp3aGVuPSIyMDIyLTAzLTMxVDEwOjUwOjIzKzAyOjAwIi8+CiAgICA8L3JkZjpTZXE+CiAgIDwveG1wTU06SGlzdG9yeT4KICA8L3JkZjpEZXNjcmlwdGlvbj4KIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InIiPz5V57uAAAABgmlDQ1BzUkdCIElFQzYxOTY2LTIuMQAAKJF1kc8rRFEUxz9maORHo1hYKC9hISNGTWwsRn4VFmOUX5uZZ36oeTOv954kW2WrKLHxa8FfwFZZK0WkZClrYoOe87ypmWTO7dzzud97z+nec8ETzaiaWd4NWtYyIiNhZWZ2TvE946WZSjqoj6mmPjE1HKWkfdxR5sSbgFOr9Ll/rXoxYapQVik8oOqGJTwqPL5i6Q5vCzeo6dii8KlwpyEXFL519LjLLw6nXP5y2IhGBsFTJ6ykijhexGra0ITl5bRqmWU1fx/nJTWJ7PSUxBbxJkwijBBGYYwhBgnRQ7/MIQIE6ZIVJfK7f/MnyUmuKrPOKgZLpEhj0SnqslRPSEyKnpCRYdXp/9++msneoFu9JgwVT7b91ga+LfjetO3PQ9v+PgLvI1xkC/m5A+h7F32zoLXug38dzi4LWnwHzjeg8UGPGbFfySvuSSbh9QRqZ6H+Gqrm3Z7l9zm+h+iafNUV7O5Bu5z3L/wAdthn7QIme0YAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAJTSURBVFiF7Zi9axRBGIefEw2IdxFBRQsLWUTBaywSK4ubdSGVIY1Y6HZql8ZKCGIqwX/AYLmCgVQKfiDn7jZeEQMWfsSAHAiKqPiB5mIgELWYOW5vzc3O7niHhT/YZvY37/swM/vOzJbIqVq9uQ04CYwCI8AhYAlYAB4Dc7HnrOSJWcoJcBS4ARzQ2F4BZ2LPmTeNuykHwEWgkQGAet9QfiMZjUSt3hwD7psGTWgs9pwH1hC1enMYeA7sKwDxBqjGnvNdZzKZjqmCAKh+U1kmEwi3IEBbIsugnY5avTkEtIAtFhBrQCX2nLVehqyRqFoCAAwBh3WGLAhbgCRIYYinwLolwLqKUwwi9pxV4KUlxKKKUwxC6ZElRCPLYAJxGfhSEOCz6m8HEXvOB2CyIMSk6m8HoXQTmMkJcA2YNTHm3congOvATo3tE3A29pxbpnFzQSiQPcB55IFmFNgFfEQeahaAGZMpsIJIAZWAHcDX2HN+2cT6r39GxmvC9aPNwH5gO1BOPFuBVWAZue0vA9+A12EgjPadnhCuH1WAE8ivYAQ4ohKaagV4gvxi5oG7YSA2vApsCOH60WngKrA3R9IsvQUuhIGY00K4flQG7gHH/mLytB4C42EgfrQb0mV7us8AAMeBS8mGNMR4nwHamtBB7B4QRNdaS0M8GxDEog7iyoAguvJ0QYSBuAOcAt71Kfl7wA8DcTvZ2KtOlJEr+ByyQtqqhTyHTIeB+ONeqi3brh+VgIN0fohUgWGggizZFTplu12yW8iy/YLOGWMpDMTPXnl+Az9vj2HERYqPAAAAAElFTkSuQmCC"
          type="image/png">
    <!-- select -->
    <link rel="stylesheet" href="/resources/assets/extensions/choices.js/public/assets/styles/choices.css">

    <!-- summernote bootstrap-->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- 부트스트랩 -->
    <link rel="stylesheet" href="/resources/assets/compiled/css/app.css">
    <link rel="stylesheet" href="/resources/css/chartModal.css">
    <link rel="stylesheet" href="/resources/css/common.css">
    <!-- FilePond CSS -->
    <link href="https://unpkg.com/filepond@^4/dist/filepond.css" rel="stylesheet"/>
    <!-- FilePond 이미지 미리보기 플러그인 -->
    <link href="https://unpkg.com/filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css" rel="stylesheet" />
    <!-- summernote -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
    <!-- jstree -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>

    <style>
        .draftDetail table {
            border: 1px solid #ddd;
            table-layout: auto;
        }

        .draftDetail table th, .draftDetail table td {
            padding: 4px 10px;
            border-right: 1px solid #ddd;
            border-width: 1px;
        }

        .draftDetail input {
            width: 100%;
            height: 100%;
            border: none;
        }

        .draftDetail .form-group {
            margin-bottom: 0;
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

        .draftDetail table.user_info th {
            width: 120px;
        }

        .draftDetail table.user_info input {
            width: 100%;
            border: none;
        }

        .draftDetail table.user_info input:focus {
            outline: none;
        }

        .draftDetail table.appr_line {
            width: 410px;
        }

        .draftDetail table.appr_line th {
            width: 44px;
            writing-mode: vertical-rl; /* 텍스트를 세로로 표시 (오른쪽에서 왼쪽) */
            text-orientation: upright; /* 문자의 방향을 정상적으로 유지 */
        }

        .draftDetail table.appr_line th, .draftDetail table.appr_line td {
            padding: 4px 0;
        }

        .draftDetail table.appr_line tr:nth-child(2) td p {
            margin-top: 0.7rem;
            margin-bottom: 0.5rem;
        }

        .draftDetail table.appr_line .sign-area {
            height: 40px;
            margin-top: 0.7rem;
        }

        .draftDetail table.appr_line .sign {
            width: 40px;
        }

        .draftDetail table.appr_line .date {
            font-size: 13px;
        }

        .draftDetail table.appr_line .date > td {
            height: 28px;
        }

        .draftDetail .buttons {
            border-bottom: 1px solid #ddd;
            padding: 4px 40px;
        }

        .draftDetail .buttons .btn {
            margin: 14px 2px;
        }

        .draftDetail h4.doc-subject {
            margin: 20px 0 50px;
            text-align: center;
        }

        .draftDetail .btm-area {
            display: flex;
            flex-wrap: wrap;
            border-left: 1px solid #ddd;
            border-top: 1px solid #ddd;
            margin-top: 40px;
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

        .draftDetail .btm-area #addr-area input[type="button"] {
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

        .attached-file ul li:not(:last-child) {
            margin-bottom: 10px;
        }

        .attached-file .file-item {
            display: flex;
            align-items: center;
            background: #666;
            border-radius: 6px;
            padding: 8px 10px;
            color: #ccc;
        }

        .attached-file .file-item p {
            color: #eee;
            font-size: 13px;
            font-weight: 500;
            margin-bottom: 0;
        }

        .attached-file .file-item .file-size {
            color: #ccc;
        }

        .attached-file .delete {
            background: #333;
            border-radius: 50%;
            width: 28px;
            height: 28px;
            border: none;
            margin-right: 10px;
            color: #eee;
        }

        /* 조직도 모달 */
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
    </style>
</head>

<body>
<!-- 부트스트랩 -->
<script src="/resources/assets/static/js/initTheme.js"></script>
<div id="app">

    <!-- 사이드바 -->
    <jsp:include page="../sidebar.jsp"/>

    <div id="main">
        <!-- 헤더 -->
        <jsp:include page="../header.jsp"/>

        <div class="page-content draftDetail">
            <section id="menu">
                <h4 class="menu-title">문서함</h4>
                <ul>
                    <li class="active"><a href="#" onclick="setForm('brand','open',this)">브랜드 등록</a></li>
                    <li><a href="#" onclick="setForm('brand','close',this)">브랜드 폐점</a></li>
                    <li><a href="#" onclick="setForm('direct','open',this)">직영점 등록</a></li>
                    <li><a href="#" onclick="setForm('direct','close',this)">직영점 폐점</a></li>
                </ul>
            </section>
            <section class="cont">

                <div class="col-12 col-lg-12">
                    <div class="tit-area">
                        <h5>기안 수정페이지</h5>
                    </div>
                    <div class="buttons">
                        <button class="btn btn-outline-primary" onclick="window.history.back()">돌아가기</button>
                        <button href="#" class="btn btn-outline-primary" onclick="loadChartModal('chartInputModal')">
                            결재라인 설정
                        </button>
                        <c:if test="${!reapproval}">
                            <button class="btn btn-primary" onclick="updateDraft()">수정</button>
                        </c:if>
                        <c:if test="${reapproval}">
                            <button class="btn btn-primary" onclick="updateDraft()">재기안</button>
                        </c:if>
                    </div>
                    <div class="cont-body">
                        <h4 class="doc-subject">업무 기안 (<span class="change-tit">브랜드 등록</span>)</h4>
                        <form id="formDraft" enctype="multipart/form-data">  <!--check!!! 먼훗날 문제가 된다면 여기..?  -->
                            <input type="hidden" name="before_draft_idx" value="${DraftInfo.draft_idx}"/>
                            <input type="hidden" name="draft_idx" value="${DraftInfo.draft_idx}"/>
                            <input type="hidden" name="target_type" value="${DraftInfo.target_type}"/>
                            <input type="hidden" name="action_type" value="${DraftInfo.action_type}"/>
                            <div class="top-area">
                                <table class="user_info">
                                    <tr>
                                        <th>기안자</th>
                                        <td>
                                            <input type="hidden" name="username" value="${DraftInfo.username}"/>
                                            <input type="text" name="sender_name" value="${DraftInfo.user_name}"
                                                   readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>기안일</th>
                                        <td>
                                            <input class="today-date" type="text" name="today"
                                                   value="${DraftInfo.create_date}" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>소속</th>
                                        <td>
                                            <input type="hidden" name="dept_id" value="${DraftInfo.dept_id}" readonly/>
                                            <input type="text" name="team_name" value="${DraftInfo.dept_name}"
                                                   readonly/>
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
                                    <tr class="position">
                                        <th rowspan="3">결재</th>
                                        <td>${ApprLine[0].position}</td>
                                        <td>${ApprLine[1].position}</td>
                                        <td>${ApprLine[2].position}</td>
                                        <td>${ApprLine[3].position}</td>
                                    </tr>
                                    <tr class="name">
                                        <td>
                                            <input type="hidden" name="appr_user" value="${ApprLine[0].username}"/>
                                            <div class="sign-area">
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap004'}">
                                                    <img class="sign" src="/photo/${ApprLine[0].sign}" alt="서명"/>
                                                </c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap003'}"><span
                                                        class="status return">반려</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap002'}"><span
                                                        class="status ing">결재중</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[0].status == 'ap001'}"><span
                                                        class="status no-read">미확인</span></c:if>
                                            </div>
                                            <p>${ApprLine[0].user_name}</p>
                                        </td>
                                        <td>
                                            <input type="hidden" name="appr_user" value="${ApprLine[1].username}"/>
                                            <div class="sign-area">
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap004'}">
                                                    <img class="sign" src="/photo/${ApprLine[1].sign}" alt="서명"/>
                                                </c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap003'}"><span
                                                        class="status return">반려</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap002'}"><span
                                                        class="status ing">결재중</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[1].status == 'ap001'}"><span
                                                        class="status no-read">미확인</span></c:if>
                                            </div>
                                            <p>${ApprLine[1].user_name}</p>
                                        </td>
                                        <td>
                                            <input type="hidden" name="appr_user" value="${ApprLine[2].username}"/>
                                            <div class="sign-area">
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap004'}">
                                                    <img class="sign" src="/photo/${ApprLine[2].sign}" alt="서명"/>
                                                </c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap003'}"><span
                                                        class="status return">반려</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap002'}"><span
                                                        class="status ing">결재중</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[2].status == 'ap001'}"><span
                                                        class="status no-read">미확인</span></c:if>
                                            </div>
                                            <p>${ApprLine[2].user_name}</p>
                                        </td>
                                        <td>
                                            <input type="hidden" name="appr_user" value="${ApprLine[3].username}"/>
                                            <div class="sign-area">
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap004'}">
                                                    <img class="sign" src="/photo/${ApprLine[3].sign}" alt="서명"/>
                                                </c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap003'}"><span
                                                        class="status return">반려</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap002'}"><span
                                                        class="status ing">결재중</span></c:if>
                                                <c:if test="${DraftInfo.status !='sv' and ApprLine[3].status == 'ap001'}"><span
                                                        class="status no-read">미확인</span></c:if>
                                            </div>
                                            <p>${ApprLine[3].user_name}</p>
                                        </td>
                                    </tr>

                                    <tr class="date">
                                        <td><c:if
                                                test="${DraftInfo.status !='sv'}"> ${ApprLine[0].approval_date} </c:if></td>
                                        <td><c:if
                                                test="${DraftInfo.status !='sv'}"> ${ApprLine[1].approval_date} </c:if></td>
                                        <td><c:if
                                                test="${DraftInfo.status !='sv'}"> ${ApprLine[2].approval_date} </c:if></td>
                                        <td><c:if
                                                test="${DraftInfo.status !='sv'}"> ${ApprLine[3].approval_date} </c:if></td>
                                    </tr>

                                </table>

                            </div>

                            <div class="btm-area">
                                <div class="line">
                                    <div class="tit"><span class="ico-required">*</span>제목</div>
                                    <div class="txt"><input class="form-control" type="text" name="subject"
                                                            value="${DraftInfo.subject}" required/></div>
                                </div>
                                <div class="line">
                                    <div class="tit"><span class="ico-required">*</span>협조부서</div>
                                    <div class="txt">
                                        <fieldset class="form-group">
                                            <select class="form-select" id="basicSelect" name="cooper_dept_id">
                                                <c:forEach items="${deptList}" var="dept">
                                                    <option value="${dept.id}"
                                                            <c:if test="${dept.id == DraftInfo.cooper_dept_id}">selected</c:if>>
                                                            ${dept.text}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </fieldset>
                                    </div>
                                </div>
                                <div class="line">
                                    <div class="tit"><span class="ico-required">*</span>브랜드명</div>
                                    <div class="txt"><input class="form-control" type="text" name="name"
                                                            value="${DraftInfo.name}" required/></div>
                                </div>
                                <div class="line">
                                    <div class="tit"><span class="ico-required">*</span>시행일자</div>
                                    <div class="txt"><input class="form-control" type="date" name="from_date"
                                                            value="${DraftInfo.from_date}" required/></div>
                                </div>

                            </div>
                            <div class="editor-area">
                                <textarea id="summernote" maxlength="10000">${DraftInfo.content}</textarea>
                            </div>
                            <br/>

                            <h5>파일 첨부</h5>
                            <br/>

                            <h6>로고 파일 첨부</h6>
                            <input type="file" name="logoFile" class="filepond" data-max-file-size="10MB" type="file"/>

                            <h6>파일 첨부</h6>
                            <input type="file" name="newAttachedFiles" class="filepond-multiple" multiple
                                   data-max-file-size="10MB" data-max-files="3"/>
                            <!-- 삭제된 첨부파일 ID -->
                            <%--                            <input type="hidden" name="deletedFiles" id="deletedFiles"/>--%>

                            <input type="hidden" name="status"/>
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
<script src="/resources/assets/extensions/choices.js/public/assets/scripts/choices.js"></script>
<script src="/resources/assets/static/js/pages/form-element-select.js"></script>

<!-- FilePond 이미지 미리보기 플러그인 -->
<script src="https://unpkg.com/filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.js"></script>
<!-- FilePond JavaScript -->
<script src="https://unpkg.com/filepond@^4/dist/filepond.js"></script>
<!-- SummerNote -->
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<!-- jstree -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
<script src='/resources/js/common.js'></script>
<script src='/resources/js/textEaditor.js'></script>
<!-- 다음 검색 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src='/resources/js/approval.js'></script>
<script src='/resources/js/charjstree.js'></script>
<script>
    // FilePond 이미지 미리보기 플러그인 등록
    FilePond.registerPlugin(FilePondPluginImagePreview);

    // 기존 로고파일
    const logoFile = {
        source: "${logoFile.new_filename}", // 고유 식별값 (파일 로드 시 서버에 넘길 값)
        options: {
            type: 'local',
            file: {
                name: "${logoFile.ori_filename}",
                type: "image/jpeg",
                size: 102394
            },
            metadata: {
                poster: "/photo/${logoFile.new_filename}" // 이미지일 경우 썸네일
            }
        }
    };
    console.log("logoFile 정보 :",logoFile);

    // 기존 일반 첨부파일 배열
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
    let deletedLogoFiles = [];

    //로고 FilePond에 설정 적용
    let logoFilePond = FilePond.create(document.querySelector('input.filepond'), {
        allowImagePreview: true,  // ✅ 썸네일 미리보기 명시적으로 활성화
        imagePreviewHeight: 100,  // 선택 사항
        //allowProcess: false,     // ✅ 중요! 썸네일만 보고 업로드는 막는 역할
        files: [logoFile],
        allowMultiple: false,
        maxFiles: 1,
        labelIdle: '파일을 드래그하거나 클릭하여 업로드하세요 (1개)',
        instantUpload: false,
        allowRevert: true,
        server: {
            load: (source, load, error, progress, abort, headers) => {
                console.log("📸 썸네일 요청 source:", source);
                fetch(`/photo/${source}`)
                    .then(res => res.blob())
                    .then(load)
                    .catch(error);
            }
        },
        onremovefile: (error, file) => {
            // 삭제된 로고파일명 저장
            deletedLogoFiles.push(file.source);
        }
    });

    let attachedFilesPond = FilePond.create(document.querySelector('input[name="newAttachedFiles"]'), {
        allowImagePreview: true,  // 썸네일 미리보기 명시적으로 활성화
        imagePreviewHeight: 100,  // 선택 사항
        allowProcess: false,     // 중요! 썸네일만 보고 업로드는 막는 역할
        files: attachedFiles,
        allowMultiple: true,
        maxFiles: 3,
        labelIdle: '파일을 드래그하거나 클릭하여 업로드하세요 (최대 3개)',
        allowRevert: true,
        instantUpload: false,
        server: {
            // 썸네일 이미지 로딩용
            load: (source, load, error, progress, abort, headers) => {
                console.log("썸네일 요청 source:", source);
                fetch(`/file/filepond/${source}`)
                    .then(res => res.blob())
                    .then(load)
                    .catch(error);
            }
        },
        onremovefile: (error, file) => {
            // 삭제된 파일명 저장
            deletedFiles.push(file.source);
            //console.log("deletedFiles :", deletedFiles);
        }
    });


    //기안일
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1;
    const date = today.getDate();
    document.querySelector('input.today-date').value = year + '-' + month + '-' + date;

    // 기안문 제목 종류 변경
    var target = '${DraftInfo.target_type}' == 'df001' ? '브랜드' : '직영점';
    var action = '${DraftInfo.action_type}' == 'df011' ? '등록' : '폐점';
    $('.change-tit').text(target + ' ' + action);


    // 기안문 종류에 따른 양식
    function setForm(type1, type2, element) {
        document.querySelectorAll('.change-tit').forEach(function (titEl) {
            titEl.innerText = element.innerText; // 클릭한 텍스트로 변경
        });

        // 브랜드 기안의 경우
        if (type1 == 'brand') {
            //document.getElementsByClassName('addr-area')[0].style.display = 'none';
            document.querySelector('input[name="target_type"]').value = 'df001';
            // 주소칸 제거
            if ($("#addr-area").length >= 1) {
                $("#addr-area").remove();
            }
            // 직영점 기안의 경우
        } else if (type1 == 'direct') {
            document.querySelector('input[name="target_type"]').value = 'df002';
            // 주소칸 추가
            if ($("#addr-area").length == 0) {

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


    //수정 요청
    function updateDraft() {
        console.log('updateDraft() 실행');
        checkApprLineNull();

        const pondLogoFile = logoFilePond.getFiles();
        const hasNewLogo = pondLogoFile.length > 0 && pondLogoFile[0].status !== 5; // status 5: removed
        const hasOldLogo = deletedLogoFiles.length === 0;

        if (!hasNewLogo && !hasOldLogo) {
            alert("로고 파일은 필수입니다. 하나 이상 업로드해주세요.");
            return false;
        } else {
            textEaditorWrite('/approval/update/${reapproval}');
        }

        //최종 글 작성
        //글 전송할 url 파라미터 포함
        //전송 버튼에 textEaditorWrite(url) 함수 사용
        function textEaditorWrite(url) {
            var formData = new FormData($('form')[0]); // formData
            var content = $('#summernote').summernote('code'); // summernote로 작성된 코드
            formData.append('content', content);
            //formData.append('deleteFiles', JSON.stringify(deleteFiles));

            // 로고 파일 추가
            const logoFile = logoFilePond.getFiles();
            if (logoFile.length > 0) {
                formData.append('logoFile', logoFile[0].file);  // 첫 번째 파일을 formData에 추가

                console.log("logoFile!!! : ", logoFile);
            }

            //첨부 파일 추가
            const newAttachedFiles = attachedFilesPond.getFiles();
            if (newAttachedFiles.length > 0) {
                newAttachedFiles.forEach(function (file, index) {
                    formData.append('newAttachedFiles', file.file);
                });
            }

            // 기존 첨부 파일(유지) 추가
            const fileIds = [...document.querySelectorAll('input.existingFile')].map(el => el.value);
            formData.append('existingFileJson', JSON.stringify(fileIds));

            // 삭제된 파일 ID 전송
            formData.append("deletedFiles", JSON.stringify(deletedFiles));

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
            console.log("fileAjax()실행");
        }

        //에디터 이미지 저장
        function fileAjax(type, url, formData) {

            var csrfToken = document.querySelector('meta[name="_csrf"]').content;
            var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            $.ajax({
                type: type,
                url: url,
                data: formData,
                contentType: false,
                processData: false,
                enctype: 'multipart/form-data',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (response) {
                    fileSuccess(response, true);
                }, error: function (e) {
                    console.log(e);
                }
            });
        }

    }

    //결재라인 각 결재자가 공백이거나 null일 경우 해당 요소 삭제
    function checkApprLineNull() {
        $('.appr_line tr.name > td > p').each(function () {
            if ($(this).text().trim() === '' || $(this).text() === null) {
                $(this).remove();
            }
        });
        $('.appr_line tr.position > td').each(function () {
            if ($(this).text().trim() === '' || $(this).text() === null) {
                $(this).remove();
            }
        });
        $('input[name="appr_user"]').each(function () {
            if ($(this).val().trim() === '' || $(this).val() === null) {
                $(this).remove();
            }
        });
    }


    /* 조직도 */
    /* 조직도노드  */
    //초기 데이터
    const initialData = {
        headers: ['이름', '부서', '직급', '구분', '삭제'],
        rows: [
            ['${userDTO.name}', '${userDTO.dept.text}', '${userDTO.position_content}', '기안', '<button class="btn btn-primary">삭제</button>'],
        ],
        footer: '<button class="btn btn-outline-secondary btn-line-write" onclick="loadModal(\'ApprLine\',\'Bookmark\')">즐겨찾기 저장</button>'
    };
    var exampleData = JSON.parse(JSON.stringify(initialData));

    //서버에 넘길 결재자 id배열
    var approvalLines = ['${userDTO.username}'];

    // 선택된 ID를 rows에 추가하는 함수
    function addSelectedIdToRows(selectedId) {
        console.log("가져온 ID:", selectedId);
        if (approvalLines.includes(selectedId)) {
            layerPopup("이미 등록된 결재자입니다.", "확인", false, removeAlert, removeAlert);
            return false;
        }
        approvalLines.push(selectedId);
        console.log("approvalLines:", approvalLines);
        $.ajax({
            type: 'GET',
            url: '/approval/getUserInfo/' + selectedId,
            data: {},
            dataType: "JSON",
            success: function (response) {
                //console.log("유저이름: ", response.name);
                //console.log("유저정보: ", response.position_content);
                //console.log("유저정보: ", response.dept.text);

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
            error: function (e) {
                console.log(e);
            }
        });
    }

    // 선택된 ID를 받아서 처리
    getSelectId(function (selectedId) {
        addSelectedIdToRows(selectedId);
    });

    function chartPrint(response) {
        //console.log(response, '받아온 데이터');

        // 데이터 정렬 (menuDepth -> menuOrder 순서로 정렬)
        response.sort(function (a, b) {
            if (a.menuDepth === b.menuDepth) {
                return a.menuOrder - b.menuOrder; // 같은 depth라면 menuOrder로 정렬
            }
            return a.menuDepth - b.menuDepth; // depth 기준 정렬
        });
        //console.log("AJAX 응답 데이터 (정렬 후):", response);

        // jsTree 데이터 형식으로 변환
        const processedData = processJsTreeData(response);
        //console.log("jsTree 변환 데이터:", processedData);

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
                "default": {"icon": "bi bi-house-fill"}, // 기본 폴더 아이콘
                "file": {"icon": "bi bi-person-fill"}    // 파일 아이콘
            },
            "search": {
                "show_only_matches": true,
                "show_only_matches_children": true
            }
        }).on('loaded.jstree', function () {
            //console.log("jsTree가 성공적으로 초기화되었습니다.");
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
            //console.log("선택된 노드:", data.selected);
            if (data.selected.length > 0) {
                if (document.getElementById('orgBody').childNodes.length > 3) {
                    layerPopup("결재자는 최대3명까지 선택 가능합니다.", "확인", false, removeAlert, removeAlert);
                    return false;
                }
                const selectedId = data.selected[0]; // 선택된 노드의 ID
                //console.log("선택된 노드 ID:", selectedId);

                // 설정된 콜백 함수 호출
                if (typeof selectIdCallback === "function") {
                    selectIdCallback(selectedId); // 콜백 함수에 선택된 ID 전달
                }
            } else {
                //console.log("선택된 노드가 없습니다.");
            }
        }).on("load_node.jstree", function (e, data) {
            //console.log("노드 로드 완료:", data.node);
        });


        // 나의 결재라인 추가
        $('#approvalBtn').text('나의 결재라인');

        $('.chart-btn-style').on('click', function () {
            /* 조직도 버튼 클릭 시 조직도 버튼 show 나의 결재선 hide */
            if ($(this).hasClass('chart-btn')) {
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

            if ($(this).hasClass('chart-mybtn')) {
                /* 나의 결재선 버튼 클릭 시 나의 결재선 show 조직도 버튼 hide */
                //$('.chart-mybtn').on('click', function() {
                $('#chartModalBox .tbody-style').hide();
                $('#chartModalBox .bookmark').show();

                $.ajax({
                    type: 'GET',
                    url: '/approval/list/line',
                    data: {},
                    datatype: 'JSON',
                    success: function (data) {
                        console.log(data);
                        drawList(data.bookmarkList);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            }
        });
    }


    // 즐겨찾기
    var thead = '';

    function drawList(list) {
        var content = '';
        var approverNames = '';
        content += '<tbody class="bookmark">';
        for (var view of list) {
            approverNames = view.approver_names.split(', ');
            var approverPositions = view.approver_positions.split(', ');
            console.log("approverNames : ", approverNames);

            content += '<tr>';
            content += '<td class="line" data-view=\'' + JSON.stringify(view) + '\' colspan="2">' + view.line_name + '</td>';
            // 결재자 수 만큼
            for (var i = 0; i < approverNames.length; i++) {
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
        for (var i = 0; i < approverNames.length; i++) {
            thead += '<th>결재자' + (i + 1) + '</th>';
        }
        thead += '<th>삭제</th>';
        thead += '</tr>';
        $('.list thead').html(thead);
    }


    // 즐겨찾기 -> 결재라인 추가
    $(document).on('click', '.bookmark .line', function () {
        //비우고
        resetTableData();
        var view = JSON.parse($(this).attr('data-view'));
        var usernames = JSON.parse(view.approver_usernames);
        for (var i = 1; i < usernames.length; i++) {
            addSelectedIdToRows(usernames[i]);
        }
    });


    //조직도 노드 해당 사원 삭제
    $(document).on('click', '#chartModalBox #orgBody .btn', function () {
        var idx = $(this).closest('tr').index();
        if (idx != 0) {
            $(this).closest('tr').remove();
            initialData.rows.splice(idx, 1);
            exampleData.rows.splice(idx, 1);
            approvalLines.splice(idx, 1);
            console.log("approvalLines 수정 : ", approvalLines);
        } else {
            layerPopup("기안자는 삭제하실 수 없습니다.", "확인", false, removeAlert, removeAlert);
        }
    });


    // 조직도노드 등록버튼 (결재라인 설정)
    var addBtn = document.getElementById('addModal');

    function addBtnFn() {
        var lineNodes = document.getElementById('orgBody').childNodes;
        // 기안문 기존 결재라인 설정 초기화
        $('.appr_line tr.name > td > p').text('');
        $('.appr_line tr.position > td').text('');
        $('input[name="appr_user"]').val('');
        // 기안문 결재라인 설정
        for (var i = 0; i <= lineNodes.length; i++) {
            userName = lineNodes[i].childNodes[0].innerText;
            userPosition = lineNodes[i].childNodes[2].innerText;
            document.querySelectorAll('.appr_line tr.name > td > p')[i].innerText = userName;
            document.querySelectorAll('.appr_line tr.position > td')[i].innerText = userPosition;
            document.querySelectorAll('input[name="appr_user"]')[i].value = approvalLines[i];
            document.getElementById('chartModalBox').style.display = "none";
        }
    }

    function setModalData() {}
    document.addEventListener('click', function (event) {
        // 결재라인 즐겨찾기 모달 등록 버튼 클릭
        if (event.target.id === 'SaveBookmark') {
            saveApprvalLine();
        }
    });

    var csrfToken = document.querySelector('meta[name="_csrf"]').content;
    var csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    function saveApprvalLine() {
        //console.log("approvalLines", approvalLines);
        var formData = new FormData($('#BookmarkFrom')[0]);
        formData.append('approvalLines', JSON.stringify(approvalLines));
        $.ajax({
            type: 'POST',
            url: '/approval/setApprLineBookmark',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'JSON',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                if (response.success) {
                    document.getElementById('modalBox').style.display = "none";
                    layerPopup("결재라인 저장이 저장되었습니다.", "확인", false, removeAlert, removeAlert);
                }
            }, error: function (e) {
                console.log(e);
            }
        });

    }


</script>

</html>