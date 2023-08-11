package kr.co.coupang.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import kr.co.coupang.member.domain.Member;
import kr.co.coupang.member.service.MemberService;

@Controller // bean등록
@SessionAttributes({"memberId","memberName"})
public class MemberController {
	
	// MemberController 에서 메소드를 계속 만들어사 사용해야해서 전역변수 선언
	@Autowired
	private MemberService service;
	
	// 핸들러 매핑
	// doGet - 페이지 이동용
	// ** 회원가입 페이지 이동
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
	// showRegisterForm -> 메소드명은 무슨 동작을 하는지 알기 쉽게 네이밍
		return "member/register";	// request.getRequestDispacher
	}
	
	// doPost - 데이터 저장용
	// ** 회원가입
	@RequestMapping(value="/member/register.do", method=RequestMethod.POST)
	public String registerMember(
			HttpServletRequest request
			, HttpServletResponse response
//			@RequestParam("member") String memberId -> request.getParameter 한것과 동일하다
//			@RequestParam("memberId")은 생략이 가능하다.
			, @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, @RequestParam("memberName") String memberName
			, @RequestParam("memberAge") int memberAge
			, @RequestParam("memberGender") String memberGender
			, @RequestParam("memberEmail") String memberEmail
			, @RequestParam("memberPhone") String memberPhone
			, @RequestParam("memberAddress") String memberAddress
			, @RequestParam("memberHobby") String memberHobby
			, Model model) {
		Member member = new Member(memberId, memberPw, memberName, memberAge, memberGender, memberEmail, memberPhone, memberAddress, memberHobby);
		// Exception 값을 뿌려주기 위해서 수기로 씀!
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				// 성공
//				response.sendRedirect("/index.jsp");
				return "redirect:/index.jsp";
			} else {
				// 실패
				model.addAttribute("msg", "회원가입이 완료되지 않았습니다.");
				return "common/errorPage";
			}
		} catch(Exception e) {
			e.printStackTrace();	// 콘솔창에 빨간색으로 뜨게함
			model.addAttribute("msg", e.getMessage()); // 콘솔창에 뜨는 메세지를 웹 페이지로 뜨게함
			return "common/errorPage";
		}
		
//		JDBC, Mybatis 에서 사용했던 Servlet 객체를 Spring 에서도 사용할 수 있다!
//		String memberId = request.getParameter("memberId");
		
//		Spring에서는 더이상 request, response를 사용하지 않음
//		request.setCharacterEncoding("UTF-8");
//		request.setAttribute("", "");
//		request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
//		request.sendRedirect("/index.jsp");
	}
	
	// ** 로그인
	@RequestMapping(value="/member/login.do", method=RequestMethod.POST)
	public String memberLogin(
			HttpServletRequest request
			, HttpServletResponse response
			, @RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, Model model) {
		try {
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberPw(memberPw);
			Member mOne = service.memberLoginCheck(member);
			if(mOne != null) {
				System.out.println(mOne.toString());
				// 성공하면 로그인 페이지로 이동
//				response.sendRedirect("/index.jsp");
//				model.addAttribute("member", mOne);
				// 로그인 세션 저장
//				HttpSession session = request.getSession();
//				session.setAttribute("memberId", mOne.getMemberId());
//				session.setAttribute("memberName", mOne.getMemberName());
//				model.addAttribute("memberId", mOne.getMemberId());
				
				// @SessionAttributes는 model에 있는 Key값을 Session에 담아줌
				// ==========================================================
				model.addAttribute("memberName", mOne.getMemberName());
				model.addAttribute("memberId", mOne.getMemberId());
				// Session에 담기 위해 model에 Key값을 추가해주는 코드
				// index.jsp에서 ${}를 통해 쓰는 것과 상관없음!!
				// ==========================================================
				
				return "redirect:/index.jsp";
			} else {
				// 실패하면 실패메세지 출력
				model.addAttribute("msg", "데이터가 조회되지 않았습니다.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// 예외 발생 시 메세지 출력
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
		
	}
	
	// ** 로그아웃
	@RequestMapping(value="/member/logout.do", method=RequestMethod.GET)
	public String memberLogout(
			HttpSession sessionPrev
			// SessionStatus는 스프링의 어노테이션(@SessionAttributes)로 지원되는 세션을 만료시킨다.
			// 사용된 메소드는 setComplete)();
			, SessionStatus session
			, Model model) {
		if(session != null) {
//			session.invalidate();
			session.setComplete();
			if(session.isComplete()) {
				// 세션만료 유효성 체크
			}
			return "redirect:/index.jsp";
		} else {
			model.addAttribute("msg", "로그아웃을 완료하지 못했습니다.");
			return "common/errorPage";
		}
	}
	
	// 마이페이지
	@RequestMapping(value="/member/myPage.do", method=RequestMethod.GET)
	public String showDetailMember(
			// SELECT * FROM MEMBER_TBL MEMBER_ID = ?
			// 로그인한 아이디로 정보를 조회해야 하기 때문에 @RequestParam 으로 memberId를 가져옴
			@RequestParam("memberId") String memberId
			, Model model) {
		try {
			Member member = service.showOneById(memberId);
			if(member != null) {
				// 성공
				model.addAttribute("member", member);
				return "member/myPage";
			} else {
				// 실패
				model.addAttribute("msg", "데이터 조회에 실패했습니다.");
				return "/common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/delete.do", method=RequestMethod.GET)
	public String removeMember(
			@RequestParam("memberId") String memberId
			,Model model) {
		try {
			int result = service.deleteMember(memberId);
			if(result > 0) {
				// 성공
				return "redirect:/member/logout.do";
			} else {
				// 실패
				model.addAttribute("msg", "회원 탈퇴가 완료되지 않았습니다..");
				return "common/errorPage";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	
	// doGet
	// 수정 페이지로 이동
	@RequestMapping(value="/member/update.do", method=RequestMethod.GET)
	public String modifyViewMember(
			@RequestParam("memberId") String memberId
			,Model model) {
		try {
			Member member = service.showOneById(memberId);
			if(member != null ) {
				// 성공
				model.addAttribute("member", member);
				return "member/modify";
			} else {
				// 실패
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/errorPage";
		}
	}
	
	// doPost
	// 수정하기
	@RequestMapping(value="/member/update.do", method=RequestMethod.POST)
	public String modifyMember(
			@RequestParam("memberId") String memberId
			, @RequestParam("memberPw") String memberPw
			, @RequestParam("memberEmail") String memberEmail
			, @RequestParam("memberPhone") String memberPhone
			, @RequestParam("memberAddress") String memberAddress
			, @ RequestParam("memberHobby") String memberHobby
			, Model model) {
		try {
			Member member = new Member(memberId, memberPw, memberEmail, memberPhone, memberAddress, memberHobby);
			int result = service.modifyMember(member);
			if(result > 0) {
				// 성공 -> 마이페이지
				return "member/myPage";
			} else {
				// 실패 -> 수정페이지 그대로
				model.addAttribute("msg", "데이터 수정에 실패했습니다.");
				return "/common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "/common/errorPage";
		}
	}
	
	
	
	
	
	
	
	
	
}