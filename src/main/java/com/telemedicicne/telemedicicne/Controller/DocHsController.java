package com.telemedicicne.telemedicicne.Controller;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.telemedicicne.telemedicicne.Dtos.DoctorDetailsDTO;
import com.telemedicicne.telemedicicne.Dtos.*;
import com.telemedicicne.telemedicicne.Entity.*;

import com.telemedicicne.telemedicicne.Exception.ResourceNotFoundException;
import com.telemedicicne.telemedicicne.Exception.UnauthorizedAccessException;
import com.telemedicicne.telemedicicne.Exception.UnauthorizedException;
import com.telemedicicne.telemedicicne.Repository.*;
import com.telemedicicne.telemedicicne.Service.DoctorHospitalService;

import com.telemedicicne.telemedicicne.Model.JwtRequest;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.PatientService;
import com.telemedicicne.telemedicicne.Service.RefreshTokenService;
import com.telemedicicne.telemedicicne.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3001")
@RequestMapping("/registerDocHs")
public class DocHsController {

    @Autowired
    private final DoctorHospitalService docHSService;
    @Autowired
    private PatientHealthMetricsRepository patientHealthMetricsRepository;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private HealthOfficerRepository docHSRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private PatientService patientService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

//    @PostMapping("/register")
//    public ResponseEntity<String> registerDocHS(@RequestHeader("Auth") String jwtToken, @RequestBody HealthOfficer docHS) {
//        // Extract username from JWT token
//        String token = jwtToken.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch user details if needed
//        Hospital user = userService.findByUsername(username);
//
//        // Check if the user's role is SUB_ADMIN
//        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("SUB_ADMIN"))) {
//            // Determine the user type based on the type field
//            String userType = docHS.getType().equals("Doctor") ? "Doctor" : "Health Officer";
//
//            // Register doctor or health officer based on type
//            docHSService.registerDocHS(docHS, userType,user);
//
//            // Set the relationship
//            docHS.setHospital(user);
////            docHSService.saveDocHS(docHS); // Assuming a method to save HealthOfficer in service
//
//            return ResponseEntity.ok(userType + " registered successfully!");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to register");
//        }
//    }
@PostMapping("/register")
public ResponseEntity<String> registerUser(@RequestHeader("Auth") String jwtToken, @RequestBody Map<String, Object> userMap) {
    String token = jwtToken.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);

    Hospital user = userService.findByUsername(username);

    if (user.getRoles().stream().anyMatch(role -> role.getName().equals("SUB_ADMIN"))) {
        String userType = (String) userMap.get("type");
        if (userType.equals("Doctor")) {
            Doctor doctor = new ObjectMapper().convertValue(userMap, Doctor.class);
            docHSService.registerUser(doctor, userType, user);
        } else if (userType.equals("Health Officer")) {
            HealthOfficer healthOfficer = new ObjectMapper().convertValue(userMap, HealthOfficer.class);
            docHSService.registerUser(healthOfficer, userType, user);
        } else {
            return ResponseEntity.badRequest().body("Invalid user type");
        }

        return ResponseEntity.ok(userType + " registered successfully!");
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to register");
    }
}


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Authenticate user
        this.doAuthenticate(request.getEmail(), request.getPassword());


        // If not found as a regular User, try to load as HealthOfficer
        Optional<HealthOfficer> docHs = docHSRepository.findByEmail(request.getEmail());
        if (docHs.isPresent()) {
            HealthOfficer docHs1 = docHs.get();
            String token = jwtHelper.generateToken(docHs1);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(docHs1.getEmail());

            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(docHs1.getDocHsId().toString())
                    .username(docHs1.getEmail())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Handle case where user is not found in either table
        throw new IllegalArgumentException("User not found");
    }

private void doAuthenticate(String email, String password) {

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
    try {
        manager.authenticate(authentication);
    }catch (BadCredentialsException e){
        throw new BadCredentialsException("Invalid Username or Password !!");
    }
}


    @GetMapping(value = "/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DoctorDetailsDTO>> getDoctorsForHealthOfficer(@RequestHeader("Auth") String jwtToken) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        HealthOfficer docHs = docHSService.findByUsername(username);

        // Check if the user has the HEALTH_OFFICER role
        if (docHs != null && docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            // Fetch all doctors with details
            List<Doctor> doctors = docHSService.findAllDoctors();

            // Map to DoctorDetailsDTO for selective information
            List<DoctorDetailsDTO> doctorDetails = doctors.stream()
                    .map(this::mapToDoctorDetailsDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(doctorDetails);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    private DoctorDetailsDTO mapToDoctorDetailsDTO(Doctor doctor) {
        return new DoctorDetailsDTO(
                doctor.getDoctorId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getMobileNo(),
                doctor.getSpecialist(),
                doctor.getType(),
                doctor.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );
    }


@PostMapping(value = "/saveVideoToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<String> saveVideoToken(@RequestBody VideoTokenRequestDTO videoTokenRequestDTO) {
    // Get the authenticated user's details
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    HealthOfficer docHs = docHSService.findByUsername(username);

    if (docHs != null) {
        // Check if the provided refId matches any existing userId
        HealthOfficer matchingUser = docHSService.findByRefId(videoTokenRequestDTO.getRefId());

        System.out.println("sdsdsd---------"+matchingUser.getDocHsId());

//        if (matchingUser != null && matchingUser.getDocHsId().equals(docHs.getDocHsId())) {
            // Set the videoToken and refId
        matchingUser.setVideoToken(videoTokenRequestDTO.getVideoToken());
//        matchingUser.setRefId(videoTokenRequestDTO.getRefId());
        matchingUser.setRefId(docHs.getDocHsId());

            // Save the updated entity
            docHSService.save(matchingUser);

            return ResponseEntity.ok("Video token saved successfully");
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Provided refId does not match your userId");
//        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or not authorized");
    }
}


    @GetMapping(value = "/getVideoToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoTokenRequestDTO> getVideoTokenAndRefId() {
        // Get the authenticated user's details
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        HealthOfficer docHs = docHSService.findByUsername(username);

        if (docHs != null) {
            // Assuming you want to return the videoToken and refId for the authenticated user
            String videoToken = docHs.getVideoToken();
            Long refId = docHs.getRefId();

            // Create a response DTO to hold the videoToken and refId
            VideoTokenRequestDTO responseDTO = new VideoTokenRequestDTO();
            responseDTO.setVideoToken(videoToken);
            responseDTO.setRefId(refId);

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




@GetMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<PatientDetailsDTO> getPatientDetails(@RequestHeader("Auth") String jwtToken, @RequestParam String addharNo) {
    // Extract username from JWT token
    String token = jwtToken.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);

    // Fetch user details if needed
    HealthOfficer docHs = docHSService.findByUsername(username);

    // Check if the user has the HEALTH_OFFICER role
    if (docHs != null && docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
        // Fetch patient details by addharNo
        Optional<Patient> patientOptional = patientService.findByAddharNo(addharNo);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            // Map to PatientDetailsDTO
            PatientDetailsDTO patientDetailsDTO = new PatientDetailsDTO(
                    patient.getPatientId(),
                    patient.getPatientName(),
                    patient.getHeight(),
                    patient.getWeight(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getEmail(),
                    patient.getMobileNo()

            );
            return ResponseEntity.ok(patientDetailsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}


        @PostMapping("/save-patient-detail")
    public ResponseEntity<Void> submitHealthMetrics(
            @RequestHeader("Auth") String jwtToken,
            @RequestParam Long patientId,
            @ModelAttribute PatientHealthMetricsDTO healthMetricsDTO) throws IOException { // Use @ModelAttribute to handle multipart files

        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        HealthOfficer docHs = docHSService.findByUsername(username);

        if (docHs != null && docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            patientService.saveHealthMetrics(patientId, healthMetricsDTO, docHs);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/upload-documents")
    public ResponseEntity<String> uploadDocuments(
            @RequestParam Long patientHealthMetricsId,
            @RequestParam("files") MultipartFile[] files,
            @RequestHeader("Auth") String jwtToken
    ) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer details including roles
        HealthOfficer docHs = docHSService.findByUsername(username);

        // Check if the user has the HEALTH_OFFICER role
        if (docHs == null || !docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to upload documents.");
        }

        try {
            patientService.uploadDocuments(patientHealthMetricsId, files);
            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload files: " + e.getMessage());
        }
    }



    @GetMapping(value = "/patient-documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDTO>> getPatientDocuments(
            @RequestParam Long patientHealthMetricsId,
            @RequestHeader("Auth") String jwtToken
    ) {
        try {
            List<DocumentDTO> documents = patientService.getDocumentsByPatient(patientHealthMetricsId, jwtToken);
            return ResponseEntity.ok(documents);
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    @GetMapping(value = "/get-patient-vitals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientHealthMetricsDTO> getHealthMetricsByPatientId(
            @RequestParam Long patientId,
            @RequestHeader("Auth") String jwtToken
    ) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details including roles
        HealthOfficer healthOfficer = docHSService.findByUsername(username);
        Doctor doctor = docHSService.findByUsernameDoctor(username);

        // Check if the user has either HEALTH_OFFICER or DOCTOR role
        if ((healthOfficer != null && healthOfficer.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) ||
                (doctor != null && doctor.getRoles().stream().anyMatch(role -> role.getName().equals("DOCTOR")))) {

            // User has either HEALTH_OFFICER or DOCTOR role, proceed to fetch patient vitals
            PatientHealthMetricsDTO healthMetricsDTO = patientService.getHealthMetricsByPatientId(patientId);
            return ResponseEntity.ok(healthMetricsDTO);
        } else {
            // User does not have required role, return unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




    @GetMapping(value = "/get-all-patients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientDetailsDTO>> getAllPatientsForHealthOfficer(
            @RequestHeader("Auth") String jwtToken
    ) {
        LocalDate localDate = LocalDate.now();

        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        HealthOfficer docHs = docHSService.findByUsername(username);
        Doctor doctor = docHSService.findByUsernameDoctor(username);

        // Check if the user has the HEALTH_OFFICER or DOCTOR role
        boolean hasHealthOfficerRole = docHs != null && docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"));
        boolean hasDoctorRole = doctor != null && doctor.getRoles().stream().anyMatch(role -> role.getName().equals("DOCTOR"));

        if (hasHealthOfficerRole || hasDoctorRole) {
            Set<Patient> patients;

            if (hasHealthOfficerRole) {
                // Fetch patients associated with this HEALTH_OFFICER
                patients = docHs.getPatients();
            } else {
                // Fetch patients associated with this DOCTOR
                patients = doctor.getPatients();
            }

            // Filter patients registered on the current date
            List<Patient> filteredPatients = patients.stream()
                    .filter(patient -> patient.getLocalDate().equals(localDate))
                    .collect(Collectors.toList());

            // Map patients to DTOs
            List<PatientDetailsDTO> patientDTOs = filteredPatients.stream()
                    .map(patient -> new PatientDetailsDTO(
                            patient.getPatientId(),
                            patient.getPatientName(),
                            patient.getLocalDate(),
                            patient.getStatus(),
                            patient.getMobileNo(),
                            patient.getHealthOfficer().getName(),
                            (hasHealthOfficerRole ? docHs.getHospital().getUserName() : doctor.getHospital().getUserName())
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(patientDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PutMapping("/update-patient-health-metrics")
    public ResponseEntity<String> updatePatientHealthMetrics(
            @RequestParam Long patientId,
            @RequestBody PatientHealthMetricsDTO metricsUpdateDTO,
            @RequestHeader("Auth") String jwtToken
    ) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Check if the user has the HEALTH_OFFICER role
        HealthOfficer docHs = docHSService.findByUsername(username);
        if (docHs == null || !docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        // Update PatientHealthMetrics
        try {
            updatePatientHealthMetrics(patientId, metricsUpdateDTO);
            return ResponseEntity.ok("Patient health metrics updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating patient health metrics");
        }
    }


//    @Transactional

    public void updatePatientHealthMetrics(Long id, PatientHealthMetricsDTO metricsUpdateDTO) {
        PatientHealthMetrics metrics = patientHealthMetricsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient health metrics not found"));

        // Update fields from DTO
        metrics.setBloodGroup(metricsUpdateDTO.getBloodGroup());
        metrics.setSpO2(metricsUpdateDTO.getSpO2());
        metrics.setRespirationRate(metricsUpdateDTO.getRespirationRate());
        metrics.setHeartRate(metricsUpdateDTO.getHeartRate());
        metrics.setDiastolicBP(metricsUpdateDTO.getDiastolicBP());
        metrics.setSystolicBP(metricsUpdateDTO.getSystolicBP());
        metrics.setPulseRate(metricsUpdateDTO.getPulseRate());
        metrics.setTemperature(metricsUpdateDTO.getTemperature());
        metrics.setHemoglobin(metricsUpdateDTO.getHemoglobin());
        metrics.setBloodSugar(metricsUpdateDTO.getBloodSugar());

        patientHealthMetricsRepository.save(metrics);
    }



    @GetMapping(value = "/get-patient-details-personal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDetailsDTO> getPatientDetails(
            @RequestParam Long patientId,
            @RequestHeader("Auth") String jwtToken
    ) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer details including roles
        HealthOfficer docHs = docHSService.findByUsername(username);
        if (docHs != null && docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            // User has HEALTH_OFFICER role, proceed to fetch patient details
            PatientDetailsDTO patientDetailsDTO = patientService.getPatientDetails(patientId);
            return ResponseEntity.ok(patientDetailsDTO);
        } else {
            // User does not have required role, return unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    //prescription

    @PostMapping("/patient-prescription")
    public ResponseEntity<String> createPrescription(
            @RequestParam Long patientId,
            @RequestBody PrescriptionDTO prescriptionDTO,
            @RequestHeader("Auth") String jwtToken
    ) {
        try {
            String result = patientService.createPrescription(patientId, prescriptionDTO, jwtToken);
            return ResponseEntity.ok(result);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create prescription: " + e.getMessage());
        }
    }


    @GetMapping(value = "/get-medicine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(
            @RequestParam Long patientId,
            @RequestHeader("Auth") String jwtToken
    ) {
        try {
            // Extract username from JWT token
            String token = jwtToken.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            // Fetch HealthOfficer details including roles
            HealthOfficer docHs = docHSService.findByUsername(username);

            // Check if the user has the HEALTH_OFFICER role
            if (docHs == null || !docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Get prescription details including additional fields from patient health metrics
            PrescriptionDTO prescriptionDTO = patientService.getPrescriptionById(patientId);

//            // Populate additional fields from patient health metrics
//            PatientHealthMetrics patientHealthMetrics = findById(prescriptionId); // Example, adjust as per your service method
//            prescriptionDTO.setAllergy(patientHealthMetrics.getAllergy());
//            prescriptionDTO.setComorbidity(patientHealthMetrics.getComorbidity());
//            prescriptionDTO.setComplaints(patientHealthMetrics.getComplaints());
//            prescriptionDTO.setOtherIllnesses(patientHealthMetrics.getOtherIllnesses());

            // Fetch PatientHealthMetrics by ID
            PatientHealthMetrics patientHealthMetrics = findById(patientId);

            // Populate additional fields from patient health metrics to prescriptionDTO
            if (patientHealthMetrics != null) {
                prescriptionDTO.setAllergy(patientHealthMetrics.getAllergy());
                prescriptionDTO.setComorbidity(patientHealthMetrics.getComorbidity());
                prescriptionDTO.setComplaints(patientHealthMetrics.getComplaints());
                prescriptionDTO.setOtherIllnesses(Collections.singletonList(patientHealthMetrics.getOtherIllness()));
            }


            return ResponseEntity.ok(prescriptionDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }




    }

//patient case history


    @GetMapping(value = "/all-history", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<PatientCaseHistoryDto>> getAllPatients(@RequestHeader("Auth") String jwtToken) {

        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer details including roles
        HealthOfficer docHs = docHSService.findByUsername(username);

        List<PatientCaseHistoryDto> patients = getAllPatientsWithDetails();
        return ResponseEntity.ok(patients);
    }


    public List<PatientCaseHistoryDto> getAllPatientsWithDetails() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(patient -> {
            PatientCaseHistoryDto dto = new PatientCaseHistoryDto();
            dto.setPatientId(patient.getPatientId());
            dto.setPatientName(patient.getPatientName());
            dto.setStatus(patient.getStatus());
            dto.setLocalDate(patient.getLocalDate());

            if (patient.getDoctor() != null) {
                dto.setDoctorName(patient.getDoctor().getName());
            }

            // Assuming the latest health metric is the one of interest
            if (!patient.getHealthMetrics().isEmpty()) {
                dto.setComplaint(patient.getHealthMetrics().get(0).getComplaints());
            }

            List<PrescriptionDTO> prescriptionDTOs = patient.getPrescriptions().stream()
                    .map(prescription -> {
                        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
                        prescriptionDTO.setId(prescription.getId());
                        prescriptionDTO.setChiefComplaints(prescription.getChiefComplaints());
                        prescriptionDTO.setSymptoms(prescription.getSymptoms());
                        prescriptionDTO.setAdvice(prescription.getAdvice());
                        prescriptionDTO.setProvisionalDiagnosis(prescription.getProvisionalDiagnosis());
                        prescriptionDTO.setFollowUpDate(prescription.getFollowUpDate());

                        // Map medications
                        List<MedicationDTO> medicationDTOs = prescription.getMedications().stream()
                                .map(medication -> {
                                    MedicationDTO medicationDTO = new MedicationDTO();
                                    medicationDTO.setMedication(medication.getMedication());
                                    medicationDTO.setFrequency(medication.getFrequency());
                                    medicationDTO.setDosage(medication.getDosage());
                                    medicationDTO.setDays(medication.getDays());
                                    return medicationDTO;
                                }).collect(Collectors.toList());
                        prescriptionDTO.setMedications(medicationDTOs);

                        // Map laboratory tests
                        List<LaboratoryDTO> laboratoryDTOs = prescription.getLaboratories().stream()
                                .map(laboratory -> {
                                    LaboratoryDTO laboratoryDTO = new LaboratoryDTO();
                                    laboratoryDTO.setLaboratoryId(laboratory.getLaboratoryId());
                                    laboratoryDTO.setLaboratory(laboratory.getLaboratory());
                                    return laboratoryDTO;
                                }).collect(Collectors.toList());
                        prescriptionDTO.setLaboratory(laboratoryDTOs);

//                        // Map additional fields from patient health metrics
//                        PatientHealthMetrics patientHealthMetrics = patientHealthMetricsRepository.findByPatientAndPrescription(patient, prescription);
//                        if (patientHealthMetrics != null) {
//                            prescriptionDTO.setAllergy(patientHealthMetrics.getAllergy());
//                            prescriptionDTO.setComorbidity(patientHealthMetrics.getComorbidity());
//                            prescriptionDTO.setComplaints(patientHealthMetrics.getComplaints());
//                            prescriptionDTO.setOtherIllnesses(Collections.singletonList(patientHealthMetrics.getOtherIllness()));
//                        }

//                        // Fetch PatientHealthMetrics by ID
//                        PatientHealthMetrics patientHealthMetrics = findById(patientId);
//
//                        // Populate additional fields from patient health metrics to prescriptionDTO
//                        if (patientHealthMetrics != null) {
//                            prescriptionDTO.setAllergy(patientHealthMetrics.getAllergy());
//                            prescriptionDTO.setComorbidity(patientHealthMetrics.getComorbidity());
//                            prescriptionDTO.setComplaints(patientHealthMetrics.getComplaints());
//                            prescriptionDTO.setOtherIllnesses(Collections.singletonList(patientHealthMetrics.getOtherIllness()));
//                        }
//

                        // Map additional fields from patient health metrics
                        PatientHealthMetrics patientHealthMetrics = findPatientHealthMetricsByPatient(patient);
                        if (patientHealthMetrics != null) {
                            prescriptionDTO.setAllergy(patientHealthMetrics.getAllergy());
                            prescriptionDTO.setComorbidity(patientHealthMetrics.getComorbidity());
                            prescriptionDTO.setComplaints(patientHealthMetrics.getComplaints());
                            prescriptionDTO.setOtherIllnesses(Collections.singletonList(patientHealthMetrics.getOtherIllness()));
                        }


                        return prescriptionDTO;
                    }).collect(Collectors.toList());

            dto.setPrescriptions(prescriptionDTOs);

            return dto;
        }).collect(Collectors.toList());
    }
    public PatientHealthMetrics findPatientHealthMetricsByPatient(Patient patient) {
        return patientHealthMetricsRepository.findByPatient(patient);
    }

    private MedicationDTO mapToMedicationDTO(Medication medication) {
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setMedication(medication.getMedication());
        medicationDTO.setFrequency(medication.getFrequency());
        medicationDTO.setDosage(medication.getDosage());
        medicationDTO.setDays(medication.getDays());
        return medicationDTO;
    }

    private LaboratoryDTO mapToLaboratoryDTO(Laboratory laboratory) {
        LaboratoryDTO laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.setLaboratoryId(laboratory.getLaboratoryId());
        laboratoryDTO.setLaboratory(laboratory.getLaboratory());
        return laboratoryDTO;
    }


    public PatientHealthMetrics findById(Long id) {
        return patientHealthMetricsRepository.findById(id).orElse(null);
    }



    @GetMapping(value = "/patient-details-symptom", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientHealthMetricsDTO> getPatientDetailsByDoctor(
            @RequestParam Long patientId,
            @RequestHeader("Auth") String jwtToken
    ) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details including roles
        Doctor doctor = docHSService.findByUsernameDoctor(username);

        // Check if the user has DOCTOR role
        if (doctor != null && doctor.getRoles().stream().anyMatch(role -> role.getName().equals("DOCTOR"))) {
            // User has DOCTOR role, proceed to fetch patient details
            PatientHealthMetricsDTO patientHealthMetricsDTO = patientService.getPatientHealthMetrics(patientId);
            return ResponseEntity.ok(patientHealthMetricsDTO);
        } else {
            // User does not have required role, return unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }






//    @PostMapping("/send")
//    public ResponseEntity<?> sendRequest(@RequestParam Long patientId, @RequestParam Long doctorId) {
//        Optional<HealthOfficer> patientOpt = healthOfficerRepository.findById(patientId);
//        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
//
//        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
//            Patient request = new Patient();
//            request.setHealthOfficer(patientOpt.get());
//            request.setDoctor(doctorOpt.get());
//            request.setAccepted(false);
//            request.setRequestTime(LocalDateTime.now());
//            patientRepository.save(request);
//            return ResponseEntity.ok("Request sent successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Invalid patient or doctor ID.");
//        }
//    }
//
//    @PostMapping("/respond")
//    public ResponseEntity<?> respondRequest(@RequestParam Long requestId, @RequestParam boolean accept) {
//        Optional<Patient> requestOpt = patientRepository.findById(requestId);
//
//        if (requestOpt.isPresent()) {
//            Patient request = requestOpt.get();
//            request.setAccepted(accept);
//            patientRepository.save(request);
//
//            Patient patient = request.getHealthOfficer();
//            patient.setStatus(accept);
//            patientRepository.save(patient);
//
//            return ResponseEntity.ok("Request " + (accept ? "accepted" : "declined") + " successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Invalid request ID.");
//        }
//    }

}
