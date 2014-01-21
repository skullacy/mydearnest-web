package com.osquare.mydearnest.util.image.similarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;

import com.jhlabs.image.RescaleFilter;

public class ImageSimilartyFinder extends JFrame{
	
	//이미지 표준RGB코드를 2차원 평균칼라로 나타낸다.
	private Color[][] signature;
	//이미지 기본사이즈 Const
	private static final int baseSize = 300;
	
	//이미지를 가져오는곳(테스트용)
	private static final String basePath = "/Users/skullacy/git/mydearnest-web/mydearnest-web/src/main/webapp/images/test";
	
	//생성자
	public ImageSimilartyFinder(File reference) throws IOException {
		// GUI 생성
		super("Image SimilartyFinder");
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		RenderedImage ref = rescale(ImageIO.read(reference));
//		cp.add(new DisplayJAI(ref), BorderLayout.WEST);
		
		
	}
	
	//이미지 300*300사이즈로 리사이징하기
	private RenderedImage rescale(RenderedImage i) {
		float scaleW = ((float) baseSize) / i.getWidth();
		float scaleH = ((float) baseSize) / i.getHeight();
		
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(i);
		pb.add(scaleW);
		pb.add(scaleH);
		pb.add(0.0F);
		pb.add(0.0F);
		pb.add(new InterpolationNearest());
		
		
		
		return JAI.create("scale", pb);
		
	}
	
}
