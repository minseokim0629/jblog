package jblog.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jblog.security.Auth;
import jblog.service.BlogService;
import jblog.service.CategoryService;
import jblog.service.FileUploadService;
import jblog.service.PostService;
import jblog.vo.BlogVo;
import jblog.vo.CategoryVo;
import jblog.vo.PostVo;

@Controller
@RequestMapping("/{userId:(?!assets).*}") // 정규표현식으로 사용
public class BlogController {
	private BlogService blogService;
	private FileUploadService fileUploadService;
	private CategoryService categoryService;
	private PostService postService;

	public BlogController(BlogService blogService, FileUploadService fileUploadService, CategoryService categoryService,
			PostService postService) {
		this.blogService = blogService;
		this.fileUploadService = fileUploadService;
		this.categoryService = categoryService;
		this.postService = postService;
	}

	@RequestMapping({ "", "/{path1}", "/{path1}/{path2}" })
	public String index(@PathVariable("userId") String id, @PathVariable("path1") Optional<Long> path1,
			@PathVariable("path2") Optional<Long> path2, Model model) {
		// Optional -> path1, path2가 null로 오면 에러가기 때문에 Optional 객체 사용

		Map<String, Object> mainContents = blogService.getMainContents(id, path1, path2);
		String result = mainContents.get("result").toString();

		if (result == "fail") {
			String data = mainContents.get("data").toString();
			
			if (data == "blog") {
				return "redirect:/";
			}
			
			if (data == "category") {
				return "redirect:/" + id;
			}
			
			if (data == "post") {
				String categoryId = mainContents.get("categoryId").toString();
				return "redirect:/" + id + "/" + categoryId;
			}
		}

		/*
		 * CategoryVo categoryVo = (CategoryVo) mainContents.get("categoryVo");
		 * categoryId = categoryVo.getId(); PostVo postVo = (PostVo)
		 * mainContents.get("postVo"); if(postVo!=null) { postId = postVo.getId(); }
		 * 
		 * System.out.println("BlogController.main(" + id + ", "+ categoryId + ", " +
		 * postId + ")");
		 */

		model.addAllAttributes(mainContents);

		return "/blog/main";
	}

	@Auth
	@RequestMapping("/admin")
	public String adminDefault(@PathVariable("userId") String id, Model model) {
		BlogVo blogVo = blogService.getBlog(id);
		model.addAttribute("blogVo", blogVo);
		return "/blog/admin-default";
	}

	@Auth
	@RequestMapping("/admin/update")
	public String adminUpdate(HttpServletRequest request, @PathVariable("userId") String id, BlogVo blogVo,
			@RequestParam("logo-file") MultipartFile file) {
		blogVo.setBlogId(id);
		// 1. profile 이름 지정
		String profile = fileUploadService.restore(file);
		// 2. profile 수정 시 blogVo에 profile 세팅
		if (profile != null) {
			blogVo.setProfile(profile);
		}
		// 3. blogVo update
		blogService.updateBlog(blogVo);
		// 4. session 정보 다시 세팅
		HttpSession session = request.getSession();
		session.setAttribute("adminBlog", blogVo);

		return "redirect:/" + id;
	}

	@Auth
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("userId") String id, Model model) {
		BlogVo blogVo = blogService.getBlog(id);
		List<CategoryVo> categories = categoryService.getContents(id);
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("categories", categories);

		return "/blog/admin-category";
	}

	@Auth
	@RequestMapping("/admin/category/delete/{categoryId}")
	public String adminCategoryDelete(@PathVariable("userId") String id, @PathVariable("categoryId") Long categoryId) {
		categoryService.deleteCategory(categoryId);

		return "redirect:/" + id + "/admin/category";
	}

	@Auth
	@PostMapping("/admin/category/add")
	public String adminCategoryAdd(@PathVariable("userId") String id, @RequestParam("name") String name,
			@RequestParam("desc") String description) {
		CategoryVo categoryVo = new CategoryVo();
		categoryVo.setName(name);
		categoryVo.setDescription(description);
		categoryVo.setBlogId(id);

		categoryService.addCategory(categoryVo);
		System.out.println(categoryVo);
		return "redirect:/" + id + "/admin/category";
	}

	@Auth
	@RequestMapping("/admin/write")
	public String adminWrite(@PathVariable("userId") String id, Model model) {
		BlogVo blogVo = blogService.getBlog(id);
		List<CategoryVo> categories = categoryService.getTitles(id);
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("categories", categories);

		return "blog/admin-write";
	}

	@Auth
	@RequestMapping(value = "/admin/write/add", method = RequestMethod.POST)
	public String adminWrite(@PathVariable("userId") String id, PostVo postVo) {
		System.out.println(postVo);
		postService.addPost(postVo);
		return "redirect:/" + id;
	}
}
