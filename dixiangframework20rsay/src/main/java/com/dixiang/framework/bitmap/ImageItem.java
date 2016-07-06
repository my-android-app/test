package com.dixiang.framework.bitmap;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;//图片编号
	public String thumbnailPath;//缩略图地址
	public String imagePath;//原图地址
    public String upImagePath;//提交服务器的图片地址
	public boolean isSelected = false;//是否被选中
    public String bucketName;//所属相册的名称
    public boolean hasSave;//照片是否已被保存为临时文件
	public String dimension;
	public void setDimension(int width ,int height){
		dimension = "{ \"width\":\""+width+"\" , \"height\":\""+height+"\"}";
	}
}
