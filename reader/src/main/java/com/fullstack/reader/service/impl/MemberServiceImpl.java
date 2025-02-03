package com.fullstack.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fullstack.reader.entity.Member;
import com.fullstack.reader.entity.MemberReadState;
import com.fullstack.reader.mapper.MemberMapper;
import com.fullstack.reader.mapper.MemberReadStateMapper;
import com.fullstack.reader.service.MemberService;
import com.fullstack.reader.service.exception.MemberException;
import com.fullstack.reader.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberReadStateMapper memberReadStateMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        List<Member> members = memberMapper.selectList(wrapper);
        if (members.size() > 0) {
            throw new MemberException("用户名已存在");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        member.setCreateTime(LocalDateTime.now());
        int salt = (int) (Math.random() * 1000);
        member.setSalt(salt);
        String md5 = Md5Utils.md5(password, salt);
        member.setPassword(md5);
        memberMapper.insert(member);
        return member;
    }

    @Override
    public Member checkLogin(String username, String password) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Member member = memberMapper.selectOne(wrapper);
        if (member == null) {
            throw new MemberException("用户名不存在");
        }
        String md5 = Md5Utils.md5(password, member.getSalt());
        if (!md5.equals(member.getPassword())) {
            throw new MemberException("密码错误");
        }
        return member;
    }

    @Override
    public Member selectById(Long memberId) {
        return memberMapper.selectById(memberId);
    }

    @Override
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("book_id", bookId);
        return memberReadStateMapper.selectOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState) {
        QueryWrapper<MemberReadState> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("book_id", bookId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(wrapper);
        if (memberReadState == null) {
            memberReadState = new MemberReadState();
            memberReadState.setMemberId(memberId);
            memberReadState.setBookId(bookId);
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(LocalDateTime.now());
            memberReadStateMapper.insert(memberReadState);
        } else {
            memberReadState.setReadState(readState);
            memberReadStateMapper.updateById(memberReadState);
        }
        return memberReadState;
    }
}
