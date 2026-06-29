package com.piaomiao.web.sys;

import java.util.ArrayList;
import java.util.List;

public class JVMDemo {

    private static void printGcInfo() {
        // 替代方案2：使用 StringBuffer/StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("=");
        }
        System.out.println(sb);
    }

    // 场景1：频繁 Minor GC（Eden 区太小）
    static void scenario1() throws Exception {
        System.out.println("场景1：频繁 Minor GC");
        System.out.println("现象：每秒多次 GC，YGC 增长极快");
        printGcInfo();

        List<byte[]> list = new ArrayList<>();
        while (true) {
            for (int i = 0; i < 1000; i++) {
                list.add(new byte[1024]); // 每次 1KB
                if (list.size() > 10000) {
                    list.clear(); // 控制内存增长
                }
            }
            Thread.sleep(10);
        }
    }

    // 场景2：内存泄漏（老年代持续增长）
    static void scenario2() throws Exception {
        System.out.println("场景2：内存泄漏");
        System.out.println("现象：老年代使用率持续上升，最终 Full GC");
        printGcInfo();

        List<byte[]> leakList = new ArrayList<>();
        while (true) {
            leakList.add(new byte[1024 * 100]); // 每次 100KB，永不释放
            Thread.sleep(10);
            if (leakList.size() % 100 == 0) {
                System.out.println("当前泄漏量: " + leakList.size() * 100 / 1024 + " MB");
            }
        }
    }

    // 场景3：大对象直接进入老年代
    static void scenario3() throws Exception {
        System.out.println("场景3：大对象直接进入老年代");
        System.out.println("现象：老年代快速增长，YGC 无法回收");
        printGcInfo();

        while (true) {
            byte[] largeObject = new byte[1024 * 1024 * 2]; // 2MB 大对象
            Thread.sleep(50);
        }
    }

    // 场景4：对象晋升过快（Survivor 区太小）
    static void scenario4() throws Exception {
        System.out.println("场景4：对象晋升过快");
        System.out.println("现象：每次 YGC 都有大量对象进入老年代");
        printGcInfo();

        List<byte[]> list = new ArrayList<>();
        while (true) {
            // 让对象存活时间稍长，超过 Survivor 区容量
            for (int i = 0; i < 500; i++) {
                list.add(new byte[1024 * 50]); // 50KB
            }
            Thread.sleep(100);
            list.clear(); // 部分存活
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("JVM 调优模拟程序");
        System.out.println("请选择场景: 1-频繁GC, 2-内存泄漏, 3-大对象, 4-晋升过快");

        int choice = System.in.read() - '0';
        System.in.read(); // 消耗换行符

        switch (choice) {
            case 1: scenario1(); break;
            case 2: scenario2(); break;
            case 3: scenario3(); break;
            case 4: scenario4(); break;
            default: System.out.println("输入错误");
        }
    }
}
