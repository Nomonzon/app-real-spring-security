package com.example.apparticle.service;

import com.example.apparticle.entity.User;
import com.example.apparticle.entity.enums.RoleName;
import com.example.apparticle.payload.ApiResponse;
import com.example.apparticle.payload.LoginDto;
import com.example.apparticle.payload.RegisterDto;
import com.example.apparticle.repository.RoleRepository;
import com.example.apparticle.repository.UserRepository;
import com.example.apparticle.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto){

        if (userRepository.existsByEmail(registerDto.getEmail())){
            return new ApiResponse("Email already exist.", false);
        }
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail((registerDto.getEmail()));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Success you have registered. Please activate your account verify your email", true);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("NomonzonRh@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("AkkountTasdiqlash");
            mailMessage.setText("http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email="+sendingEmail+"");
            javaMailSender.send(mailMessage);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> userOptional = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (userOptional.isPresent()){
           User user = userOptional.get();
           user.setEnabled(true);
           user.setEmailCode(null);
           userRepository.save(user);
           return new ApiResponse("Email success enabled", true);
        }
        return new ApiResponse("Email already enabled. Error",false);
    }


    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token", true, token);
        }catch (BadCredentialsException badCredentialsException){
                return new ApiResponse("Error", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "  topilmadi"));

    }
}
