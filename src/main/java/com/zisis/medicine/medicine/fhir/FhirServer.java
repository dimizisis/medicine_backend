package com.zisis.medicine.medicine.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/fhir/*"}, loadOnStartup = 1)
public class FhirServer extends RestfulServer {

    private static final long serialVersionUID = 1L;

    public FhirServer() {
        super(FhirContext.forR5Cached());
    }

    @Override
    protected void initialize() {
        super.initialize();
    }
}
