package kr.co.coupang.member.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;
import kr.co.coupang.member.store.MemberStore;

// bean ���
@Service
public class MemberServiceImpl implements MemberService {
	
	// SqlSession �� root-context.xml�� ������ ����
	// root-context.xml �� sqlSession bean�� ����ؼ� ����� �� ����.
	@Autowired
	private SqlSession sqlSession;	// connection ��� ���� �Ѱ���
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
