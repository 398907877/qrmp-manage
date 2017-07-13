package com.yanyan.core.jcaptcha;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.GlyphsPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.GlyphsVisitors;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.OverlapGlyphsUsingShapeVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateAllToRandomPointVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateGlyphsVerticalRandomVisitor;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.ImageFilter;

/**
 * from https://code.google.com/p/musicvalley/source/browse/trunk/musicvalley/doc/springSecurity/springSecurityIII/src/main/java/com/spring/security/jcaptcha/GMailEngine.java?spec=svn447&r=447
 * JCaptcha验证码图片生成引擎, 仿照JCaptcha2.0编写类似GMail验证码的样式.
 * User: Saintcy
 * Date: 2016/3/27
 * Time: 23:29
 */
public class GMailImageCaptchaEngine extends ListImageCaptchaEngine {
    private static final String NUMERIC_CHARS = "23456789";// No numeric 0

    private static final String UPPER_ASCII_CHARS = "ABCDEFGHJKLMNPQRSTXYZ";// No upper O I

    // we dont use the lower characters because it cause dificult in some case,
    // so that
    // we must always UPPERCASE the input from user (currently in
    // OnlineUserImpl)
    private static final String LOWER_ASCII_CHARS = "abcdefghjkmnpqrstxyz";// No lower o i

    @Override
    protected void buildInitialFactories() {

        // 图片和字体大小设置
        int minWordLength = 4;
        int maxWordLength = 4;
        int fontSize = 32;
        int imageWidth = 100;
        int imageHeight = 36;

        WordGenerator dictionaryWords = new RandomWordGenerator(NUMERIC_CHARS + UPPER_ASCII_CHARS);

        // word2image components
        /*TextPaster randomPaster = new DecoratedRandomTextPaster(minWordLength,
                maxWordLength, new RandomListColorGenerator(new Color[]{
                new Color(23, 170, 27), new Color(220, 34, 11),
                new Color(23, 67, 172)}), new TextDecorator[]{});*/
        TextPaster randomPaster = new GlyphsPaster(minWordLength, maxWordLength,
                new RandomListColorGenerator(
                        new Color[]{
                                new Color(23, 170, 27),
                                new Color(220, 34, 11),
                                new Color(23, 67, 172)})
                , new GlyphsVisitors[]{
                new TranslateGlyphsVerticalRandomVisitor(1),
                // new RotateGlyphsRandomVisitor(Math.PI/32),
                new OverlapGlyphsUsingShapeVisitor(3),
                new TranslateAllToRandomPointVisitor()
                //,

                //
        });
        BackgroundGenerator background = new UniColorBackgroundGenerator(
                imageWidth, imageHeight, Color.white);
        FontGenerator font = new RandomFontGenerator(fontSize, fontSize,
                new Font[]{new Font("nyala", Font.BOLD, fontSize),
                        new Font("Bell MT", Font.PLAIN, fontSize),
                        new Font("Credit valley", Font.BOLD, fontSize)});

        ImageDeformation postDef = new ImageDeformationByFilters(
                new ImageFilter[]{});
        ImageDeformation backDef = new ImageDeformationByFilters(
                new ImageFilter[]{});
        ImageDeformation textDef = new ImageDeformationByFilters(
                new ImageFilter[]{});

        WordToImage word2image = new DeformedComposedWordToImage(font,
                background, randomPaster, backDef, textDef, postDef);

        addFactory(new GimpyFactory(dictionaryWords, word2image));
    }

}