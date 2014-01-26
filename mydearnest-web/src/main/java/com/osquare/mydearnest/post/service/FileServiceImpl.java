package com.osquare.mydearnest.post.service;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
import javax.imageio.stream.ImageInputStream;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
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
import com.osquare.mydearnest.util.amazon.MdnAmazonManager;
import com.sun.media.sound.Toolkit;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Resource private SessionFactory sessionFactory;
	
	@Autowired private PropertiesManager conf;
	@Autowired private MdnAmazonManager mdnAmazonManager;

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
            
            String rgb = getAveColor(tmpFile);
            System.out.println("RGB_COLOR: "+rgb);
            result.setAveColor(rgb);
            
			
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
	
	public void setImageFiletoAmazon(ImageSource imageSource) {
		
	}
	
	public ImageSourceFile getImageFromAmazon(ImageSource imageSource, String type) {
		return getImageFromAmazon(imageSource, type, null, null, null, null);
	}
	
	public ImageSourceFile getImageFromAmazon(ImageSource imageSource, String type, Long width, Long height, String thumbnail_type, Long postId) {
		if (imageSource == null) return null;
		
		//type에 따라 선택되어야할 파일명
		String fileName = null;
		if("source".equals(type)) fileName = "source";
		else if("thumbnail".equals(type)) fileName = width + "x" + height + "_" + thumbnail_type + ".jpg";
		else return null;
		
		//S3에 저장되는 경로 조합.
		String filePath = imageSource.getStoragePath() + "/" + imageSource.getId();
		String fileLocation = filePath + "/" + fileName;
		
		//필요한 변수들
		ImageSourceFile result = null;
		InputStream sourceImg = null;
		
		//CloudFront를 통해서 이미지 가져오기 시도.
		try {
			result = new ImageSourceFile();
			
			//CloudFront에서 파일을 못찾은경우, Catch로 Exception 발생시킨다.
			URL sourceURL = new URL(conf.get("amazon.fs.serverUrl") + "/" + fileLocation);
			sourceImg = sourceURL.openStream();
			
			//파일 크기 구하기
			URLConnection conn = sourceURL.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			
			long fileSize = (long) httpConn.getContentLength();
			
			//ImageSourceFile 오브젝트에 담는다.
			result.setFileLength(fileSize);
			result.setInputStream(sourceImg);
			
		}
		catch (IOException e) {
			//CloudFront에서 파일을 못찾은 경우, S3에서 가져와본다.
			try {
				//아마존 S3객체 생성
				AmazonS3Client fileStorageServer = mdnAmazonManager.getAmazonS3();
				
				//AmazonS3 에서 이미지 가져오기.
				S3Object object = fileStorageServer.getObject(new GetObjectRequest(conf.get("amazon.fs.bucketName"), fileLocation));
				
				sourceImg = object.getObjectContent();
				
				//ImageSourceFile 오브젝트에 담는다.
				result.setFileLength(object.getObjectMetadata().getContentLength());
				result.setInputStream(sourceImg);
			} 
			catch(AmazonS3Exception s3e) {
				//S3에서도 파일을 못찾은 경우, 에러발생.
				//추후 기본적으로 불려질 이미지를 넣어도 괜찮을듯 하다.
				System.out.println("FILE NOT FOUND IN S3 TOO : " + s3e.getMessage());
				
				result = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource) {
		return getImageFromAmazon(imageSource, "source");
	}

	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type) {
		return this.getSourceFile(imageSource, width, height, thumbnail_type, null);
	}
	
	@Override
	public ImageSourceFile getSourceFile(ImageSource imageSource, Long width, Long height, String thumbnail_type, Long postId) {
		
		//일단 썸네일을 가져와본다.
		ImageSourceFile target = getImageFromAmazon(imageSource, "thumbnail", width, height, thumbnail_type, postId);
		ImageSourceFile result = new ImageSourceFile();
		if(target == null) {
			//썸네일이 없다면 원본 이미지를 가져와본다.
			target = getImageFromAmazon(imageSource, "source");
			
			//원본 이미지마저 없다면 요청이 잘못되거나 에러발생. null 리턴해준다.
			if(target == null) return null;
			else {
				//원본이미지만 있는상황이므로 썸네일을 생성한 후, S3에 저장.
				
				//S3에 저장되는 경로 조합.
				String file_location = imageSource.getStoragePath() + "/" + imageSource.getId();
				//Thumbnail 파일명 조합.
				String thumbFileName = width + "x" + height + "_" + thumbnail_type + ".jpg";
				
				//s3패스 + thumb파일명
				final String thumbFileLoc = file_location + "/" + thumbFileName;
				
				try {
					BufferedImage sourceImg = ImageIO.read(target.getInputStream());
					BufferedImage resultImg = null;
					
					//필요한 변수들
					double width_per;
			        double height_per;
			        double per = 0;
			        double resize_width = width;
			        double resize_height = height;
			        
			        //리사이즈 비율 설정
			        if (resize_width > 0 && sourceImg.getWidth() >= resize_width) width_per = resize_width / sourceImg.getWidth();
			        else width_per = 1;
			
			        if (resize_height > 0 && sourceImg.getHeight() >= resize_height) height_per = resize_height / sourceImg.getHeight();
			        else height_per = 1;
			        
			        //비율에 맞춰서 리사이즈 하는경우 최종 리사이즈 크기 결정
			        if (thumbnail_type.equals("ratio"))
			        {
			            if (width_per > height_per) per = height_per;
			            else per = width_per;
			            resize_width = ((double)sourceImg.getWidth() * per);
			            resize_height = ((double)sourceImg.getHeight() * per);
			        }
			        //crop인 경우 최종 리사이즈 크기 결정
			        else
			        {
			            if (width_per < height_per) per = height_per;
			            else per = width_per;
			        }
			
			        if (per == 0) per = 1;
			
			        int _x = 0;
			        int _y = 0;
			
			        int new_width = (int)(sourceImg.getWidth() * per);
			        int new_height = (int)(sourceImg.getHeight() * per);
			
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
			        
			        //최종 저장될 스트림.
			        InputStream destIs = null;
			        		
			        //ByteArrayOutputStream 의 toByteArray의 메모리 에러 방지 위해 미리 오버라이드 해준다.
			        final ByteArrayOutputStream output = new ByteArrayOutputStream() {
			            @Override
			            public synchronized byte[] toByteArray() {
			                return this.buf;
			            }
			        };
			        
			        //소스 이미지의 타입을 분석해서 BufferedImage에 알맞는 캔버스 타입을 설정한다.
			        //현재 0을 제외한 나머지 타입은 RGB로 통일시켰는데, PNG와 GIF 모두 제대로 출력되나 테스트 해보아야함.
			        int type = sourceImg.getType() == 0? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			        
			        resultImg = new BufferedImage((int) resize_width, (int) resize_height, type);
			    	Graphics2D g = resultImg.createGraphics();
			    	//알파값 보존하는 알고리즘
//			    	if (true) {
//			    		g.setComposite(AlphaComposite.Src);
//			    	}
			    	g.drawImage(sourceImg, _x - 2, _y - 2, (int)new_width + 4, (int)new_height + 4, null); 
			    	g.dispose();
			        
			    	ImageIO.write(resultImg, "jpg", output);
			    	
			    	final long fileSize = output.size();
			        
			        destIs = new ByteArrayInputStream(output.toByteArray(), 0, (int) fileSize);
			        		
			        result.setFileLength(fileSize);
			        result.setInputStream(destIs);
			        
			        
			        final InputStream uploadIs = destIs;
			        
			        //반응속도를 조금이나마 빠르게하기위해 아마존 업로드는 쓰레드로 돌린다.
			        new Thread(
		                    new Runnable() {
		                        @Override
		                        public void run() {
		                        	//Amazon S3에 생성된 섬네일 저장.
		        			        try {
		        			        	//아마존 SDK가져오기
			        			        AmazonS3Client fileStorageServer = mdnAmazonManager.getAmazonS3();
			        			        
			        			        //메타데이터 입력
			        			        ObjectMetadata meta = new ObjectMetadata();
			        					meta.setContentLength(fileSize);
			        					
			        					//스트림 초기화 : putObject 대비.
			        					uploadIs.reset();
			        					
			        					PutObjectRequest putObjectRequest = new PutObjectRequest(conf.get("amazon.fs.bucketName"), thumbFileLoc, uploadIs, meta);
			        					
			        					//PublicRead권한 설정
			        					putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			        					
			        					//이미지 업로드
			        					fileStorageServer.putObject(putObjectRequest);
		        			        }
		        			        catch(AmazonS3Exception s3e) {
		        			        	s3e.printStackTrace();
		        			        }
		        			        catch(Exception e) {
		        			        	e.printStackTrace();
		        			        }
		        			        finally {
		        			        	Thread.currentThread().interrupt();
		        			        }
		        			        
		                        }
		                    }
		            ).start();
			        
			        
			        
			        return result;
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			result = target;
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
	
	public String getAveColor(File img) {
		
		String rgbHex;
		try {
			BufferedImage bufferedImg = ImageIO.read(img.toURI().toURL());
			
			BufferedImage tdestImg = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D g = tdestImg.createGraphics();
			g.drawImage(bufferedImg, 0, 0, 1, 1, null);
			
	        rgbHex = "#"+Integer.toHexString(tdestImg.getRGB(0, 0)).substring(2);
		}
		catch(Exception e) {
			e.printStackTrace();
			
			rgbHex = "#FFFFFF";
		}
		
		return rgbHex;
        
	}

}
