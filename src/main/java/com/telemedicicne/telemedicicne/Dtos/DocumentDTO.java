package com.telemedicicne.telemedicicne.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DocumentDTO {
    private String documentName;
    private String document;
    private String documentUrl; // New field for document URL


    public DocumentDTO(String documentName, String document, String documentUrl) {
        this.documentName = documentName;
        this.document = document;
        this.documentUrl = documentUrl;
    }
}
