package org.ospic.platform.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.ospic.platform.patient.infos.domain.Patient;
import org.ospic.platform.patient.infos.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@ApiIgnore
@RequestMapping("/api/test")
public class TestController {
	final ModelAndView model = new ModelAndView();
	final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ApplicationContext context;
	@Autowired
	PatientRepository repository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}


	@GetMapping(value= "/welcome")
	public ModelAndView index() {
		log.info("Showing the welcome page.");
		model.setViewName("welcome");
		return model;
	}

	// Method to create the pdf report via jasper framework.

	@GetMapping("/view")
	@ResponseBody
	public void viewReport(HttpServletResponse response) throws IOException, JRException, SQLException {
		OutputStream out = response.getOutputStream();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + "example.pdf");
		this.exportPdfReport(repository.findAll(), out);
	}

	// Method to create the pdf file using the employee list datasource.
	private void  exportPdfReport(final List<Patient> employees, OutputStream outputStream) throws JRException, IOException {

		// Fetching the .jrxml file from the resources folder.
		Resource resource = context.getResource("classpath:jasperreports/patient_list.jrxml");
		final InputStream stream = resource.getInputStream();

		// Compile the Jasper report from .jrxml to .japser
		final JasperReport report = JasperCompileManager.compileReport(stream);

		// Fetching the employees from the data source.
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(employees);


		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "javacodegeek.com");

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
		final String filePath = "\\";
		// Export the report to a PDF file.
		 JasperExportManager.exportReportToPdfStream(print, outputStream);
	}
}
