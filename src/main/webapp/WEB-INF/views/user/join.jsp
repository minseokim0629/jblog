<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script src="${pageContext.request.contextPath}/assets/js/jquery/jquery-1.9.0.js"></script>
<script>
$(function(){
	$("#btn-checkemail").click(function() {
		var id = $("#id").val();
		if(id == "") {
			return;
		}
		
		$.ajax({
			url: "${pageContext.request.contextPath}/api/user/checkid?id=" + id,
			type: "get",
			dataType: "json",
			success: function(response) {
				//console.log(response);
				if(response.result != "success") {
					console.error(response.message);
					return;
				}
				if(response.data.exist) {
					alert("아이디가 존재합니다. 다른 아이디를 사용해 주세요.");
					$("#id").val("");
					$("#id").focus();
					return;
				}
				
				$("#img-checkemail").show();
				$("#btn-checkemail").hide();
			},
			error: function(xhr, status,err){
				console.error(err);
			}
		});
	});
});
</script>
</head>
<body>
	<div class="center-content">
		<h1 class="logo">JBlog</h1>
		<c:import url="/WEB-INF/views/user/menu.jsp"/>
		<form:form class="join-form" id="join-form" modelAttribute="userVo" method="post" action="${pageContext.request.contextPath}/user/join">
			<label class="block-label" for="name">이름</label>
			<form:input path="name"/>
			<p style="color:#f00; text-align:left; padding:0">
				<form:errors path="name" />
			</p>
			<label class="block-label" for="blog-id">아이디</label>
			<form:input path="id"/>
			<p style="color:#f00; text-align:left; padding:0">
				<form:errors path="id" />
			</p>
			<input id="btn-checkemail" type="button" value="id 중복체크">
			<img id="img-checkemail" style="display: none;" src="${pageContext.request.contextPath}/assets/images/check.png" style="display:none">

			<label class="block-label" for="password">패스워드</label>
			<form:password path="password"/>
			<p style="color:#f00; text-align:left; padding:0">
				<form:errors path="password"/>
			</p>

			<fieldset>
				<legend>약관동의</legend>
				<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
				<label class="l-float">서비스 약관에 동의합니다.</label>
			</fieldset>

			<input type="submit" value="가입하기">

		</form:form>
	</div>
</body>
</html>
