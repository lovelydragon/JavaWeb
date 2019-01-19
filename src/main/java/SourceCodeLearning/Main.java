package SourceCodeLearning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class Main {
    @Autowired
    private TestBean testBean;


    public static void main(String[] args) {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
//        TestBean testBean = (TestBean)context.getBean("testBean");
        Main main = new Main();
        main.test();
    }

    public void test(){
        System.out.println(testBean.getName());
        System.out.println(testBean);
    }
}
