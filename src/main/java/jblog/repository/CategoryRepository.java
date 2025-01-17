package jblog.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {
	private SqlSession sqlSession;
	
	public CategoryRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(CategoryVo categoryVo) {
		return sqlSession.insert("category.insert", categoryVo);
	}

	public Long findDefaultId(String id) {
		return sqlSession.selectOne("category.findDefaultId", id);
	}

	public CategoryVo findById(Long categoryId) {
		return sqlSession.selectOne("category.findById", categoryId);
	}

	public String findBlogId(String id, Long categoryId) {
		return sqlSession.selectOne("category.findBlogId", Map.of("blogId", id, "categoryId", categoryId));
	}
	
	public List<CategoryVo> findTitles(String blogId) {
		return sqlSession.selectList("category.findTitles", blogId);
	}

	public List<CategoryVo> findContents(String id) {
		return sqlSession.selectList("category.findContents", id);
	}

	public int deleteById(Long categoryId) {
		return sqlSession.delete("category.deleteById", categoryId);
	}
}
