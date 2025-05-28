<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="sidebar">
	<div class="sidebar-wrapper scrollbar-custom active">
		<div class="sidebar-header position-relative">
			<div class="d-flex justify-content-between align-items-center">
				<div class="logo">
					<a href="/main"><img src="/resources/img/logo.jpg"
						alt="Logo" srcset=""></a>
				</div>
			</div>
		</div>
		<div class="sidebar-menu">
			<ul class="menu"></ul>
		</div>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
	var authority = "";
	$.ajax({
		type:'GET',
		url:'/sidebar',
		data:{},
		datatype:'JSON',
		xhrFields: {
			withCredentials: true
		},
		success:function(data){
			console.log(data.menuList);
			drawMenu(data.menuList);
		},
		error:function(e){
			console.log(e);
		}
	});
		
		
	function drawMenu(menuList){
		var content ='';
		for (var menu of menuList) {
			content +='<li class="sidebar-item';

			if(menu.childMenus.length > 0){
				content +=' has-sub">';
				content +='<a class="sidebar-link"> ';
			}else{
				content +='">';
				content +='<a href="'+menu.url+'" class=\"sidebar-link\"> ';
			}

			content +='<i class="bi '+menu.icon+'"></i> ';
			content +='<span>'+menu.menu_name+'</span></a>';

			if(menu.childMenus.length > 0){
				content +='<ul class="submenu">';
				for (var menuDepth2 of menu.childMenus) {
					content +='<li class="submenu-item ">';
					content +='<a href="'+menuDepth2.url+'" class="submenu-link">'+menuDepth2.menu_name+'</a>';
					content +='</li>';
				}
			}

			content +='</ul>';
			content +='</li>';
		}

	     $('.sidebar-menu .menu').append(content);
	}

</script>