package com.example.learndemo.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.learndemo.common.LibraryException;
import com.example.learndemo.domain.User;
import com.example.learndemo.domain.Members;
import com.example.learndemo.dto.*;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.mapper.MembersMapper;
import com.example.learndemo.service.UserService;
import com.example.learndemo.util.JwtUtil;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

import com.example.learndemo.service.FinService;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private MembersMapper membersMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private FinService finService;

    @Override
    public void register(RegisterDto registerDto) {
         User originuser = userMapper.findOneByUsername(registerDto.getUsername());
         if (originuser != null) {
              throw new LibraryException(400,"用户名已存在");
         }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setPhone(registerDto.getPhone());
        user.setNickName(registerDto.getNickName());
        userMapper.insert(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
            
        LoginResponseDto loginResponseDto = new LoginResponseDto();
            String token = jwtUtil.generateToken(loginRequestDto.getUsername());
            loginResponseDto.setToken(token);
            
            return loginResponseDto;
        } catch (Exception e) {
            throw new LibraryException(401, "用户名或密码错误");
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        // 验证管理员密码
        if (!"1234".equals(resetPasswordDto.getAuthorize())) {
            throw new LibraryException(400, "管理员验证码错误");
        }

        // 查找用户
        User user = userMapper.findOneByUsername(resetPasswordDto.getUsername());
        if (user == null) {
            throw new LibraryException(400, "用户不存在");
        }

        // 验证手机号码
        if (user.getPhone() == null || !user.getPhone().equals(resetPasswordDto.getPhone())) {
            throw new LibraryException(400, "手机号码与注册时不一致");
        }

        // 更新密码和更新时间
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public UserInfoDto getUserByUsername(String username) {
        User user = userMapper.findOneByUsername(username);
        if(user==null){
        return null;
        }
        return toUserInfoDto(user);
    }

    @Override
    public IPage<UserInfoDto> searchUser(Integer pageNum, Integer pageSize, String name, String phone) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
        wrappers.orderByDesc(User::getUpdatedAt);
        if (StringUtils.hasText(name)) {
            wrappers.like(User::getNickName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrappers.like(User::getPhone, phone);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);
        if (userPage == null) {
            return null;
        }
        return userPage.convert(this::toUserInfoDto);
    }

    @Override
    public void updateUser(Long id, UserInfoDto userInfoDto) {
        User user = toUser(id, userInfoDto);
        userMapper.updateById(user);
    }

    private User toUser(Long id, UserInfoDto userInfoDto) {
        User user = new User();
        user.setId(id);
        user.setUsername(userInfoDto.getUserName());
        user.setNickName(userInfoDto.getNickName());
        user.setPhone(userInfoDto.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        userMapper.deleteBatchIds(ids);
    }

    private UserInfoDto toUserInfoDto(User user) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setNickName(user.getNickName());
        userInfoDto.setId(user.getId());
        userInfoDto.setUserName(user.getUsername());
        userInfoDto.setRole(user.getRole());
        userInfoDto.setPhone(user.getPhone());
        return userInfoDto;
    }

    @Override
    public List<Members> getAllMembers() {
        return membersMapper.selectList(null);
    }

    @Override
    public void updateMember(Members member) {
        membersMapper.updateById(member);
    }

    @Override
    public void rechargeMember(Integer memberId, Double amount) {
        Members member = membersMapper.selectById(memberId);
        if (member != null) {
            // 充值逻辑，假设押金字段为 deposit
            java.math.BigDecimal deposit = member.getDeposit() == null ? java.math.BigDecimal.ZERO : member.getDeposit();
            member.setDeposit(deposit.add(java.math.BigDecimal.valueOf(amount)));
            membersMapper.updateById(member);
        }
    }

    @Override
    public void freezeMember(Integer memberId) {
        Members member = membersMapper.selectById(memberId);
        if (member != null) {
            // 删除 member.setStatus("冻结")
        }
    }

    @Override
    @Transactional
    public void registerMember(RegisterMemberDto dto) {
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.findOneByUsername(username);
        if (user == null) {
            throw new LibraryException(400, "用户不存在");
        }

        // 检查是否已经是会员
        LambdaQueryWrapper<Members> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Members::getUserId, user.getId());
        Members existingMember = membersMapper.selectOne(wrapper);
        if (existingMember != null) {
            throw new LibraryException(400, "该用户已经是会员");
        }

        // 计算所需预存款
        int duration = dto.getDuration();
        double need = duration * 10.0;
        double balance = finService.getUserPrepaymentBalance(username);
        if (balance < need) {
            throw new LibraryException(400, "预存款不足，请先充值");
        }
        // 扣除预存款（插入负的prepayment记录）
        finService.prepaymentOperation(username, "register_member", -need, "注册会员扣费");

        // 创建新会员
        Members member = new Members();
        member.setUserId(user.getId().intValue());
        member.setMaxBooks(5); // 默认可借5本
        member.setFreeDays(30); // 默认30天免费
        member.setDeposit(new java.math.BigDecimal("100")); // 默认押金100元
        member.setRentPay(java.math.BigDecimal.ZERO);
        member.setOverdueFine(java.math.BigDecimal.ZERO);
        membersMapper.insert(member);
    }

    @Override
    public long countAllUsers() {
        return userMapper.selectCount(null);
    }
}
