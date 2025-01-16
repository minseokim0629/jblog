package jblog.repository;

import java.util.List;

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

	public Long findDefaultId() {
		return sqlSession.selectOne("category.findDefaultId");
	}

	public CategoryVo findById(Long categoryId) {
		return sqlSession.selectOne("category.findById", categoryId);
	}

	public String findBlogId(Long categoryId) {
		return sqlSession.selectOne("category.findBlogId", categoryId);
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
