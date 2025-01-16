package jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import jblog.vo.PostVo;

@Repository
public class PostRepository {
	private SqlSession sqlSession;
	
	public PostRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Long findDefaultId(Long categoryId) {
		return sqlSession.selectOne("post.findDefaultId", categoryId);
	}

	public PostVo findById(Long postId) {
		return sqlSession.selectOne("post.findById", postId);
	}
	
	public Long findCategoryId(Long postId) {
		return sqlSession.selectOne("post.findCategoryId", postId);
	}
	public List<PostVo> findByCategoryId(Long categoryId) {
		return sqlSession.selectList("post.findByCategoryId", categoryId);
	}

	public int deleteByCategoryId(Long categoryId) {
		return sqlSession.delete("post.deleteByCategoryId", categoryId);
	}

	public int insert(PostVo postVo) {
		return sqlSession.insert("post.insert", postVo);
	}
}
