package com.osquare.mydearnest.post.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.junglebird.webframe.common.PropertiesManager;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.post.vo.ImageSourceFile;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Resource private SessionFactory sessionFactory;
	@Autowired private PropertiesManager conf;

	@Override
	public ImageSource getImageSource(Long imageId) {

		ImageSource result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {
			result = (ImageSource) session.get(ImageSource.class, imageId);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		return result;
	}
	
	@Override
	public ImageSource createImageSourceForURL(String imageUrl) {

		ImageSource result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		URLConnection imgConnection = null;
		InputStream in = null;
		OutputStream out = null; 
		
		try {
			Date now = new Date();
			URL imgURL = new URL(imageUrl);			
			
			String suffix = "jpeg";
			try {
				if (imgURL.getPath().substring(imgURL.getPath().lastIndexOf(".") + 1).toLowerCase() == "jpg") {
					suffix = "jpeg";
				}
				else if (imgURL.getPath().substring(imgURL.getPath().lastIndexOf(".") + 1).toLowerCase() == "png") {
					suffix = "png";
				}
				else if (imgURL.getPath().substring(imgURL.getPath().lastIndexOf(".") + 1).toLowerCase() == "gif") {
					suffix = "gif";
				}
			}
			catch(Exception ex) {}

			result = new ImageSource();
			result.setFileName(imgURL.getPath().substring(imgURL.getPath().lastIndexOf("/") + 1));
			result.setExtension("image");
			result.setContentType("image/" + suffix);
			result.setByteLength(0);
			result.setStoragePath(new SimpleDateFormat("yyyy/MM").format(now));
			result.setCreatedAt(now);
			
			if (result.getFileName().length() > 255) 
				result.setFileName(result.getFileName().substring(0, 250));
			
			session.persist(result);
						
			File dir = new File(conf.get("imageSource.savePath") + "/" + result.getStoragePath() + "/" + result.getId());
			if(!dir.isDirectory()) dir.mkdirs();
			
			imgConnection = imgURL.openConnection();
			in = imgConnection.getInputStream(); 
			out = new BufferedOutputStream(new FileOutputStream(dir.toString() + "/source")); 
			
			byte[] buffer = new byte[1024]; 
            int numRead; 
            long readBytes = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                readBytes += numRead;
            }
            result.setByteLength(readBytes);

            if (in != null) in.close(); 
            if (out != null) out.close(); 

            String[] cmd = new String[] { "/bin/sh", "-c", conf.get("imagemagick.path") + " " + dir.toString() + "/source " + dir.toString() + "/source.jpg" };
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();

            String[] cmd2 = new String[] { "/bin/sh", "-c", "mv -f " + dir.toString() + "/source.jpg " + dir.toString() + "/source" };
            Process process2 = Runtime.getRuntime().exec(cmd2);
            process2.waitFor();

            BufferedImage img = ImageIO.read(new File(dir.toString() + "/source"));
            result.setWidth(img.getWidth());
            result.setHeight(img.getHeight());
            
            session.merge(result);

			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		finally { 
	        try { 
	            if (in != null) in.close(); 
	            if (out != null) out.close(); 
	        } 
	        catch (IOException ioe) {} 
	    } 
		
		return result;
	}
	
	@Override
	public ImageSource createImageSourceForData(CommonsMultipartFile filedata) {

		ImageSource result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		InputStream in = null;
		OutputStream out = null; 
		
		try {

			Date now = new Date();
			
			//ImageSource Table에 저장한다.
			result = new ImageSource();
			result.setFileName(filedata.getFileItem().getName());
			result.setExtension("image");
			result.setByteLength(0);
			result.setStoragePath(new SimpleDateFormat("yyyy/MM").format(now));
			result.setContentType(filedata.getFileItem().getContentType());
			result.setCreatedAt(now);
			
			if (result.getFileName().length() > 255) 
				result.setFileName(result.getFileName().substring(0, 250));
			
			session.persist(result);
			
			result.setByteLength(filedata.getSize());
			
			String file_location = result.getStoragePath() + "/" + result.getId();
			File dir = new File(conf.get("postFile.tmpPath") + "/" + file_location);
			if(!dir.isDirectory()) dir.mkdirs();
			
			File tmpFile = new File(dir, "tmpData");
			filedata.transferTo(tmpFile);
			
			BufferedImage img = ImageIO.read(tmpFile);
			result.setWidth(img.getWidth());
            result.setHeight(img.getHeight());
            
			
			//원이미지는 해당 ID/source로 저장. 
            AWSCredentials credentials = new BasicAWSCredentials(conf.get("amazon.credential.accessKey"), conf.get("amazon.credential.secretKey"));
			AmazonS3 fileStorageServer = new AmazonS3Client(credentials);
			PutObjectRequest putObjectRequest = new PutObjectRequest(conf.get("amazon.fs.bucketName"), file_location + "/" + "source", tmpFile);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			
			fileStorageServer.putObject(putObjectRequest);
			
			
            
            session.merge(result);

			session.getTransaction().commit();
			
			
			
			
			
			
//			//이미지 경로를 만들고 없으면 디렉토리 생성.
//			File dir = new File(conf.get("imageSource.savePath") + "/" + result.getStoragePath() + "/" + result.getId());
//			if(!dir.isDirectory()) dir.mkdirs();
//			
//			//해당 디렉토리에 source로 저장한다.
//			in = filedata.getInputStream(); 
//			out = new BufferedOutputStream(new FileOutputStream(dir.toString() + "/source")); 
//
//			byte[] buffer = new byte[1024]; 
//            int numRead; 
//            long readBytes = 0;
//            while ((numRead = in.read(buffer)) != -1) {
//                out.write(buffer, 0, numRead);
//                readBytes += numRead;
//            }
//            result.setByteLength(readBytes);
//            if (in != null) in.close(); 
//            if (out != null) out.close(); 
//            
//            if (conf.get("imagemagick.path") != null && !conf.get("imagemagick.path").isEmpty()) {
//	            String[] cmd = new String[] { "/bin/sh", "-c", conf.get("imagemagick.path") + " " + dir.toString() + "/source " + dir.toString() + "/source.jpg" };
//	            Process process = Runtime.getRuntime().exec(cmd);
//	            process.waitFor();
//	
//	            String[] cmd2 = new String[] { "/bin/sh", "-c", "mv -f " + dir.toString() + "/source.jpg " + dir.toString() + "/source" };
//	            Process process2 = Runtime.getRuntime().exec(cmd2);
//	            process2.waitFor();
//            }
//
//            
//            BufferedImage img = ImageIO.read(new File(dir.toString() + "/source"));
//            result.setWidth(img.getWidth());
//            result.setHeight(img.getHeight());
//            
//            session.merge(result);
//
//			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		finally { 
	        try { 
	            if (in != null) in.close(); 
	            if (out != null) out.close(); 
	        } 
	        catch (IOException ioe) {} 
	    } 
		
		return result;
	}


	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource) {
		
		if (imageSource == null) return null;
 
		ImageSourceFile result = null;

		try {
			result = new ImageSourceFile();
			
			String file_location = imageSource.getStoragePath() + "/" + imageSource.getId();
			System.out.println(file_location);
			
			File fileDir = new File(conf.get("postFile.tmpPath") + "/" + file_location);
			if(!fileDir.isDirectory()) fileDir.mkdirs();
			
			File tmp_aFile = new File(fileDir, "tmp_aFile");
			Boolean tmp_aFileExist = true;
			
			AWSCredentials credentials = new BasicAWSCredentials(conf.get("amazon.credential.accessKey"), conf.get("amazon.credential.secretKey"));
			AmazonS3 fileStorageServer = new AmazonS3Client(credentials);
			
			try {
				System.out.println("getThumbnailSource");
				fileStorageServer.getObject(new GetObjectRequest(conf.get("amazon.fs.bucketName"), file_location + "/source"), tmp_aFile);
				System.out.println("getThumbnailSource Complete");
			} catch (AmazonS3Exception s3e) {
				if(s3e.getStatusCode() == 404) {
					tmp_aFileExist = false;
				} else {
					throw s3e;
				}
			}
			
			result.setFileLength(tmp_aFile.length());
			result.setInputStream(new FileInputStream(tmp_aFile));
			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FileSize : " + result.getFileLength());
		
		return result;
	}

	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type) {
		return this.getSourceFile(imageSource, width, height, thumbnail_type, null);
	}
	
	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type, Long postId) {

		if (imageSource == null) return null;
		
		
//		String suffix = "";
//		if (postId != null) {
//			suffix = "/" + postId;
//		}

		ImageSourceFile result = new ImageSourceFile();
		
		String file_location = imageSource.getStoragePath() + "/" + imageSource.getId();
		System.out.println(file_location);
		
		File fileDir = new File(conf.get("postFile.tmpPath") + "/" + file_location);
		if(!fileDir.isDirectory()) fileDir.mkdirs();
		
		File tmp_aFile = new File(fileDir, "tmp_aFile");
		Boolean tmp_aFileExist = true;
		
		File tmp_tFile = new File(fileDir, "tmp_fFile");
		Boolean tmp_tFileExist = true;
		
		
		AWSCredentials credentials = new BasicAWSCredentials(conf.get("amazon.credential.accessKey"), conf.get("amazon.credential.secretKey"));
		AmazonS3 fileStorageServer = new AmazonS3Client(credentials);
		
		//YOU HAVE TO MODIFY ONCE AGAIN
		//이부분의 코드 반복되므로 리팩토링 해야함, AmazonS3를 이용하면 속도면에서 문제가 생김, CloudFront를 이용하도록 수정해야함.
		try {
			System.out.println("getImageSource");
			fileStorageServer.getObject(new GetObjectRequest(conf.get("amazon.fs.bucketName"), file_location + "/source"), tmp_aFile);
			System.out.println("getImageSource Complete");
		} catch(AmazonS3Exception s3e) {
			if (s3e.getStatusCode() == 404) {
				tmp_aFileExist = false;
			} else {
				throw s3e;
			}
		}
		
		String thumbFile_location = file_location + "/" + width + "x" + height + "_" + thumbnail_type + ".jpg";
		try {
			System.out.println("getThumbnailSource");
			fileStorageServer.getObject(new GetObjectRequest(conf.get("amazon.fs.bucketName"), thumbFile_location),	tmp_tFile);
			System.out.println("getThumbnailSource Complete");
		} catch (AmazonS3Exception s3e) {
			if(s3e.getStatusCode() == 404) {
				tmp_tFileExist = false;
			} else {
				throw s3e;
			}
		}
		
		
		
		
		
		
		
//		result.setFileLength(tmpFile.length());
//		result.setInputStream(new FileInputStream(tmpFile));
		
//		File afile = new File(conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + suffix + "/source");
//		if (!afile.exists()) suffix = "";
//		
//		File tFile = new File(conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + suffix + "/" + width + "x" + height + "_" + thumbnail_type + ".jpg");
		
		System.out.println("tmp_tFileExist : " + tmp_tFileExist);
		if (tmp_tFileExist) {
			try {
				System.out.println("Thumbnail Cache Enable");
				result.setFileLength(tmp_tFile.length());
				result.setInputStream(new FileInputStream(tmp_tFile));
				return result;
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			System.out.println("Make Thumbnail Start!!!");
//			String dir = conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + suffix;
			            
			File file = tmp_aFile;
			System.out.println(file);
			BufferedImage img = ImageIO.read(file.toURI().toURL());
			
	        double width_per;
	        double height_per;
	        double per = 0;
	        double resize_width = width;
	        double resize_height = height;
	
	        if (resize_width > 0 && img.getWidth() >= resize_width) width_per = resize_width / img.getWidth();
	        else width_per = 1;
	
	        if (resize_height > 0 && img.getHeight() >= resize_height) height_per = resize_height / img.getHeight();
	        else height_per = 1;
	
	        if (thumbnail_type.equals("ratio"))
	        {
	            if (width_per > height_per) per = height_per;
	            else per = width_per;
	            resize_width = ((double)img.getWidth() * per);
	            resize_height = ((double)img.getHeight() * per);
	        }
	        else
	        {
	            if (width_per < height_per) per = height_per;
	            else per = width_per;
	        }
	
	        if (per == 0) per = 1;
	
	        int _x = 0;
	        int _y = 0;
	
	        int new_width = (int)(img.getWidth() * per);
	        int new_height = (int)(img.getHeight() * per);
	
	        if (thumbnail_type.equals("crop"))
	        {
	            _x = (int)(resize_width / 2 - new_width / 2);
	            _y = (int)(resize_height / 2 - new_height / 2);
	        }
	        else
	        {
	            _x = 0;
	            _y = 0;
	        }

			ResampleOp resampleOp = new ResampleOp(new_width, new_height);
			resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
	        BufferedImage destImg = resampleOp.filter(img, null);

			int type = img.getType() == 0? BufferedImage.TYPE_INT_ARGB : img.getType();
	        BufferedImage targetImage = new BufferedImage((int)resize_width, (int)resize_height, type);
	        Graphics2D graphics2D = targetImage.createGraphics();
	        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        graphics2D.setBackground(Color.WHITE);
	        graphics2D.fillRect(0, 0, (int)resize_width, (int)resize_height);
	        graphics2D.drawImage(destImg, _x - 2, _y - 2, (int)new_width + 4, (int)new_height + 4, null);
	        
	        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
	        ImageWriter writer = (ImageWriter)iter.next();
	        ImageWriteParam iwp = writer.getDefaultWriteParam();
	        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	        iwp.setCompressionQuality(0.9f);
	        
	        FileImageOutputStream output = new FileImageOutputStream(tmp_tFile);
	        writer.setOutput(output);
	        
	        IIOImage image = new IIOImage(targetImage, null, null);
	        writer.write(null, image, iwp);
	        writer.dispose();
	        
			result = new ImageSourceFile();
			result.setFileLength(tmp_tFile.length());
			result.setInputStream(new FileInputStream(tmp_tFile));
			
			//아마존에 만들어진 파일 업로드
			PutObjectRequest putObjectRequest = new PutObjectRequest(conf.get("amazon.fs.bucketName"), thumbFile_location, tmp_tFile);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			
			fileStorageServer.putObject(putObjectRequest);
			
		} 
		catch (Exception e) {
		}
		
		return result;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<ImageSource> getImageSourceByFolder(Folder folder, int maxCount) {

		Collection<ImageSource> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			Query q = session
					.getNamedQuery("findImageSourceByFolder")
					.setLong("folderId", folder.getId())
					.setMaxResults(maxCount);
			
						
			result = q.list();
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<ImageSource> getImageSourceByReaded(long accountId, int maxCount) {

		Collection<ImageSource> result = null;
		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();

		try {

			Query q = session
					.getNamedQuery("findImageSourceByReaded")
					.setLong("accountId", accountId)
					.setMaxResults(maxCount);
			
						
			result = q.list();
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		return result;
	}


	@Override
	public void cropImageSource(Post post, ImageSource imageSource, String crop) {
		
		JSONObject object = JSONObject.fromObject(crop);

		File dir = new File(conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + "/" + post.getId());
		if(!dir.isDirectory()) dir.mkdirs();
		
		File tFile = new File(conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + "/" + post.getId() + "/source");

		Session session = sessionFactory.getCurrentSession();
		session.getTransaction().begin();
		
		try {
			File file = new File(conf.get("imageSource.savePath") + "/" + imageSource.getStoragePath() + "/" + imageSource.getId() + "/source");
			BufferedImage img = ImageIO.read(file);
	
	        BufferedImage targetImage = new BufferedImage(object.getInt("w"), object.getInt("h"), BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics2D = targetImage.createGraphics();
	        graphics2D.setBackground(Color.WHITE);
	        graphics2D.setPaint(Color.WHITE);
	        graphics2D.fillRect(0, 0, object.getInt("w"), object.getInt("h"));
	        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        graphics2D.drawImage(img, 0 - object.getInt("x"), 0 - object.getInt("y"), img.getWidth(), img.getHeight(), null);
			ImageIO.write(targetImage, "jpeg", tFile);
			
			post.setImageWidth(object.getInt("w"));
			post.setImageHeight(object.getInt("h"));
			session.merge(post);
			session.getTransaction().commit();
		}
		catch(Exception ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
		}
	}

}
