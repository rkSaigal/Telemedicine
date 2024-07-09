package com.telemedicicne.telemedicicne.Service;

import com.telemedicicne.telemedicicne.Dtos.*;
import com.telemedicicne.telemedicicne.Entity.*;
import com.telemedicicne.telemedicicne.Exception.ResourceNotFoundException;
import com.telemedicicne.telemedicicne.Exception.UnauthorizedException;
import com.telemedicicne.telemedicicne.Repository.*;
import com.telemedicicne.telemedicicne.Security.JwtHelper;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientHealthMetricsRepository patientHealthMetricsRepository;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private DoctorHospitalService docHSService;

    @Autowired
    private DoctorRepository doctorRepository;

//    public void registerPatient(Patient patient) {
//        // Additional logic for patient registration, if needed
//
//        // Example: You might want to validate input fields, check for existing patients, etc.
//        patientRepository.save(patient);
//    }


    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public PatientHealthMetrics findPatientHealthMetricsById(Long id) {
        Optional<PatientHealthMetrics> optional = patientHealthMetricsRepository.findById(id);
        return optional.orElse(null);
    }

    public void savePatientHealthMetrics(PatientHealthMetrics patientHealthMetrics) {
        patientHealthMetricsRepository.save(patientHealthMetrics);
    }
    public void save(PatientHealthMetrics patientHealthMetrics) {
        patientHealthMetricsRepository.save(patientHealthMetrics);
    }
    public void registerPatient(Patient patient) {
        // Assign the PATIENT role to the new patient
        Role patientRole = roleRepo.findByName("PATIENT");
        if (patientRole == null) {
            throw new IllegalArgumentException("Role not found: PATIENT");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);
        patient.setRoles(roles);

        // Save the new Patient entity
        patientRepository.save(patient);
    }
    public Optional<Patient> findByAddharNo(String addharNo) {
        return patientRepository.findByAddharNo(addharNo);
    }


    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> findAllPatientsByDocHs(HealthOfficer docHs) {
        return patientRepository.findByHealthOfficer(docHs);
    }

    public List<Patient> findAllPatientsByDocHsAndRegistrationDate(HealthOfficer docHs, LocalDate localDate) {
        return patientRepository.findByHealthOfficerAndLocalDate(docHs, localDate);
    }


@Transactional
public void saveHealthMetrics(Long patientId, PatientHealthMetricsDTO healthMetricsDTO, HealthOfficer docHs) throws IOException {
    // Fetch patient from database
    Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

    // Fetch doctor details by username
    Doctor doctor = doctorRepository.findByEmail(healthMetricsDTO.getDoctor())
            .orElseThrow(() -> new EntityNotFoundException("Doctor not found with email: " + healthMetricsDTO.getDoctor()));

    // Print doctor email for debugging
    System.out.println("Doctor's Email: " + doctor.getEmail());

    // Set the doctor in the patient entity
    patient.setDoctor(doctor);
    patientRepository.save(patient); // Save the patient entity with the updated doctor

    // Check current date
    LocalDate currentDate = LocalDate.now();

    // Find the latest entry for the current date
    PatientHealthMetrics lastEntry = patientHealthMetricsRepository.findTopByPatientAndLocalDateOrderByPatientIddDesc(patient, currentDate);

    Long newPatientIdd = 1L;
    if (lastEntry != null) {
        // Increment patientIdd based on the last entry on the same date
        newPatientIdd = lastEntry.getPatientIdd() + 1;
    }

    // Map DTO data to entity
    PatientHealthMetrics healthMetrics = new PatientHealthMetrics();
    healthMetrics.setHealthOfficer(docHs);
    healthMetrics.setPatient(patient);
    healthMetrics.setLocalDate(currentDate);
    healthMetrics.setPatientIdd(newPatientIdd);  // Set patientIdd
    healthMetrics.setDoctor(doctor);

    healthMetrics.setRespirationRate(healthMetricsDTO.getRespirationRate());
    healthMetrics.setSpO2(healthMetricsDTO.getSpO2());
    healthMetrics.setBloodGroup(healthMetricsDTO.getBloodGroup());
    healthMetrics.setHeartRate(healthMetricsDTO.getHeartRate());
    healthMetrics.setDiastolicBP(healthMetricsDTO.getDiastolicBP());
    healthMetrics.setSystolicBP(healthMetricsDTO.getSystolicBP());
    healthMetrics.setTemperature(healthMetricsDTO.getTemperature());
    healthMetrics.setHemoglobin(healthMetricsDTO.getHemoglobin());
    healthMetrics.setComorbidity(healthMetricsDTO.getComorbidity());
    healthMetrics.setComplaints(healthMetricsDTO.getComplaints());
    healthMetrics.setBloodSugar(healthMetricsDTO.getBloodSugar());
    healthMetrics.setPulseRate(healthMetricsDTO.getPulseRate());
    healthMetrics.setOtherIllness(healthMetricsDTO.getOtherIllness());
    healthMetrics.setAllergy(healthMetricsDTO.getAllergy());

    // Handle file uploads
    if (healthMetricsDTO.getFiles() != null && healthMetricsDTO.getFiles().length > 0) {
        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        for (MultipartFile file : healthMetricsDTO.getFiles()) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.copy(file.getInputStream(), filePath);

            Document document = new Document();
            document.setDocumentName(file.getOriginalFilename());
            document.setDocument(fileName); // Save file name or path here
            document.setPatientHealthMetrics(healthMetrics);

            healthMetrics.getDocuments().add(document);
        }
    }

    // Save health metrics
    patientHealthMetricsRepository.save(healthMetrics);
}


    @Autowired
    private DocumentRepository documentRepository;
    // Injecting the file upload directory path from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;


@Transactional
public void uploadDocuments(Long patientHealthMetricsId, MultipartFile[] files) throws IOException {
    PatientHealthMetrics healthMetrics = patientHealthMetricsRepository.findById(patientHealthMetricsId)
            .orElseThrow(() -> new IllegalArgumentException("Health metrics not found with id: " + patientHealthMetricsId));

    // Create upload directory if it doesn't exist
    Path directoryPath = Paths.get(uploadDir);
    if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
    }

    for (MultipartFile file : files) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), filePath);

        Document document = new Document();
        document.setDocumentName(file.getOriginalFilename());
        document.setDocument(fileName); // Save file name or path here
        document.setPatientHealthMetrics(healthMetrics);

        healthMetrics.getDocuments().add(document);
    }

    patientHealthMetricsRepository.save(healthMetrics);
}



    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByPatient(Long patientId, String jwtToken) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer details including roles
        HealthOfficer healthOfficer = docHSService.findByUsername(username);
        Doctor doctor = docHSService.findByUsernameDoctor(username);


//        // Check if the user has the HEALTH_OFFICER role
//        if (docHs == null || !docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
//            throw new UnauthorizedAccessException("Unauthorized to access documents.");
//        }
        // Check if the user has either HEALTH_OFFICER or DOCTOR role
        if ((healthOfficer != null && healthOfficer.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) ||
                (doctor != null && doctor.getRoles().stream().anyMatch(role -> role.getName().equals("DOCTOR")))) {
        }

            // Retrieve health metrics for the patient
        PatientHealthMetrics healthMetrics = patientHealthMetricsRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Health metrics not found for patient ID: " + patientId));

        // Retrieve documents associated with the patient's health metrics
        List<Document> documents = healthMetrics.getDocuments();

        // Map Document entities to DocumentDTOs with document URLs and content types
        return documents.stream()
                .map(document -> {
                    String documentUrl = "/documents/" + document.getDocument(); // Example URL format
                    String contentType = determineContentType(document.getDocument()); // Implement this method

                    return new DocumentDTO(document.getDocumentName(), contentType, documentUrl);
                })
                .collect(Collectors.toList());
    }

    // Method to determine content type based on file extension
    private String determineContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else {
            return "application/octet-stream"; // Default content type for unknown file types
        }
    }



    public PatientHealthMetricsDTO getHealthMetricsByPatientId(Long patientId) {
        // Retrieve HealthOfficer with HEALTH_OFFICER role
//        HealthOfficer HealthOfficer = docHSService.findHealthOfficer(); // Implement this method in DocHsService

        // Fetch PatientHealthMetrics for the given patientId and HealthOfficer
        PatientHealthMetrics healthMetrics = patientHealthMetricsRepository.findByPatientPatientIdAndHealthOfficerRolesName(patientId, "HEALTH_OFFICER")
                .orElseThrow(() -> new EntityNotFoundException("Health metrics not found for patient with id: " + patientId));

        // Map entity to DTO
        return new PatientHealthMetricsDTO(
                healthMetrics.getId(),
                healthMetrics.getBloodGroup(),
                healthMetrics.getSpO2(),
                healthMetrics.getRespirationRate(),
                healthMetrics.getHeartRate(),
                healthMetrics.getDiastolicBP(),
                healthMetrics.getSystolicBP(),
                healthMetrics.getPulseRate(),
                healthMetrics.getTemperature(),
                healthMetrics.getHemoglobin(),
                healthMetrics.getBloodSugar()
        );
    }



    public PatientDetailsDTO getPatientDetails(Long patientId) {
        // Retrieve patient details from repository
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        // Get a list of PatientHealthMetrics associated with the patient
        List<PatientHealthMetrics> healthMetricsList = patient.getHealthMetrics();

        // Assuming you want to fetch a specific metric from the list
        Long serialNo = null;
        if (!healthMetricsList.isEmpty()) {
            // For example, fetching the first metric's patientIdd
            serialNo = healthMetricsList.get(0).getPatientIdd();
        }

        // Construct DTO with required fields including serialNo
        return new PatientDetailsDTO(
                patient.getPatientId(),
                patient.getPatientName(),
                patient.getEmail(),
                serialNo // Pass serialNo to the constructor
        );
    }








    //prescription

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    // Method to retrieve a patient by ID
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    public String createPrescription(Long patientId, PrescriptionDTO prescriptionDTO, String jwtToken) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer details including roles
        Doctor docHs = docHSService.findByUsernameDoctor(username);

        // Check if the user has the DOCTOR role
        if (docHs == null || !docHs.getRoles().stream().anyMatch(role -> role.getName().equals("DOCTOR"))) {
            throw new UnauthorizedException("Unauthorized to create prescriptions.");
        }



        // Fetch patient
        Patient patient = getPatientById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found with id: " + patientId);
        }

        // Create Prescription entity and set basic details
        Prescription prescription = new Prescription();
        prescription.setChiefComplaints(prescriptionDTO.getChiefComplaints());
        prescription.setSymptoms(prescriptionDTO.getSymptoms());
        prescription.setAdvice(prescriptionDTO.getAdvice());
        prescription.setProvisionalDiagnosis(prescriptionDTO.getProvisionalDiagnosis());
        prescription.setFollowUpDate(prescriptionDTO.getFollowUpDate());
        prescription.setPatient(patient);

        // Create Medication entities and associate with the prescription
        List<MedicationDTO> medicationsDTO = prescriptionDTO.getMedications();
        List<Medication> medications = new ArrayList<>();

        for (MedicationDTO medicationDTO : medicationsDTO) {
            Medication medication = new Medication();
            medication.setMedication(medicationDTO.getMedication());
            medication.setFrequency(medicationDTO.getFrequency());
            medication.setDosage(medicationDTO.getDosage());
            medication.setDays(medicationDTO.getDays()); // Ensure days is set or handle default
//            medication.setLaboratory(medicationDTO.getLaboratory());
            medication.setPrescription(prescription); // Associate with the prescription
            medications.add(medication);
        }

        // Create Laboratory entities and associate with the prescription
        List<LaboratoryDTO> laboratoriesDTO = prescriptionDTO.getLaboratory();
        List<Laboratory> laboratories = new ArrayList<>();

        for (LaboratoryDTO laboratoryDTO : laboratoriesDTO) {
            Laboratory laboratory = new Laboratory();
            laboratory.setLaboratory(laboratoryDTO.getLaboratory());
            laboratory.setPrescription(prescription); // Associate with the prescription
            laboratories.add(laboratory);
        }

        // Set medications for the prescription
        prescription.setMedications(medications);
        prescription.setLaboratories(laboratories);

        // Save prescription and associated medications
        prescription = prescriptionRepository.save(prescription);

        return "Prescription created successfully.";
    }
    public PrescriptionDTO getPrescriptionById(Long patientId) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findByPatientPatientId(patientId);
        if (prescriptionOptional.isEmpty()) {
            throw new IllegalArgumentException("Prescription not found with id: " + patientId);
        }

        Prescription prescription = prescriptionOptional.get();
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setId(prescription.getId());
        prescriptionDTO.setChiefComplaints(prescription.getChiefComplaints());
        prescriptionDTO.setSymptoms(prescription.getSymptoms());
        prescriptionDTO.setAdvice(prescription.getAdvice());
        prescriptionDTO.setProvisionalDiagnosis(prescription.getProvisionalDiagnosis());
        prescriptionDTO.setFollowUpDate(prescription.getFollowUpDate());

        // Example: Fetch medications from prescription entity and map to MedicationDTO
        List<MedicationDTO> medicationsDTO = prescription.getMedications().stream()
                .map(medication -> {
                    MedicationDTO medicationDTO = new MedicationDTO();
                    medicationDTO.setMedication(medication.getMedication());
                    medicationDTO.setFrequency(medication.getFrequency());
                    medicationDTO.setDosage(medication.getDosage());
                    medicationDTO.setDays(medication.getDays());
//                    medicationDTO.setLaboratory(medication.getLaboratory());
                    return medicationDTO;
                })
                .collect(Collectors.toList());

        prescriptionDTO.setMedications(medicationsDTO);

        // Map laboratories from prescription entity to LaboratoryDTO
        List<LaboratoryDTO> laboratoriesDTO = prescription.getLaboratories().stream()
                .map(laboratory -> {
                    LaboratoryDTO laboratoryDTO = new LaboratoryDTO();
                    laboratoryDTO.setLaboratoryId(laboratory.getLaboratoryId());
                    laboratoryDTO.setLaboratory(laboratory.getLaboratory());
                    return laboratoryDTO;
                })
                .collect(Collectors.toList());

        prescriptionDTO.setLaboratory(laboratoriesDTO);

        return prescriptionDTO;
    }



    // Method to fetch patient health metrics DTO accessible to doctors only
    public PatientHealthMetricsDTO getPatientHealthMetrics(Long patientId) {
        PatientHealthMetrics patient = patientHealthMetricsRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        // Create DTO with required details
        PatientHealthMetricsDTO patientHealthMetricsDTO = new PatientHealthMetricsDTO(
                patient.getId(),
                patient.getComorbidity(),
                patient.getComplaints(),
                patient.getAllergy(),
                patient.getOtherIllness()
        );

        return patientHealthMetricsDTO;
    }
}
