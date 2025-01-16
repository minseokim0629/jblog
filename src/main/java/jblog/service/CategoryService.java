package jblog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jblog.repository.CategoryRepository;
import jblog.repository.PostRepository;
import jblog.vo.CategoryVo;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;
	private PostRepository postRepository;
	
	public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository) {
		this.categoryRepository = categoryRepository;
		this.postRepository = postRepository;
	}
	
	public List<CategoryVo> getTitles(String id) {
		return categoryRepository.findTitles(id);
	}
	
	public List<CategoryVo> getContents(String id) {
		return categoryRepository.findContents(id);
	}
	
	@Transactional
	public void deleteCategory(Long categoryId) {
		int count = categoryRepository.deleteById(categoryId);
		if(count == 1) {
			postRepository.deleteByCategoryId(categoryId);
		}
	}

	public void addCategory(CategoryVo categoryVo) {
		categoryRepository.insert(categoryVo);
	}
}
