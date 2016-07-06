package com.dixiang.framework.utils;

import java.io.File;

import com.dixiang.framework.exception.CreateFileEorrException;


public class FileUtils {
	
	public static File dataCache(String dirPath, String fileName) throws CreateFileEorrException,Exception{
		if (dirPath != null && dirPath.length() > 0){
			File dir = new File(dirPath);
			
			if (!dir.exists()){
				dir.mkdirs();
			}
			
			if(fileName != null && fileName.length() >= 0){
				File file = new File(dirPath+fileName);
				if(!file.exists()) file.createNewFile();
				return file;
			}else{
				throw new CreateFileEorrException("the file path is null");
			}
		}else{
			throw new CreateFileEorrException("the dir path is null");
		}
		
	}
}

