package com.telemedicicne.telemedicicne.Service;


import com.telemedicicne.telemedicicne.Entity.Patient.Patient;
import com.telemedicicne.telemedicicne.Entity.Patient.PatientRepository;
import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Repository.RefreshTokenRepository;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

//    public long refreshTokenValidity = 5*60*60*1000;

    public long refreshTokenValidity = 14 * 24 * 60 * 60 * 1000; // 7 days in milliseconds


//    public long refreshTokenValidity = 2*60*1000;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository ;

//    public RefreshToken createRefreshToken(String userName) {
//
//        User user = userRepository.findByEmail(userName).get();
//        RefreshToken refreshToken1 = user.getRefreshToken();
//
//        if (refreshToken1==null){
//            refreshToken1 =  RefreshToken.builder()
//                    .refreshToken(UUID.randomUUID().toString())
//                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
//                    .user(userRepository.findByEmail(userName).get())
//                    .build();
//
//        }else {
//            refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
//        }
//
//        user.setRefreshToken(refreshToken1);
//
//
//       refreshTokenRepository.save(refreshToken1);
//
//        return refreshToken1;
//    }
//
//    public RefreshToken verifyRefreshToken(String refreshToken){
//
//        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Given Token Does Not Expsist"));
//
//        if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(refreshTokenOb);
//            throw new RuntimeException("Refresh Token Expired !!");
//
//        }
//        return refreshTokenOb;
//
//    }


    @Autowired
    private DocHSRepository docHsRepository;

//    public RefreshToken createRefreshToken(String userName) {
//        // Check in UserRepository
//        User user = userRepository.findByEmail(userName).orElse(null);
//
//        // Check in DocHsRepository if user not found in UserRepository
//        if (user == null) {
//            DocHs docHs = docHsRepository.findByEmail(userName).orElseThrow(() -> new RuntimeException("User not found"));
//            return createOrUpdateRefreshTokenForDocHs(docHs);
//        }
//
//        return createOrUpdateRefreshTokenForUser(user);
//    }
//
//    private RefreshToken createOrUpdateRefreshTokenForUser(User user) {
//        RefreshToken refreshToken = user.getRefreshToken();
//
//        if (refreshToken == null) {
//            refreshToken = RefreshToken.builder()
//                    .refreshToken(UUID.randomUUID().toString())
//                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
//                    .user(user)
//                    .build();
//        } else {
//            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
//        }
//
//        user.setRefreshToken(refreshToken);
//        refreshTokenRepository.save(refreshToken);
//        return refreshToken;
//    }
//
//    private RefreshToken createOrUpdateRefreshTokenForDocHs(DocHs docHs) {
//        RefreshToken refreshToken = docHs.getRefreshToken();
//
//        if (refreshToken == null) {
//            refreshToken = RefreshToken.builder()
//                    .refreshToken(UUID.randomUUID().toString())
//                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
//                    .docHs(docHs)
//                    .build();
//        } else {
//            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
//        }
//
//        docHs.setRefreshToken(refreshToken);
//        refreshTokenRepository.save(refreshToken);
//        return refreshToken;
//    }
//
//    public RefreshToken verifyRefreshToken(String refreshToken) {
//        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new RuntimeException("Given Token Does Not Exist"));
//
//        if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(refreshTokenOb);
//            throw new RuntimeException("Refresh Token Expired!!");
//        }
//        return refreshTokenOb;
//    }


    @Autowired
    private PatientRepository patientRepository;

    public RefreshToken createRefreshToken(String userName) {
        // Check in UserRepository
        User user = userRepository.findByEmail(userName).orElse(null);

        // Check in DocHsRepository if user not found in UserRepository
        if (user == null) {
            DocHs docHs = docHsRepository.findByEmail(userName).orElse(null);
            if (docHs == null) {
                // Check in PatientRepository if user not found in DocHsRepository
                Patient patient = patientRepository.findByMobileNo(userName)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return createOrUpdateRefreshTokenForPatient(patient);
            }
            return createOrUpdateRefreshTokenForDocHs(docHs);
        }

        return createOrUpdateRefreshTokenForUser(user);
    }

    private RefreshToken createOrUpdateRefreshTokenForUser(User user) {
        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        user.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private RefreshToken createOrUpdateRefreshTokenForDocHs(DocHs docHs) {
        RefreshToken refreshToken = docHs.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .docHs(docHs)
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        docHs.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private RefreshToken createOrUpdateRefreshTokenForPatient(Patient patient) {
        RefreshToken refreshToken = patient.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .patient(patient)
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        patient.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Given Token Does Not Exist"));

        if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshTokenOb);
            throw new RuntimeException("Refresh Token Expired!!");
        }
        return refreshTokenOb;
    }

}
