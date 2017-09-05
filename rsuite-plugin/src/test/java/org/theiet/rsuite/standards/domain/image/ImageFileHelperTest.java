package org.theiet.rsuite.standards.domain.image;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImageFileHelperTest {

	@Test
	public void test_getBaseImageFileName_from_fileWithVariantSuffix(){
		String baseImageFileName = ImageFileHelper.getBaseImageFileName("testImage_thumbnail2.png");		
		assertEquals("testImage", baseImageFileName);		
	}
	
	@Test
	public void test_getBaseImageFileName_from_epsFile(){
		String baseImageFileName = ImageFileHelper.getBaseImageFileName("testImage.eps");		
		assertEquals("testImage", baseImageFileName);		
	}
	
	@Test
	public void test_getImageFileSuffix_from_epsFile(){
		String suffix = ImageFileHelper.getImageFileSuffix("testImage.eps");	
		assertEquals("", suffix);		
	}

	@Test
	public void test_getImageFileSuffix_from_thumbnail2(){
		String suffix = ImageFileHelper.getImageFileSuffix("testImage_thumbnail2.png");
		assertEquals("thumbnail2", suffix);		
	}
	
	@Test
	public void test_getImageFileSuffix_from_eps(){
		String suffix = ImageFileHelper.getImageFileSuffix("testImage_.eps");
		assertEquals("", suffix);	
	}

	
	@Test
	public void test_getImageFilePrefix_from_noPrefixFile(){
		String prefix = ImageFileHelper.getImageFilePrefix("testImage.eps");
		assertEquals("", prefix);
	}
	
	@Test
	public void test_getImageFilePrefix_from_prefixFile(){
		String prefix = ImageFileHelper.getImageFilePrefix("wre_testImage_.eps");
		assertEquals("wre", prefix);
		
		
	}
	
	@Test
	public void test_getMainWebVariantFileName(){
		String mainWebVariantFileName = ImageFileHelper.getMainWebVariantFileName("wre_testImage");
		assertEquals("wre_testImage_mainWeb.png", mainWebVariantFileName);		
	}
	
	@Test
	public void test_getMasterVariantFileName(){
		String filename = ImageFileHelper.getMasterVariantFileName("wre_testImage");		
		assertEquals("wre_testImage.eps", filename);		
	}
	
	@Test
	public void test_getMimeTypeFromImageFileName_png(){
		String mimeType = ImageFileHelper.getMimeTypeFromImageFileName("wre_testImage.png");		
		assertEquals("image/png", mimeType);		
	}
	
	@Test
	public void test_getMimeTypeFromImageFileName_eps(){
		String mimeType = ImageFileHelper.getMimeTypeFromImageFileName("wre_testImage.eps");		
		assertEquals("image/eps", mimeType);		
	}
	
	@Test
	public void test_getThumbnail1VariantFileName(){
		String filename = ImageFileHelper.getThumbnail1VariantFileName("wre_testImage");		
		assertEquals("wre_testImage_thumbnail1.png", filename);
	}
	
	@Test
	public void test_isThumbnail2VariantFile_epsFile(){
		assertFalse(ImageFileHelper.isThumbnail2VariantFile("wre_testImage.eps"));		
		
	}
	
	@Test
	public void test_isThumbnail2VariantFile_thumbnail1(){
		assertFalse(ImageFileHelper.isThumbnail2VariantFile("wre_testImage_thumbnail1.png"));				
	}
	
	@Test
	public void test_isThumbnail2VariantFile_thumbnail2(){
		assertTrue(ImageFileHelper.isThumbnail2VariantFile("wre_testImage_thumbnail2.png"));
	}
	
	@Test
	public void test_isMasterFile_thumbnail2(){
		assertFalse(ImageFileHelper.isMasterFile("wre_testImage_thumbnail2.png"));
	}
	
	@Test
	public void test_isMasterFile_eps(){
		assertTrue(ImageFileHelper.isMasterFile("wre_testImage.eps"));
	}
	
	@Test
	public void test_isMasterFile_tiff(){
		assertTrue(ImageFileHelper.isMasterFile("wre_testImage.tiff"));
	}
	
	
	
}
