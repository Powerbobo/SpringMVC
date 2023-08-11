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

@Controller // bean���
@SessionAttributes({"memberId","memberName"})
public class MemberController {
	
	// MemberController ���� �޼ҵ带 ��� ������ ����ؾ��ؼ� �������� ����
	@Autowired
	private MemberService service;
	
	// �ڵ鷯 ����
	// doGet - ������ �̵���
	// ** ȸ������ ������ �̵�
	@RequestMapping(value="/member/register.do", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
	// showRegisterForm -> �޼ҵ���� ���� ������ �ϴ��� �˱� ���� ���̹�
		return "member/register";	// request.getRequestDispacher
	}
	
	// doPost - ������ �����
	// ** ȸ������
	@RequestMapping(value="/member/register.do", method=RequestMethod.POST)
	public String registerMember(
			HttpServletRequest request
			, HttpServletResponse response
//			@RequestParam("member") String memberId -> request.getParameter �ѰͰ� �����ϴ�
//			@RequestParam("memberId")�� ������ �����ϴ�.
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
		// Exception ���� �ѷ��ֱ� ���ؼ� ����� ��!
		try {
			int result = service.registerMember(member);
			if(result > 0) {
				// ����
//				response.sendRedirect("/index.jsp");
				return "redirect:/index.jsp";
			} else {
				// ����
				model.addAttribute("msg", "ȸ�������� �Ϸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch(Exception e) {
			e.printStackTrace();	// �ܼ�â�� ���������� �߰���
			model.addAttribute("msg", e.getMessage()); // �ܼ�â�� �ߴ� �޼����� �� �������� �߰���
			return "common/errorPage";
		}
		
//		JDBC, Mybatis ���� ����ߴ� Servlet ��ü�� Spring ������ ����� �� �ִ�!
//		String memberId = request.getParameter("memberId");
		
//		Spring������ ���̻� request, response�� ������� ����
//		request.setCharacterEncoding("UTF-8");
//		request.setAttribute("", "");
//		request.getRequestDispatcher("/WEB-INF/views/member/register.jsp");
//		request.sendRedirect("/index.jsp");
	}
	
	// ** �α���
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
				// �����ϸ� �α��� �������� �̵�
//				response.sendRedirect("/index.jsp");
//				model.addAttribute("member", mOne);
				// �α��� ���� ����
//				HttpSession session = request.getSession();
//				session.setAttribute("memberId", mOne.getMemberId());
//				session.setAttribute("memberName", mOne.getMemberName());
//				model.addAttribute("memberId", mOne.getMemberId());
				
				// @SessionAttributes�� model�� �ִ� Key���� Session�� �����
				// ==========================================================
				model.addAttribute("memberName", mOne.getMemberName());
				model.addAttribute("memberId", mOne.getMemberId());
				// Session�� ��� ���� model�� Key���� �߰����ִ� �ڵ�
				// index.jsp���� ${}�� ���� ���� �Ͱ� �������!!
				// ==========================================================
				
				return "redirect:/index.jsp";
			} else {
				// �����ϸ� ���и޼��� ���
				model.addAttribute("msg", "�����Ͱ� ��ȸ���� �ʾҽ��ϴ�.");
				return "common/errorPage";
			}
		} catch (Exception e) {
			// ���� �߻� �� �޼��� ���
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
		
	}
	
	// ** �α׾ƿ�
	@RequestMapping(value="/member/logout.do", method=RequestMethod.GET)
	public String memberLogout(
			HttpSession sessionPrev
			// SessionStatus�� �������� ������̼�(@SessionAttributes)�� �����Ǵ� ������ �����Ų��.
			// ���� �޼ҵ�� setComplete)();
			, SessionStatus session
			, Model model) {
		if(session != null) {
//			session.invalidate();
			session.setComplete();
			if(session.isComplete()) {
				// ���Ǹ��� ��ȿ�� üũ
			}
			return "redirect:/index.jsp";
		} else {
			model.addAttribute("msg", "�α׾ƿ��� �Ϸ����� ���߽��ϴ�.");
			return "common/errorPage";
		}
	}
	
	// ����������
	@RequestMapping(value="/member/myPage.do", method=RequestMethod.GET)
	public String showDetailMember(
			// SELECT * FROM MEMBER_TBL MEMBER_ID = ?
			// �α����� ���̵�� ������ ��ȸ�ؾ� �ϱ� ������ @RequestParam ���� memberId�� ������
			@RequestParam("memberId") String memberId
			, Model model) {
		try {
			Member member = service.showOneById(memberId);
			if(member != null) {
				// ����
				model.addAttribute("member", member);
				return "member/myPage";
			} else {
				// ����
				model.addAttribute("msg", "������ ��ȸ�� �����߽��ϴ�.");
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
				// ����
				return "redirect:/member/logout.do";
			} else {
				// ����
				model.addAttribute("msg", "ȸ�� Ż�� �Ϸ���� �ʾҽ��ϴ�..");
				return "common/errorPage";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "common/errorPage";
		}
	}
	
	
	// doGet
	// ���� �������� �̵�
	@RequestMapping(value="/member/update.do", method=RequestMethod.GET)
	public String modifyViewMember(
			@RequestParam("memberId") String memberId
			,Model model) {
		try {
			Member member = service.showOneById(memberId);
			if(member != null ) {
				// ����
				model.addAttribute("member", member);
				return "member/modify";
			} else {
				// ����
				return "common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/errorPage";
		}
	}
	
	// doPost
	// �����ϱ�
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
				// ���� -> ����������
				return "member/myPage";
			} else {
				// ���� -> ���������� �״��
				model.addAttribute("msg", "������ ������ �����߽��ϴ�.");
				return "/common/errorPage";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", e.getMessage());
			return "/common/errorPage";
		}
	}
	
	
	
	
	
	
	
	
	
}