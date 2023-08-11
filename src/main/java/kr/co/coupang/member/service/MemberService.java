package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * ��� ��� Service
	 * @param member
	 * @return int
	 */
	public int registerMember(Member member);
	
	/**
	 * ��� �α��� Service
	 * @param member
	 * @return member ��ü
	 */
	public Member memberLoginCheck(Member member);
	
	/** 
	 * ��� ��ȸ Service
	 * @param memberId
	 * @return member ��ü
	 */
	public Member showOneById(String memberId);
	
	/**
	 * ��� ���� Service
	 * @param memberId
	 * @return int
	 */
	public int deleteMember(String memberId);
	
	/**
	 * ��� ���� Service
	 * @param member
	 * @return int
	 */
	public int modifyMember(Member member);
}
