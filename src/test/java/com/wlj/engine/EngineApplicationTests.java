package com.wlj.engine;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorLong;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class EngineApplicationTests {

    @Test
    void testCompile() {
        String expression = "a-(b-c)>100";
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Map<String, Object> env = new HashMap<>(16);
        env.put("a", 100.3);
        env.put("b", 45);
        env.put("c", -199.100);
        // 执行表达式
        Boolean result = (Boolean) compiledExp.execute(env);
        System.out.println(result);  // false
    }

    @Test
    void testAddFunction() {
        //注册函数
        AviatorEvaluator.addFunction(new AddFunction());
        System.out.println(AviatorEvaluator.execute("add(1, 2)"));           // 3.0
        System.out.println(AviatorEvaluator.execute("add(add(1, 2), 100)")); // 103.0
    }

    @Test
    void testList() {
        final List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(" world");
        final int[] array = new int[3];
        array[1] = 1;
        array[2] = 3;
        final Map<String, Date> map = new HashMap<>();
        map.put("date", new Date());
        Map<String, Object> env = new HashMap<>();
        env.put("list", list);
        env.put("array", array);
        env.put("mmap", map);
        System.out.println(AviatorEvaluator.execute("list[0]+list[1]", env));   // hello world
        System.out.println(AviatorEvaluator.execute("'array[0]+array[1]+array[2]=' + (array[0]+array[1]+array[2])", env));  // array[0]+array[1]+array[2]=4
        System.out.println(AviatorEvaluator.execute("'today is ' + mmap.date ", env));  // today is Wed Feb 24 17:31:45 CST 2016
    }

    @Test
    void testTernaryExpression() {
        System.out.println(AviatorEvaluator.exec("a>0? 'yes':'no'", 1));   // yes
    }

    @Test
    void testRegularExpression() {
        String email = "killme2008@gmail.com";
        Map<String, Object> env = new HashMap<>();
        env.put("email", email);
        String username = (String) AviatorEvaluator.execute("email=~/([\\w0-8]+)@\\w+[\\.\\w+]+/ ? $1 : 'unknow' ", env);
        System.out.println(username); // killme2008
    }

    @Test
    void testNil() {
        AviatorEvaluator.execute("nil == nil");   //true
        AviatorEvaluator.execute(" 3> nil");      //true
        AviatorEvaluator.execute(" true!= nil");  //true
        AviatorEvaluator.execute(" ' '>nil ");    //true
        AviatorEvaluator.execute(" a==nil ");     //true, a 是 null
    }

    @Test
    void testDateCompare() {
        Map<String, Object> env = new HashMap<>(16);
        final Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(date);
        env.put("date", date);
        env.put("dateStr", dateStr);
        Boolean result = (Boolean) AviatorEvaluator.execute("date==dateStr", env);
        System.out.println(result);  // true
        result = (Boolean) AviatorEvaluator.execute("date > '2010-12-20 00:00:00:00' ", env);
        System.out.println(result);  // true
        result = (Boolean) AviatorEvaluator.execute("date < '2200-12-20 00:00:00:00' ", env);
        System.out.println(result);  // true
        result = (Boolean) AviatorEvaluator.execute("date==date ", env);
        System.out.println(result);  // true
    }

    @Test
    void testBigNumber() {
        Object rt = AviatorEvaluator.exec("9223372036854775807100.356M * 2");
        System.out.println(rt + " " + rt.getClass());  // 18446744073709551614200.712 class java.math.BigDecimal
        rt = AviatorEvaluator.exec("92233720368547758074+1000");
        System.out.println(rt + " " + rt.getClass());  // 92233720368547759074 class java.math.BigInteger
        BigInteger a = new BigInteger(String.valueOf(Long.MAX_VALUE) + String.valueOf(Long.MAX_VALUE));
        BigDecimal b = new BigDecimal("3.2");
        BigDecimal c = new BigDecimal("9999.99999");
        rt = AviatorEvaluator.exec("a+10000000000000000000", a);
        System.out.println(rt + " " + rt.getClass());  // 92233720368547758089223372036854775807 class java.math.BigInteger
        rt = AviatorEvaluator.exec("b+c*2", b, c);
        System.out.println(rt + " " + rt.getClass());  // 20003.19998 class java.math.BigDecimal
        rt = AviatorEvaluator.exec("a*b/c", a, b, c);
        System.out.println(rt + " " + rt.getClass());  // 2.951479054745007313280155218459508E+34 class java.math.BigDecimal
    }

    @Test
    void testSeq() {
        Map<String, Object> env = new HashMap<>(16);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(20);
        list.add(10);
        env.put("list", list);
        Object result = AviatorEvaluator.execute("count(list)", env);
        System.out.println(result);  // 3
        result = AviatorEvaluator.execute("reduce(list,+,0)", env);
        System.out.println(result);  // 33
        result = AviatorEvaluator.execute("filter(list,seq.gt(9))", env);
        System.out.println(result);  // [10, 20]
        result = AviatorEvaluator.execute("include(list,10)", env);
        System.out.println(result);  // true
        result = AviatorEvaluator.execute("sort(list)", env);
        System.out.println(result);  // [3, 10, 20]
        AviatorEvaluator.execute("map(list,println)", env);
    }

    @Test
    void testBool() {
        Map<String, Object> env = new HashMap<>(16);
        env.put("a", true);
        env.put("b", false);
        env.put("c", true);
        System.out.println(AviatorEvaluator.execute("a || b || c", env));
        System.out.println(AviatorEvaluator.execute("a && b && c", env));
    }

    @Test
    void testCache() {
        String expression = "a && b && c";
        Expression exp = AviatorEvaluator.getInstance().compile("test", expression, true);
        Map<String, Object> env = new HashMap<>(16);
        env.put("a", true);
        env.put("b", false);
        env.put("c", true);
        System.out.println(exp.execute(env));
    }

    @Test
    void testWoe() {
            String exp = "r_u1_E6_02_01_u1_fql_f1 = double(r_u1_E6_02_01_u1_fql_f1);if(r_u1_E6_02_01_u1_fql_f1 < 1){'-0.45130885495927964'}elsif(1<=r_u1_E6_02_01_u1_fql_f1){'0.13112435081095022'}";
        Map<String, Object> env = new HashMap<>(16);
        env.put("r_u1_E6_02_01_u1_fql_f1", 1);
        System.out.println(AviatorEvaluator.execute(exp, env));
    }
}
