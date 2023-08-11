package kr.co.coupang.member.store;

import org.apache.ibatis.session.SqlSession;

import kr.co.coupang.member.domain.Member;

public interface MemberStore {
	public int insertMember(SqlSession sqlSessoin, Member member);

	public Member selectMemberLogin(SqlSession sqlSession, Member member);

	public Member showOneById(SqlSession sqlSession, String memberId);

	public int deleteMember(SqlSession sqlSession, String memberId);

	public int modifyMember(SqlSession sqlSession, Member member);
}
