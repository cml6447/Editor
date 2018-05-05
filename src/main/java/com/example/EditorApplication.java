package com.example;

import com.example.editor.controller.EditorDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class EditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EditorApplication.class, args);
		EditorDemo editorDemo = new EditorDemo();
		String code = "public class demo{\n" +
				"\tpublic static void main(String args[]){\n" +
				"\t\t//body\n" +
				"\t\tSystem.out.println(\"hello world\");\n" +
				"\t}\n" +
				"}";
		try {
			editorDemo.doJava(code,"demo");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
