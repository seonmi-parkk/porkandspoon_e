<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
	<h2>로그인 성공 ★</h2>
	
	<form method="post" action="/logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	    <button type="submit">Logout</button>
	</form>
	
	<a href="/sa/saTest">super admin</a> <br/>
	<a href="/a/aTest">admin</a> <br/>
	<a href="/m/mTest">manager</a> <br/>
	<a href="/u/uTest">user (text page)</a> <br/>
	<!-- <a href="/chart">chart</a> -->

	<a href="/chat/list">채팅방 이동</a> <br/>
	<a href="/ad/user/listView">직원 리스트 이동</a> <br/>
	<a href="/ma/dept/listView">브랜드 리스트</a> <br/>
	<a href="/ad/employeeTransfer">인사이동 리스트</a> <br/>




	<a href="/calender">캘린더 이동</a> <br/>
	<a href="/ad/education">교육 이동</a><br/>
	<a href="/mEducation">직영점주 교육 이동</a><br/>
	<a href="/articleResevation">물품예약 이동</a><br/>
	<a href="/roomResevation">물품예약 이동</a><br/>
	<a href="/ad/resevation/list">물품등록 이동</a><br/>

	<a href="/board/View">공지사항 게시판</a> <br/>
	<a href="/lbboardlist/View">라이브러리 게시판</a> <br/>
	<a href="/chartlist/View">조직도</a> <br/>
	<a href="/foodielist/View">맛집지도</a> <br/> <!-- 404 -->

	<a href="/ad/mealMenu">식단표</a> <br/>
	<a href="/ad/myPageBuy">나의 구매기록</a> <br/>
	<a href="/ad/part">아르바이트 리스트</a> <br/>
	<a href="/ad/spotManage">직영점 관리</a> <br/>
	<a href="/us/directManage">매장 관리</a> <br/>
	<a href="/ad/brandManage">브랜드 관리</a>

	<a href="/mail/write">메일작성</a> <br/>
	<a href="/mail/listView/recv">받은메일함</a> <br/>
	<a href="/mail/listView/sd">보낸메일함</a> <br/>
	<a href="/mail/listView/sv">임시보관함</a> <br/>
	<a href="/mail/listView/bk">중요메일함</a> <br/>
	<a href="/mail/listView/del">휴지통</a> <br/>

	<a href="/approval/writeView">기안작성</a> <br/>
	<a href="/approval/listView/my">나의문서함</a> <br/>
	<a href="/approval/listView/dept">부서문서함</a> <br/>

	<a href="/trip/listView">개인 출장 기록 리스트 </a> <br/>
	<a href="/trip/write">개인 출장 기록 작성</a> <br/>
	<a href="/trip/detail">개인 출장 기록 상세</a> <br/>
	<a href="/trip/update">개인 출장 기록 수정</a> <br/>






</body>

<script src='/resources/js/common.js'></script>
<script>

</script>
</html>