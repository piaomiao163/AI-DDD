package com.piaomiao.web.sys;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GCPractice {
    // 模拟缓存：无界增长（内存泄漏）
    private static List<byte[]> cache = new ArrayList<>();
    private static AtomicLong counter = new AtomicLong();

    public static void main(String[] args) throws Exception {
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        System.out.println("程序启动，PID: " + pid);
        System.out.println("按回车开始模拟...");
        System.in.read();

        // 模拟三种问题场景，可以注释/取消注释来切换

        // 场景1：频繁 Minor GC（循环内大量临时对象）
        // scenario1();

        // 场景2：内存泄漏（无界缓存）
        // scenario2();

        // 场景3：大对象直接进老年代
        scenario3();
    }

    // 场景1：频繁 Minor GC - 每秒分配大量临时对象
    static void scenario1() throws Exception {
        while (true) {
            for (int i = 0; i < 1000; i++) {
                byte[] temp = new byte[1024 * 10]; // 每次 10KB
                counter.incrementAndGet();
            }
            Thread.sleep(10);
            if (counter.get() % 100000 == 0) {
                System.out.println("已分配: " + counter.get() + " 个对象");
            }
        }
    }

    // 场景2：内存泄漏 - 静态 List 无限增长
    static void scenario2() throws Exception {
        while (true) {
            cache.add(new byte[1024 * 100]); // 每次 100KB
            counter.incrementAndGet();
            Thread.sleep(10);
            if (counter.get() % 1000 == 0) {
                System.out.println("缓存大小: " + cache.size() + ", 已用内存: "
                        + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB");
            }
        }
    }

    // 场景3：大对象直接进老年代
    static void scenario3() throws Exception {
        while (true) {
            // 分配超过 Region 一半的大对象（G1 下 > 512KB 可能直接进 Humongous Region）
            byte[] large = new byte[1024 * 1024 * 2]; // 2MB 大对象
            counter.incrementAndGet();
            Thread.sleep(50);
            if (counter.get() % 100 == 0) {
                System.out.println("已分配大对象: " + counter.get());
                // 让部分对象变成垃圾
                if (counter.get() % 200 == 0) {
                    System.gc();
                }
            }
        }
    }
}