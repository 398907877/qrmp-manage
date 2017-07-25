package com.yanyan.data.domain.system;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 * User: Saintcy
 * Date: 2016/5/15
 * Time: 22:16
 */
@Data
public class Menu implements Cloneable, Serializable {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String url;
    private String target;
    private boolean hide;
    private List<Menu> children;

    private boolean active;
    private boolean open;

    public Menu(long id, String code, String name, String icon, String url, String target, boolean hide) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.icon = icon;
        this.url = url;
        this.target = target;
        this.hide = hide;
    }

    public boolean checkAndSetActive(String code) {
        if (StringUtils.equals(code, this.code)) {
            this.active = true;
            return true;
        } else {
            return checkAndSetChildActive(code);
        }
    }

    public boolean checkAndSetChildActive(String code) {
        if (children != null) {
            for (Menu menu : children) {
                if (menu.checkAndSetActive(code)) {
                    this.active = true;
                    this.open = true;
                    return true;
                } else {
                    if (menu.checkAndSetChildActive(code)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Menu> cloneChildren() throws CloneNotSupportedException {
        List<Menu> newChildren = new ArrayList<Menu>();
        if (children != null) {
            for (Menu menu : children) {
                newChildren.add(menu.clone());
            }
            return newChildren;
        } else {
            return null;
        }
    }

    @Override
    public Menu clone() throws CloneNotSupportedException {
        Menu menu = (Menu) super.clone();
        menu.setChildren(cloneChildren());

        return menu;
    }
}
