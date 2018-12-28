package cf.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import cf.dao.StorageService;
@Service
public class StorageServiceImpl implements StorageService {

	private Path rootLocation;
	
	@Autowired
	public StorageServiceImpl(StorageProperties storeproperties) {
		System.out.println("========"+storeproperties.getLocation());
		this.rootLocation = Paths.get(storeproperties.getLocation());
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(this.rootLocation);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("init error");
		}

	}

	@Override
	public void store(MultipartFile file) {
		try {
			if(file.isEmpty()) {
				throw new RuntimeException();
			}
			InputStream inputStream = file.getInputStream();
			String originalFilename = file.getOriginalFilename();
			Path resolve = this.rootLocation.resolve(originalFilename);
			if(resolve.toFile().exists()) {
				resolve = this.rootLocation.resolve(originalFilename+"_1");
			}
			Files.copy(inputStream, resolve);

		} catch (Exception e) {
			throw new RuntimeException("存储文件出错");
		}
		
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, FileVisitOption.FOLLOW_LINKS)
					.filter(path->{
						System.out.println("loadAll-path:"+path);
						System.out.println("loadAll-rootlocation:"+this.rootLocation);
						return !path.equals(this.rootLocation);
//						return true;
					})
					.map(path->this.rootLocation.relativize(path));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path load = load(filename);
			Resource resource=new UrlResource(load.toUri());
			if(resource.exists() && resource.isReadable()) {
				return resource;
			}else {
				throw new RuntimeException("文件不存在或者没有读取权限");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("文件不存在或者没有读取权限");
		}
	}

	@Override
	public void deleteAll() {
		try {
			FileSystemUtils.deleteRecursively(this.rootLocation);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("删除所有文件失败");
		}
	}

}
