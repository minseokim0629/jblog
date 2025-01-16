package jblog.security;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jblog.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. 정적자원 핸들러가 아닌지 확인
		if(!(handler instanceof HandlerMethod)) {
			return true;
		}
		
		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 3. auth annotation이 있는지 확인
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. 만약 auth annotation이 없다면
		if(auth == null) {
			return true;
		}
		
		// 5. auth annotation이 있다면 login 여부 확인
		HttpSession session = request.getSession();
		UserVo userVo = (UserVo) session.getAttribute("authUser");
		
		
		if(userVo == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 6. 로그인 한 사람이 본인 블로그로 접근한건지 확인
		String urlBlog = request.getRequestURI().split("/")[2];
		System.out.println(userVo.getId() + ":" + urlBlog);
		if(!(urlBlog.equals(userVo.getId()))) {
			response.sendRedirect(request.getContextPath() + "/" + urlBlog);
			return false;
		}
		
		return true;
	}
	
}
