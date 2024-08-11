# Medicine Management Application

This Medicine Management Application is designed to help individuals keep track of medication information while checking for drug interactions among the medicines. It leverages the FHIR standard for interoperability and can query external FHIR servers for drug interaction data.

For now, only the backend being developed.

## Features

- **Medicine Management**: Add, update, delete, and view medicines in the database.
- **Drug Interaction Checks**: Automatically checks for drug interactions when adding a new medicine using external FHIR servers.
- **FHIR Integration**: Utilizes the HAPI FHIR client to fetch `MedicinalProductInteraction` and `Substance` resources.
- **REST API**: Provides a RESTful API for interacting with the medicine database.

## Technologies Used

- **Java**: Programming language used for the backend.
- **Spring Boot**: Framework for building the application.
- **HAPI FHIR**: Library used to interact with FHIR resources.
- **H2 Database**: In-memory database for storing medicine data.
- **Maven/Gradle**: Dependency management and build tool.

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven
- Internet connection to access external FHIR servers

### Clone the Repository

```bash
git clone https://github.com/yourusername/medicine-management-app.git
cd medicine-management-app
```

## Access the Application

The application will be accessible at ```http://localhost:8080/api```.

## API Endpoints
### Add a Medicine
```POST``` ```/api/medicines```

#### Request Body
```json
{
    "name": "Citalopram",
    "ingredient": "Celexa",
    "manufacturer": "Citalopram",
    "expiryDate": "2024-05-15",
    "quantity": 1
}
```

### Get All Medicines

```GET``` ```/api/medicines```

### Get a Medicine by ID

```GET``` ```/api/medicines/{id}```

### Update a Medicine

```PUT``` ```/api/medicines/{id}```

#### Request Body
```json
{
    "name": "Citalopram",
    "ingredient": "Celexa",
    "manufacturer": "Citalopram",
    "expiryDate": "2024-05-15",
    "quantity": 2
}
```

### Delete a Medicine

```DELETE``` ```/api/medicines/{id}```

## Drug Interaction Check (Under development)
Automatically performed when adding a new medicine
Fetches interactions using ```MedicinalProductInteraction``` resources from the test FHIR server.

Within ```MedicineServiceImpl``` class:

```java
@Override
public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {

    List<String> interactionWarnings = medicinalProductInteractionService.checkForDrugInteractions(medicineRepository.findAll(), Medicine.fromRequestDTO(request));

    if (!interactionWarnings.isEmpty()) {
        // Handle the interaction warning, logging for now
        System.out.println("Drug interactions found: " + String.join(", ", interactionWarnings));
}
```

So within the ````` we want something like this:

```java
public List<String> checkForDrugInteractions(Collection<Medicine> existingMedicines, Medicine newMedicine) {
// Check for interactions using FHIR MedicinalProductInteraction resource
return existingMedicines.stream()
    .flatMap(existingMedicine -> getDrugInteractions(newMedicine, existingMedicine).stream())
    .collect(Collectors.toList());
}

private List<String> getDrugInteractions(Medicine newMedicine, Medicine existingMedicine) {
    Bundle bundle = fhirClient.search()
        .forResource(MedicinalProductInteraction.class)
        .where(??)
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
```
