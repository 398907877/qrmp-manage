<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="zh-cn" class="login">
<head>
    <meta charset="utf-8">
    <title>${APP_NAME}</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <!-- #CSS Links -->
    <!-- Basic Styles -->
    <link rel="stylesheet" type="text/css" media="screen"
          href="${home}/assets/libs/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen"
          href="${home}/assets/fonts/fontawesome/css/font-awesome.min.css">

    <!-- COMMON Styles : Caution! DO NOT change the order -->
    <link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/css/global.css">
    <link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/css/animation.css">

    <!-- RTL Support -->
    <!--link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/css/rtl.min.css"-->

    <link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/libs/layer/skin/layer.min.css">

    <!-- Page Style -->
    <link rel="stylesheet" type="text/css" media="screen" href="${home}/assets/css/login.css">

    <!-- #FAVICONS -->
    <link rel="shortcut icon" href="${home}/assets/img/favicon/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${home}/assets/img/favicon/favicon.ico" type="image/x-icon">

    <!-- #GOOGLE FONT -->
    <link rel="stylesheet" href="${home}/assets/fonts/opensans/css/open-sans.min.css">

    <!-- #APP SCREEN / ICONS -->
    <!-- Specifying a Webpage Icon for Web Clip
         Ref: https://developer.apple.com/library/ios/documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html -->
    <link rel="apple-touch-icon" href="${home}/assets/img/splash/sptouch-icon-iphone.png">
    <link rel="apple-touch-icon" sizes="76x76" href="${home}/assets/img/splash/touch-icon-ipad.png">
    <link rel="apple-touch-icon" sizes="120x120" href="${home}/assets/img/splash/touch-icon-iphone-retina.png">
    <link rel="apple-touch-icon" sizes="152x152" href="${home}/assets/img/splash/touch-icon-ipad-retina.png">

    <!-- iOS web-app metas : hides Safari UI Components and Changes Status Bar Appearance -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <!-- Startup image for web apps -->
    <link rel="apple-touch-startup-image" href="${home}/assets/img/splash/ipad-landscape.png"
          media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
    <link rel="apple-touch-startup-image" href="${home}/assets/img/splash/ipad-portrait.png"
          media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
    <link rel="apple-touch-startup-image" href="${home}/assets/img/splash/iphone.png"
          media="screen and (max-device-width: 320px)">

</head>

<body class="animated fadeInDown">

<header class="page-header">

    <div class="logo-group">
        <span class="logo"> <img src="${home}/assets/img/logo.png" alt="${COMPANY_NAME}"> </span>
    </div>

    <span class="login-header-space">
        <span class="hidden-xs">Need an account?</span>
        <a href="${home}/register.html" class="btn btn-danger">Create account</a>
    </span>

</header>

<div class="main" ${style=="mini"?" style='padding-top:0'":""}>

    <!-- MAIN CONTENT -->
    <div class="content container">

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-7 col-lg-8 hidden-xs hidden-sm">
                <h1 class="txt-color-red login-header-big">${COMPANY_NAME}</h1>
                <div class="hero">

                    <div class="pull-left login-desc-box-l">
                        <h4 class="paragraph-header">It's Okay to be Smart. Experience the simplicity of SmartAdmin,
                            everywhere you go!</h4>
                        <div class="login-app-icons">
                            <a href="javascript:void(0);" class="btn btn-danger btn-sm">Frontend Template</a>
                            <a href="javascript:void(0);" class="btn btn-danger btn-sm">Find out more</a>
                        </div>
                    </div>

                    <img src="${home}/assets/img/demo/iphoneview.png" class="pull-right display-image" alt=""
                         style="width:210px">

                </div>

                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                        <h5 class="about-heading">About SmartAdmin - Are you up to date?</h5>
                        <p>
                            Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque
                            laudantium, totam rem aperiam, eaque ipsa.
                        </p>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                        <h5 class="about-heading">Not just your average template!</h5>
                        <p>
                            Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta
                            nobis est eligendi voluptatem accusantium!
                        </p>
                    </div>
                </div>

            </div>
            <div class="col-xs-12 col-sm-12 col-md-5 col-lg-4">
                <div class="well no-padding ${style=="mini"?" no-margin":""}">
                    <form action="${home}/login" id="login-form" method="post">
                        <legend>
                            登录
                        </legend>
                        <fieldset>
                            <div class="form-group">
                                <label class="control-label ${style=="mini"?" hidden":""}">用户名</label>
                                <div class="input-group">
                                    <input type="username" id="username" name="username" tabindex="1"
                                           class="form-control" value="${login.username}">
                                    <span class="input-group-addon"><i class="fa fa-user"></i></span>
                                    <b class="tooltip tooltip-top-right"><i class="fa fa-user txt-color-teal"></i>
                                        请输入你的用户名</b>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label ${style=="mini"?" hidden":""}">密码</label>
                                <div class="input-group">
                                    <input type="password" id="password" name="password" tabindex="2"
                                           class="form-control" value="${login.password}">
                                    <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                                    <b class="tooltip tooltip-top-right"><i class="fa fa-lock txt-color-teal"></i>
                                        请输入你的密码</b>
                                </div>
                                <c:if test="${style!='mini'}">
                                    <div class="note">
                                        <a href="${home}/forgotpassword.html">忘记密码?</a>
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-group" style="${isCaptcha?"":"display:none"}">
                                <label class="control-label hidden">验证码</label>
                                <div class="input-group">
                                    <input type="text" id="captcha" name="captcha" tabindex="3" class="form-control"
                                           value="${login.captcha}">
                                    <span class="input-group-addon" style="padding: 0">
                                        <img id="captchaImage" title="点击更换验证码" alt="点击更换验证码"
                                             src="captcha?<%=System.currentTimeMillis()%>"
                                             style="width:120px;height: 30px"></span>
                                    <b class="tooltip tooltip-top-right"><i class="fa fa-shield txt-color-teal"></i>
                                        请输入验证码</b>
                                </div>
                            </div>
                            <c:if test="${style!='mini'}">
                                <div class="form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" id="remember_me" class="checkbox"
                                                   name="rememberMe" ${login.rememberMe?'checked="checked"':''}
                                                   tabindex="4">
                                            <span>记住我</span>
                                        </label>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${!success}">
                                <div class="form-group text-left">
                                    <small class="invalid help-block">${message}</small>
                                </div>
                            </c:if>
                        </fieldset>

                        <div class="form-actions text-right">
                            <button id="btnSubmit" type="submit"
                                    class="btn btn-primary ${style=="mini"?" btn-block btn-lg":""}" tabindex="5">
                                登&nbsp;&nbsp;录
                            </button>
                        </div>

                    </form>

                </div>
                <c:if test="${style=='mini'}">
                    <div class="note">
                        <a href="${home}/forgotpassword.html">忘记密码?</a>
                        <a class="pull-right" href="${home}/register.html">免费注册</a>
                    </div>
                </c:if>
                <c:if test="${style!='mini'}">
                    <h5 class="text-center"> - Or sign in using -</h5>

                    <ul class="list-inline text-center">
                        <li>
                            <a href="javascript:void(0);" class="btn btn-primary btn-circle"><i
                                    class="fa fa-facebook"></i></a>
                        </li>
                        <li>
                            <a href="javascript:void(0);" class="btn btn-info btn-circle"><i class="fa fa-twitter"></i></a>
                        </li>
                        <li>
                            <a href="javascript:void(0);" class="btn btn-warning btn-circle"><i
                                    class="fa fa-linkedin"></i></a>
                        </li>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>

</div>

<!--================================================== -->

<!-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)-->
<script src="${home}/assets/libs/pace/pace.min.js"></script>

<!-- jQuery -->
<script src="${home}/assets/libs/jquery-1.12.4.min.js"></script>

<!-- BOOTSTRAP JS -->
<script src="${home}/assets/libs/bootstrap/js/bootstrap.min.js"></script>

<!-- browser msie issue fix -->
<script src="${home}/assets/libs/jquery-mb-browser/jquery.mb.browser.min.js"></script>

<!-- FastClick: For mobile devices: you can disable this in app.js -->
<script src="${home}/assets/libs/fastclick/fastclick.min.js"></script>

<script src="${home}/assets/libs/layer/layer.min.js"></script>

<!--Service Invoke-->
<script src="${home}/assets/libs/jquery.service.min.js"></script>

<!--[if IE 8]>

<h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>

<![endif]-->

<!-- JQUERY VALIDATE -->
<script src="${home}/assets/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${home}/assets/plugins/jquery-validation/localization/messages_zh.min.js"></script>
<script src="${home}/assets/plugins/jquery-validation/jquery.validate.custom.min.js"></script>

<script src="${home}/assets/libs/layer/layer.min.js"></script>

<!-- RSA Encrypt and Decrypt -->
<script src="${home}/assets/plugins/rsa/Barrett.min.js" charset="UTF-8"></script>
<script src="${home}/assets/plugins/rsa/BigInt.min.js" charset="UTF-8"></script>
<script src="${home}/assets/plugins/rsa/RSA.min.js" charset="UTF-8"></script>

<!-- MAIN APP JS FILE -->
<script src="${home}/assets/js/app.min.js"></script>

<script type="text/javascript">
    function encryptPassword(password, publicExponent, modulus) {
        setMaxDigits(130);
        var key = new RSAKeyPair(publicExponent, '', modulus);
        password = encryptedString(key, password); //不支持汉字

        return password;
    }

    var isDoLogin = false;//控制不重复登录

    function check(publicExponent, modulus) {
        return $.ServiceClient.invoke("${home}/login.json", {
            data: {
                username: $("#username").val(),
                password: encryptPassword($("#password").val(), publicExponent, modulus),
                captcha: $("#captcha").val(),
                rememberMe: $("#remember_me").is(":checked")
            }
        });
    }

    function getKey() {
        return $.ServiceClient.invoke("${home}/login_key.json", {
            data: {}
        });
    }

    function reset(data) {
        layer.alert(data.errmsg, {
            icon: 0, end: function () {
                layer.closeAll()
            }
        }, function (index) {
            $("#btnSubmit").prop("disabled", false).html("登录");
            $("#captchaImage").attr("src", "${home}/captcha?" + new Date());
            isDoLogin = false;
            layer.close(index);
        });
    }

    function redirect(data) {
        <c:if test="${style!='mini'}">
        if (data.redirectUrl) {
            location.replace("${home}/redirect" + data.redirectUrl);//跳转到先前的页面
        } else {
            location.replace("${home}/index");
        }
        $("#login-error").remove();
        $("#btnSubmit").prop("disabled", false).html("登录");
        </c:if>
        <c:if test="${style=='mini'}">
        parent.layer.close(parent.layer.getFrameIndex(window.name));
        </c:if>
    }

    function login() {
        if (isDoLogin)
            return;
        isDoLogin = true;
        $("#login-error").remove();
        $("#btnSubmit").prop("disabled", true).html("登录中...");

        $.when(getKey()).then(function (data) {
            return check(data.publicKey.publicExponent, data.publicKey.modulus)
        }).done(redirect).fail(reset);
    }

    $(function () {
        $("#captchaImage").on("click", function (e) {
            $(this).attr("src", "${home}/captcha?" + new Date().getTime());
        });
        // Validation
        var validator = $("#login-form").validate({
            onkeyup: false,
            // Rules for form validation
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                },
                captcha: {
                    required: ${isCaptcha}
                }
            },

            // Messages for form validation
            messages: {
                username: {
                    required: '请输入您的用户名'
                },
                password: {
                    required: '请输入您的密码'
                },
                captcha: {
                    required: '请输入验证码'
                }
            },

            // Do not change code below
            errorPlacement: function (error, element) {
                error.insertAfter(element.parent());
            }
        });


        $("#btnSubmit").on('click', function (e) {
            //方式1：form方式提交
            //$("#password").val(encryptPassword($("#password").val()));
            //方式2：ajax方式提交
            e.preventDefault();
            if (validator.form()) {
                login();
            }
        })
    });
</script>
</body>
</html>