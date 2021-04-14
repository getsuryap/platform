package org.ospic.platform.controllers;

import io.swagger.annotations.ApiOperation;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
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
	@Autowired
	FilesStorageService filesStorageService;
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

	@ApiOperation(value = "UPDATE Patient upload Thumbnail image", notes = "UPDATE Patient upload Thumbnail image")
	@RequestMapping(value = "/reports", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadReportFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			//filesStorageService.uploadReportFile(file);
			return ResponseEntity.ok().body("FIle saved with name: " +filesStorageService.uploadReportFile(file));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}


	@GetMapping(value= "/welcome")
	public ModelAndView index() {
		log.info("Showing the welcome page.");
		model.setViewName("welcome");
		return model;
	}

	@GetMapping(value= "/login")
	public ModelAndView login() {
		model.setViewName("login");
		return model;
	}

	@GetMapping("/view")
	@ResponseBody
	public ResponseEntity<?> viewReport() throws IOException, JRException,ServletException, SQLException {
		//OutputStream out = response.getOutputStream();
		HttpHeaders headers = new HttpHeaders();

		String filename = "pdf1.pdf";

		headers.setContentType(MediaType.APPLICATION_PDF);

		headers.add("Content-Disposition", "inline; filename=" + filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		byte[] bytes = this.exportPdfReport(repository.findAll());
		headers.setContentLength(bytes.length);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		return responseEntity;
	}

	// Method to create the pdf file using the employee list datasource.
	private byte[]  exportPdfReport(final List<Patient> employees) throws ServletException, JRException, IOException {

		try {
			// Fetching the .jrxml file from the resources folder.
			Resource resource = context.getResource("classpath:reports/patient_list.jrxml");
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
			return JasperExportManager.exportReportToPdf(print);
		} catch (JRException ex) {
			throw new ServletException(ex);
		}
	}
}
