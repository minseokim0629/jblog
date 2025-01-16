package jblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jblog.repository.BlogRepository;
import jblog.repository.CategoryRepository;
import jblog.repository.UserRepository;
import jblog.vo.BlogVo;
import jblog.vo.CategoryVo;
import jblog.vo.UserVo;

@Service
public class UserService {
	private UserRepository userRepository;
	private BlogRepository blogRepository;
	private CategoryRepository categoryRepository;
	
	public UserService(UserRepository userRepository, BlogRepository blogRepository, CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.blogRepository = blogRepository;
		this.categoryRepository = categoryRepository;
	}

	@Transactional
	public void join(UserVo userVo) {
		int count = userRepository.insert(userVo);
		if (count == 1) {
			BlogVo blogVo = new BlogVo();
			blogVo.setBlogId(userVo.getId());
			blogVo.setTitle(blogVo.getBlogId() + "'s Blog");
			blogVo.setProfile("/assets/images/loopy.jpg");
			count = blogRepository.insert(blogVo);
			if(count == 1) {
				CategoryVo categoryVo = new CategoryVo();
				categoryVo.setName("미분류");
				categoryVo.setDescription("");
				categoryVo.setBlogId(blogVo.getBlogId());
				categoryRepository.insert(categoryVo);
			}
		}
	}

	public UserVo getUser(String id, String password) {
		return userRepository.findByIdAndPassword(id, password);
	}
	
	public UserVo getUser(String id) {
		return userRepository.findById(id);
	}

}
