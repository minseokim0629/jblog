package jblog.security;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jblog.service.BlogService;
import jblog.service.UserService;
import jblog.vo.BlogVo;
import jblog.vo.UserVo;

public class LoginInterceptor implements HandlerInterceptor {
	
	private UserService userService;
	//private BlogService blogService;
	
	public LoginInterceptor(UserService userService, BlogService blogService) {
		this.userService = userService;
		//this.blogService = blogService;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 로그인 한 정보 받아오기
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		// 디비에서 해당 유저 있는지 찾아서 반환
		UserVo authUser = userService.getUser(id, password);
		
		if(authUser == null) {
			// 다시 로그인하는 곳으로 보내기
			request.setAttribute("id", id);
			request.setAttribute("result", "fail");
			System.out.println(request.getContextPath());
			request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
			return false;
		}
		
		//BlogVo adminBlog = blogService.getAdminBlog(id);
		HttpSession session = request.getSession();
		session.setAttribute("authUser", authUser);
		//session.setAttribute("adminBlog", adminBlog);
		
		response.sendRedirect(request.getContextPath() + "/" + id);
		
		return false;
	}
	
}
