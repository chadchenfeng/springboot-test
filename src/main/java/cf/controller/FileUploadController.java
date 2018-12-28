package cf.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cf.dao.StorageService;

@Controller
public class FileUploadController {

	@Autowired
	private StorageService storageservice;
	
	@GetMapping("/")
	public String listUploadFiles(Model model) {
		model.addAttribute("files",storageservice
									.loadAll()
									.map(path->MvcUriComponentsBuilder.fromMethodName(FileUploadController.class
																					, "serveFile"
																					, path.getFileName().toString()).build().toString())
									.filter(path-> {
										System.out.println("listUploadFiles===="+path);
										return true;
									})
									.collect(Collectors.toList())
							);
		
		return "uploadForm";
	}
	@RequestMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename){
		Resource file = storageservice.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
				.body(file);
	}
	
	@RequestMapping(value="/getfiles",method=RequestMethod.GET)
	@ResponseBody
	public String getFile(@RequestParam("filename") String filename) throws IOException{
		Resource file = storageservice.loadAsResource(filename);
		return file.getURI().toString();
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
									@RequestParam(value="fieldtest",required=false) String test,
                                   RedirectAttributes redirectAttributes) {
		System.out.println(test);
		storageservice.store(file);
		redirectAttributes.addFlashAttribute("message",file.getOriginalFilename() +"upload success!");
		return "redirect:/";
	}
	
	
}
