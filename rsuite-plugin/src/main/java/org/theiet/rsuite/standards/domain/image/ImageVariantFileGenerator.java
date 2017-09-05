package org.theiet.rsuite.standards.domain.image;

import static org.theiet.rsuite.standards.StandardsBooksConstans.*;
import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.*;
import static org.theiet.rsuite.standards.domain.image.ImageVariant.*;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.utils.ImageMagickUtils;

import com.reallysi.rsuite.api.RSuiteException;

class ImageVariantFileGenerator {

    private ImageMagickUtils imageMagickUtils;

    public ImageVariantFileGenerator(File imageMagickHomeFolder) throws RSuiteException {
        imageMagickUtils = ImageMagickUtils.getInstance(imageMagickHomeFolder);
    }

    public void generateWebVariantsFiles(File imagesBaseFolder) throws RSuiteException {

        for (File file : imagesBaseFolder.listFiles()) {

            if (file.isDirectory()) {
                generateWebVariantsFiles(file);
            } else if (isPsdFile(file.getName())) {
                generateVariantsFiles(file);
            }
        }

    }

    protected void generateVariantsFiles(File file) throws RSuiteException {
        createThumbnail1File(file);
        createThumbnail2File(file);
        createMainWebFile(file);
    }

    private void createThumbnail1File(File inputFile) throws RSuiteException {
        String imageMagickParameters = "-resample 150 -adaptive-resize 160";
        generateImageVariantFile(inputFile, THUMBNAIL1, imageMagickParameters);
    }

    private void createThumbnail2File(File inputFile) throws RSuiteException {
        String imageMagickParameters = "-resample 300 -format png -depth 8 -remap netscape:";        
        generateImageVariantFile(inputFile, THUMBNAIL2, imageMagickParameters);
    }

    private void createMainWebFile(File inputFile) throws RSuiteException {
        String imageMagickParameters = "-colorspace RGB  -resample 300";
        generateImageVariantFile(inputFile, MAIN_WEB, imageMagickParameters);
    }

    protected void generateImageVariantFile(File inputFile, ImageVariant variant,
            String imageMagickParameters) throws RSuiteException {
        List<String> parameters = convertToParameterList(imageMagickParameters);

        String baseFileName = FilenameUtils.getBaseName(inputFile.getName());

        File outputFile =
                new File(inputFile.getParentFile(), baseFileName + PREFIX_SEPARATOR
                        + variant.getVariantName() + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PNG);

        imageMagickUtils.convertImage(inputFile, parameters, outputFile, "1");
    }

    private List<String> convertToParameterList(String parameters) {
        String[] split = parameters.split("\\s+");
        return Arrays.asList(split);
    }
}
