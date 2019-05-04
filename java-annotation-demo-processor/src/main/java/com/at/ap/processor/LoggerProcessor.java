package com.at.ap.processor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
//import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.at.ap.annotation.Logger;
//import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

//@AutoService(Processor.class)
public class LoggerProcessor extends AbstractProcessor{

    /**
     * <p>This method will be run whenever new files are generated
     * </p>
     * @param annotations the Logger.class
     * @param roundEnv 
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Logger.class);
        
        if(elements == null || elements.size() <= 0) {
            return true;
        }
        
        final Elements elementUtils = processingEnv.getElementUtils();
        final Filer filer = processingEnv.getFiler();
        final Messager messager = processingEnv.getMessager();
        
        for(Element element : elements) {
            if(element.getKind() != ElementKind.INTERFACE) {
                messager.printMessage(Kind.ERROR, "The annotation could only be applied to interface", element);
                return false;
            }
            TypeElement typeElement = (TypeElement)element;
            ClassName className = ClassName.get(typeElement);
            messager.printMessage(Kind.NOTE, "className: '" + className + "'");
            
            String newClassName = className.simpleName() + "Logger";
            messager.printMessage(Kind.NOTE, "newClassName: '" + newClassName + "'");
            
            MethodSpec methodSpec = 
                    MethodSpec
                        .methodBuilder("logMessage")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, "message")
                        .beginControlFlow("if(message != null)")
                        .addStatement("log.info(\"message: '{}'\", message)")
                        .nextControlFlow("else")
                        .addStatement("log.info(\"defaultMessage: '{}'\", defaultMessage)")
                        .endControlFlow()
                        .build()
                        ;
            FieldSpec fieldSpecStaticLog =
                    FieldSpec
                        .builder(org.slf4j.Logger.class, "log", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$T.getLogger($L.class)", org.slf4j.LoggerFactory.class, newClassName)
                        .build()
                        ;
            FieldSpec fieldSpecDefaultMessage =
                    FieldSpec
                        .builder(String.class, "defaultMessage", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "DefaultLogMessage")
                        .build()
                        ;
            TypeSpec typeSpec =
                    TypeSpec
                        .classBuilder(newClassName)
                        .addModifiers(Modifier.PUBLIC)
                        .addField(fieldSpecStaticLog)
                        .addField(fieldSpecDefaultMessage)
                        .addMethod(methodSpec)
                        .build()
                        ;
            JavaFile javaFile = 
                    JavaFile
                        .builder(className.packageName(), typeSpec)
                        .build()
                        ;
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Kind.ERROR, "javaFile.writeTo(filer) failed.", element);
                return false;
            }
        }
        
        return true;
    }
    @Override
    public Set<String> getSupportedAnnotationTypes(){
        Set<String> supportedAnnotationTypes =  new HashSet<String>();
        supportedAnnotationTypes.add(Logger.class.getCanonicalName());
        return supportedAnnotationTypes;
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
    
}
