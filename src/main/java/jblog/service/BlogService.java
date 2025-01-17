package jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import jblog.repository.BlogRepository;
import jblog.repository.CategoryRepository;
import jblog.repository.PostRepository;
import jblog.vo.BlogVo;
import jblog.vo.CategoryVo;
import jblog.vo.PostVo;

@Service
public class BlogService {
	private BlogRepository blogRepository;
	private CategoryRepository categoryRepository;
	private PostRepository postRepository;

	public BlogService(BlogRepository blogRepository, CategoryRepository categoryRepository,
			PostRepository postRepository) {
		this.blogRepository = blogRepository;
		this.categoryRepository = categoryRepository;
		this.postRepository = postRepository;
	}

	public Map<String, Object> getMainContents(String id, Optional<Long> path1, Optional<Long> path2) {
		Map<String, Object> map = new HashMap<>();

		Long categoryId = 0L;
		Long postId = 0L;

		// category 및 post id 세팅
		if (path2.isPresent()) {
			categoryId = path1.get();
			postId = path2.get();
		} else if (path1.isPresent()) {
			categoryId = path1.get();
			postId = postRepository.findDefaultId(categoryId);
		} else {
			categoryId = categoryRepository.findDefaultId(id);
			postId = postRepository.findDefaultId(categoryId);
		}

		System.out.println("result: " + categoryId + ":" + postId);
		BlogVo blogVo = blogRepository.findById(id);

		// 해당 사용자가 없을 경우
		if (blogVo == null) {
			map.put("result", "fail");
			map.put("data", "blog");
			return map;
		}

		String blogIdOfCategoryId = categoryRepository.findBlogId(id, categoryId);
		System.out.println(id+":"+blogIdOfCategoryId);
		// 블로그에 속한 카테고리가 아닐 경우
		if (!(id.equals(blogIdOfCategoryId))) {
			map.put("result", "fail");
			map.put("data", "category");
			return map;
		}

		CategoryVo categoryVo = categoryRepository.findById(categoryId);

		List<CategoryVo> categories = categoryRepository.findTitles(id);

		PostVo postVo = postRepository.findById(postId);

		if (postVo == null) {
			// postId를 지정했는데 해당 글이 없을 경우 
			if (postId != 0L) {
				map.put("result", "fail");
				map.put("data", "post");
				map.put("categoryId", categoryId);
				return map;
			}
		} else {
			Long categoryIdOfpostId = postRepository.findCategoryId(postId);

			// 카테고리에 속한 글이 아닐 경우
			if (!(categoryIdOfpostId.equals(categoryId))) {
				map.put("result", "fail");
				map.put("data", "post");
				map.put("categoryId", categoryId);
				return map;
			}
		}

		List<PostVo> posts = postRepository.findByCategoryId(categoryId);

		map.put("result", "success");
		map.put("blogVo", blogVo);
		map.put("categoryVo", categoryVo);
		map.put("categories", categories);
		Optional.ofNullable(postVo).ifPresent(vo -> map.put("postVo", vo));
		Optional.ofNullable(posts).ifPresent(vo -> map.put("posts", vo));

		return map;
	}

	public BlogVo getBlog(String id) {
		return blogRepository.findById(id);
	}

	public void updateBlog(BlogVo blogVo) {
		blogRepository.update(blogVo);
	}

}
