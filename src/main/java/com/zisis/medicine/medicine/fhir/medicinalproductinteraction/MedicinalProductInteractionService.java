package com.zisis.medicine.medicine.fhir.medicinalproductinteraction;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.zisis.medicine.medicine.entity.Medicine;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicinalProductInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicinalProductInteractionService {

    private final IGenericClient fhirClient;

    @Autowired
    public MedicinalProductInteractionService(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    public List<String> checkForDrugInteractions(Collection<Medicine> existingMedicines, Medicine newMedicine) {
        // Check for interactions using FHIR MedicinalProductInteraction resource
        return existingMedicines.stream()
                .flatMap(existingMedicine -> getDrugInteractions(newMedicine, existingMedicine).stream())
                .collect(Collectors.toList());
    }

    private List<String> getDrugInteractions(Medicine newMedicine, Medicine existingMedicine) {
        Bundle bundle = fhirClient.search()
                .forResource(MedicinalProductInteraction.class)
                .where(MedicinalProductInteraction.SUBJECT.hasId(existingMedicine.getName()))
                .returnBundle(Bundle.class)
                .execute();

        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource instanceof MedicinalProductInteraction)
                .map(resource -> (MedicinalProductInteraction) resource)
                .flatMap(interaction -> interaction.getInteractant().stream())
                .filter(interactant -> interactant.hasItem() &&
                        interactant.hasItemReference() &&
                        interactant.getItemReference().getReference().contains(newMedicine.getName()))
                .map(interactant -> "Interaction between " + existingMedicine.getName() + " and " + newMedicine.getName())
                .collect(Collectors.toList());
    }
}
