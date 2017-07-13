package com.yanyan.web.controller.console;

import com.yanyan.core.util.SigarFactory;
import com.yanyan.core.util.SystemUtils;
import com.yanyan.core.web.DataResponse;
import com.yanyan.data.domain.console.vo.ClassLoadingVo;
import com.yanyan.data.domain.console.vo.DiskVo;
import com.yanyan.data.domain.console.vo.ThreadVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperic.sigar.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 23:35
 */
@Slf4j
@Controller
@RequestMapping("/console/server")
public class ServerController {
    private Map<Long, ThreadVo> threadMap = new ConcurrentHashMap<Long, ThreadVo>();

    @RequestMapping("index")
    public String index() {
        return "/console/server/index";
    }

    @RequestMapping("/sysenv")
    public String sysenv(Model model) {
        //model.addAttribute("ips", SystemUtils.getLocalIPs(true));
        model.addAttribute("osName", SystemUtils.OS_NAME);
        model.addAttribute("osArch", SystemUtils.OS_ARCH);
        model.addAttribute("osVersion", SystemUtils.OS_VERSION);
        try {
            Sigar sigar = SigarFactory.create();
            model.addAttribute("netInfo", sigar.getNetInfo());
            List<String> ips = new ArrayList<String>();
            for (String ni : sigar.getNetInterfaceList()) {
                NetInterfaceConfig config = sigar.getNetInterfaceConfig(ni);
                if (!StringUtils.equals(config.getAddress(), "0.0.0.0") && !StringUtils.equals(config.getAddress(), "127.0.0.1")) {
                    ips.add(config.getAddress());
                }
            }
            model.addAttribute("ips", ips);//得不到IP？
            model.addAttribute("cpus", sigar.getCpuInfoList());
            model.addAttribute("mem", sigar.getMem());
            Map<String, Double> fsMap = new LinkedHashMap<String, Double>();
            for (FileSystem fs : sigar.getFileSystemList()) {
                if (fs.getType() == FileSystem.TYPE_LOCAL_DISK || fs.getType() == FileSystem.TYPE_RAM_DISK || fs.getType() == FileSystem.TYPE_SWAP) {
                    FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                    fsMap.put(fs.getDirName(), usage.getUsePercent());
                }
            }
            model.addAttribute("fsMap", fsMap);
        } catch (Throwable e) {
            log.warn("sigar error", e);
        }
        model.addAttribute("jvm", SystemUtils.getJvm());
        model.addAttribute("classLoading", getClassLoading());
        MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
        model.addAttribute("heapMemory", mm.getHeapMemoryUsage());
        model.addAttribute("nonHeapMemory", mm.getNonHeapMemoryUsage());
        model.addAttribute("objectPendingFinalizationCount", mm.getObjectPendingFinalizationCount());
        model.addAttribute("properties", SystemUtils.getSortedSystemProperties());

        return "/console/server/sysenv";
    }

    private ClassLoadingVo getClassLoading() {
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        ClassLoadingVo vo = new ClassLoadingVo();

        //从JVM启动至今共加载类
        vo.setTotalLoadedClassCount(classLoadingMXBean.getTotalLoadedClassCount());
        //从JVM启动至今共卸载类
        vo.setUnloadedClassCount(classLoadingMXBean.getUnloadedClassCount());
        //当前共加载类
        vo.setLoadedClassCount((long) classLoadingMXBean.getLoadedClassCount());

        return vo;
    }

    @RequestMapping("/net")
    public String net(Model model) {
        List<NetInterfaceConfig> networks = new ArrayList<NetInterfaceConfig>();
        model.addAttribute("networks", networks);
        Sigar sigar = SigarFactory.create();
        try {
            for (String s : sigar.getNetInterfaceList()) {
                networks.add(sigar.getNetInterfaceConfig(s));
            }
        } catch (Throwable e) {
            log.warn("sigar error", e);
        }

        return "/console/server/net";
    }

    @RequestMapping("/thread")
    public String thread(Model model) {
        model.addAttribute("threads", getThreads());

        return "/console/server/thread";
    }

    @RequestMapping(value = "/threads", method = RequestMethod.POST)
    @ResponseBody
    public Model threads() {
        return DataResponse.success("threads", getThreads());
    }

    @RequestMapping("/thread_detail")
    public String threadDump() {
        return "/console/server/thread_detail";
    }

    private List<ThreadVo> getThreads() {
        ThreadMXBean tm = ManagementFactory.getThreadMXBean();

        long[] threadIds = tm.getAllThreadIds();
        tm.setThreadCpuTimeEnabled(true);
        ThreadInfo[] threadInfos = tm.getThreadInfo(threadIds);

        List<ThreadVo> threads = new ArrayList<ThreadVo>();

        for (int i = 0; i < threadInfos.length; i++) {
            ThreadInfo threadInfo = threadInfos[i];
            ThreadVo thread = new ThreadVo();
            thread.setId(threadInfo.getThreadId());
            thread.setName(threadInfo.getThreadName());
            thread.setState(threadInfo.getThreadState().name());
            thread.setCpuTime(tm.getThreadCpuTime(threadInfo.getThreadId()));
            ThreadVo oldThread = threadMap.get(threadInfo.getThreadId());
            thread.setLastTime(System.nanoTime());
            if (oldThread == null) {
                thread.setStartTime(System.nanoTime());
            } else {
                thread.setCpuPercent(Math.floor((thread.getCpuTime() - oldThread.getCpuTime()) / (thread.getLastTime() - oldThread.getLastTime())));
            }
            threads.add(thread);
            threadMap.put(thread.getId(), thread);
        }

        Collections.sort(threads, new Comparator<ThreadVo>() {
            public int compare(ThreadVo o1, ThreadVo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return threads;
    }

    @RequestMapping("/cpu")
    public String cpu(Model model) {
        return "/console/server/cpu";
    }

    @RequestMapping(value = "/cpu", method = RequestMethod.POST)
    @ResponseBody
    public Model cpu() {
        Sigar sigar = SigarFactory.create();
        try {
            return DataResponse.success("cpu", sigar.getCpu());
        } catch (SigarException e) {
            log.warn("sigar error", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/mem")
    public String mem(Model model) {
        return "/console/server/mem";
    }

    @RequestMapping(value = "/mem", method = RequestMethod.POST)
    @ResponseBody
    public Model mem() {
        Sigar sigar = SigarFactory.create();
        try {
            return DataResponse.success("mem", sigar.getMem());
        } catch (SigarException e) {
            log.warn("sigar error", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/disk")
    public String disk(Model model) {
        return "/console/server/disk";
    }

    @RequestMapping(value = "/disk", method = RequestMethod.POST)
    @ResponseBody
    public Model disk() {
        Sigar sigar = SigarFactory.create();
        try {
            List<DiskVo> disks = new ArrayList<DiskVo>();
            for (FileSystem fs : sigar.getFileSystemList()) {
                if (fs.getType() == FileSystem.TYPE_LOCAL_DISK || fs.getType() == FileSystem.TYPE_RAM_DISK || fs.getType() == FileSystem.TYPE_SWAP) {
                    DiskVo disk = new DiskVo();
                    disk.setName(fs.getDirName());
                    disk.setType(fs.getTypeName());
                    disk.setUsage(sigar.getFileSystemUsage(fs.getDirName()));
                    disks.add(disk);
                }
            }

            return DataResponse.success("disks", disks);
        } catch (SigarException e) {
            log.warn("sigar error", e);
            return DataResponse.failure(e);
        }
    }
}
