package kr.co.coupang.member.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;
import kr.co.coupang.member.store.MemberStore;

// bean 등록
@Service
public class MemberServiceImpl implements MemberService {
	
	// SqlSession 은 root-context.xml과 관련이 있음
	// root-context.xml 에 sqlSession bean을 등록해서 사용할 수 있음.
	@Autowired
	private SqlSession sqlSession;	// connection 대신 연결 넘겨줌
	@Autowired
	private MemberStore mStore;

	@Override
	public int registerMember(Member member) {
//		Connection conn = JDBCTemplate.createConnection();
		int result = mStore.insertMember(sqlSession, member);
		return result;
	}

	@Override
	public Member memberLoginCheck(Member member) {
		Member mOne = mStore.selectMemberLogin(sqlSession, member);
		return mOne;
	}

	@Override
	public Member showOneById(String memberId) {
		Member mOne = mStore.showOneById(sqlSession, memberId);
		return mOne;
	}

	@Override
	public int deleteMember(String memberId) {
		int result = mStore.deleteMember(sqlSession, memberId);
		return result;
	}

	@Override
	public int modifyMember(Member member) {
		int result = mStore.modifyMember(sqlSession, member);
		return result;
	}
	
}
