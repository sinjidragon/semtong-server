package com.example.shemtong.domain.auth.service;

import com.example.shemtong.domain.auth.dto.EmailRequest;
import com.example.shemtong.domain.auth.exception.AuthErrorCode;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailService {
    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;

    public String generateVerificationCode() { // 인증코드 생성
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public ResponseEntity<SuccessResponse> sendMail(String email) throws MessagingException { // 이메일 전송

        if (userRepository.findByEmail(email).isPresent())
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXIST);

        String verificationCode = generateVerificationCode();

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, verificationCode, 5, TimeUnit.MINUTES); // 5분 후 만료

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("semtong 인증 코드");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        String htmlContent = templateEngine.process("mail.html", context);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        return ResponseEntity.ok(new SuccessResponse("send email successful"));
    }

    public ResponseEntity<SuccessResponse> verify(EmailRequest emailRequest) { // 이메일 인증
        String savedCode = getVerificationCode(emailRequest.getMail());

        if (savedCode == null) {
            throw new CustomException(AuthErrorCode.CODE_NOT_FOUND);
        }

        if (!savedCode.equals(emailRequest.getVerifyCode())) {
            throw new CustomException(AuthErrorCode.CODE_NOT_MATCH);
        }

        redisTemplate.delete(emailRequest.getMail());

        return ResponseEntity.ok(new SuccessResponse("verify email successful"));
    }

    public String getVerificationCode(String email) { //
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(email);
    }

}
