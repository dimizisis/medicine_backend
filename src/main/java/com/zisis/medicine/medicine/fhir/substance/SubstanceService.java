package com.zisis.medicine.medicine.fhir.substance;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Substance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubstanceService {

    private final IGenericClient fhirClient;

    @Autowired
    public SubstanceService(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    // Fetch the display name of a Substance by reference
    public String getSubstanceDisplayName(String substanceReference) {
        // Extract the ID from the reference URL
        String substanceId = extractSubstanceId(substanceReference);

        // Fetch the Substance resource using the ID
        Substance substance = fhirClient.read()
                .resource(Substance.class)
                .withId(substanceId)
                .execute();

        // Retrieve and return the display name
        if (substance.hasCode()) {
            return substance.getCode().getCodingFirstRep().getDisplay();
        } else {
            throw new RuntimeException("Display name not found for Substance with ID: " + substanceId);
        }
    }

    // Helper method to extract the ID from the reference URL
    private String extractSubstanceId(String referenceUrl) {
        // Assuming referenceUrl is in the format "Substance/{id}"
        return referenceUrl.substring(referenceUrl.lastIndexOf('/') + 1);
    }
}
