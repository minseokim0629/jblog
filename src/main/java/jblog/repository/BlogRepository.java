package jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	private SqlSession sqlSession;
	
	public BlogRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(BlogVo blogVo) {
		return sqlSession.insert("blog.insert", blogVo);
	}

	public BlogVo findById(String id) {
		return sqlSession.selectOne("blog.findById", id);
	}

	public int update(BlogVo blogVo) {
		return sqlSession.update("blog.update", blogVo);
	}
}
