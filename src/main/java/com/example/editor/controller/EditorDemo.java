package com.example.editor.controller;

import com.example.util.JsonUtil;
import com.example.util.MapUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@ComponentScan
@Configuration
@RestController
@RequestMapping("/editor")
public class EditorDemo {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @RequestMapping("/demo")
    public void runJava(String code, String name, HttpServletResponse response, HttpServletRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {


        JsonUtil.toJSON(doJava(code,name),response);
    }

    public Map doJava(String code,String name) throws IOException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Map map1,map0 = null;
        String message = null;

        //构建java源文件
        String javaFileName = name + ".java";

        StringBuilder builder = new StringBuilder(100);
        builder.append(code);

        Resource resource = new ClassPathResource("/javacode");
        //将字符串写入到文件
        File javaDir = new File(ResourceUtils.getURL("classpath:").getPath() + "/javaCode");
        if (!javaDir.exists()) {
            javaDir.mkdirs();
        }
        File javaFile = new File(javaDir, javaFileName);

        Writer out = new FileWriter(javaFile);
        out.write("package javaCode; \r\n");
        out.write(builder.toString());
        out.close();

        //获取编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //创建诊断信息监听器, 用于诊断信息
        DiagnosticCollector<JavaFileObject> diagnosticListeners = new DiagnosticCollector<>();

        //获取FileManager
        StandardJavaFileManager javaFileManager = compiler.getStandardFileManager(diagnosticListeners, null, null);
        Iterable it = javaFileManager.getJavaFileObjects(javaFile);
        File distDir = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!distDir.exists()) {
            distDir.mkdir();
        }

        //生成编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(
                null, javaFileManager, diagnosticListeners,
                Arrays.asList("-d", distDir.getAbsolutePath()), null, it
        );

        //执行编译任务
        boolean bool = task.call();

        //输出诊断信息
        for(Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListeners.getDiagnostics()) {
            //编译诊(错误)断信息
            message = "Error on line" + diagnostic.getLineNumber();
            map0 = MapUtil.toMap(0,message,null);
        }

        //关闭FileManager
        javaFileManager.close();

        //动态执行
        String className = ResourceUtils.getURL("classpath:").getPath()+"/com/example/".getClass().getName();
        Class klass = Class.forName("javaCode."+name);
        Method method = klass.getDeclaredMethod("main", String[].class);
        Object object = method.invoke(klass, new String[]{null});
        map1 = MapUtil.toMap(1,null,object);

        javaFile.delete();

        if (bool){
            return map1;

        } else {
            return map0;
        }
    }
}
