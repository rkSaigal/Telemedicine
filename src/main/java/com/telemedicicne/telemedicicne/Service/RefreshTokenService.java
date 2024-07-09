package com.telemedicicne.telemedicicne.Service;


import com.telemedicicne.telemedicicne.Entity.Doctor;
import com.telemedicicne.telemedicicne.Entity.Patient;

import com.telemedicicne.telemedicicne.Repository.PatientRepository;
import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
import com.telemedicicne.telemedicicne.Repository.DoctorRepository;
import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.Hospital;
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




//    public RefreshToken createRefreshToken(String userName) {
//        // Check in UserRepository
//        User user = userRepository.findByEmail(userName).orElse(null);
//
//        // Check in DocHsRepository if user not found in UserRepository
//        if (user == null) {
//            HealthOfficer docHs = docHsRepository.findByEmail(userName).orElseThrow(() -> new RuntimeException("User not found"));
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
//    private RefreshToken createOrUpdateRefreshTokenForDocHs(HealthOfficer docHs) {
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

    @Autowired
    private HealthOfficerRepository healthOfficerRepository;
    @Autowired
    private DoctorRepository doctorRepository;

//    public RefreshToken createRefreshToken(String userName) {
//        // Check in UserRepository
//        Hospital user = userRepository.findByEmail(userName).orElse(null);
//
//        // Check in DocHsRepository if user not found in UserRepository
//        if (user == null) {
//            HealthOfficer docHs = docHsRepository.findByEmail(userName).orElse(null);
//            if (docHs == null) {
//                // Check in PatientRepository if user not found in DocHsRepository
//                Patient patient = patientRepository.findByMobileNo(userName)
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                return createOrUpdateRefreshTokenForPatient(patient);
//            }
//            return createOrUpdateRefreshTokenForHealthOfficer(docHs);
//        }
//
//        return createOrUpdateRefreshTokenForUser(user);
//    }
public RefreshToken createRefreshToken(String userName) {
    // Check in UserRepository
    Hospital user = userRepository.findByEmail(userName).orElse(null);

    if (user != null) {
        return createOrUpdateRefreshTokenForUser(user);
    }

    // Check in HealthOfficerRepository if user not found in UserRepository
    HealthOfficer docHs = healthOfficerRepository.findByEmail(userName).orElse(null);
    if (docHs != null) {
        return createOrUpdateRefreshTokenForHealthOfficer(docHs);
    }

    // Check in DoctorRepository if user not found in HealthOfficerRepository
    Doctor doctor = doctorRepository.findByEmail(userName).orElse(null);
    if (doctor != null) {
        return createOrUpdateRefreshTokenForDoctor(doctor);
    }

    // Check in PatientRepository if user not found in DoctorRepository
    Patient patient = patientRepository.findByMobileNo(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return createOrUpdateRefreshTokenForPatient(patient);
}

    private RefreshToken createOrUpdateRefreshTokenForUser(Hospital user) {
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

    private RefreshToken createOrUpdateRefreshTokenForHealthOfficer(HealthOfficer healthOfficer) {
        RefreshToken refreshToken = healthOfficer.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .healthOfficer(healthOfficer)
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        healthOfficer.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private RefreshToken createOrUpdateRefreshTokenForDoctor(Doctor doctor) {
        RefreshToken refreshToken = doctor.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .doctor(doctor)
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        doctor.setRefreshToken(refreshToken);
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
