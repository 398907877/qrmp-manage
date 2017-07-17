<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page trimDirectiveWhitespaces="true"%>
<header class="page-header">
    <div class="logo-group">

        <!-- PLACE YOUR LOGO HERE -->
        <span class="logo"> <img src="${home}/assets/img/logo.png" alt="<c:out value="${COMPANY_NAME}"/>"> </span>
        <!-- END LOGO PLACEHOLDER -->
        <span class="activity" data-action="toggleActivity"><i class="fa fa-bell"></i> <b class="badge"> 21 </b></span>
    </div>

    <!-- #TOGGLE LAYOUT BUTTONS -->
    <!-- pulled right: nav area -->
    <div class="pull-right">
        <!-- collapse menu button -->
        <div class="btn-header hidden-menu pull-right btn-hide-menu">
            <span> <a href="javascript:void(0);" data-action="toggleMenu" title="Collapse Menu"><i
                    class="fa fa-reorder"></i></a> </span>
        </div>
        <!-- end collapse menu -->

        <!-- Top menu profile link : this shows only when top menu is active -->
        <ul class="login-info header-dropdown-list padding-5 pull-right">
            <li class="">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <img src="${home}/assets/img/avatars/sunny.png" alt="${staffName}" class="img-circle online"/>
                    <span class="hidden-xs"><c:out value="${staffName}"/><b class="caret"></b></span>
                </a>
                <ul class="dropdown-menu pull-right">
                    <li>
                        <a href="${home}/user/profile" class="padding-10 padding-top-0 padding-bottom-0"><i
                                class="fa fa-user"></i> 个人资料</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="${home}/user/password" class="padding-10 padding-top-0 padding-bottom-0"> <i
                                class="fa fa-user-secret"></i> 密码修改</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="${home}/logout" class="padding-10 padding-top-5 padding-bottom-5" data-action="userLogout"><i
                                class="fa fa-sign-out fa-lg"></i> <strong>退出</strong></a>
                    </li>
                </ul>
            </li>
        </ul>


        <!-- fullscreen button -->
        <div class="btn-header transparent pull-right btn-fullscreen">
            <span> <a href="javascript:void(0);" data-action="launchFullscreen" title="Full Screen"><i
                    class="fa fa-arrows-alt"></i></a> </span>
        </div>
        <!-- end fullscreen button -->

        <!-- Note: The activity badge color changes when clicked and resets the number to 0
             Suggestion: You may want to set a flag when this happens to tick off all checked messages / notifications -->
        <div class="btn-header transparent pull-right activity" data-action="toggleActivity">
            <span><a href="javascript:void(0)" title="Activity"><i class="fa fa-bell"></i> <b class="badge"> 21 </b>
            </a></span>
            <!-- AJAX-DROPDOWN : control this dropdown height, look and feel from the LESS variable file -->
            <div class="ajax-dropdown">

                <!-- the ID links are fetched via AJAX to the ajax container "ajax-notifications" -->
                <div class="btn-group btn-group-justified" data-toggle="buttons">
                    <label class="btn btn-default">
                        <input type="radio" name="activity" value="ajax/notify/mail.html">
                        Msgs (14) </label>
                    <label class="btn btn-default">
                        <input type="radio" name="activity" value="ajax/notify/notifications.html">
                        notify (3) </label>
                    <label class="btn btn-default">
                        <input type="radio" name="activity" value="ajax/notify/tasks.html">
                        Tasks (4) </label>
                </div>

                <!-- notification content -->
                <div class="ajax-notifications custom-scroll">

                    <div class="alert alert-transparent">
                        <h4>Click a button to show messages here</h4>
                        This blank page message helps protect your privacy, or you can show the first message here
                        automatically.
                    </div>

                    <i class="fa fa-lock fa-4x fa-border"></i>

                </div>
                <!-- end notification content -->

                <!-- footer: refresh area -->
					<span> Last updated on: 12/12/2013 9:43AM
						<button type="button" data-loading-text="<i class='fa fa-refresh fa-spin'></i> Loading..."
                                class="btn btn-xs btn-default pull-right">
                            <i class="fa fa-refresh"></i>
                        </button> </span>
                <!-- end footer -->

            </div>
            <!-- END AJAX-DROPDOWN -->
        </div>


        <!-- search mobile button (this is hidden till mobile view port) -->
        <div class="btn-header transparent pull-right btn-search-mobile" data-action="searchMobile">
            <span> <a href="javascript:void(0)" title="Search"><i class="fa fa-search"></i></a> </span>
        </div>
        <!-- end search mobile button -->

        <!-- #SEARCH -->
        <!-- input: search field -->
        <form action="#ajax/search.html" class="header-search pull-right">
            <input type="text" name="param" placeholder="Find reports and more">
            <button type="submit">
                <i class="fa fa-search"></i>
            </button>
            <a href="javascript:void(0);" data-action="cancelSearchMobile" title="Cancel Search"
               class="btn-cancel-search-mobile"><i class="fa fa-times"></i></a>
        </form>
        <!-- end input: search field -->
    </div>
    <!-- end pulled right: nav area -->

</header>