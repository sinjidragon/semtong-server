package com.example.shemtong.user.service;

import com.example.shemtong.user.dto.EmailRequest;
import com.example.shemtong.user.dto.ErrorResponse;
import com.example.shemtong.user.dto.SuccessResponse;
import com.example.shemtong.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
public class MailService {

    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;
    private static final String senderEmail= "shemtong11@gmail.com";

    public MailService(JavaMailSender javaMailSender, UserRepository userRepository, StringRedisTemplate redisTemplate, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.templateEngine = templateEngine;
    }

    public String generateVerificationCode() { // 인증코드 생성
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public ResponseEntity<?> sendMail(String email) throws MessagingException { // 이메일 전송

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("send email failed","이미 사용중인 이메일입니다"));
        }

        String verificationCode = generateVerificationCode();

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, verificationCode, 5, TimeUnit.MINUTES); // 5분 후 만료

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("shemtong 인증 코드");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        String htmlContent = templateEngine.process("mail.html", context);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        return ResponseEntity.ok(new SuccessResponse("send email successful"));
    }

    private String generateEmailContent(String verificationCode) { // 이메일 내용 생성
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);  // 템플릿에 변수 전달

        return templateEngine.process("mail", context);  // 'mail'은 templates/mail.html 파일을 참조
    }

    public ResponseEntity<?> verify(EmailRequest emailRequest) { // 이메일 인증
        String savedCode = getVerificationCode(emailRequest.getMail());

        if (savedCode == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("verify email failed","인증 코드가 만료되었거나 존재하지 않습니다."));
        }

        if (!savedCode.equals(emailRequest.getVerifyCode())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("verify email failed","인증 코드가 일치하지 않습니다."));
        }

        redisTemplate.delete(emailRequest.getMail());

        return ResponseEntity.ok(new SuccessResponse("verify email successful"));
    }

    public String getVerificationCode(String email) { //
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(email);
    }

}
