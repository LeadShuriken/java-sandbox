package server;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;

import common.AssetPOJOIF;

public class AssetPOJO implements AssetPOJOIF, Serializable {

	private static final long serialVersionUID = 1L;

	private String filesFolder;
	private String file = null;
	private byte[] data = null;

	public AssetPOJO(String filesFolder) {
		this.filesFolder = filesFolder;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public void init() {
		Path fileLocation = Paths.get(filesFolder + file);
		try {
			data = Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getData() {
		return data;
	};

	@Override
	public String getFilename() {
		return file;
	};
}