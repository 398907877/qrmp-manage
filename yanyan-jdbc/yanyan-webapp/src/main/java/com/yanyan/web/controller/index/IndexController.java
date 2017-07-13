package com.yanyan.web.controller.index;

import com.google.common.collect.Lists;
import com.yanyan.Configs;
import com.yanyan.core.BusinessException;
import com.yanyan.core.jcaptcha.JCaptcha;
import com.yanyan.core.shiro.UsernamePasswordToken;
import com.yanyan.core.util.*;
import com.yanyan.core.web.DataResponse;
import com.yanyan.data.domain.system.Menu;
import com.yanyan.data.domain.system.RSAPrivateKey;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.vo.FileMeta;
import com.yanyan.data.vo.LoginModel;
import com.yanyan.data.vo.Pinyin;
import com.yanyan.service.system.PrivilegeService;
import com.yanyan.service.system.RSAService;
import com.yanyan.service.system.StaffService;
import com.yanyan.web.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 系统首页
 * User: Saintcy
 * Date: 2016/3/27
 * Time: 23:29
 */
@Controller
@RequestMapping("")
@Slf4j
public class IndexController {
    @Autowired
    private RSAService rsaService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PrivilegeService privilegeService;
    @Resource(name = "webCacheManager")
    private CacheManager cacheManager;

    /**
     * 跳转至登陆页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "style", required = false) String style, Model model, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/index";
        }

        LoginModel login = new LoginModel();
        login.setUsername(SessionUtils.getStaffAccount());
        login.setRememberMe(false);
        model.addAttribute("login", login);
        model.addAttribute("style", style);
        model.addAttribute("isCaptcha", Configs.LOGIN_CAPTCHA);
        return "/login";
    }

    @RequestMapping(value = "/login_status")
    @ResponseBody
    public Model loginStatus(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return DataResponse.success();
        } else {
            response.setStatus(401);
            return DataResponse.failure(DataResponse.ERRCODE_UNAUTHORIZED, "请先登录后再操作！");
        }
    }

    @RequestMapping(value = "/login_key")
    @ResponseBody
    public Model loginKey() {
        try {
            return DataResponse.success("publicKey", rsaService.getPublicKey());
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }


    /**
     * 用户登录操作
     *
     * @return 登录后的地址
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginByForm(@ModelAttribute("login") LoginModel login, HttpServletRequest request, Model model) {
        try {
            String redirectUrl = login(login, request);
            if (StringUtils.isNotEmpty(redirectUrl)) {
                return StringUtils.replaceFirst(redirectUrl, request.getContextPath(), "");
            } else {
                return "/index";
            }
        } catch (Exception e) {
            login.setPassword("");
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "/login";
        }
    }

    /**
     * 用户登录操作
     *
     * @return 登录后的地址
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model loginByData(@RequestBody LoginModel login, HttpServletRequest request) {
        try {
            String redirectUrl = StringUtils.replaceFirst(login(login, request), request.getContextPath(), "");

            Model model = DataResponse.success("redirectUrl", redirectUrl);
            //刷新token(这里为了兼容旧的登录，所以采用返回多个字段方式)
            String token = Hex.encodeToString(PasswordUtils.generateSalt(32));
            //TODO： 需要持久化，否则重启就得不到了
            cacheManager.getCache("web-tokenCache").put(request.getSession().getId(), token);
            model.addAttribute("token", token);
            return model;
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }

    /**
     * 用户登录操作
     *
     * @return 登录后的地址
     */
    private String login(LoginModel login, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();

        if (Configs.LOGIN_CAPTCHA) {//需要验证码登录
            if (StringUtils.isEmpty(login.getCaptcha())) {//安全码方式登录
                if (request.getSession(false) == null) {
                    throw new BusinessException("login.token.not.matched", "安全码错误");
                } else {
                    String id = request.getSession().getId();
                    String cacheToken = cacheManager.getCache("web-tokenCache").get(id, String.class);
                    if (!StringUtils.equals(cacheToken, login.getToken())) {
                        throw new BusinessException("login.token.not.matched", "安全码错误");
                    }
                }
            } else {//验证码方式登录
                if (!JCaptcha.validateResponse(request, StringUtils.upperCase(login.getCaptcha()))) {
                    throw new BusinessException("login.captcha.not.matched", "验证码错误");
                }
            }
        }

        String host = HttpUtils.getClientIp(request);
        RSAPrivateKey privateKey = rsaService.getPrivateKey();
        java.security.interfaces.RSAPrivateKey pk = RSAUtil.generateRSAPrivateKeyFromHex(privateKey.getModulus(), privateKey.getPrivateExponent());
        String plainPassword = PasswordUtils.decryptPasswordWithRSA(pk, login.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(login.getUsername(), plainPassword.toCharArray(), login.isRememberMe(), host, login.getCaptcha(), false);
        try {
            subject.login(token);

            //将登录信息设到会话中
            Staff staff = staffService.getStaffByAccount(login.getUsername());

            SessionUtils.initSession(staff, request);
        } catch (LockedAccountException e) {
            throw new BusinessException("login.account.locked", "该用户禁止登陆");
        } catch (UnknownAccountException e) {
            throw new BusinessException("login.account.unknown", "用户名/密码错误");
        } catch (IncorrectCredentialsException e) {
            throw new BusinessException("login.credentials.incorrect", "用户名/密码错误");
        } catch (ExcessiveAttemptsException e) {
            throw new BusinessException("login.attempts.excessive", "密码重试超出系统限制，请在" + (NumberUtils.toInt(e.getMessage()) / 60) + "分钟后重试。");
        } catch (Exception e) {
            //其他错误，比如锁定，如果想单独处理请单独catch 处理
            log.error("", e);
            throw new BusinessException("login.error", "其他错误:" + e.getMessage());
        }

        /*if (StringUtils.isEmpty(login.getCaptcha())) {//返回新的安全码
            return Hex.encodeToString(PasswordUtils.generateSalt(32));
        } else {*/
        SavedRequest sr = WebUtils.getSavedRequest(request);
        if (sr == null) {
            return "";
        } else {
            return sr.getRequestUrl();
        }
        //}
    }

    /**
     * 用户登出操作
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login?logout";
    }

    /**
     * 生成验证码图片io流
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JCaptcha.generateImage(request, response);
    }

    /**
     * 导航主页面跳转
     * 默认是个空白页，子项目需要覆盖index.jsp页面，在index.jsp页面中可以通过ajax加载或<c:import>方式添加页面内容
     */
    @RequestMapping({"/index", "/"})
    public ModelAndView index() {
        try {
            ModelAndView model = new ModelAndView("/index");
            model.addObject("apm", "index");
            return model;
        } catch (Exception e) {
            log.error("首页加载失败！", e);
            throw new RuntimeException("导航失败!");
        }
    }

    /**
     * 测试页面
     *
     * @return
     */
    @RequestMapping(value = "/test")
    public String test() {
        return "/test";
    }

    /**
     * 重定向到指定url
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(Configs.REDIRECT_URL_PREFIX + "/**")
    public String redirect(HttpServletRequest request, Model model) {
        String uri = StringUtils.substringAfter(request.getRequestURI(), Configs.REDIRECT_URL_PREFIX);
        StringBuilder redirectUrl = new StringBuilder(request.getContextPath() + uri);
        if (request.getQueryString() != null) {
            redirectUrl.append("?").append(request.getQueryString());
        }

        model.addAttribute("url", redirectUrl);

        return "/redirect";
    }

    /**
     * 表单方式上传
     *
     * @return
     */
    /*@RequestMapping(value = "/upload")
    @ResponseBody
    public String uploadByForm(HttpServletRequest request) {
        try {
            return "上传成功";
        } catch (Exception e) {
            return e.getMessage();
        }
    }*/

    /**
     * ajax方式上传
     *
     * @return
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public Model uploadByData(HttpServletRequest request) {
        try {
            return DataResponse.success("files", upload(request));
        } catch (Exception e) {
            return DataResponse.failure(e.getMessage());
        }
    }

    /**
     * 上传附件
     *
     * @param request
     */
    private List<FileMeta> upload(HttpServletRequest request) throws IOException, FileUploadException {
        List<FileItem> fileItemList = HttpUtils.getFileItemList(request);
        String basePath = FilenameUtils.normalizeNoEndSeparator(Configs.BASE_FILE_PATH, true) + "/";
        String tempPath = FilenameUtils.normalizeNoEndSeparator(Configs.TEMP_FILE_PATH, true) + "/"
                + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + DateFormatUtils.format(new Date(), "HHmm") + "/";//上传临时路径
        FileUtils.forceMkdir(new File(basePath + tempPath));

        List<FileMeta> fileMetaList = Lists.newArrayList();
        for (FileItem item : fileItemList) {
            //String fieldName = item.getFieldName();
            String attachName = item.getName();

            byte[] attach = item.get();

            if (!ArrayUtils.isEmpty(attach)) {
                String ext = StringUtils.substringAfterLast(attachName, ".");
                String newAttachName = UUID.randomUUID() + (StringUtils.isEmpty(ext) ? "" : ("." + ext));

                OutputStream os = new FileOutputStream(FilenameUtils.normalizeNoEndSeparator(basePath + tempPath, true) + "/" + newAttachName);
                os.write(attach);
                os.close();
                fileMetaList.add(new FileMeta(attachName, attach.length, item.getContentType(), tempPath + newAttachName,
                        FilenameUtils.normalizeNoEndSeparator(Configs.FILE_URL_PREFIX + "/" + tempPath, true) + "/" + newAttachName,
                        FilenameUtils.normalizeNoEndSeparator(Configs.THUMBNAIL_URL_PREFIX + "/" + tempPath, true) + "/" + newAttachName));
            }
        }
        return fileMetaList;
    }

    /**
     * 缩略图
     * 在图片文件地址前加上前缀/thumbnail，则自动生成缩略图，支持宽高参数
     * 例如：/thumbnail/xxxx.jpg?200x200
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = Configs.THUMBNAIL_URL_PREFIX + "/**", method = RequestMethod.GET)
    public String thumbnail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int width = NumberUtils.toInt(Configs.THUMB_DEFAULT_WIDTH, 160);
        int height = NumberUtils.toInt(Configs.THUMB_DEFAULT_HEIGHT, -1);
        String queryString = request.getQueryString();//例如：100x100
        if (StringUtils.isNoneEmpty(queryString)) {
            String[] aa = StringUtils.split(queryString.toLowerCase(), "x");
            if (aa.length > 0) {
                width = NumberUtils.toInt(aa[0], width);
            }
            if (aa.length > 1) {
                height = NumberUtils.toInt(aa[1], height);
            }
        }
        String basePath = FilenameUtils.normalizeNoEndSeparator(Configs.BASE_FILE_PATH, true) + "/";
        String thumbPath = FilenameUtils.normalizeNoEndSeparator(Configs.THUMB_FILE_PATH, true) + "/";
        String image = StringUtils.substringAfter(request.getRequestURI(), Configs.THUMBNAIL_URL_PREFIX);
        String name = FilenameUtils.getBaseName(image);
        String ext = FilenameUtils.getExtension(image);
        String path = FilenameUtils.getPath(image);
        File srcFile = new File(basePath + image);
        //如果没有后缀名，需要判断文件格式并加上，否则thumbnailor会自动加上后缀，导致找不到文件
        if (StringUtils.isEmpty(ext)) {
            ext = StringUtils.defaultIfEmpty(ImageUtils.getImageFormat(srcFile), "JPEG").toLowerCase();
        }

        //例如：/2/1/20160906/1020/imagename@100x100.jpg
        String thumbImage = path + name + "@" + width + "x" + height + (StringUtils.isEmpty(ext) ? "" : "." + ext);
        File thumbFile = new File(basePath + thumbPath + thumbImage);
        if (srcFile.exists() && !thumbFile.exists()) {
            try {
                thumbFile.getParentFile().mkdirs();
                if (width < 0) {
                    Thumbnails.of(srcFile).height(height).toFile(thumbFile);
                } else if (height < 0) {
                    Thumbnails.of(srcFile).width(width).toFile(thumbFile);
                } else {
                    Thumbnails.of(srcFile).size(width, height).toFile(thumbFile);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "redirect:" + Configs.FILE_URL_PREFIX + thumbPath + thumbImage;
    }

    /**
     * 读取文件
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = Configs.FILE_URL_PREFIX + "/**", method = RequestMethod.GET)
    public void file(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String file = StringUtils.substringAfter(request.getRequestURI(), Configs.FILE_URL_PREFIX);
        String basePath = FilenameUtils.normalizeNoEndSeparator(Configs.BASE_FILE_PATH, true) + "/";
        //获取要下载的文件输入流
        InputStream in = new FileInputStream(basePath + file);

        int len = 0;
        //创建数据缓冲区
        byte[] buffer = new byte[1024];

        //通过response对象获取OutputStream流

        OutputStream out = response.getOutputStream();

        //将FileInputStream流写入到buffer缓冲区

        while ((len = in.read(buffer)) > 0) {
            //使用OutputStream将缓冲区的数据输出到客户端浏览器
            out.write(buffer, 0, len);
        }

        in.close();
    }

    /**
     * ajax方式上传
     *
     * @return
     */
    @RequestMapping(value = "/pinyin/{words}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model pinyin(@PathVariable String words) {
        try {
            List<Pinyin> pinyinList = Lists.newArrayList();
            List<String> completeList = PinyinUtils.getPinyin(words, PinyinUtils.PinyinFormat.WITHOUT_TONE, " ");
            for (String complete : completeList) {
                Pinyin pinyin = new Pinyin();
                pinyin.setComplete(WordUtils.capitalizeFully(complete));
                pinyin.setSimple(WordUtils.initials(pinyin.getComplete()));
                pinyinList.add(pinyin);
            }

            return DataResponse.success("pinyinList", pinyinList);
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }

    /**
     * 公用css
     *
     * @return
     */
    @RequestMapping("/libs-css")
    public String libs_css() {
        return "/include/libs-css";
    }

    /**
     * 头部区域（横幅、登录信息等）
     *
     * @param model
     * @return
     */
    @RequestMapping("/header")
    public String header(Model model) {
        model.addAttribute("staffName", SessionUtils.getStaffName());
        model.addAttribute("userName", SessionUtils.getStaffAccount());
        return "/include/header";
    }

    /**
     * 侧边栏（菜单）
     *
     * @return
     */
    @RequestMapping("/sidebar")
    public String sidebar(@RequestParam(value = "menu", required = false) String menu_code, @RequestParam(value = "branches", required = false) String branches, Model model) {
        List<Menu> menus = SessionUtils.getAttribute("USER_MENUS");
        if (menus == null) {
            menus = privilegeService.getAccessibleMenuList(SessionUtils.getStaffId());

            SessionUtils.setAttribute("USER_MENUS", menus);
        }

        List<Menu> newMenus = Lists.newArrayList();
        for (Menu menu : menus) {
            try {
                newMenus.add(menu.clone());
            } catch (Throwable t) {

            }
        }

        if (StringUtils.isNotEmpty(menu_code)) {
            for (Menu menu : newMenus) {
                if (menu.checkAndSetActive(menu_code)) {//找到活动的菜单
                    break;
                }
            }
        }

        model.addAttribute("menus", newMenus);
        model.addAttribute("branches", branches);

        return "/include/sidebar";
    }

    /**
     * 菜单
     *
     * @param model
     * @return
     */
    @RequestMapping("/menu")
    public String menu(Model model) {

        return "/include/menu";
    }

    /**
     * 顶部横条（面包屑/导航，选项卡/标签等）
     *
     * @return
     */
    @RequestMapping("topbar")
    public String topbar() {
        return "/include/topbar";
    }

    /**
     * 底部（copyright，即时消息等）
     *
     * @return
     */
    @RequestMapping("/footer")
    public String footer() {
        return "/include/footer";
    }

    /**
     * 公用js
     *
     * @return
     */
    @RequestMapping("/libs-js")
    public String libs_js() {
        return "/include/libs-js";
    }

    /**
     * 分页栏
     *
     * @return
     */
    @RequestMapping("/pagination-bar")
    public String pagination_bar(@RequestParam(value = "rowList", required = false) int[] rowList,
                                 Model model) {
        if (rowList == null) {
            rowList = new int[]{10, 20, 50};
        }
        model.addAttribute("rowList", rowList);

        return "/include/pagination-bar";
    }

    @RequestMapping("/tree")
    public String tree(@RequestParam(value = "style", required = false) String style,
                       Model model) {
        model.addAttribute("style", StringUtils.defaultString(style, "none"));

        return "/include/tree";
    }
}
