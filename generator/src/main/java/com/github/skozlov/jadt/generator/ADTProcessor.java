package com.github.skozlov.jadt.generator;

import com.google.auto.service.AutoService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.lang.model.element.ElementKind.PACKAGE;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Generates basic ADT interfaces from descriptor classes and interfaces annotated with {@link ADT}.
 */
@SupportedAnnotationTypes("com.github.skozlov.jadt.generator.ADT")
@SupportedSourceVersion(RELEASE_8)
@AutoService(Processor.class)
public class ADTProcessor extends AbstractProcessor {
	private static final VelocityEngine VELOCITY = new VelocityEngine();

	static {
		VELOCITY.setProperty(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
		VELOCITY.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
		VELOCITY.init();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
		if (environment.processingOver() || environment.errorRaised()){
			return false;
		}
		annotations.forEach(annotation -> environment.getElementsAnnotatedWith(annotation).forEach(this::generateFor));
		return true;
	}

	private void generateFor(Element element){
		try {
			ADT settings = element.getAnnotation(ADT.class);
			int subtypeNumberMin = settings.subtypeNumberMin();
			if (subtypeNumberMin < 1){
				processingEnv.getMessager().printMessage(ERROR, "Subtype number should be at least 1", element);
				return;
			}
			int subtypeNumberMax = settings.subtypeNumberMax();
			if (subtypeNumberMax < subtypeNumberMin){
				processingEnv.getMessager().printMessage(
					ERROR,
					String.format(
						"Max subtype number (%d) is less than min subtype number (%d)",
						subtypeNumberMax,
						subtypeNumberMin
					),
					element
				);
				return;
			}
			for (int subtypeNumber = subtypeNumberMin; subtypeNumber <= subtypeNumberMax; subtypeNumber++){
				String className = String.format(settings.classNameFormat(), subtypeNumber);
				String packageName = ADT.CURRENT_PACKAGE.equals(settings.package_())
					? getPackageName(element)
					: settings.package_();
				generate(subtypeNumber, className, packageName);
			}
		} catch (Exception e){
			e.printStackTrace();
			processingEnv.getMessager().printMessage(ERROR, e.getMessage(), element);
		}
	}

	private void generate(int subtypeNumber, String className, String packageName) throws IOException {
		Context velocityContext = createVelocityContext(subtypeNumber, className, packageName);
		String classFullName = packageName.isEmpty() ? className : String.format("%s.%s", packageName, className);
		try (InputStream templateStream = getClass().getClassLoader().getResourceAsStream("ADT.vm")){
			//noinspection ConstantConditions
			try (Reader templateReader = new InputStreamReader(templateStream, StandardCharsets.UTF_8)){
				try (Writer writer = processingEnv.getFiler().createSourceFile(classFullName).openWriter()){
					VELOCITY.evaluate(velocityContext, writer, classFullName, templateReader);
				}
			}
		}
	}

	private static Context createVelocityContext(int subtypeNumber, String className, String packageName){
		Map<String, Object> params = new HashMap<>();
		params.put("subtypeNumber", subtypeNumber);
		params.put("className", className);
		params.put("packageName", packageName);
		return new VelocityContext(params);
	}

	private static String getPackageName(Element element){
		Element enclosing = element;
		while (!PACKAGE.equals(enclosing.getKind())){
			enclosing = enclosing.getEnclosingElement();
		}
		return ((PackageElement) enclosing).getQualifiedName().toString();
	}
}
