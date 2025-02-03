package com.fullstack.reader.controller;

import com.fullstack.reader.entity.Member;
import com.fullstack.reader.service.EvaluationService;
import com.fullstack.reader.service.MemberService;
import com.fullstack.reader.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/register")
    public ResponseUtils register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("nickname") String nickname, @RequestParam("vc") String vc, HttpServletRequest request) {
        String verifyCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
        if (verifyCode == null || vc == null || !vc.equalsIgnoreCase(verifyCode)) {
            return new ResponseUtils("VerifyCodeError", "验证码错误");
        }
        try {
            memberService.createMember(username, password, nickname);
            return new ResponseUtils();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @PostMapping("/check_login")
    public ResponseUtils login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("vc") String vc, HttpServletRequest request) {
        String verifyCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
        if (verifyCode == null || vc == null || !vc.equalsIgnoreCase(verifyCode)) {
            return new ResponseUtils("VerifyCodeError", "验证码错误");
        }
        try {
            Member member = memberService.checkLogin(username, password);
            member.setPassword(null);
            member.setSalt(null);
            return new ResponseUtils().put("member", member);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @GetMapping("/select_by_id")
    public ResponseUtils selectById(@RequestParam("memberId") Long memberId) {
        try {
            Member member = memberService.selectById(memberId);
            member.setPassword(null);
            member.setSalt(null);
            return new ResponseUtils().put("member", member);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @GetMapping("/select_read_state")
    public ResponseUtils selectMemberReadState(@RequestParam("memberId") Long memberId, @RequestParam("bookId") Long bookId) {
        try {
            return new ResponseUtils().put("readState", memberService.selectMemberReadState(memberId, bookId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @PostMapping("/update_read_state")
    public ResponseUtils updateMemberReadState(@RequestParam("memberId") Long memberId, @RequestParam("bookId") Long bookId, @RequestParam("readState") Integer readState) {
        try {
            return new ResponseUtils().put("readState", memberService.updateMemberReadState(memberId, bookId, readState));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @PostMapping("/evaluate")
    public ResponseUtils evaluate(@RequestParam("memberId") Long memberId, @RequestParam("bookId") Long bookId, @RequestParam("score") Integer score, @RequestParam("content") String content) {
        try {
            return new ResponseUtils().put("evaluation", evaluationService.evaluate(memberId, bookId, score, content));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @PostMapping("/enjoy")
    public ResponseUtils enjoy(@RequestParam("evaluationId") Long evaluationId) {
        try {
            return new ResponseUtils().put("evaluation", evaluationService.enjoy(evaluationId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
