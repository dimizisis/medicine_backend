package com.zisis.medicine.medicine.fhir.medication;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Substance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MedicationService {

    @Autowired
    private final IGenericClient fhirClient;

    public MedicationService(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    // Fetch the substances in a Medication by medication ID
    public List<String> getMedicationSubstances(String medicationId) {
        // Fetch the Medication resource using the ID
        Medication medication = fhirClient.read()
                .resource(Medication.class)
                .withId(medicationId)
                .execute();

        // Retrieve and return the substance names
        List<String> substances = new ArrayList<>();

        // Check if the medication has ingredients
        if (medication.hasIngredient()) {
            medication.getIngredient().forEach(ingredient -> {
                CodeableConcept item = ingredient.getItemCodeableConcept();
                if (item != null) {
                    item.getCoding().forEach(coding -> substances.add(coding.getDisplay()));
                }
                // If using itemReference instead
                Reference reference = ingredient.getItemReference();
                if (reference != null) {
                    String substanceId = reference.getReferenceElement().getIdPart();
                    substances.add(getSubstanceDisplayName(substanceId));
                }
            });
        }
        return substances;
    }

    // Helper method to fetch the substance display name from the substance ID
    private String getSubstanceDisplayName(String substanceId) {
        Substance substance = fhirClient.read()
                .resource(Substance.class)
                .withId(substanceId)
                .execute();
        return substance.getCode().getCodingFirstRep().getDisplay();
    }
}
