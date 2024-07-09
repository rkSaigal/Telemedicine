            package com.telemedicicne.telemedicicne.Entity;

            import jakarta.persistence.*;
            import lombok.AllArgsConstructor;
            import lombok.Getter;
            import lombok.NoArgsConstructor;
            import lombok.Setter;


            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
            @Entity
            public class Document {

                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Long documentId;
                private String documentName;
                private String document;

                @ManyToOne(fetch = FetchType.LAZY)
                private PatientHealthMetrics patientHealthMetrics;
            }
