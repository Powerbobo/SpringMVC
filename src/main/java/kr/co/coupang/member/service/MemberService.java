package kr.co.coupang.member.service;

import kr.co.coupang.member.domain.Member;

public interface MemberService {
	/**
	 * ¸â¹ö µî·Ï Service
	 * @param member
	 * @return int
	 */
	public int registerMember(Member member);
	
	/**
	 * ¸â¹ö ·Î±×ÀÎ Service
	 * @param member
	 * @return member °´Ã¼
	 */
	public Member memberLoginCheck(Member member);
	
	/** 
	 * ¸â¹ö Á¶È¸ Service
	 * @param memberId
	 * @return member °´Ã¼
	 */
	public Member showOneById(String memberId);
	
	/**
	 * ¸â¹ö »èÁ¦ Service
	 * @param memberId
	 * @return int
	 */
	public int deleteMember(String memberId);
	
	/**
	 * ¸â¹ö ¼öÁ¤ Service
	 * @param member
	 * @return int
	 */
	public int modifyMember(Member member);
}
